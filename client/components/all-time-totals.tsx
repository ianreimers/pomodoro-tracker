import axiosInstance from "@/api/axiosInstance";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { Card, CardContent, CardTitle } from "./ui/card";
import { cn, mapTotalDataToTodayUIData } from "@/lib/utils";
import { PomodoroTotalsAPIData, PomodoroTotalUIData } from "@/types/types";

export function AllTimeTotals() {

	const queryClient = useQueryClient();

	const { data, isLoading, isError, isSuccess } = useQuery({
		queryKey: ["allTimeTotal"],
		queryFn: (): Promise<PomodoroTotalsAPIData> => axiosInstance.get("/pomodoro-sessions/analytics/all-time-totals").then(res => res.data)
	});

	if (isLoading) {
		return <p>Loading...</p>
	}

	if (isError || !data) {
		return <p>Error retreiving overview data from the server</p>
	}

	const uiData = mapTotalDataToTodayUIData(data);
	//Object.keys(mapOverviewDataToUIData(data)) as Array<keyof PomodoroOverviewUIData>;

	return (
		<>

			<h2 className="text-2xl font-bold">All Time</h2>
			<div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
				{
					(Object.keys(uiData) as Array<keyof PomodoroTotalUIData>).map(key => (
						<Card key={key}>
							<CardTitle >
								<p className="text-base p-6 pb-1">{uiData[key].title}</p>
							</CardTitle>
							<CardContent><p className="text-2xl font-bold">{uiData[key].data}</p></CardContent>

						</Card>

					))
				}
			</div>
		</>
	)
}
