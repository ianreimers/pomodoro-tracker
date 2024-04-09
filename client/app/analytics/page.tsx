"use client"
import TodayTotals from "@/components/today-totals";
import withAuth from "@/components/with-auth";

import { PageWrapper } from "@/components/page-wrapper";
import { AllTimeTotals } from "@/components/all-time-totals";
import WeekChart from "@/components/week-chart";



function AnalyticsPage() {

  return (
    <PageWrapper>
      <div className="flex-1 flex-col space-y-4">
        <h1 className="text-4xl font-bold">Analytics</h1>
        <h2 className="text-2xl font-bold">Today</h2>
        <TodayTotals />
        <h2 className="text-2xl font-bold">Week Totals</h2>
        <WeekChart />
        <h2 className="text-2xl font-bold">All Time</h2>
        <AllTimeTotals />
      </div >
    </PageWrapper>

  )

}

export default withAuth(AnalyticsPage);
