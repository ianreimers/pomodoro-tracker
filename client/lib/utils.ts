import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"
import { TimeUnits, UserSettings, UserSettingsState, WeekAnalaytics } from "@/types/types"
import { UserSettingsFormData } from "@/validation_schema/schemas"
import { PomodoroTotalsAPIData, PomodoroTotalUIData } from "@/types/types"
import humanize from "humanize-duration"
import humanizeDuration from "humanize-duration"

const shortEnglishHumanizer = humanizeDuration.humanizer({
  language: "shortEn",
  spacer: "",
  languages: {
    shortEn: {
      y: () => "y",
      mo: () => "mo",
      w: () => "w",
      d: () => "d",
      h: () => "h",
      m: () => "m",
      s: () => "s",
      ms: () => "ms",
    },
  },
})

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function timeUnitsToSeconds({ hours, mins, secs }: TimeUnits) {
  return secs + (mins * 60) + (hours * 60 * 60)

}

function secondsToMinutes(seconds: number) {
  return Math.floor(seconds / 60)
}

export function secondsToTime(seconds: number) {
  const secs = seconds % 60;
  const mins = Math.floor(seconds / 60) % 60;
  const hours = Math.floor(seconds / 60 / 60) % 24;

  const secs_str = secs.toString().padStart(2, "0")
  const mins_str = mins.toString().padStart(2, "0")
  const hours_str = hours.toString().padStart(2, "0")

  return `${hours_str}:${mins_str}:${secs_str}`
}

export function title(words: string) {
  const word_arr = words.split(" ");

  for (let i = 0; i < word_arr.length; ++i) {
    word_arr[i] = word_arr[i].charAt(0).toUpperCase() + word_arr[i].slice(1);
  }

  return word_arr.join(" ");
}

export function secondsToTimeUnits(seconds: number, withPadding: boolean = false) {
  const secs = seconds % 60;
  const mins = Math.floor(seconds / 60) % 60;
  const hours = Math.floor(seconds / 60 / 60) % 24;

  if (withPadding) {
    const hours_str = hours.toString().padStart(2, "0")
    const mins_str = mins.toString().padStart(2, "0")
    const secs_str = secs.toString().padStart(2, "0")

    return {
      hours: hours_str,
      mins: mins_str,
      secs: secs_str
    }
  }

  return {
    hours,
    mins,
    secs
  }
}

export function mapSettingsToState(settings: UserSettings): UserSettingsState {
  return {
    taskSeconds: settings.taskSeconds,
    shortBreakSeconds: settings.shortBreakSeconds,
    longBreakSeconds: settings.longBreakSeconds,
    taskTimeUnits: secondsToTimeUnits(settings.taskSeconds),
    shortBreakTimeUnits: secondsToTimeUnits(settings.shortBreakSeconds),
    longBreakTimeUnits: secondsToTimeUnits(settings.longBreakSeconds),
    pomodoroInterval: settings.pomodoroInterval
  }
}

export function mapUserSettingsFormDataToRequest(settingsFormData: UserSettingsFormData): UserSettings {
  const {
    taskSeconds,
    shortBreakSeconds,
    longBreakSeconds,
    pomodoroInterval } = mapUserSettingsFormDataToState(settingsFormData);

  return {
    taskSeconds,
    shortBreakSeconds,
    longBreakSeconds,
    pomodoroInterval
  }
}

export function mapUserSettingsFormDataToState(settingsFormData: UserSettingsFormData): UserSettingsState {
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
    pomodoroInterval

  } = settingsFormData;

  const taskTimeUnits = {
    hours: taskTimeHours,
    mins: taskTimeMinutes,
    secs: taskTimeSeconds
  };
  const shortBreakTimeUnits = {
    hours: shortBreakHours,
    mins: shortBreakMinutes,
    secs: shortBreakSeconds,

  };
  const longBreakTimeUnits = {
    hours: longBreakHours,
    mins: longBreakMinutes,
    secs: longBreakSeconds,
  };

  return {
    taskSeconds: timeUnitsToSeconds(taskTimeUnits),
    shortBreakSeconds: timeUnitsToSeconds(shortBreakTimeUnits),
    longBreakSeconds: timeUnitsToSeconds(longBreakTimeUnits),
    taskTimeUnits,
    shortBreakTimeUnits,
    longBreakTimeUnits,
    pomodoroInterval
  }
}


export function mapTotalDataToTodayUIData(data: PomodoroTotalsAPIData): PomodoroTotalUIData {
  return {
    totalTasks: {
      title: "Tasks",
      data: data.totalTasks
    },
    totalTaskSeconds: {
      title: "Task Time",
      data: shortEnglishHumanizer(data.totalTaskSeconds * 1000)
    },
    totalPomodoros: {
      title: "Pomodoros Completed",
      data: data.totalPomodoros
    },
    totalSeconds: {
      title: "Cumulative Time",
      data: shortEnglishHumanizer(data.totalSeconds * 1000)
    }
  }
}

export function mapWeekAnalyticsToChartData(data: WeekAnalaytics[]) {
  const daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
  const chartData = [];

  if (!data.length) {
    for (let day of daysOfWeek) {
      const dayObj = {
        dayOfTheWeek: day,
        totalTaskMinutes: 0,
        totalShortBreakMinutes: 0,
        totalLongBreakMinutes: 0
      }
      chartData.push(dayObj);
    }
    return chartData;
  }
  for (let i = 0; i < daysOfWeek.length; ++i) {
    const day = daysOfWeek[i];

    const potentialObj = data.find(obj => obj.dayOfTheWeek.trim() === day);

    if (potentialObj) {
      chartData.push({
        dayOfTheWeek: day,
        totalTaskMinutes: secondsToMinutes(potentialObj.totalTaskSeconds),
        totalShortBreakMinutes: secondsToMinutes(potentialObj.totalShortBreakSeconds),
        totalLongBreakMinutes: secondsToMinutes(potentialObj.totalLongBreakSeconds)

      })
      continue;
    }

    chartData.push({
      dayOfTheWeek: day,
      totalTaskMinutes: 0,
      totalShortBreakMinutes: 0,
      totalLongBreakMinutes: 0
    });

  }
  return chartData;
}
