import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { useUserSettingsContext } from '@/contexts/user-settings-context';
import { userSettingsFormSchema } from '@/validation_schema/schemas';

/*

	Note: When developing, adding the seconds code below to the form allows easier 
	testing for the session timer

	defaultValues: {
			taskTimeSeconds: taskTimeUnits.secs,
			shortBreakSeconds: shortBreakTimeUnits.secs,
			longBreakSeconds: longBreakTimeUnits.secs,
	}

	<FormField
		control={form.control}
		name="taskTimeSeconds"
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

export default function UserSettingsForm() {
	const {
		taskTimeUnits,
		shortBreakTimeUnits,
		longBreakTimeUnits,
		pomodoroInterval,
		updateSettings
	} = useUserSettingsContext();

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
			taskTimeSeconds: 0,
			shortBreakSeconds: 0,
			longBreakSeconds: 0,
		}
	})

	function onSubmit(values: z.infer<typeof userSettingsFormSchema>) {
		updateSettings(values);
	}


	return (
		<Form {...form}>
			<form onSubmit={form.handleSubmit(onSubmit)} className="grid grid-cols-2 gap-6 sm:px-10">
				<fieldset className='grid grid-cols-2 gap-x-3'>
					<legend className='font-extrabold'>Task Time</legend>
					<FormField
						control={form.control}
						name="taskTimeHours"
						render={({ field }) => (
							<FormItem>
								<FormLabel>Hours</FormLabel>
								<FormControl>
									<Input {...field} />
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
									<Input {...field} />
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>

				</fieldset>
				<fieldset className='grid grid-cols-2 gap-x-3'>
					<legend className='font-extrabold'>Short Break Time</legend>
					<FormField
						control={form.control}
						name="shortBreakHours"
						render={({ field }) => (
							<FormItem>
								<FormLabel>Hours</FormLabel>
								<FormControl>
									<Input {...field} />
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
									<Input {...field} />
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
						<FormItem className='space-y-8'>
							<FormLabel className='text-md font-extrabold'>Pomodoro Interval</FormLabel>
							<FormControl>
								<Input {...field} />
							</FormControl>
							<FormMessage />
						</FormItem>
					)}
				/>
				<fieldset className='grid grid-cols-2 gap-3'>
					<legend className='font-extrabold'>Long Break Time</legend>
					<FormField
						control={form.control}
						name="longBreakHours"
						render={({ field }) => (
							<FormItem>
								<FormLabel>Hours</FormLabel>
								<FormControl>
									<Input {...field} />
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
									<Input {...field} />
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>
				</fieldset>
				<Button size="lg" type="submit" className="text-base col-span-full mt-4">Submit</Button>
			</form>
		</Form>
	);

}
