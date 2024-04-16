'use client';

import { createContext, useContext, useEffect, useReducer } from 'react';
import { useUserSettingsContext } from '@/contexts/user-settings-context';
import { useMutation } from '@tanstack/react-query';
import axiosInstance from '@/api/axiosInstance';
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
  const { toast } = useToast();
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

  const mutationPost = useMutation({
    mutationFn: async (newPomodoro: PomodoroSession) => {
      const response = await axiosInstance.post(
        '/pomodoro-sessions',
        newPomodoro
      );
      return response.data;
    },
    onSuccess: (data) => {
      // The temp uuid will be replaced with the id the server sends back, for future updates
      const newId = data.id as number;
      dispatch({
        type: 'update_pomdoro_id',
        payload: { newId },
      });
    },
    onError: (error) => {
      console.error('Error in pomodoro post mutation:', error);
    },
  });

  const mutationPatch = useMutation({
    mutationFn: async (data: { sessionType: string; id: string | number }) =>
      await axiosInstance.patch(`/pomodoro-sessions/${data.id}`, {
        sessionType: data.sessionType,
      }),
    onSuccess: () => {
      toast({
        description: `Pomodoro updated`,
      });
    },
    onError: (error) => {
      console.error('Error in pomodoro patch mutation:', error);
    },
  });

  function togglePlaying() {
    dispatch({
      type: 'toggle_playing',
    });
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
