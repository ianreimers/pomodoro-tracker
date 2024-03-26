"use client";

import { useAuthContext } from '@/contexts/auth-context';
import Link from 'next/link';
import { Button } from './ui/button';
import ThemeToggleButton from './theme-toggle-button';
import { Sheet, SheetContent, SheetTrigger } from './ui/sheet';
import { Menu, Package2 } from 'lucide-react';
import Icon1 from './icons/icon1';

const Navbar = () => {
	const { logout, isAuthenticated } = useAuthContext();
	return (

		<header className="sticky top-0 flex h-16 items-center gap-4 border-b bg-background px-4 md:px-6">
			<nav className="hidden flex-col gap-6 text-lg font-medium md:flex md:flex-row md:items-center md:gap-5 md:text-sm lg:gap-6">
				<Link
					href="/"
					className="flex items-center gap-2 text-lg font-semibold md:text-base"
				>
					{/*<Package2 className="h-6 w-6" />*/}
					<Icon1 className="w-10 h-10" />
					<span className="sr-only">Acme Inc</span>
				</Link>
				{isAuthenticated() ? (
					<>
						<Link href={"/dashboard"} className="text-muted-foreground transition-colors hover:text-foreground">
							Dashboard
						</Link>
						<Button onClick={() => logout()}>Logout</Button>
					</>
				) : (
					<>
						<Link href="/login" className="text-muted-foreground transition-colors hover:text-foreground">
							Login
						</Link>
						<Link href="/register" className="text-muted-foreground transition-colors hover:text-foreground">
							Register
						</Link>
					</>
				)}
			</nav>
			<Sheet>
				<SheetTrigger asChild>
					<Button
						variant="outline"
						size="icon"
						className="shrink-0 md:hidden"
					>
						<Menu className="h-5 w-5" />
						<span className="sr-only">Toggle navigation menu</span>
					</Button>
				</SheetTrigger>
				<SheetContent side="left">
					<nav className="grid gap-6 text-lg font-medium">
						<Link
							href="/"
							className="flex items-center gap-2 text-lg font-semibold"
						>
							{/*<Package2 className="h-6 w-6" />*/}
							<Icon1 className="w-10 h-10" />
							<span className="sr-only">Acme Inc</span>
						</Link>
						{isAuthenticated() ? (
							<>
								<Link href={"/dashboard"} className="text-muted-foreground hover:text-foreground">
									Dashboard
								</Link>
								<Button onClick={() => logout()}>Logout</Button>
							</>
						) : (
							<>
								<Link href="/login" className="text-muted-foreground hover:text-foreground">
									Login
								</Link>
								<Link href="/register" className="text-muted-foreground hover:text-foreground">
									Register
								</Link>
							</>
						)}
					</nav>
				</SheetContent>
			</Sheet>
			<ThemeToggleButton className='ml-auto' />
		</header>
	)

};

export default Navbar;

// return (
// 	<nav className='bg-background flex justify-between items-center max-w-[97%] mx-auto'>
// 		<h1>Logo</h1>
// 		<div className='flex justify-center items-center'>
// 			<ThemeToggleButton className='mr-8' />
// 			<ul className='flex items-center justify-end min-h-[10vh] gap-10 text-primary-foreground'>
// 				<li>
// 					<Link href="/" className='p-4 bg-primary hover:bg-primary/90 rounded-md'>
// 						Home
// 					</Link>
// 				</li>
// 				{isAuthenticated() ? (
// 					<>
// 						<Link href={"/dashboard"} className='p-4  bg-primary hover:bg-primary/90 rounded-md'>
// 							Dashboard
// 						</Link>
// 						<Button onClick={() => logout()}>Logout</Button>
// 					</>
// 				) : (
// 					<>
// 						<li>
// 							<Link href="/login" className='p-4 bg-primary hover:bg-primary/90 rounded-md'>
// 								Login
// 							</Link>
// 						</li>
// 						<li>
// 							<Link href="/register" className='p-4 bg-primary hover:bg-primary/90 rounded-md mr-5'>
// 								Register
// 							</Link>
// 						</li>
// 					</>
// 				)}
// 			</ul>
// 		</div>
// 	</nav>
// );

