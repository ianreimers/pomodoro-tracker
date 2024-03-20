"use client"
import axios, { isAxiosError, Axios, AxiosError, AxiosResponse } from "axios";
import { AuthContext, useAuthContext } from "@/contexts/auth-context";
import { useContext, useEffect, useState } from "react";
import { toast } from "@/components/ui/use-toast";
import { handleError } from "@/helpers/error-handler";
import { useRouter } from "next/navigation";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json"
  }
})

function AxiosInterceptor({ children }: { children: React.ReactNode }) {
  const [isSet, setIsSet] = useState(false);
  const router = useRouter();
  const { logout, isAuthenticated } = useAuthContext();

  useEffect(() => {
    const requestInterceptorId = axiosInstance.interceptors.request.use(
      (request) => {
        console.log("Axios request interceptor: made a request");
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
        console.log("Axios response interceptor: received a response");

        return response;
      },
      (error) => {
        console.log("Axios response interceptor error: received a response error");
        //console.log("Response?:", error);
        handleError(error);


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
