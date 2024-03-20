import { z } from 'zod';

export const userSettingsFormSchema = z.object({
  taskTimeHours: z.coerce.number().max(23).min(0),
  taskTimeMinutes: z.coerce.number(),
  taskTimeSeconds: z.coerce.number(),
  shortBreakHours: z.coerce.number(),
  shortBreakMinutes: z.coerce.number(),
  shortBreakSeconds: z.coerce.number(),
  longBreakHours: z.coerce.number(),
  longBreakMinutes: z.coerce.number(),
  longBreakSeconds: z.coerce.number(),
  pomodoroInterval: z.coerce.number()
});

export type UserSettingsFormData = z.infer<typeof userSettingsFormSchema>;
