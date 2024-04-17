import { useMutation, useQueryClient } from '@tanstack/react-query';
import { patchPomodoro, postPomodoro, putUserSettings } from './api';
import {
  PomodoroSession,
  PomodoroSessionPatchRequest,
  UserSettings,
} from '@/types/types';
import { useToast } from '@/components/ui/use-toast';
import { Dispatch } from 'react';
import { UserSettingsAction } from '@/contexts/user-settings-context';
import { PomodoroAction } from '@/contexts/pomodoro/pomodoro-reducer';

export function useUpdateUserSettings(dispatch: Dispatch<UserSettingsAction>) {
  const queryClient = useQueryClient();
  const { toast } = useToast();

  return useMutation({
    mutationFn: (data: UserSettings) => putUserSettings(data),
    onError: (error) => {
      toast({
        description: error.message,
      });
    },
    onSuccess: async (data) => {
      await queryClient.setQueryData(['userSettings'], data);
    },
  });
}

export function useCreatePomodoro(dispatch: Dispatch<PomodoroAction>) {
  const { toast } = useToast();

  return useMutation({
    mutationFn: (data: PomodoroSession) => postPomodoro(data),
    onError: (error) => {
      toast({
        description: error.message,
      });
    },
    onSuccess: (data) => {
      dispatch({
        type: 'update_pomdoro_id',
        payload: { newId: data.id },
      });
    },
  });
}

export function useUpdatePomodoro() {
  const { toast } = useToast();

  return useMutation({
    mutationFn: (data: { id: string | number; sessionType: string }) =>
      patchPomodoro(data),
    onError: (error) => {
      toast({
        description: error.message,
      });
    },
    onSuccess: () => {
      toast({
        description: 'Pomodoro progress saved',
      });
    },
  });
}
