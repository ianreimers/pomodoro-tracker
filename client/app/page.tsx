"use client"

import { Button } from "@/components/ui/button";
import { useEffect, useState } from "react";
import { secondsToTime } from "@/lib/utils";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"


const TIME = 1500;

export default function Home() {
  const [time, setTime] = useState(TIME);
  const [isPlaying, setIsPlaying] = useState(false);
  const [intervalTimeRemaing, setIntervalTimeRemaining] = useState(0);

  useEffect(() => {
    if (!isPlaying) {
      return
    }

    // Capture the moment, in millis, this second began
    let startTime = Date.now();

    const intervalId = setInterval(() => {
      setTime(time - 1);

      // Interval completed so ensure that the remaining time from last stoppage is reset
      setIntervalTimeRemaining((_) => 0);

    }, 1000 - intervalTimeRemaing); // The remaining time is when the user paused in (second - timeRemaining)

    return () => {
      const stopTime = Date.now();
      const milliDiff = stopTime - startTime;

      setIntervalTimeRemaining((t) => {
        if (t == 0 && milliDiff >= 1000) {
          return 0
        }

        return milliDiff + t;
      })

      // Clear the interval upon unmount
      clearInterval(intervalId);
    }
  }, [time, isPlaying])

  function handleClick() {
    setIsPlaying(!isPlaying);
  }


  return (
    <Tabs defaultValue="session">
      <TabsList>
        <TabsTrigger value="session">Session</TabsTrigger>
        <TabsTrigger value="settings">Settings</TabsTrigger>

      </TabsList>
      <TabsContent value="session">
        <div className="flex flex-col items-center w-full">
          <p className="text-9xl mb-8 text-center w-full">{secondsToTime(time)}</p>
          <Button variant={"outline"} size="lg" onClick={handleClick}>{isPlaying ? "Stop" : "Start"}</Button>


        </div>

      </TabsContent>

    </Tabs>

  )
}
