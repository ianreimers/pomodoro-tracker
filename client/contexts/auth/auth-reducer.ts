import { AuthState, User } from "@/types/types";

type ACTIONTYPE =
	| { type: "register_success", payload: { user: User; token: string; } }
	| { type: "login_success", payload: { user: User; token: string } }
	| { type: "register_fail" }
	| { type: "logout" };

export function reducer(state: AuthState, action: ACTIONTYPE): AuthState {
	switch (action.type) {
		case "login_success":
		case "register_success":
			return {
				...state,
				user: action.payload.user,
				token: action.payload.token,
			}
		case "register_fail":
			return {
				...state,
			}
		case "logout":
			return {
				user: null,
				token: "",
				isLoading: false
			}
	}
}
