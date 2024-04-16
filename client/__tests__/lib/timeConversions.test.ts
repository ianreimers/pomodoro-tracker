import { secondsToTimeUnits } from '@/lib/timeConversions';
import { hoursToSeconds, minutesToSeconds } from 'date-fns';

describe('Time Conversion Seconds To Time Units', () => {
  it('should convert seconds to Time Units without padding', () => {
    const seconds = hoursToSeconds(12) + minutesToSeconds(25);

    const result = secondsToTimeUnits(seconds, false);

    expect(result.hours).toBe(12);
    expect(result.mins).toBe(25);
    expect(result.secs).toBe(0);
  });

  it('should convert seconds to Time Units with padding', () => {
    const seconds = hoursToSeconds(12) + minutesToSeconds(25);

    const result = secondsToTimeUnits(seconds, true);

    expect(result.hours).toBe('12');
    expect(result.mins).toBe('25');
    expect(result.secs).toBe('00');
  });
});
