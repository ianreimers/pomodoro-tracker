'use client';

import { createContext, useContext, useEffect, useReducer } from 'react';
import { useUserSettingsContext } from '@/contexts/user-settings-context';
import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from '@/services/api';
import { useToast } from '@/components/ui/use-toast';
import { useAuthContext } from '@/contexts/auth/auth-context';
import useAudioPlayer from '@/hooks/use-audio-player';
import {
  PomodoroContextType,
  PomodoroReducerState,
  PomodoroSession,
} from '@/types/types';
import { reducer } from './pomodoro-reducer';
import { v4 as uuid } from 'uuid';
import usePomodoroTimer from '@/hooks/use-pomodoro-timer';
import { useCreatePomodoro, useUpdatePomodoro } from '@/services/mutations';

type UserContextProviderProps = {
  children: React.ReactNode;
  initialStateOverride?: Partial<PomodoroReducerState>;
};

export const PomodoroContext = createContext<PomodoroContextType | null>(null);

const initialPomodoroState: PomodoroReducerState = {
  remainingSeconds: 1500,
  intervalTimeRemaining: 1000,
  isPlaying: false,
  totalPomodoros: 0,
  currSessionType: 'task',
  currPomodoroId: uuid(),
  currBreakType: 'SHORT',
};

export default function PomodoroContextProvider({
  children,
  initialStateOverride,
}: UserContextProviderProps) {
  const {
    taskSeconds,
    shortBreakSeconds,
    longBreakSeconds,
    pomodoroInterval,
    sound,
    soundVolume,
  } = useUserSettingsContext();
  const { isAuthenticated } = useAuthContext();
  const [state, dispatch] = useReducer(reducer, {
    ...initialPomodoroState,
    ...initialStateOverride,
  });
  const { playSound } = useAudioPlayer(sound, soundVolume);
  const {
    remainingSeconds,
    intervalTimeRemaining,
    isPlaying,
    totalPomodoros,
    currSessionType,
    currPomodoroId,
    currBreakType,
  } = state;

  const createPomodoroMutation = useCreatePomodoro(dispatch);
  const updatePomodoroMutation = useUpdatePomodoro();

  usePomodoroTimer({
    remainingSeconds,
    shortBreakSeconds,
    longBreakSeconds,
    pomodoroInterval,
    sound,
    soundVolume,
    taskSeconds,
    intervalTimeRemaining,
    isPlaying,
    dispatch,
  });

  // Check if any of the used user settings were updated and reset the session
  useEffect(() => {
    dispatch({
      type: 'settings_update',
      payload: {
        taskSeconds,
        shortBreakSeconds,
        longBreakSeconds,
        pomodoroInterval,
      },
    });
  }, [
    taskSeconds,
    shortBreakSeconds,
    longBreakSeconds,
    pomodoroInterval,
    sound,
  ]);

  // Check if a minute passed to update the pomodoro
  useEffect(() => {
    handleUpdatePomodoro();
  }, [remainingSeconds]);

  function handleUpdatePomodoro() {
    if (!isAuthenticated() || remainingSeconds % 60 !== 0) {
      return;
    }

    const sessionSecondsMap = new Map<string, number>([
      ['task', taskSeconds],
      ['short break', shortBreakSeconds],
      ['long break', longBreakSeconds],
    ]);

    if (sessionSecondsMap.get(currSessionType) !== remainingSeconds) {
      updatePomodoroMutation.mutate({
        id: currPomodoroId,
        sessionType: currSessionType === 'task' ? 'task' : 'break',
      });
    }
  }

  function handleCreateNewPomodoro() {
    if (!isAuthenticated() || currSessionType !== 'task') {
      return;
    }

    if (
      typeof currPomodoroId === 'string' &&
      !createPomodoroMutation.isPending
    ) {
      createPomodoroMutation.mutate({
        tempUuid: uuid(),
        breakType: currBreakType,
        sessionTaskSeconds: taskSeconds,
        sessionShortBreakSeconds: shortBreakSeconds,
        sessionLongBreakSeconds: longBreakSeconds,
        sessionStartTime: new Date().toISOString(),
        sessionUpdateTime: new Date().toISOString(),
        taskDuration: 0,
        breakDuration: 0,
      });
    }
  }

  function togglePlaying() {
    dispatch({
      type: 'toggle_playing',
    });

    handleCreateNewPomodoro();
  }

  function resetCycle() {
    dispatch({
      type: 'reset_cycle',
      payload: {
        taskSeconds,
        pomodoroInterval,
      },
    });
  }

  function skipSession() {
    dispatch({
      type: 'switch_session',
      payload: {
        taskSeconds,
        shortBreakSeconds,
        longBreakSeconds,
        pomodoroInterval,
      },
    });
  }

  const contextValue: PomodoroContextType = {
    remainingSeconds,
    currSessionType,
    isPlaying,
    totalPomodoros,
    intervalTimeRemaining,
    dispatch,
    resetCycle,
    togglePlaying,
    skipSession,
  };

  return (
    <>
      <PomodoroContext.Provider value={contextValue}>
        {children}
      </PomodoroContext.Provider>
    </>
  );
}

export function usePomodoroContext() {
  const context = useContext(PomodoroContext);

  if (!context) {
    throw new Error(
      'usePomodoroContext must be used within a PomodoroContextProvider'
    );
  }

  return context;
}
