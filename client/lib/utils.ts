import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function timeUnitsToSeconds(hours: number, mins: number, secs: number) {
  return secs + (mins * 60) + (hours * 60 * 60)

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
