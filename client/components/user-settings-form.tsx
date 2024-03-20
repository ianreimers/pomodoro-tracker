import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { useUserSettingsContext } from '@/contexts/user-settings-context';
import { userSettingsFormSchema } from '@/validation_schema/schemas';


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
			taskTimeSeconds: taskTimeUnits.secs,
			shortBreakHours: shortBreakTimeUnits.hours,
			shortBreakMinutes: shortBreakTimeUnits.mins,
			shortBreakSeconds: shortBreakTimeUnits.secs,
			longBreakHours: longBreakTimeUnits.hours,
			longBreakMinutes: longBreakTimeUnits.mins,
			longBreakSeconds: longBreakTimeUnits.secs,
			pomodoroInterval
		}
	})

	function onSubmit(values: z.infer<typeof userSettingsFormSchema>) {
		updateSettings(values);
	}


	return (
		<Form {...form}>
			<form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
				<fieldset className='grid grid-cols-3 gap-3'>
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

				</fieldset>
				<fieldset className='grid grid-cols-3 gap-3'>
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

				</fieldset>
				<fieldset className='grid grid-cols-3 gap-3'>
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
				</fieldset>
				<FormField
					control={form.control}
					name="pomodoroInterval"
					render={({ field }) => (
						<FormItem>
							<FormLabel className='text-md font-extrabold'>Pomodoro Interval</FormLabel>
							<FormControl>
								<Input {...field} />
							</FormControl>
							<FormDescription>Quantity of tasks complete before long break.</FormDescription>
							<FormMessage />
						</FormItem>
					)}
				/>

				<Button type="submit" >Submit</Button>
			</form>
		</Form>
	);

}
