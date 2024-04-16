import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { useUserSettingsContext } from '@/contexts/user-settings-context';
import { userSettingsFormSchema } from '@/validation_schema/schemas';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from './ui/select';
import useAudioPlayer from '@/hooks/use-audio-player';
import { Slider } from './ui/slider';

export default function UserSettingsForm() {
  const {
    taskTimeUnits,
    shortBreakTimeUnits,
    longBreakTimeUnits,
    pomodoroInterval,
    sound,
    soundVolume,
    updateSettings,
  } = useUserSettingsContext();
  const { playSound, stopSound } = useAudioPlayer();

  const form = useForm<z.infer<typeof userSettingsFormSchema>>({
    resolver: zodResolver(userSettingsFormSchema),
    defaultValues: {
      taskTimeHours: taskTimeUnits.hours,
      taskTimeMinutes: taskTimeUnits.mins,
      shortBreakHours: shortBreakTimeUnits.hours,
      shortBreakMinutes: shortBreakTimeUnits.mins,
      longBreakHours: longBreakTimeUnits.hours,
      longBreakMinutes: longBreakTimeUnits.mins,
      pomodoroInterval,
      sound,
      soundVolume,
      taskTimeSeconds: 0,
      shortBreakSeconds: 0,
      longBreakSeconds: 0,
    },
  });

  function onSubmit(values: z.infer<typeof userSettingsFormSchema>) {
    updateSettings(values);
  }

  function handleSoundSelectChange(sound: string, newVolume: number) {
    stopSound();
    playSound(sound, newVolume);
  }
  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="grid grid-cols-2 gap-6 sm:px-10"
      >
        <fieldset className="grid grid-cols-2 gap-x-3">
          <legend className="font-extrabold">Task Time</legend>
          <FormField
            control={form.control}
            name="taskTimeHours"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Hours</FormLabel>
                <FormControl>
                  <Input type="number" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="taskTimeMinutes"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Minutes</FormLabel>
                <FormControl>
                  <Input type="number" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        </fieldset>
        <fieldset className="grid grid-cols-2 gap-x-3">
          <legend className="font-extrabold">Short Break Time</legend>
          <FormField
            control={form.control}
            name="shortBreakHours"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Hours</FormLabel>
                <FormControl>
                  <Input type="number" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="shortBreakMinutes"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Minutes</FormLabel>
                <FormControl>
                  <Input type="number" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        </fieldset>
        <FormField
          control={form.control}
          name="pomodoroInterval"
          render={({ field }) => (
            <FormItem>
              <FormLabel className="text-md font-extrabold mb-6 inline-block">
                Pomodoro Interval
              </FormLabel>
              <FormControl>
                <Input type="number" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <fieldset className="grid grid-cols-2 gap-3">
          <legend className="font-extrabold">Long Break Time</legend>
          <FormField
            control={form.control}
            name="longBreakHours"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Hours</FormLabel>
                <FormControl>
                  <Input type="number" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="longBreakMinutes"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Minutes</FormLabel>
                <FormControl>
                  <Input type="number" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        </fieldset>
        <FormField
          control={form.control}
          name="sound"
          render={({ field }) => (
            <FormItem className="">
              <FormLabel className="text-md font-extrabold">Sound</FormLabel>
              <Select
                onValueChange={(value) => {
                  field.onChange(value);
                  handleSoundSelectChange(value, form.getValues('soundVolume'));
                }}
                defaultValue={field.value}
              >
                <FormControl>
                  <SelectTrigger className="">
                    <SelectValue />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  <SelectItem value="bells">Bells</SelectItem>
                  <SelectItem value="congas">Congas</SelectItem>
                  <SelectItem value="ringtone">Ringtone</SelectItem>
                  <SelectItem value="shakers">Shakers</SelectItem>
                  <SelectItem value="triangle">Triangle</SelectItem>
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="soundVolume"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel className="text-md font-extrabold">
                Sound Volume
              </FormLabel>
              <FormControl className="h-full">
                <Slider
                  name="soundVolume"
                  onPointerUp={() => {
                    playSound(
                      form.getValues('sound'),
                      form.getValues('soundVolume')
                    );
                  }}
                  onValueChange={(value) => {
                    field.onChange(value[0]);
                    stopSound();
                  }}
                  defaultValue={[soundVolume]}
                  max={100}
                  step={1}
                />
              </FormControl>
            </FormItem>
          )}
        ></FormField>

        <Button
          size="lg"
          type="submit"
          className="text-base col-span-full mt-4"
        >
          Submit
        </Button>
      </form>
    </Form>
  );
}
/*

	Note: When developing, adding the seconds code below to the form allows easier 
	testing for the session timer

	defaultValues: {
			taskTimeSeconds: taskTimeUnits.secs,
			shortBreakSeconds: shortBreakTimeUnits.secs,
			longBreakSeconds: longBreakTimeUnits.secs,
	}

	<FormField control={form.control}
		name="taskTimeSeconds"
		render={({ field }) => (
			<FormItem>
				<FormLabel>Seconds</FormLabel>
				<FormControl>
					<Input {...field} />
				</FormControl>
				<FormMessage />
			</FormItem>)}
	/>
	<FormField
		control={form.control}
		name="shortBreakSeconds"
		render={({ field }) => (
			<FormItem>
				<FormLabel>Seconds</FormLabel>
				<FormControl>
					<Input {...field} />
				</FormControl>
				<FormMessage />
			</FormItem>
		)}
	/>
	<FormField
		control={form.control}
		name="longBreakSeconds"
		render={({ field }) => (
			<FormItem>
				<FormLabel>Seconds</FormLabel>
				<FormControl>
					<Input {...field} />
				</FormControl>
				<FormMessage />
			</FormItem>
		)}
	/>

 */
