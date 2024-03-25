"use client"
import axiosInstance from "@/api/axiosInstance";
import TodayTotals from "@/components/today-totals";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import withAuth from "@/components/with-auth";
import { useAuthContext } from "@/contexts/auth-context";
import { useQuery } from "@tanstack/react-query";
import { Bar, BarChart, CartesianGrid, Legend, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts"

import tailwindConfig from "@/tailwind.config"
import { PageWrapper } from "@/components/page-wrapper";
import { AllTimeTotals } from "@/components/all-time-totals";
import { mapWeekAnalyticsToChartData } from "@/lib/utils";

const primaryColor = tailwindConfig.theme.extend.colors.primary.DEFAULT;
const primaryColorForeground = tailwindConfig.theme.extend.colors.primary.foreground;
const secondaryColor = tailwindConfig.theme.extend.colors.secondary.DEFAULT;
const accentColor = tailwindConfig.theme.extend.colors.accent.foreground;
const mutedColor = tailwindConfig.theme.extend.colors.muted.foreground;
const foregroundColor = tailwindConfig.theme.extend.colors.foreground;
const destructiveColor = tailwindConfig.theme.extend.colors.destructive.DEFAULT;


function DashboardPage() {

  const { data, isLoading, isError } = useQuery({
    queryKey: ["currentWeekTotals"],
    queryFn: () => axiosInstance.get("/pomodoro-sessions/analytics/current-week").then(res => res.data),

  });

  if (isLoading) {
    return <p>Loading...</p>
  }

  if (isError) {
    return <p>Error retreiving week analytics</p>
  }


  return (
    <PageWrapper>
      <div className="flex-1 space-y-4 p-8 pt-6">
        <h1 className="text-4xl font-bold">Dashboard</h1>
        <TodayTotals />
        <Card>
          <CardHeader>
            <p className="text-xl font-semibold">Week Totals</p>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer className="mt-4" width="100%" height={450} >
              <BarChart data={mapWeekAnalyticsToChartData(data)}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="dayOfTheWeek" />
                <YAxis label={{ value: "Minutes", angle: -90, position: "insideLeft" }} />
                <Tooltip contentStyle={{ backgroundColor: primaryColorForeground }} />
                <Legend />
                <Bar dataKey="totalTaskMinutes" name="Task Minutes" fill={primaryColor} />
                <Bar dataKey="totalShortBreakMinutes" name="Short Break" fill={mutedColor} />
                <Bar dataKey="totalLongBreakMinutes" name="Long Break" fill={destructiveColor} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
        <AllTimeTotals />
      </div >
    </PageWrapper>

  )

}

export default withAuth(DashboardPage);
