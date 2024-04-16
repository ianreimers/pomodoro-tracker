import { useEffect, useRef } from 'react';

export default function useAudioPlayer(
  sound: string | undefined = 'bells',
  soundVolume?: number
) {
  const audioRef = useRef<HTMLAudioElement>(new Audio(`/sounds/${sound}.mp3`));
  if (soundVolume) {
    audioRef.current.volume = mapSoundVolumeToAudioVolume(soundVolume);
  }

  function playSound(sound: string, newVolume: number) {
    const soundPath = `/sounds/${sound}.mp3`;
    audioRef.current.pause();
    audioRef.current.currentTime = 0;
    if (!audioRef.current.src.includes(soundPath)) {
      audioRef.current.src = soundPath;
      audioRef.current.load();
    }

    audioRef.current.volume = mapSoundVolumeToAudioVolume(newVolume);
    audioRef.current.play();
  }

  function stopSound() {
    audioRef.current.pause();
    audioRef.current.currentTime = 0;
  }

  function mapSoundVolumeToAudioVolume(soundVolume: number) {
    return Number((soundVolume * 0.01).toPrecision(2));
  }

  useEffect(() => {
    //return () => {
    //	audioRef.current.pause();
    //	audioRef.current.fastSeek(0);
    //}
  }, []);

  return { playSound, stopSound };
}
