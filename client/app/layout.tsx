import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css"
import AuthContextProvider from "@/contexts/auth-context";
import { Toaster } from "@/components/ui/toaster";
import Navbar from "@/components/main-nav";
import UserSettingsContextProvider from "@/contexts/user-settings-context";
import PomodoroContextProvider from "@/contexts/pomodoro-context";
import { ThemeProvider } from "@/components/theme-provider";


import App from "@/components/app";
import { AxiosInterceptor } from "@/api/axiosInstance";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Create Next App",
  description: "Generated by create next app",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${inter.className} bg-background`}>
        <App>
          <ThemeProvider attribute="class" defaultTheme="system" enableSystem disableTransitionOnChange>
            <AuthContextProvider>
              <AxiosInterceptor>
                <Navbar />
                <UserSettingsContextProvider>
                  <PomodoroContextProvider>
                    {children}
                  </PomodoroContextProvider>
                </UserSettingsContextProvider>
              </AxiosInterceptor>
            </AuthContextProvider>
            <Toaster />
          </ThemeProvider>
        </App>
      </body>
    </html>
  )
}
