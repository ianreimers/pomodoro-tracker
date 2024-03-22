"use client";

import { Dispatch, SetStateAction, createContext, useContext, useEffect, useReducer, useState } from "react";
import { useUserSettingsContext } from "./user-settings-context";
import { useMutation } from "@tanstack/react-query";
import axiosInstance from "@/api/axiosInstance";
import { useToast } from "@/components/ui/use-toast";
import { useAuthContext } from "./auth-context";

type UserContextProviderProps = {
	children: React.ReactNode;
}

type SessionType = "task" | "short break" | "long break";

interface PomodoroContextType {
	remainingSeconds: number;
	currSessionType: SessionType;
	isPlaying: boolean;
	totalPomodoros: number;
	togglePlaying: () => void;
};

interface PomodoroReducerState {
	remainingSeconds: number;
	intervalTimeRemaining: number;
	isPlaying: boolean;
	totalPomodoros: number;
	currSessionType: SessionType;
	currPomodoroId: string | number;
	currBreakType: "SHORT" | "LONG"
}

interface PomodoroSession {
	tempUuid: string;
	taskDuration: number;
	breakDuration: number;
	sessionTaskSeconds: number;
	sessionShortBreakSeconds: number;
	sessionLongBreakSeconds: number;
	sessionStartTime: string;
	sessionUpdateTime: string;
	breakType: string;
}


type ACTION =
	| { type: "complete_interval" }
	| { type: "increment_total_pomodoros" }
	| { type: "duration_remaining", payload: { intervalTimeRemaining: number } }
	| {
		type: "switch_session", payload: {
			pomodoroInterval: number,
			taskSeconds: number,
			shortBreakSeconds: number,
			longBreakSeconds: number
		}
	}
	| { type: "update_pomdoro_id", payload: { newId: number } }
	| {
		type: "settings_update", payload: {
			taskSeconds: number,
			longBreakSeconds: number,
			shortBreakSeconds: number,
			pomodoroInterval: number
		}
	}
	| { type: "toggle_playing" }


function reducer(state: PomodoroReducerState, action: ACTION): PomodoroReducerState {
	switch (action.type) {
		case "complete_interval":
			return {
				...state,
				remainingSeconds: state.remainingSeconds - 1,
				intervalTimeRemaining: 0
			}
		case "duration_remaining":
			return {
				...state,
				intervalTimeRemaining: action.payload.intervalTimeRemaining
			}
		case "increment_total_pomodoros":
			return {
				...state,
				totalPomodoros: state.totalPomodoros + 1
			}
		case "switch_session": {
			let nextState: PomodoroReducerState = { ...state };
			nextState.intervalTimeRemaining = 0;
			nextState.isPlaying = true;
			nextState.currBreakType = "SHORT";

			const hasLongBreakNext = state.totalPomodoros % action.payload.pomodoroInterval === action.payload.pomodoroInterval - 1;

			if (state.currSessionType === "task") {
				if (hasLongBreakNext) {
					nextState.remainingSeconds = action.payload.longBreakSeconds;
					nextState.currSessionType = "long break";
				} else {
					nextState.remainingSeconds = action.payload.shortBreakSeconds;
					nextState.currSessionType = "short break";
					nextState.currBreakType = "SHORT";
				}
				nextState.totalPomodoros += 1;
			} else {
				nextState.currBreakType = hasLongBreakNext ? "LONG" : "SHORT"
				nextState.remainingSeconds = action.payload.taskSeconds;
				nextState.currSessionType = "task";
				nextState.currPomodoroId = crypto.randomUUID();
			}
			return nextState;
		}
		case "update_pomdoro_id":
			return {
				...state,
				currPomodoroId: action.payload.newId
			}
		case "settings_update": {
			return {
				...state,
				remainingSeconds: action.payload.taskSeconds,
				currSessionType: "task",
				currBreakType: action.payload.pomodoroInterval === 1 ? "LONG" : "SHORT",
				isPlaying: false,
				intervalTimeRemaining: 0,
				currPomodoroId: crypto.randomUUID()
			}
		}
		case "toggle_playing":
			return {
				...state,
				isPlaying: !state.isPlaying
			}
	}
}


export const PomodoroContext = createContext<PomodoroContextType | null>(null);

export default function PomodoroContextProvider({ children }: UserContextProviderProps) {
	const { taskSeconds, shortBreakSeconds, longBreakSeconds, pomodoroInterval } = useUserSettingsContext();
	const { isAuthenticated } = useAuthContext();
	const [state, dispatch] = useReducer(reducer, {
		remainingSeconds: taskSeconds,
		intervalTimeRemaining: 0,
		isPlaying: false,
		totalPomodoros: 0,
		currSessionType: "task",
		currPomodoroId: crypto.randomUUID(),
		currBreakType: pomodoroInterval === 1 ? "LONG" : "SHORT"
	});
	const { toast } = useToast()
	const {
		remainingSeconds,
		intervalTimeRemaining,
		isPlaying,
		totalPomodoros,
		currSessionType,
		currPomodoroId,
		currBreakType
	} = state;

	const mutationPost = useMutation({
		mutationFn: async (newPomodoro: PomodoroSession) => {
			const response = await axiosInstance.post("/pomodoro-sessions", newPomodoro)
			return response.data
		},
		onSuccess: (data) => {
			console.log(data);
			const newId = data.id as number;
			dispatch({
				type: "update_pomdoro_id",
				payload: { newId }
			})
		},
		onError: (error) => {
			console.error("Error in pomodoro post mutation:", error);
		}
	});

	const mutationPatch = useMutation({
		mutationFn: async (data: { sessionType: string, id: string | number }) => await axiosInstance.patch(`/pomodoro-sessions/${data.id}`, { sessionType: data.sessionType }),
		onSuccess: () => {
			toast({
				description: `Pomodoro ${currPomodoroId} updated`
			})
		},
		onError: (error) => {
			console.error("Error in pomodoro patch mutation:", error);
		}
	});

	function togglePlaying() {
		dispatch({
			type: "toggle_playing"
		})
	}

	// Check if any of the used user settings were updated and reset the session
	useEffect(() => {
		dispatch({
			type: "settings_update",
			payload: {
				taskSeconds,
				shortBreakSeconds,
				longBreakSeconds,
				pomodoroInterval
			}
		})

	}, [taskSeconds, shortBreakSeconds, longBreakSeconds, pomodoroInterval]);

	useEffect(() => {
		if (!isPlaying) {
			return
		}

		// If we're logged in and at the start of a task, create a new pomodoro session with the server
		if (isAuthenticated() && currSessionType === "task") {
			if (taskSeconds === remainingSeconds) {
				console.log("Attempting new pomdoro session creation");
				mutationPost.mutate({
					tempUuid: currPomodoroId as string,
					breakType: currBreakType,
					sessionTaskSeconds: taskSeconds,
					sessionShortBreakSeconds: shortBreakSeconds,
					sessionLongBreakSeconds: longBreakSeconds,
					sessionStartTime: new Date().toISOString(),
					sessionUpdateTime: new Date().toISOString(),
					taskDuration: 0,
					breakDuration: 0
				})
			}
		}

		// Update the posted pomodoro every minute
		if (isAuthenticated() && remainingSeconds % 60 === 0) {
			const sessionSecondsMap = new Map<string, number>([
				["task", taskSeconds],
				["short break", shortBreakSeconds],
				["long break", longBreakSeconds]
			]);

			if (sessionSecondsMap.get(currSessionType) !== remainingSeconds) {
				console.log("Attempting to patch current pomodoro");
				mutationPatch.mutate({
					id: currPomodoroId,
					sessionType: currSessionType === "task" ? "task" : "break",
				})
			}
		}

		// Capture the moment, in millis, that this second began
		let startTime = Date.now();

		// Calculate the current intervalDelay from any previous remaining time
		let intervalDelay = 1000 - intervalTimeRemaining


		const intervalId = setInterval(() => {
			// If end of session, change to next session state
			if (remainingSeconds === 0) {
				dispatch({
					type: "switch_session",
					payload: {
						taskSeconds,
						shortBreakSeconds,
						longBreakSeconds,
						pomodoroInterval
					}
				})
			} else {
				dispatch({ type: "complete_interval" });
			}

		}, intervalDelay); // The delay is when the user paused in (second - timeRemaining)

		return () => {
			const stopTime = Date.now();
			const milliDiff = stopTime - startTime;

			if (milliDiff < 1000) {
				//setIntervalTimeRemaining((prev) => prev + milliDiff > 1000 ? milliDiff : milliDiff + prev);
				dispatch({
					type: "duration_remaining",
					payload: {
						intervalTimeRemaining: (intervalTimeRemaining + milliDiff > 1000
							? milliDiff
							: milliDiff + intervalTimeRemaining
						)
					}
				})
			}

			// Clear the interval upon unmount
			clearInterval(intervalId);
		}
	}, [remainingSeconds, isPlaying])

	const contextValue: PomodoroContextType = {
		remainingSeconds,
		currSessionType,
		isPlaying,
		totalPomodoros,
		togglePlaying
	}

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
