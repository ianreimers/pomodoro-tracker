import {
  mapTotalDataToTodayUIData,
  mapWeekAnalyticsToChartData,
} from '@/lib/mappers/pomodoroMapper';
import {
  DayOfTheWeek,
  PomodoroTotalsResponse,
  WeekAnalayticsResponse,
  WeekChartData,
} from '@/types/types';
import { minutesToSeconds } from 'date-fns';

describe('Pomodoro Mappers', () => {
  it('should map today total data to ui', () => {
    const data: PomodoroTotalsResponse = {
      totalTasks: 4,
      totalSeconds: minutesToSeconds(50),
      totalPomodoros: 2,
      totalTaskSeconds: minutesToSeconds(25),
    };

    const result = mapTotalDataToTodayUIData(data);

    expect(result.totalTasks.title).toBe('Tasks Completed');
    expect(result.totalTasks.data).toBe(4);
    expect(result.totalTaskSeconds.title).toBe('Total Task Time');
    expect(result.totalTaskSeconds.data).toBe('25m');
    expect(result.totalPomodoros.title).toBe('Pomodoros Completed');
    expect(result.totalPomodoros.data).toBe(2);
    expect(result.totalSeconds.title).toBe('Total Time');
    expect(result.totalSeconds.data).toBe('50m');
  });

  it('should map empty week totals to chart data', () => {
    const data: WeekAnalayticsResponse[] = [];
    const daysOfWeek: DayOfTheWeek[] = [
      'Monday',
      'Tuesday',
      'Wednesday',
      'Thursday',
      'Friday',
      'Saturday',
      'Sunday',
    ];

    const result = mapWeekAnalyticsToChartData(data);

    for (let i = 0; i < daysOfWeek.length; i++) {
      expect(result[i].dayOfTheWeek).toBe(daysOfWeek[i]);
      expect(result[i].totalTaskMinutes).toBe(0);
      expect(result[i].totalShortBreakMinutes).toBe(0);
      expect(result[i].totalLongBreakMinutes).toBe(0);
    }
  });

  it('should map week totals to chart data', () => {
    const data: WeekAnalayticsResponse[] = [
      {
        dayOfTheWeek: 'Tuesday',
        totalTaskSeconds: 60,
        totalLongBreakSeconds: 60,
        totalShortBreakSeconds: 50,
      },
    ];

    const result = mapWeekAnalyticsToChartData(data);

    expect(result[0].dayOfTheWeek).toBe('Monday');
    expect(result[0].totalTaskMinutes).toBe(0);
    expect(result[0].totalShortBreakMinutes).toBe(0);
    expect(result[0].totalLongBreakMinutes).toBe(0);

    expect(result[1].dayOfTheWeek).toBe('Tuesday');
    expect(result[1].totalTaskMinutes).toBe(1);
    expect(result[1].totalShortBreakMinutes).toBe(0);
    expect(result[1].totalLongBreakMinutes).toBe(1);
  });
});
