import { useQuery } from '@tanstack/react-query';
import {
  getAllTimeTotals,
  getTodayTotals,
  getUserSettings,
  getWeekTotals,
} from './api';

export function useUserSettings(isAuthenticated: boolean) {
  return useQuery({
    queryKey: ['userSettings'],
    queryFn: getUserSettings,
    enabled: isAuthenticated,
    refetchOnWindowFocus: false,
  });
}

export function useTodayTotals() {
  return useQuery({
    queryKey: ['todayTotals'],
    queryFn: getTodayTotals,
  });
}

export function useWeekTotals() {
  return useQuery({
    queryKey: ['weekTotals'],
    queryFn: getWeekTotals,
  });
}

export function useAllTimeTotals() {
  return useQuery({
    queryKey: ['allTimeTotals'],
    queryFn: getAllTimeTotals,
  });
}
