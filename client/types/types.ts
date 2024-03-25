
export type SessionType = "task" | "short break" | "long break";

export interface TimeUnits {
  hours: number
  mins: number,
  secs: number,
}

export interface UserSettings {
  taskSeconds: number;
  shortBreakSeconds: number;
  longBreakSeconds: number;
  pomodoroInterval: number;
}

export interface AuthenticatedUser {
  token: string;
  username: string;
  userSettings: UserSettings
}

export interface User {
  username: string;
}

export interface RegistrationData {
  username: string;
  password: string;
  email: string;
}

export interface UserSettingsState {
  taskSeconds: number;
  shortBreakSeconds: number;
  longBreakSeconds: number;
  taskTimeUnits: TimeUnits;
  shortBreakTimeUnits: TimeUnits;
  longBreakTimeUnits: TimeUnits;
  pomodoroInterval: number;

}

export type PomodoroTotalsAPIData = {
  totalTasks: number;
  totalTaskSeconds: number;
  totalPomodoros: number;
  totalSeconds: number;
}

export interface PomodoroTotalUIData {
  totalTasks: {
    title: String,
    data: number
  }
  totalTaskSeconds: {
    title: String,
    data: string
  }
  totalPomodoros: {
    title: String,
    data: number
  }
  totalSeconds: {
    title: String,
    data: string
  }
}

export interface WeekAnalaytics {
  dayOfTheWeek: String;
  totalLongBreakSeconds: number;
  totalShortBreakSeconds: number;
  totalTaskSeconds: number;
}
