import axios from "axios";

const http = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 5000
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  config.headers.Authorization = token ? `Bearer ${token}` : "";
  return config;
});


http.interceptors.response.use(
  (response) => response,

  (error) => {
    if (error.response && error.response.status === 401 && !import.meta.env.VITE_DEV) {
      localStorage.removeItem("token");

      window.location.href = "/signin";
    }

    return Promise.reject(error);
  }
);

export default http;

