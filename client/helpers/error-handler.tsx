"use client";
import { isAxiosError } from "axios";
import { toast } from "@/components/ui/use-toast";
import Router from "next/navigation";
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime";

export function handleError(error: any) {

	if (isAxiosError(error)) {
		const err = error.response;

		if (Array.isArray(err?.data.errors)) {
			for (let message of err.data.errors) {
				toast({
					description: message,
					variant: "destructive"
				});
			}
		}
	}

}
