import { useEffect, useRef } from "react";

export default function useAudioPlayer(sound: string | undefined = "bells") {
	const audioRef = useRef<HTMLAudioElement>(new Audio(`/sounds/${sound}.mp3`));

	function playSound(sound: string) {
		const soundPath = `/sounds/${sound}.mp3`;
		audioRef.current.pause();
		audioRef.current.fastSeek(0);
		if (!audioRef.current.src.includes(soundPath)) {
			audioRef.current.src = soundPath;
			audioRef.current.load();
		}
		audioRef.current.play();
	}

	useEffect(() => {

		//return () => {
		//	audioRef.current.pause();
		//	audioRef.current.fastSeek(0);
		//}
	}, [])

	return { playSound };
}
