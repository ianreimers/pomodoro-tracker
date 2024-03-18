import Link from 'next/link';

const Navbar = () => {
	return (
		<nav className='bg-background'>
			<ul className='flex items-center justify-end min-h-[10vh] gap-10 text-primary-foreground'>
				<li>
					<Link href="/" className='p-4 bg-primary hover:bg-primary/90 rounded-md'>
						Home
					</Link>
				</li>
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
			</ul>
		</nav>
	);
};

export default Navbar;
