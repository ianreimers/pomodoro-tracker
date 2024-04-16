import { z } from 'zod';

export const userSettingsFormSchema = z
  .object({
    taskTimeHours: z.coerce.number().int().max(23).min(0),
    taskTimeMinutes: z.coerce.number().int().max(59).min(0),
    taskTimeSeconds: z.coerce.number().int(),
    shortBreakHours: z.coerce.number().int().max(23).min(0),
    shortBreakMinutes: z.coerce.number().int().max(59).min(0),
    shortBreakSeconds: z.coerce.number().int(),
    longBreakHours: z.coerce.number().int().max(23).min(0),
    longBreakMinutes: z.coerce.number().int().max(59).min(0),
    longBreakSeconds: z.coerce.number().int(),
    pomodoroInterval: z.coerce.number().int().min(1),
    sound: z.coerce.string(),
    soundVolume: z.number(),
  })
  .superRefine((data, ctx) => {
    if (data.taskTimeHours > 0 && data.taskTimeMinutes < 1) {
      // This is accepted
    } else if (data.taskTimeHours === 0 && data.taskTimeMinutes === 0) {
      ctx.addIssue({
        code: 'custom',
        message: 'Task hours or minutes must be greater than 0',
        path: ['taskTimeMinutes'],
      });
    }

    if (data.shortBreakHours > 0 && data.shortBreakMinutes < 1) {
      // This is accepted
    } else if (data.shortBreakHours === 0 && data.shortBreakMinutes === 0) {
      ctx.addIssue({
        code: 'custom',
        message: 'Short break hours or minutes must be greater than 0',
        path: ['shortBreakMinutes'],
      });
    }

    if (data.longBreakHours > 0 && data.longBreakMinutes < 1) {
      // This is accepted
    } else if (data.longBreakHours === 0 && data.longBreakMinutes === 0) {
      ctx.addIssue({
        code: 'custom',
        message: 'Long break hours or minutes must be greater than 0',
        path: ['longBreakMinutes'],
      });
    }
  });

export type UserSettingsFormData = z.infer<typeof userSettingsFormSchema>;
