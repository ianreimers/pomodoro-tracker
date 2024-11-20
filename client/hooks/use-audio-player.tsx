import { useEffect, useRef } from 'react';
import { Howl, Howler } from 'howler';

export default function useAudioPlayer(
  sound: string | undefined = 'bells',
  soundVolume?: number
) {
  const howlerCacheRef = useRef(Howler);
  const soundCacheRef = useRef<Record<string, Howl>>({
    [sound]: new Howl({
      src: [`/sounds/${sound}.mp3`],
    }),
  });

  if (soundVolume) {
    howlerCacheRef.current.volume(soundVolume);
  }

  function playSound(sound: string, newVolume: number) {
    if (!soundCacheRef.current[sound]) {
      const soundPath = `/sounds/${sound}.mp3`;
      const newSound = new Howl({
        src: [soundPath],
      });
      soundCacheRef.current[sound] = newSound;
    }

    Howler.volume(mapSoundVolumeToAudioVolume(newVolume));
    soundCacheRef.current[sound].play();
  }

  function stopSound() {
    Howler.stop();
  }

  function mapSoundVolumeToAudioVolume(soundVolume: number) {
    return Number((soundVolume * 0.01).toPrecision(2));
  }

  useEffect(() => {}, []);

  return { playSound, stopSound };
}
