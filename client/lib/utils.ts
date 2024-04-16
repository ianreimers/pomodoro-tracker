import { type ClassValue, clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';
import {
  TimeUnitNums,
  TimeUnitStrs,
  UserSettings,
  UserSettingsState,
  WeekAnalayticsResponse,
} from '@/types/types';
import { UserSettingsFormData } from '@/validation_schema/schemas';
import { PomodoroTotalsResponse, PomodoroTotalUIData } from '@/types/types';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

// export function timeUnitsToSeconds({ hours, mins, secs }: TimeUnitNums) {
//   return secs + mins * 60 + hours * 60 * 60;
// }
//
// function secondsToMinutes(seconds: number) {
//   return Math.floor(seconds / 60);
// }
//
// export function secondsToTime(seconds: number) {
//   const secs = seconds % 60;
//   const mins = Math.floor(seconds / 60) % 60;
//   const hours = Math.floor(seconds / 60 / 60) % 24;
//
//   const secs_str = secs.toString().padStart(2, '0');
//   const mins_str = mins.toString().padStart(2, '0');
//   const hours_str = hours.toString().padStart(2, '0');
//
//   return `${hours_str}:${mins_str}:${secs_str}`;
// }

export function title(words: string) {
  const word_arr = words.split(' ');

  for (let i = 0; i < word_arr.length; ++i) {
    word_arr[i] = word_arr[i].charAt(0).toUpperCase() + word_arr[i].slice(1);
  }

  return word_arr.join(' ');
}

// export function secondsToTimeUnits(
//   seconds: number,
//   withPadding: false
// ): TimeUnitNums;
// export function secondsToTimeUnits(
//   seconds: number,
//   withPadding: true
// ): TimeUnitStrs;
// export function secondsToTimeUnits(
//   seconds: number,
//   withPadding: boolean
// ): TimeUnitNums | TimeUnitStrs {
//   const secs = seconds % 60;
//   const mins = Math.floor(seconds / 60) % 60;
//   const hours = Math.floor(seconds / 60 / 60) % 24;
//
//   if (withPadding) {
//     const hours_str = hours.toString().padStart(2, '0');
//     const mins_str = mins.toString().padStart(2, '0');
//     const secs_str = secs.toString().padStart(2, '0');
//     const obj: TimeUnitStrs = {
//       hours: hours_str,
//       mins: mins_str,
//       secs: secs_str,
//     };
//
//     return obj;
//   }
//
//   const obj: TimeUnitNums = {
//     hours,
//     mins,
//     secs,
//   };
//   return obj;
// }

// export function mapSettingsToState(settings: UserSettings): UserSettingsState {
//   return {
//     taskSeconds: settings.taskSeconds,
//     shortBreakSeconds: settings.shortBreakSeconds,
//     longBreakSeconds: settings.longBreakSeconds,
//     taskTimeUnits: secondsToTimeUnits(settings.taskSeconds, false),
//     shortBreakTimeUnits: secondsToTimeUnits(settings.shortBreakSeconds, false),
//     longBreakTimeUnits: secondsToTimeUnits(settings.longBreakSeconds, false),
//     pomodoroInterval: settings.pomodoroInterval,
//     sound: settings.sound,
//     soundVolume: settings.soundVolume,
//   };
// }
//
// export function mapUserSettingsFormDataToRequest(
//   settingsFormData: UserSettingsFormData
// ): UserSettings {
//   const {
//     taskSeconds,
//     shortBreakSeconds,
//     longBreakSeconds,
//     pomodoroInterval,
//     sound,
//     soundVolume,
//   } = mapUserSettingsFormDataToState(settingsFormData);
//
//   return {
//     taskSeconds,
//     shortBreakSeconds,
//     longBreakSeconds,
//     pomodoroInterval,
//     sound,
//     soundVolume,
//   };
// }
//
// export function mapUserSettingsFormDataToState(
//   settingsFormData: UserSettingsFormData
// ): UserSettingsState {
//   const {
//     taskTimeHours,
//     taskTimeMinutes,
//     taskTimeSeconds,
//     shortBreakHours,
//     shortBreakMinutes,
//     shortBreakSeconds,
//     longBreakHours,
//     longBreakMinutes,
//     longBreakSeconds,
//     pomodoroInterval,
//     sound,
//     soundVolume,
//   } = settingsFormData;
//
//   const taskTimeUnits = {
//     hours: taskTimeHours,
//     mins: taskTimeMinutes,
//     secs: taskTimeSeconds || 0,
//   };
//   const shortBreakTimeUnits = {
//     hours: shortBreakHours,
//     mins: shortBreakMinutes,
//     secs: shortBreakSeconds || 0,
//   };
//   const longBreakTimeUnits = {
//     hours: longBreakHours,
//     mins: longBreakMinutes,
//     secs: longBreakSeconds || 0,
//   };
//
//   return {
//     taskSeconds: timeUnitsToSeconds(taskTimeUnits),
//     shortBreakSeconds: timeUnitsToSeconds(shortBreakTimeUnits),
//     longBreakSeconds: timeUnitsToSeconds(longBreakTimeUnits),
//     taskTimeUnits,
//     shortBreakTimeUnits,
//     longBreakTimeUnits,
//     pomodoroInterval,
//     sound,
//     soundVolume,
//   };
// }
