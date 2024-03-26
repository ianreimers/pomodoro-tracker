"use client"
import { useRouter } from 'next/navigation';
import { FunctionComponent, useEffect } from 'react';
import { useAuthContext } from '@/contexts/auth-context';

export default function withAuth(WrappedComponent: FunctionComponent) {
	return function WithAuth(props: any) {
		const router = useRouter();
		const { isAuthenticated, isLoading } = useAuthContext();

		useEffect(() => {
			if (!isLoading && !isAuthenticated()) {
				router.push(`/login`);
			}
		}, [isAuthenticated, isLoading, router]);

		return isAuthenticated() ? <WrappedComponent {...props} /> : null; // Or a loading indicator
	};
};

