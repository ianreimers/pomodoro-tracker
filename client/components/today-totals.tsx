import axiosInstance from "@/api/axiosInstance";
import { useQuery } from "@tanstack/react-query";
import { Card, CardContent, CardTitle } from "./ui/card";
import { mapTotalDataToTodayUIData } from "@/lib/utils";
import { PomodoroTotalsAPIData, PomodoroTotalUIData } from "@/types/types";


export default function TodayTotals() {
	const { data, isLoading, isError } = useQuery({
		queryKey: ["dailyTotal"],
		queryFn: (): Promise<PomodoroTotalsAPIData> => axiosInstance.get("/pomodoro-sessions/analytics/today-totals").then(res => res.data)
	});

	if (isLoading) {
		return <p>Loading...</p>
	}

	if (isError || !data) {
		return <p>Error retreiving overview data from the server</p>
	}

	const uiData = mapTotalDataToTodayUIData(data);

	return (
		<>

			<div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
				{
					(Object.keys(uiData) as Array<keyof PomodoroTotalUIData>).map(key => (
						<Card key={key}>
							<CardTitle >
								<p className="font-normal text-base p-6 pb-1">{uiData[key].title}</p>
							</CardTitle>
							<CardContent><p className="text-2xl font-bold">{uiData[key].data}</p></CardContent>

						</Card>

					))
				}
			</div>
		</>
	)
}
