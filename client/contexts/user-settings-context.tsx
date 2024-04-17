'use client';

import { useToast } from '@/components/ui/use-toast';
import {
  mapSettingsToState,
  mapUserSettingsFormDataToState,
} from '@/lib/mappers/userSettingsMapper';
import { secondsToTimeUnits, timeUnitsToSeconds } from '@/lib/timeConversions';
import { TimeUnitNums, UserSettings, UserSettingsState } from '@/types/types';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { createContext, useContext, useEffect, useReducer } from 'react';
import { useAuthContext } from '@/contexts/auth/auth-context';
import { axiosInstance } from '@/services/api';
import { UserSettingsFormData } from '@/validation_schema/schemas';
import { isEqual } from 'lodash';
import { useUserSettings } from '@/services/queries';
import { useUpdateUserSettings } from '@/services/mutations';

type UserContextProviderProps = {
  children: React.ReactNode;
  initialStateOverride?: Partial<UserSettingsState>;
};

interface UserSettingsContextType {
  taskSeconds: number;
  shortBreakSeconds: number;
  longBreakSeconds: number;
  taskTimeUnits: TimeUnitNums;
  shortBreakTimeUnits: TimeUnitNums;
  longBreakTimeUnits: TimeUnitNums;
  pomodoroInterval: number;
  sound: string;
  soundVolume: number;
  updateSettings: (newSettings: UserSettingsFormData) => void;
}

const initialTaskTimeUnits = { hours: 0, mins: 25, secs: 0 };
const initialShortBreakTimeUnits = { hours: 0, mins: 10, secs: 0 };
const initialLongBreakTimeUnits = { hours: 0, mins: 20, secs: 0 };
const initialState: UserSettingsState = {
  taskTimeUnits: initialTaskTimeUnits,
  shortBreakTimeUnits: initialShortBreakTimeUnits,
  longBreakTimeUnits: initialLongBreakTimeUnits,
  taskSeconds: timeUnitsToSeconds(initialTaskTimeUnits),
  shortBreakSeconds: timeUnitsToSeconds(initialShortBreakTimeUnits),
  longBreakSeconds: timeUnitsToSeconds(initialLongBreakTimeUnits),
  pomodoroInterval: 4,
  sound: 'bells',
  soundVolume: 75,
};

export type UserSettingsAction =
  | { type: 'update_settings'; payload: UserSettingsState }
  | { type: 'default_settings' };

function reducer(
  state: UserSettingsState,
  action: UserSettingsAction
): UserSettingsState {
  switch (action.type) {
    case 'default_settings':
      return initialState;
    case 'update_settings':
      return action.payload;
  }
}

export const UserSettingsContext =
  createContext<UserSettingsContextType | null>(null);

export default function UserSettingsContextProvider({
  children,
  initialStateOverride,
}: UserContextProviderProps) {
  const { isAuthenticated } = useAuthContext();
  const [state, dispatch] = useReducer(reducer, {
    ...initialState,
    ...initialStateOverride,
  });
  const { toast } = useToast();
  const {
    taskSeconds,
    shortBreakSeconds,
    longBreakSeconds,
    taskTimeUnits,
    shortBreakTimeUnits,
    longBreakTimeUnits,
    pomodoroInterval,
    sound,
    soundVolume,
  } = state;

  const {
    data: userSettings,
    isPending,
    isLoading,
    isError,
    error,
  } = useUserSettings(isAuthenticated());

  const updateUserSettingsMutation = useUpdateUserSettings(dispatch);

  // Update the state with the fetched user settings for the logged in user
  useEffect(() => {
    if (isPending) {
      return;
    }

    if (isError) {
      toast({
        description: error.message,
      });
      return;
    }

    dispatch({
      type: 'update_settings',
      payload: mapSettingsToState(userSettings),
    });
  }, [userSettings]);

  // User logged out, ensure the state is reset
  useEffect(() => {
    dispatch({ type: 'default_settings' });
  }, [!isAuthenticated()]);

  function updateSettings(newSettings: UserSettingsFormData) {
    const newState = mapUserSettingsFormDataToState(newSettings);

    // State is the same so no need to update
    if (isEqual(newState, state)) {
      return;
    }

    toast({
      description: 'Your settings are updated',
    });

    // Dont send a mutation, only update the state if we're logged in
    if (!isAuthenticated()) {
      dispatch({
        type: 'update_settings',
        payload: newState,
      });
      return;
    }

    updateUserSettingsMutation.mutate(newState);
  }

  const contextValue: UserSettingsContextType = {
    taskSeconds,
    shortBreakSeconds,
    longBreakSeconds,
    taskTimeUnits,
    shortBreakTimeUnits,
    longBreakTimeUnits,
    pomodoroInterval,
    sound,
    soundVolume,
    updateSettings,
  };

  return (
    <UserSettingsContext.Provider value={contextValue}>
      {!isLoading && children}
    </UserSettingsContext.Provider>
  );
}

export function useUserSettingsContext() {
  const context = useContext(UserSettingsContext);

  if (!context) {
    throw new Error('useUserContext must be used within a UserContextProvider');
  }

  return context;
}
