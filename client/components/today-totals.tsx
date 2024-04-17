import { Card, CardContent, CardTitle } from './ui/card';
import { LoaderIcon } from 'lucide-react';
import { mapTotalDataToTodayUIData } from '@/lib/mappers/pomodoroMapper';
import { PomodoroTotalUIData } from '@/types/types';
import { useTodayTotals } from '@/services/queries';

export default function TodayTotals() {
  const todayTotals = useTodayTotals();

  if (todayTotals.isPending) {
    return (
      <div className="flex items-center justify-center">
        <LoaderIcon className="animate-spin" />
      </div>
    );
  }

  if (todayTotals.isError) {
    return (
      <div className="flex items-center justify-center">
        <p>Error retreiving overview data from the server</p>
      </div>
    );
  }

  const uiData = mapTotalDataToTodayUIData(todayTotals.data);

  return (
    <>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {(Object.keys(uiData) as Array<keyof PomodoroTotalUIData>).map(
          (key) => (
            <Card key={key}>
              <CardTitle>
                <p className="font-normal text-base p-6 pb-1">
                  {uiData[key].title}
                </p>
              </CardTitle>
              <CardContent>
                <p className="text-2xl font-bold">{uiData[key].data}</p>
              </CardContent>
            </Card>
          )
        )}
      </div>
    </>
  );
}
