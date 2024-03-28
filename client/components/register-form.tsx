import { useRouter } from 'next/navigation';
import { useAuthContext } from '@/contexts/auth/auth-context';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';

const formSchema = z.object({
	username: z.string()
		.min(3, {
			message: "Username must be at least 3 characters"

		}).max(255, {
			message: "Username must be less than 256 characters"

		}),
	password: z.string().min(6).max(255),
	email: z.string().email()
});

export default function RegisterForm() {
	const { register, isAuthenticated } = useAuthContext();
	const router = useRouter();

	const form = useForm<z.infer<typeof formSchema>>({
		resolver: zodResolver(formSchema),
		defaultValues: {
			username: "",
			password: "",
			email: ""
		}
	})

	if (isAuthenticated()) {
		router.back();
		return <p>Redirecting...</p>
	}

	async function onSubmit(values: z.infer<typeof formSchema>) {
		try {
			await register(values)
			console.log("Login was successful");

		} catch (error) {
			// Error could be handles in axios interceptor
			console.log("Error in LoginForm component");
		}
	}


	return (
		<Form {...form}>
			<form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
				<FormField
					control={form.control}
					name="username"
					render={({ field }) => (
						<FormItem>
							<FormLabel>Username</FormLabel>
							<FormControl>
								<Input placeholder="Username" {...field} />
							</FormControl>
							<FormDescription>
								This is your public display name.
							</FormDescription>
							<FormMessage />
						</FormItem>
					)}
				/>
				<FormField
					control={form.control}
					name="password"
					render={({ field }) => (
						<FormItem>
							<FormLabel>Password</FormLabel>
							<FormControl>
								<Input placeholder="Password" type='password' {...field} />
							</FormControl>
							<FormMessage />
						</FormItem>
					)}
				/>
				<FormField
					control={form.control}
					name="email"
					render={({ field }) => (
						<FormItem>
							<FormLabel>Email</FormLabel>
							<FormControl>
								<Input placeholder="Email" type='email' {...field} />
							</FormControl>
							<FormMessage />
						</FormItem>
					)}
				/>
				<Button type="submit">Submit</Button>
			</form>
		</Form>
	);

}
