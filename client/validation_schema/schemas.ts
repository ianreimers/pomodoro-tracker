import { z } from 'zod';

export const userSettingsFormSchema = z.object({
  taskTimeHours: z.coerce.number().max(23).min(0),
  taskTimeMinutes: z.coerce.number().max(59).min(1),
  taskTimeSeconds: z.coerce.number(),
  shortBreakHours: z.coerce.number().max(23).min(0),
  shortBreakMinutes: z.coerce.number().max(59).min(1),
  shortBreakSeconds: z.coerce.number(),
  longBreakHours: z.coerce.number().max(23).min(0),
  longBreakMinutes: z.coerce.number().max(59).min(0),
  longBreakSeconds: z.coerce.number(),
  pomodoroInterval: z.coerce.number().min(1),
  sound: z.coerce.string()
});

export type UserSettingsFormData = z.infer<typeof userSettingsFormSchema>;
