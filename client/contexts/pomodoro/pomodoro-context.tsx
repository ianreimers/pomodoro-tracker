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
  PomodoroSession,
  SessionType,
} from '@/types/types';
import { reducer } from './pomodoro-reducer';

type UserContextProviderProps = {
  children: React.ReactNode;
};

export const PomodoroContext = createContext<PomodoroContextType | null>(null);

export default function PomodoroContextProvider({
  children,
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
    remainingSeconds: taskSeconds,
    intervalTimeRemaining: 1000,
    isPlaying: false,
    totalPomodoros: 0,
    currSessionType: 'task',
    currPomodoroId: crypto.randomUUID(),
    currBreakType: pomodoroInterval === 1 ? 'LONG' : 'SHORT',
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

  useEffect(() => {
    if (!isPlaying) {
      return;
    }

    // If we're logged in and at the start of a task, create a new pomodoro session with the server
    if (isAuthenticated() && currSessionType === 'task') {
      if (taskSeconds === remainingSeconds) {
        mutationPost.mutate({
          tempUuid: currPomodoroId as string,
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

    // Update the posted pomodoro every minute
    if (isAuthenticated() && remainingSeconds % 60 === 0) {
      const sessionSecondsMap = new Map<string, number>([
        ['task', taskSeconds],
        ['short break', shortBreakSeconds],
        ['long break', longBreakSeconds],
      ]);

      if (sessionSecondsMap.get(currSessionType) !== remainingSeconds) {
        mutationPatch.mutate({
          id: currPomodoroId,
          sessionType: currSessionType === 'task' ? 'task' : 'break',
        });
      }
    }

    // Capture the moment, in millis, that this second began
    let startTime = Date.now();

    // Used to decide wheather to calculate any remaining time on component unmount
    let intervalCompleted = false;

    const intervalId = setInterval(() => {
      // Check if we need to switch to the next session or decrement remaining seconds
      if (remainingSeconds === 0) {
        playSound(sound, soundVolume);
        dispatch({
          type: 'switch_session',
          payload: {
            taskSeconds,
            shortBreakSeconds,
            longBreakSeconds,
            pomodoroInterval,
          },
        });
      } else {
        dispatch({ type: 'complete_interval' });
      }

      intervalCompleted = true;
    }, intervalTimeRemaining); // The delay is when the user paused in (second - timeRemaining)

    return () => {
      const stopTime = Date.now();

      let milliDiff = stopTime - startTime;
      milliDiff = milliDiff > 1000 ? 1000 : milliDiff;

      let remainingMilli = intervalTimeRemaining - milliDiff;
      remainingMilli = remainingMilli <= 0 ? 1000 : remainingMilli;

      if (!intervalCompleted && remainingSeconds !== 0) {
        dispatch({
          type: 'duration_remaining',
          payload: {
            intervalTimeRemaining: remainingMilli,
          },
        });
      }

      // Clear the interval upon unmount
      clearInterval(intervalId);
    };
  }, [remainingSeconds, isPlaying]);

  const contextValue: PomodoroContextType = {
    remainingSeconds,
    currSessionType,
    isPlaying,
    totalPomodoros,
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
