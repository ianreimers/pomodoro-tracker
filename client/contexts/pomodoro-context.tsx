"use client";

import { Dispatch, SetStateAction, createContext, useContext, useEffect, useReducer, useState } from "react";
import { useUserContext } from "./user-context";

type UserContextProviderProps = {
	children: React.ReactNode;
}

type SessionType = "task" | "short break" | "long break";

interface PomodoroContextType {
	remainingSeconds: number;
	currSession: SessionType;
	isPlaying: boolean;
	totalPomodoros: number;
	setIsPlaying: Dispatch<SetStateAction<boolean>>;
};



export const PomodoroContext = createContext<PomodoroContextType | null>(null);

export default function PomodoroContextProvider({ children }: UserContextProviderProps) {
	const { taskSeconds, shortBreakSeconds, longBreakSeconds, pomodoroInterval } = useUserContext();

	const [remainingSeconds, setRemainingSeconds] = useState(taskSeconds);
	const [intervalTimeRemaining, setIntervalTimeRemaining] = useState(0);
	const [isPlaying, setIsPlaying] = useState(false);
	const [totalPomodoros, setTotalPomodoros] = useState(0);
	const [currSession, setCurrSession] = useState<SessionType>("task");

	const contextValue: PomodoroContextType = {
		remainingSeconds: remainingSeconds,
		currSession,
		isPlaying,
		setIsPlaying,
		totalPomodoros
	}

	useEffect(() => {
		setRemainingSeconds(taskSeconds);
		setCurrSession("task")
		setIsPlaying(false);

	}, [taskSeconds]);

	useEffect(() => {
		if (!isPlaying) {
			return
		}

		// Capture the moment, in millis, that this second began
		let startTime = Date.now();

		// Calculate the current intervalDelay from any previous remaining time
		let intervalDelay = 1000 - intervalTimeRemaining


		const intervalId = setInterval(() => {
			setRemainingSeconds(remainingSeconds - 1);

			// Interval completed so ensure that the remaining time from last stoppage is reset
			setIntervalTimeRemaining((_) => 0);

			// If end of session, change to next session state
			if (remainingSeconds == 0) {
				switch (currSession) {
					case "task": {
						if (totalPomodoros % pomodoroInterval == pomodoroInterval - 1) {
							setCurrSession("long break");
							setRemainingSeconds(longBreakSeconds);
							return
						}

						setCurrSession("short break");
						setRemainingSeconds(shortBreakSeconds);
						break;
					}
					case "short break":
					case "long break": {
						setCurrSession("task");
						setRemainingSeconds(taskSeconds);
						setTotalPomodoros(totalPomodoros + 1);
					}
				}
			}

		}, intervalDelay); // The remaining time is when the user paused in (second - timeRemaining)

		return () => {
			const stopTime = Date.now();
			const milliDiff = stopTime - startTime;

			if (milliDiff < 1000) {
				setIntervalTimeRemaining((prev) => prev + milliDiff > 1000 ? milliDiff : milliDiff + prev);
			}


			// Clear the interval upon unmount
			clearInterval(intervalId);
		}
	}, [remainingSeconds, isPlaying])

	return (
		<PomodoroContext.Provider value={contextValue}>
			{children}
		</PomodoroContext.Provider>
	)
}

export function usePomodoroContext() {
	const context = useContext(PomodoroContext);

	if (!context) {
		throw new Error("usePomodoroContext must be used within a PomodoroContextProvider");
	}

	return context;
}
