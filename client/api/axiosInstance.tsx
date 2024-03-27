"use client"
import axios, { AxiosError } from "axios";
import { useAuthContext } from "@/contexts/auth-context";
import { useEffect, useState } from "react";
import { useToast } from "@/components/ui/use-toast";

const axiosInstance = axios.create({
  baseURL: `${process.env.NEXT_PUBLIC_API_BASE_URL}`,
  headers: {
    "Content-Type": "application/json"
  }
})

function AxiosInterceptor({ children }: { children: React.ReactNode }) {
  const [isSet, setIsSet] = useState(false);
  const { logout, isAuthenticated } = useAuthContext();
  const { toast } = useToast();

  /*
    *
    * As of right now, the server only returns an array of error messages
    * for the errors that are handled
    * {
    *   "errors": string[]
    * }
    *
    */

  function handleAxiosError(error: AxiosError) {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          handleUnauthorizedError(error.response.data);
          return
        default:
          console.error(`Unhandled error [${error.response.status}]:`, error.response.data);
      }

    } else if (error.request) {
      // A request was made but no response was received
      console.error("No response received:", error.request)
      toast({
        description: "No response received from the server",
        variant: "destructive"
      })
    } else {
      console.error("Something happened in setting up the request that triggered an error", error.message);
    }
  }

  function handleUnauthorizedError(data: any) {
    // Check if the error is due to invalid credentials or if its a token issue
    if (data.errors && data.errors.some((msg: string) => msg === "Invalid credentials")) {
      toast({
        description: "Invalid credentials",
        variant: "destructive"
      });
    } else {
      // Token issue 
      // We could implement refresh token logic here
      toast({
        description: "Authentication required. Redirection to login..."
      })
      logout();
    }
  }

  useEffect(() => {
    const requestInterceptorId = axiosInstance.interceptors.request.use(
      (request) => {
        const token = localStorage.getItem("token");
        if (token) {
          request.headers.setAuthorization(`Bearer ${token}`);

        }

        return request;
      },
      (error) => {
        console.log("Axios request interceptor error: receive a request error");

        return Promise.reject(error);
      }
    );

    const responseInterceptorId = axiosInstance.interceptors.response.use(
      (response) => {
        return response;
      },
      (error) => {
        // This will type narrow to give the AxiosError type to the error
        if (axios.isAxiosError(error)) {
          handleAxiosError(error);
        } else {
          // This is a non-Axios error
          console.error("An unexpected error occurred:", error);
        }

        return Promise.reject(error);
      }
    );

    setIsSet(true);

    return () => {
      axiosInstance.interceptors.request.eject(requestInterceptorId);
      axiosInstance.interceptors.response.eject(responseInterceptorId);
    }

  }, []);

  return isSet && children;

}

export default axiosInstance;
export { AxiosInterceptor };
