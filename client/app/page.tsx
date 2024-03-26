"use client"

import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import UserSettingsForm from "@/components/user-settings-form";
import { PageWrapper } from "@/components/page-wrapper";
import SessionTimer from "@/components/session-timer";

export default function Home() {

  return (
    <PageWrapper>
      <div className="max-w-4xl mx-auto">
        <Tabs defaultValue="session">
          <TabsList className="grid w-full grid-cols-2 h-12">
            <TabsTrigger className="py-2 text-base" value="session">Session</TabsTrigger>
            <TabsTrigger className="py-2 text-base" value="settings">Settings</TabsTrigger>
          </TabsList>
          <TabsContent value="session">
            <SessionTimer />
          </TabsContent>
          <TabsContent value="settings">
            <UserSettingsForm />
          </TabsContent>

        </Tabs>
      </div>


    </PageWrapper>

  )
}
