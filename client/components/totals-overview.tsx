import axiosInstance from "@/api/axiosInstance";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { Card, CardContent, CardTitle } from "./ui/card";
import { cn } from "@/lib/utils";

type PomodoroOverview = {
	dailyTotal: number;
	totalCompletedPomodoros: number;
	totalCompletedTasks: number;
	totalTaskSeconds: number;
}

export default function TotalsOverview() {
	const queryClient = useQueryClient();

	const { data, isLoading, isError, isSuccess } = useQuery({
		queryKey: ["dailyTotal"],
		queryFn: (): Promise<PomodoroOverview> => axiosInstance.get("/pomodoro-sessions/analytics/daily-total").then(res => res.data)
	});

	if (isLoading) {
		return <p>Loading...</p>
	}

	if (isError) {
		return <p>Error occurred</p>
	}

	return (
		<>
			{isSuccess &&

				<div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
					{
						(Object.keys(data) as Array<keyof PomodoroOverview>).map(key => (
							<Card key={key}>
								<CardTitle >
									<p className="text-base p-6 pb-1">{key}</p>
								</CardTitle>
								<CardContent><p className="text-2xl font-bold">{data[key]}</p></CardContent>

							</Card>

						))
					}
				</div>
			}
		</>
	)
}
