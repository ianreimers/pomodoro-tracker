import { Dispatch, useEffect } from 'react';
import useAudioPlayer from './use-audio-player';
import { PomodoroAction } from '@/contexts/pomodoro/pomodoro-reducer';

interface Props {
  taskSeconds: number;
  shortBreakSeconds: number;
  longBreakSeconds: number;
  pomodoroInterval: number;
  sound: string;
  soundVolume: number;
  remainingSeconds: number;
  intervalTimeRemaining: number;
  isPlaying: boolean;
  dispatch: Dispatch<PomodoroAction>;
}

export default function usePomodoroTimer({
  taskSeconds,
  shortBreakSeconds,
  longBreakSeconds,
  pomodoroInterval,
  sound,
  soundVolume,
  remainingSeconds,
  intervalTimeRemaining,
  isPlaying,
  dispatch,
}: Props) {
  const { playSound } = useAudioPlayer();

  useEffect(() => {
    let intervalId: number | undefined = undefined;

    // Used to decide wheather to calculate any remaining time on component unmount
    let intervalCompleted = false;

    let startTime: number;

    if (isPlaying) {
      // Capture the moment, in millis, that this second began
      startTime = Date.now();

      intervalId = setInterval(() => {
        // Check if we need to switch to the next session or decrement remaining seconds
        if (remainingSeconds <= 0) {
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
      }, intervalTimeRemaining) as unknown as number; // The delay is when the user paused in (second - timeRemaining)
    }

    return () => {
      // Clear the interval upon unmount
      clearInterval(intervalId);

      if (intervalCompleted) {
        return;
      }

      const stopTime = Date.now();

      let milliDiff = stopTime - startTime;
      milliDiff = milliDiff > 1000 ? 1000 : milliDiff;

      let remainingMilli = intervalTimeRemaining - milliDiff;
      remainingMilli = remainingMilli <= 0 ? 1000 : remainingMilli;

      if (remainingSeconds !== 0) {
        dispatch({
          type: 'duration_remaining',
          payload: {
            intervalTimeRemaining: remainingMilli,
          },
        });
      }
    };
  }, [remainingSeconds, isPlaying]);
}
