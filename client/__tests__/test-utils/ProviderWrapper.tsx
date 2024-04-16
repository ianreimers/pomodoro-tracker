import React, { ReactNode } from 'react';
import PomodoroContextProvider from '@/contexts/pomodoro/pomodoro-context';
import UserSettingsContextProvider from '@/contexts/user-settings-context';
import AuthContextProvider from '@/contexts/auth/auth-context';
import { QueryClientProvider, QueryClient } from '@tanstack/react-query';
import { PomodoroReducerState } from '@/types/types';

const queryClient = new QueryClient();

interface Props {
  children: ReactNode;
  pomodoroProps?: Partial<PomodoroReducerState>;
}

export default function ProviderWrapper({ children, pomodoroProps }: Props) {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthContextProvider>
        <UserSettingsContextProvider>
          <PomodoroContextProvider initialStateOverride={{ ...pomodoroProps }}>
            {children}
          </PomodoroContextProvider>
        </UserSettingsContextProvider>
      </AuthContextProvider>
    </QueryClientProvider>
  );
}
