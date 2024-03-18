'use client';

import { useRouter } from "next/navigation";
import { createContext, useContext, useState, useEffect } from "react";

type AuthContextProviderProps = {
	children: React.ReactNode
}

interface User {
	username: string;
}

interface Credentials {
	username: string;
	password: string;
}

interface AuthContextType {
	user: User | null;
	login: (credentials: Credentials) => Promise<void>;
	logout: () => Promise<void>;
	register: (userData: Credentials) => Promise<void>;


}

export const AuthContext = createContext<AuthContextType | null>(null);


export default function AuthContextProvider({ children }: AuthContextProviderProps) {
	const router = useRouter();
	const [user, setUser] = useState<User | null>(null);
	const apiUrl = "http://localhost:8080/api"

	const login = async (credentials: Credentials) => {
		try {
			console.log(apiUrl);
			const response = await fetch(`${apiUrl}/login`, {
				method: "POST",
				headers: {
					"Content-Type": "application/json"
				},
				body: JSON.stringify(credentials)
			})

			if (!response.ok) {
				console.log(response)
				throw new Error("Login Failed");
			}

			const data = await response.json();
			const token = data.token;
			setUser({ username: credentials.username });
			localStorage.setItem('token', token);

			router.push("/dashboard");
		} catch (error) {
			console.error("Login failed:", error);
		}
	}

	// Function to log out user
	const logout = async (): Promise<void> => {
		try {
			// Perform logout logic (e.g., clear authentication state)
			setUser(null); // Clear user state
			localStorage.removeItem('token'); // Remove token from storage
			router.push('/'); // Redirect to home page
		} catch (error) {
			console.error('Logout failed:', error);
		}
	};

	// Function to register user
	const register = async (userData: Credentials): Promise<void> => {
		try {
			// Perform registration logic (e.g., send registration request to backend)
			// Upon successful registration, set authenticated user
			const response = await fetch(`${apiUrl}/register`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(userData)
			});

			if (!response.ok) {
				throw new Error('Registration failed');
			}

			const data = await response.json();
			const token = data.token;
			setUser({ username: userData.username });
			localStorage.setItem('token', token);

			router.push('/dashboard'); // Redirect to dashboard page
		} catch (error) {
			console.error('Registration failed:', error);
			// Handle registration failure
		}
	};

	// Restore user session on page load
	useEffect(() => {
		const token = localStorage.getItem('token');
		if (token) {
		}
	}, []);

	const contextValue: AuthContextType = {
		user,
		login,
		logout,
		register
	}

	return (
		<AuthContext.Provider value={contextValue} >
			{children}
		</AuthContext.Provider>
	)
}

export function useAuthContext() {
	const context = useContext(AuthContext);

	if (!context) {
		throw new Error("useAuthContext must be used within a AuthContextProvider");
	}

	return context;
}

