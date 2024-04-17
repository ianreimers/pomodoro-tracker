import { Card, CardContent, CardTitle } from './ui/card';
import { mapTotalDataToTodayUIData } from '@/lib/mappers/pomodoroMapper';
import { PomodoroTotalUIData } from '@/types/types';
import { useAllTimeTotals } from '@/services/queries';
import { LoaderIcon } from 'lucide-react';

export function AllTimeTotals() {
  const allTimeTotals = useAllTimeTotals();

  if (allTimeTotals.isPending) {
    return (
      <div className="flex items-center justify-center">
        <LoaderIcon className="animate-spin" />
      </div>
    );
  }

  if (allTimeTotals.isError) {
    return (
      <div className="flex items-center justify-center">
        <p>Error retreiving overview data from the server</p>
      </div>
    );
  }

  const uiData = mapTotalDataToTodayUIData(allTimeTotals.data);

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
