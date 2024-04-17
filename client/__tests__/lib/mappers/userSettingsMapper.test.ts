import { mapSettingsToState } from '@/lib/mappers/userSettingsMapper';
import { TimeUnitNums, UserSettings } from '@/types/types';

describe('User Settings Mapper', () => {
  it('should map user settings to state', () => {
    const settings: UserSettings = {
      sound: 'bells',
      soundVolume: 75,
      taskSeconds: 1500,
      longBreakSeconds: 300,
      shortBreakSeconds: 300,
      pomodoroInterval: 3,
    };

    const result = mapSettingsToState(settings);

    expect(result.pomodoroInterval).toBe(3);
    expect(result.soundVolume).toBe(75);
    expect(result.sound).toBe('bells');
    expect(result.shortBreakSeconds).toBe(300);
    expect(result.shortBreakTimeUnits).toEqual({
      hours: 0,
      mins: 5,
      secs: 0,
    } as TimeUnitNums);
    expect(result.longBreakSeconds).toBe(300);
    expect(result.longBreakTimeUnits).toEqual({
      hours: 0,
      mins: 5,
      secs: 0,
    } as TimeUnitNums);
    expect(result.taskSeconds).toBe(1500);
    expect(result.taskTimeUnits).toEqual({
      hours: 0,
      mins: 25,
      secs: 0,
    } as TimeUnitNums);
  });
});
