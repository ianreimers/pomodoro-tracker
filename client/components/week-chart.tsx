import {
  Bar,
  BarChart,
  CartesianGrid,
  Legend,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts';
import axiosInstance from '@/api/axiosInstance';
import { useQuery } from '@tanstack/react-query';
import { mapWeekAnalyticsToChartData } from '@/lib/mappers/pomodoroMapper';

import tailwindConfig from '@/tailwind.config';

const primaryColor = tailwindConfig.theme.extend.colors.primary.DEFAULT;
const primaryColorForeground =
  tailwindConfig.theme.extend.colors.primary.foreground;
const secondaryColor = tailwindConfig.theme.extend.colors.secondary.DEFAULT;
const accentColor = tailwindConfig.theme.extend.colors.accent.foreground;
const mutedColor = tailwindConfig.theme.extend.colors.muted.foreground;
const foregroundColor = tailwindConfig.theme.extend.colors.foreground;
const destructiveColor = tailwindConfig.theme.extend.colors.destructive.DEFAULT;

export default function WeekChart() {
  const { data, isLoading, isError } = useQuery({
    queryKey: ['currentWeekTotals'],
    queryFn: () =>
      axiosInstance
        .get('/pomodoro-sessions/analytics/current-week')
        .then((res) => res.data),
  });

  if (isLoading) {
    return <p>Loading...</p>;
  }

  if (isError) {
    return <p>Error retreiving week analytics</p>;
  }
  return (
    <ResponsiveContainer width="100%" height={450}>
      <BarChart className="m-0 p-0" data={mapWeekAnalyticsToChartData(data)}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="dayOfTheWeek" />
        <YAxis
          width={27}
          label={{
            value: 'Minutes',
            angle: -90,
            position: 'insideLeft',
            offset: 0,
          }}
        />
        <Tooltip contentStyle={{ backgroundColor: primaryColorForeground }} />
        <Legend />
        <Bar
          dataKey="totalTaskMinutes"
          name="Task Minutes"
          fill={primaryColor}
        />
        <Bar
          dataKey="totalShortBreakMinutes"
          name="Short Break"
          fill={mutedColor}
        />
        <Bar
          dataKey="totalLongBreakMinutes"
          name="Long Break"
          fill={destructiveColor}
        />
      </BarChart>
    </ResponsiveContainer>
  );
}
