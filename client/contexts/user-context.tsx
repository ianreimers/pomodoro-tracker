"use client";

import { timeUnitsToSeconds } from "@/lib/utils";
import { createContext, useContext, useState } from "react";


type UserContextProviderProps = {
	children: React.ReactNode
}

interface TimeUnits {
	hours: number
	mins: number,
	secs: number,
}


interface UserContextType {
	taskSeconds: number;
	shortBreakSeconds: number;
	longBreakSeconds: number;
	taskTimeUnits: TimeUnits;
	shortBreakTimeUnits: TimeUnits;
	longBreakTimeUnits: TimeUnits;
	pomodoroInterval: number;
	changeTaskTime: (newTimeUnits: TimeUnits) => void
	changeShortBreakTime: (newTimeUnits: TimeUnits) => void
	changeLongBreakTime: (newTimeUnits: TimeUnits) => void
	changePomodoroInterval: (newPomodoroInterval: number) => void
}

export const UserContext = createContext<UserContextType | null>(null);

function useTimeUnits(initialTimeUnits: TimeUnits) {
	const [timeUnits, setTimeUnits] = useState(initialTimeUnits);

	const updateTimeUnits = (newTimeUnits: TimeUnits) => {
		setTimeUnits({
			hours: newTimeUnits.hours,
			mins: newTimeUnits.mins,
			secs: newTimeUnits.secs
		});
	};

	return [timeUnits, updateTimeUnits] as const;
}

const initialTaskTimeUnits = { hours: 0, mins: 0, secs: 4 };
const initialShortBreakTimeUnits = { hours: 0, mins: 0, secs: 2 };
const initialLongBreakTimeUnits = { hours: 0, mins: 0, secs: 5 };

export default function UserContextProvider({ children }: UserContextProviderProps) {
	const [taskTimeUnits, setTaskTimeUnits] = useTimeUnits(initialTaskTimeUnits);
	const [shortBreakTimeUnits, setShortBreakTimeUnits] = useTimeUnits(initialShortBreakTimeUnits);
	const [longBreakTimeUnits, setLongBreakTimeUnits] = useTimeUnits(initialLongBreakTimeUnits);
	const [pomodoroInterval, setPomodoroInterval] = useState(4);

	const taskSeconds = timeUnitsToSeconds(taskTimeUnits.hours, taskTimeUnits.mins, taskTimeUnits.secs);
	const shortBreakSeconds = timeUnitsToSeconds(shortBreakTimeUnits.hours, shortBreakTimeUnits.mins, shortBreakTimeUnits.secs);
	const longBreakSeconds = timeUnitsToSeconds(longBreakTimeUnits.hours, longBreakTimeUnits.mins, longBreakTimeUnits.secs);

	function changeTaskTime(newTimeUnits: TimeUnits) {
		setTaskTimeUnits(newTimeUnits)
	}

	function changeShortBreakTime(newTimeUnits: TimeUnits) {
		setShortBreakTimeUnits(newTimeUnits);
	}

	function changeLongBreakTime(newTimeUnits: TimeUnits) {
		setLongBreakTimeUnits(newTimeUnits);
	}

	function changePomodoroInterval(newPomdoroInterval: number) {
		setPomodoroInterval(newPomdoroInterval);
	}

	const contextValue: UserContextType = {
		taskSeconds,
		shortBreakSeconds,
		longBreakSeconds,
		taskTimeUnits,
		shortBreakTimeUnits,
		longBreakTimeUnits,
		pomodoroInterval,

		changeTaskTime,
		changeShortBreakTime,
		changeLongBreakTime,
		changePomodoroInterval,
	}

	return (
		<UserContext.Provider value={contextValue}>
			{children}
		</UserContext.Provider>
	)
}

export function useUserContext() {
	const context = useContext(UserContext);

	if (!context) {
		throw new Error("useUserContext must be used within a UserContextProvider");
	}

	return context;
}
