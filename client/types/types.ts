
export interface Credentials {
  username: string;
  password: string;
}

export interface AuthContextType {
  user: User | null;
  isLoading: boolean;
  login: (credentials: Credentials) => Promise<void>;
  register: (data: RegistrationData) => Promise<void>;
  logout: () => void;
  isAuthenticated: () => boolean;
}

export interface AuthState {
  user: User | null;
  token: string;
  isLoading: boolean;

}

export type SessionType = "task" | "short break" | "long break";

export interface PomodoroContextType {
  remainingSeconds: number;
  currSessionType: SessionType;
  isPlaying: boolean;
  totalPomodoros: number;
  resetCycle: () => void,
  togglePlaying: () => void;
  skipSession: () => void;
};

export interface PomodoroReducerState {
  remainingSeconds: number;
  intervalTimeRemaining: number;
  isPlaying: boolean;
  totalPomodoros: number;
  currSessionType: SessionType;
  currPomodoroId: string | number;
  currBreakType: "SHORT" | "LONG"
}

export interface PomodoroSession {
  tempUuid: string;
  taskDuration: number;
  breakDuration: number;
  sessionTaskSeconds: number;
  sessionShortBreakSeconds: number;
  sessionLongBreakSeconds: number;
  sessionStartTime: string;
  sessionUpdateTime: string;
  breakType: string;
}

export interface TimeUnitNums {
  hours: number;
  mins: number;
  secs: number;
}

export interface TimeUnitStrs {
  hours: string;
  mins: string;
  secs: string;
}

export interface UserSettings {
  taskSeconds: number;
  shortBreakSeconds: number;
  longBreakSeconds: number;
  pomodoroInterval: number;
  sound: string;
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
  taskTimeUnits: TimeUnitNums;
  shortBreakTimeUnits: TimeUnitNums;
  longBreakTimeUnits: TimeUnitNums;
  pomodoroInterval: number;
  sound: string;

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
