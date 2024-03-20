"use client"
import withAuth from "@/components/with-auth";
import { useAuthContext } from "@/contexts/auth-context";


function DashboardPage() {
  const { user } = useAuthContext();
  let username;

  if (user) {
    username = user.username
  }

  return (
    <>
      <h1>Dashboard</h1>
      <h2>Welcome {username}</h2>

    </>

  )

}

export default withAuth(DashboardPage);
