import { usePomodoroContext } from '@/contexts/pomodoro/pomodoro-context';
import { secondsToTimeUnits } from '@/lib/timeConversions';
import { title } from '@/lib/utils';
import { Button } from './ui/button';
import { Pause, Play, RefreshCcw, SkipForward } from 'lucide-react';

export default function SessionTimer() {
  const {
    remainingSeconds,
    currSessionType,
    isPlaying,
    resetCycle,
    togglePlaying,
    skipSession,
  } = usePomodoroContext();
  const timeUnits = secondsToTimeUnits(remainingSeconds, true);

  function handleResetBtnClick() {
    resetCycle();
  }

  function handlePlayBtnClick() {
    togglePlaying();
  }

  function handleSkipBtnClick() {
    skipSession();
  }
  return (
    <div className="flex flex-col items-center w-full bg-background">
      <h2 className="text-4xl w-full text-center mb-4 font-bold text-foreground underline">
        {title(currSessionType)}
      </h2>
      <div className="flex items-center tabular-nums justify-center text-7xl sm:text-8xl lg:text-9xl p-6 mb-4 text-center w-full text-foreground">
        <span
          aria-label="hours"
          className="border-accent border-solid border-4 border-b-[10px] rounded-xl p-2 flex items-center justify-center"
        >
          {timeUnits.hours}
        </span>
        <span className="text-6xl sm:text-7xl lg:text-8xl sm:px-0.5">:</span>
        <span
          aria-label="minutes"
          className="border-accent border-solid border-4 border-b-8 rounded-xl p-2 flex items-center justify-center"
        >
          {timeUnits.mins}
        </span>
        <span className="text-6xl sm:text-7xl lg:text-8xl sm:px-0.5">:</span>
        <span
          aria-label="seconds"
          className="border-accent border-solid border-4 border-b-8 rounded-xl p-2 flex items-center justify-center"
        >
          {timeUnits.secs}
        </span>
      </div>
      <div className="w-full flex gap-4 justify-center items-center">
        <Button size="xl" className="text-lg" onClick={handleResetBtnClick}>
          <RefreshCcw />
          <span className="sr-only">Reset Cycle</span>
        </Button>
        <Button
          aria-label={
            isPlaying ? 'Stop Pomodoro Timer' : 'Start Pomodoro Timer'
          }
          size="xl"
          className="text-lg"
          onClick={handlePlayBtnClick}
        >
          {isPlaying ? <Pause /> : <Play />}
          <span className="sr-only">{isPlaying ? 'Stop' : 'Play'}</span>
        </Button>
        <Button size="xl" className="text-lg" onClick={handleSkipBtnClick}>
          <SkipForward />
          <span className="sr-only">Skip Session</span>
        </Button>
      </div>
    </div>
  );
}
