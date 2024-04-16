import { TimeUnitNums, TimeUnitStrs } from '@/types/types';
import {
  secondsToMinutes,
  secondsToHours,
  minutesToSeconds,
  hoursToSeconds,
} from 'date-fns';

export function timeUnitsToSeconds({ hours, mins, secs }: TimeUnitNums) {
  return secs + minutesToSeconds(mins) + hoursToSeconds(hours);
}

export function secondsToTime(seconds: number) {
  const secs = seconds % 60;
  const mins = secondsToMinutes(seconds);
  const hours = secondsToHours(seconds);

  const secs_str = secs.toString().padStart(2, '0');
  const mins_str = mins.toString().padStart(2, '0');
  const hours_str = hours.toString().padStart(2, '0');

  return `${hours_str}:${mins_str}:${secs_str}`;
}

export function secondsToTimeUnits(
  seconds: number,
  withPadding: false
): TimeUnitNums;
export function secondsToTimeUnits(
  seconds: number,
  withPadding: true
): TimeUnitStrs;

export function secondsToTimeUnits(
  seconds: number,
  withPadding: boolean
): TimeUnitNums | TimeUnitStrs {
  const secs = seconds % 60;
  const mins = secondsToMinutes(seconds) % 60;
  const hours = secondsToHours(seconds);

  if (withPadding) {
    const hours_str = hours.toString().padStart(2, '0');
    const mins_str = mins.toString().padStart(2, '0');
    const secs_str = secs.toString().padStart(2, '0');
    const obj: TimeUnitStrs = {
      hours: hours_str,
      mins: mins_str,
      secs: secs_str,
    };

    return obj;
  }

  const obj: TimeUnitNums = {
    hours,
    mins,
    secs,
  };

  return obj;
}
