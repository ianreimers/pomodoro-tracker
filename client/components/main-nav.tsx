"use client";

import { useAuthContext } from '@/contexts/auth-context';
import Link from 'next/link';
import { Button } from './ui/button';
import ThemeToggleButton from './theme-toggle-button';

const Navbar = () => {
	const { logout, isAuthenticated } = useAuthContext();

	return (
		<nav className='bg-background flex justify-between items-center max-w-[97%] mx-auto'>
			<h1>Logo</h1>
			<div className='flex justify-center items-center'>
				<ThemeToggleButton className='mr-8' />
				<ul className='flex items-center justify-end min-h-[10vh] gap-10 text-primary-foreground'>
					<li>
						<Link href="/" className='p-4 bg-primary hover:bg-primary/90 rounded-md'>
							Home
						</Link>
					</li>
					{isAuthenticated() ? (
						<>
							<Link href={"/dashboard"} className='p-4  bg-primary hover:bg-primary/90 rounded-md'>
								Dashboard
							</Link>
							<Button onClick={() => logout()}>Logout</Button>
						</>
					) : (
						<>
							<li>
								<Link href="/login" className='p-4 bg-primary hover:bg-primary/90 rounded-md'>
									Login
								</Link>
							</li>
							<li>
								<Link href="/register" className='p-4 bg-primary hover:bg-primary/90 rounded-md mr-5'>
									Register
								</Link>
							</li>
						</>
					)}
				</ul>
			</div>
		</nav>
	);
};

export default Navbar;
