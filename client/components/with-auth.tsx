

import { useRouter } from 'next/navigation';
import Router from 'next/router';
import { FunctionComponent, useEffect } from 'react';
import { useAuthContext } from '@/contexts/auth-context';

const withAuth = (WrappedComponent: FunctionComponent) => {
	return (props: any) => {
		const router = useRouter();
		const { isAuthenticated, isLoading } = useAuthContext(); // Assuming your AuthContext provides these

		useEffect(() => {
			if (!isLoading && !isAuthenticated()) {
				// Redirect them to the login page, but save the current location they were trying to go to.
				router.push(`/login`);
			}
		}, [isAuthenticated, isLoading, Router]);

		return isAuthenticated() ? <WrappedComponent {...props} /> : null; // Or a loading indicator
	};
};

export default withAuth;
