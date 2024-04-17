import {
  PomodoroSession,
  PomodoroSessionResponse,
  PomodoroTotalsResponse,
  UserSettings,
  WeekAnalayticsResponse,
} from '@/types/types';
import axios from 'axios';

export const axiosInstance = axios.create({
  baseURL: `${process.env.NEXT_PUBLIC_API_BASE_URL}`,
  headers: {
    'Content-Type': 'application/json',
  },
});

export async function patchPomodoro(data: {
  sessionType: string;
  id: string | number;
}) {
  return (
    await axiosInstance.patch<void>(`/pomodoro-sessions/${data.id}`, {
      sessionType: data.sessionType,
    })
  ).data;
}

export async function getUserSettings() {
  return (await axiosInstance.get<UserSettings>('/settings')).data;
}

export async function putUserSettings(newSettings: UserSettings) {
  return (await axiosInstance.put<UserSettings>('/settings', newSettings)).data;
}

export async function postPomodoro(newPomodoro: PomodoroSession) {
  return (
    await axiosInstance.post<PomodoroSessionResponse>(
      '/pomodoro-sessions',
      newPomodoro
    )
  ).data;
}

export async function getTodayTotals() {
  return (
    await axiosInstance.get<PomodoroTotalsResponse>(
      '/pomodoro-sessions/analytics/today-totals'
    )
  ).data;
}

export async function getWeekTotals() {
  return (
    await axiosInstance.get<WeekAnalayticsResponse[]>(
      '/pomodoro-sessions/analytics/current-week'
    )
  ).data;
}

export async function getAllTimeTotals() {
  return (
    await axiosInstance.get<PomodoroTotalsResponse>(
      '/pomodoro-sessions/analytics/all-time-totals'
    )
  ).data;
}
