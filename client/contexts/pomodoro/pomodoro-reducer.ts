import { PomodoroReducerState } from '@/types/types';
import { v4 as uuid } from 'uuid';

export type PomodoroAction =
  | { type: 'complete_interval' }
  | { type: 'increment_total_pomodoros' }
  | { type: 'duration_remaining'; payload: { intervalTimeRemaining: number } }
  | {
      type: 'switch_session';
      payload: {
        pomodoroInterval: number;
        taskSeconds: number;
        shortBreakSeconds: number;
        longBreakSeconds: number;
      };
    }
  | { type: 'update_pomdoro_id'; payload: { newId: number } }
  | {
      type: 'settings_update';
      payload: {
        taskSeconds: number;
        longBreakSeconds: number;
        shortBreakSeconds: number;
        pomodoroInterval: number;
      };
    }
  | {
      type: 'reset_cycle';
      payload: {
        taskSeconds: number;
        pomodoroInterval: number;
      };
    }
  | { type: 'toggle_playing' };

export function reducer(
  state: PomodoroReducerState,
  action: PomodoroAction
): PomodoroReducerState {
  switch (action.type) {
    case 'complete_interval':
      return {
        ...state,
        remainingSeconds: state.remainingSeconds - 1,
        intervalTimeRemaining: 1000,
      };
    case 'duration_remaining':
      return {
        ...state,
        intervalTimeRemaining: action.payload.intervalTimeRemaining,
      };
    case 'increment_total_pomodoros':
      return {
        ...state,
        totalPomodoros: state.totalPomodoros + 1,
      };
    case 'switch_session': {
      let nextState: PomodoroReducerState = { ...state };
      nextState.intervalTimeRemaining = 1000;
      nextState.isPlaying = true;
      nextState.currBreakType = 'SHORT';

      const hasLongBreakNext =
        state.totalPomodoros % action.payload.pomodoroInterval ===
        action.payload.pomodoroInterval - 1;

      if (state.currSessionType === 'task') {
        if (hasLongBreakNext) {
          nextState.remainingSeconds = action.payload.longBreakSeconds;
          nextState.currSessionType = 'long break';
        } else {
          nextState.remainingSeconds = action.payload.shortBreakSeconds;
          nextState.currSessionType = 'short break';
          nextState.currBreakType = 'SHORT';
        }
        nextState.totalPomodoros += 1;
      } else {
        nextState.currBreakType = hasLongBreakNext ? 'LONG' : 'SHORT';
        nextState.remainingSeconds = action.payload.taskSeconds;
        nextState.currSessionType = 'task';
        nextState.currPomodoroId = uuid();
      }

      return nextState;
    }
    case 'reset_cycle':
      return {
        remainingSeconds: action.payload.taskSeconds,
        intervalTimeRemaining: 1000,
        isPlaying: false,
        totalPomodoros: 0,
        currSessionType: 'task',
        currPomodoroId: uuid(),
        currBreakType: action.payload.pomodoroInterval === 1 ? 'LONG' : 'SHORT',
      };
    case 'update_pomdoro_id':
      return {
        ...state,
        currPomodoroId: action.payload.newId,
      };
    case 'settings_update': {
      return {
        ...state,
        remainingSeconds: action.payload.taskSeconds,
        currSessionType: 'task',
        currBreakType: action.payload.pomodoroInterval === 1 ? 'LONG' : 'SHORT',
        isPlaying: false,
        intervalTimeRemaining: 1000,
        currPomodoroId: uuid(),
      };
    }
    case 'toggle_playing':
      return {
        ...state,
        isPlaying: !state.isPlaying,
      };
  }
}
