"use client";

import RegisterForm from "@/components/register-form";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import { cn } from "@/lib/utils";


const RegisterPage = () => {
  return (
    <Card className={cn("w-[380px] m-auto mt-12")}>
      <CardHeader>
        <CardTitle>Register</CardTitle>
      </CardHeader>
      <CardContent>
        <RegisterForm />
      </CardContent>
    </Card>
  )
};

export default RegisterPage;
