import { UserSettings, UserSettingsState } from '@/types/types';
import { UserSettingsFormData } from '@/validation_schema/schemas';

import { secondsToTimeUnits, timeUnitsToSeconds } from '../timeConversions';

export function mapSettingsToState(settings: UserSettings): UserSettingsState {
  return {
    taskSeconds: settings.taskSeconds,
    shortBreakSeconds: settings.shortBreakSeconds,
    longBreakSeconds: settings.longBreakSeconds,
    taskTimeUnits: secondsToTimeUnits(settings.taskSeconds, false),
    shortBreakTimeUnits: secondsToTimeUnits(settings.shortBreakSeconds, false),
    longBreakTimeUnits: secondsToTimeUnits(settings.longBreakSeconds, false),
    pomodoroInterval: settings.pomodoroInterval,
    sound: settings.sound,
    soundVolume: settings.soundVolume,
  };
}
export function mapUserSettingsFormDataToRequest(
  settingsFormData: UserSettingsFormData
): UserSettings {
  const {
    taskSeconds,
    shortBreakSeconds,
    longBreakSeconds,
    pomodoroInterval,
    sound,
    soundVolume,
  } = mapUserSettingsFormDataToState(settingsFormData);

  return {
    taskSeconds,
    shortBreakSeconds,
    longBreakSeconds,
    pomodoroInterval,
    sound,
    soundVolume,
  };
}

export function mapUserSettingsFormDataToState(
  settingsFormData: UserSettingsFormData
): UserSettingsState {
  const {
    taskTimeHours,
    taskTimeMinutes,
    taskTimeSeconds,
    shortBreakHours,
    shortBreakMinutes,
    shortBreakSeconds,
    longBreakHours,
    longBreakMinutes,
    longBreakSeconds,
    pomodoroInterval,
    sound,
    soundVolume,
  } = settingsFormData;

  const taskTimeUnits = {
    hours: taskTimeHours,
    mins: taskTimeMinutes,
    secs: taskTimeSeconds || 0,
  };
  const shortBreakTimeUnits = {
    hours: shortBreakHours,
    mins: shortBreakMinutes,
    secs: shortBreakSeconds || 0,
  };
  const longBreakTimeUnits = {
    hours: longBreakHours,
    mins: longBreakMinutes,
    secs: longBreakSeconds || 0,
  };

  return {
    taskSeconds: timeUnitsToSeconds(taskTimeUnits),
    shortBreakSeconds: timeUnitsToSeconds(shortBreakTimeUnits),
    longBreakSeconds: timeUnitsToSeconds(longBreakTimeUnits),
    taskTimeUnits,
    shortBreakTimeUnits,
    longBreakTimeUnits,
    pomodoroInterval,
    sound,
    soundVolume,
  };
}
