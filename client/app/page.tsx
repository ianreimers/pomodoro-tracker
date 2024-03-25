"use client"

import { Button } from "@/components/ui/button";
import { secondsToTime, secondsToTimeUnits, title } from "@/lib/utils";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import UserSettingsForm from "@/components/user-settings-form";
import { usePomodoroContext } from "@/contexts/pomodoro-context";
import { PageWrapper } from "@/components/page-wrapper";


export default function Home() {
  const {
    remainingSeconds,
    currSessionType,
    isPlaying,
    totalPomodoros,
    togglePlaying
  } = usePomodoroContext();
  const timeUnits = secondsToTimeUnits(remainingSeconds, true);


  function handleClick() {
    togglePlaying();
  }

  return (
    <PageWrapper>
      <div className="max-w-4xl mx-auto p-8">
        <Tabs defaultValue="session">
          <TabsList className="grid w-full grid-cols-2">
            <TabsTrigger value="session">Session</TabsTrigger>
            <TabsTrigger value="settings">Settings</TabsTrigger>
          </TabsList>
          <TabsContent value="session">
            <div className="flex flex-col items-center w-full bg-background">
              <h2 className="text-4xl w-full text-center my-4 font-bold text-foreground underline">{title(currSessionType)}</h2>
              <div className="flex items-center tabular-nums justify-center text-7xl sm:text-8xl lg:text-9xl p-6 mb-4 text-center w-full text-foreground">
                <span className="border-accent border-solid border-4 border-b-[10px] rounded-xl p-2 flex items-center justify-center">{timeUnits.hours}</span>
                <span className="text-6xl sm:text-7xl lg:text-8xl sm:px-0.5">:</span>
                <span className="border-accent border-solid border-4 border-b-8 rounded-xl p-2 flex items-center justify-center">{timeUnits.mins}</span>
                <span className="text-6xl sm:text-7xl lg:text-8xl sm:px-0.5">:</span>
                <span className="border-accent border-solid border-4 border-b-8 rounded-xl p-2 flex items-center justify-center">{timeUnits.secs}</span>
              </div>
              <h3 className="mb-4">Total Pomodoros: {totalPomodoros}</h3>
              <Button size="lg" variant="default" onClick={handleClick}>{isPlaying ? "Stop" : "Start"}</Button>
            </div>

          </TabsContent>
          <TabsContent value="settings">
            <UserSettingsForm />
          </TabsContent>

        </Tabs>
      </div>

    </PageWrapper>

  )
}
