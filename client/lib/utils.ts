import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function secondsToTime(seconds: number) {
  let secs = seconds % 60;
  let mins = Math.floor(seconds / 60) % 60;
  let hours = Math.floor(seconds / 60 / 60) % 60;

  let secs_str = secs.toString().padStart(2, "0");
  let min_str = mins.toString().padStart(2, "0");
  let hours_str = hours.toString().padStart(2, "0");

  return `${hours_str}:${min_str}:${secs_str}`;
}
