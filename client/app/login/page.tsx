"use client";

import LoginForm from "@/components/login-form";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import { cn } from "@/lib/utils";


const LoginPage = () => {
  return (
    <Card className={cn("w-[380px] m-auto mt-12")}>
      <CardHeader>
        <CardTitle>Login</CardTitle>
      </CardHeader>
      <CardContent>
        <LoginForm />
      </CardContent>
    </Card>
  )
};

export default LoginPage;
