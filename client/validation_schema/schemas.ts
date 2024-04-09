import { z } from 'zod'

export const userSettingsFormSchema = z.object({
  taskTimeHours: z.coerce.number().int().max(23).min(0),
  taskTimeMinutes: z.coerce.number().int().max(59).min(1),
  taskTimeSeconds: z.coerce.number().int(),
  shortBreakHours: z.coerce.number().int().max(23).min(0),
  shortBreakMinutes: z.coerce.number().int().max(59).min(1),
  shortBreakSeconds: z.coerce.number().int(),
  longBreakHours: z.coerce.number().int().max(23).min(0),
  longBreakMinutes: z.coerce.number().int().max(59).min(0),
  longBreakSeconds: z.coerce.number().int(),
  pomodoroInterval: z.coerce.number().int().min(1),
  sound: z.coerce.string(),
  soundVolume: z.number(),
})

export type UserSettingsFormData = z.infer<typeof userSettingsFormSchema>
