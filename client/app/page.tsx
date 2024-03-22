"use client"

import { Button } from "@/components/ui/button";
import { secondsToTime, title } from "@/lib/utils";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import UserSettingsForm from "@/components/user-settings-form";
import usePomodoro from "@/hooks/pomodoro-hook";
import { usePomodoroContext } from "@/contexts/pomodoro-context";


export default function Home() {
  const {
    remainingSeconds,
    currSessionType,
    isPlaying,
    totalPomodoros,
    togglePlaying
  } = usePomodoroContext();


  function handleClick() {
    togglePlaying();
  }

  return (
    <div className="max-w-screen-sm mx-auto mt-12">
      <Tabs defaultValue="session">
        <TabsList className="grid w-full grid-cols-2">
          <TabsTrigger value="session">Session</TabsTrigger>
          <TabsTrigger value="settings">Settings</TabsTrigger>
        </TabsList>
        <TabsContent value="session">
          <div className="flex flex-col items-center w-full bg-background">
            <h2 className="text-4xl w-full text-center my-4 font-bold text-foreground underline">{title(currSessionType)}</h2>
            <p className="text-9xl mb-8 text-center w-full text-foreground">{secondsToTime(remainingSeconds)}</p>
            <h3 className="mb-4">Total Pomodoros: {totalPomodoros}</h3>
            <Button size="lg" variant="default" onClick={handleClick}>{isPlaying ? "Stop" : "Start"}</Button>
          </div>

        </TabsContent>
        <TabsContent value="settings">
          <UserSettingsForm />
        </TabsContent>

      </Tabs>

    </div>

  )
}
