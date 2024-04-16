import {
  DayOfTheWeek,
  PomodoroTotalUIData,
  PomodoroTotalsResponse,
  WeekAnalayticsResponse,
  WeekChartData,
} from '@/types/types';
import { secondsToMinutes } from 'date-fns';
import humanizeDuration from 'humanize-duration';

const shortEnglishHumanizer = humanizeDuration.humanizer({
  language: 'shortEn',
  spacer: '',
  languages: {
    shortEn: {
      y: () => 'y',
      mo: () => 'mo',
      w: () => 'w',
      d: () => 'd',
      h: () => 'h',
      m: () => 'm',
      s: () => 's',
      ms: () => 'ms',
    },
  },
});

export function mapTotalDataToTodayUIData(
  data: PomodoroTotalsResponse
): PomodoroTotalUIData {
  return {
    totalTasks: {
      title: 'Tasks Completed',
      data: data.totalTasks,
    },
    totalTaskSeconds: {
      title: 'Total Task Time',
      data: shortEnglishHumanizer(data.totalTaskSeconds * 1000),
    },
    totalPomodoros: {
      title: 'Pomodoros Completed',
      data: data.totalPomodoros,
    },
    totalSeconds: {
      title: 'Total Time',
      data: shortEnglishHumanizer(data.totalSeconds * 1000),
    },
  };
}

export function mapWeekAnalyticsToChartData(
  data: WeekAnalayticsResponse[]
): WeekChartData[] {
  const daysOfWeek: DayOfTheWeek[] = [
    'Monday',
    'Tuesday',
    'Wednesday',
    'Thursday',
    'Friday',
    'Saturday',
    'Sunday',
  ];
  const chartData = [];

  if (!data.length) {
    for (let day of daysOfWeek) {
      const dayObj = {
        dayOfTheWeek: day,
        totalTaskMinutes: 0,
        totalShortBreakMinutes: 0,
        totalLongBreakMinutes: 0,
      };
      chartData.push(dayObj);
    }
    return chartData;
  }
  for (let i = 0; i < daysOfWeek.length; ++i) {
    const day = daysOfWeek[i];

    const potentialObj = data.find((obj) => obj.dayOfTheWeek.trim() === day);

    if (potentialObj) {
      chartData.push({
        dayOfTheWeek: day,
        totalTaskMinutes: secondsToMinutes(potentialObj.totalTaskSeconds),
        totalShortBreakMinutes: secondsToMinutes(
          potentialObj.totalShortBreakSeconds
        ),
        totalLongBreakMinutes: secondsToMinutes(
          potentialObj.totalLongBreakSeconds
        ),
      });
      continue;
    }

    chartData.push({
      dayOfTheWeek: day,
      totalTaskMinutes: 0,
      totalShortBreakMinutes: 0,
      totalLongBreakMinutes: 0,
    });
  }
  return chartData;
}
