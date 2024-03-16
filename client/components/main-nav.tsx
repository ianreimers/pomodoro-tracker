import Link from 'next/link';

const Navbar = () => {
	return (
		<nav className='bg-red-100'>
			<ul className='flex items-center justify-end min-h-[10vh] gap-10'>
				<li>
					<Link href="/" className='p-4 bg-white'>
						Home
					</Link>
				</li>
				<li>
					<Link href="/login" className='p-4 bg-white'>
						Login
					</Link>
				</li>
				<li>
					<Link href="/register" className='p-4 bg-white mr-5'>
						Register
					</Link>
				</li>
			</ul>
		</nav>
	);
};

export default Navbar;
