import axios from "axios";

const http = axios.create({
  // baseURL: "http://26.160.101.150:8081/api",
  baseURL: "http://localhost:8081/api",
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
    if (error.response && error.response.status === 401) {
      localStorage.removeItem("token");

      window.location.href = "/signin";
    }

    return Promise.reject(error);
  }
);

export default http;

