import axios from "axios";

const API_BASE_URL =
  import.meta.env.VITE_API_URL ||
  "http://localhost:8080";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use((config) => {

  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization =
      `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,

  (error) => {

    if (error.response?.status === 401) {

      localStorage.removeItem("token");
      localStorage.removeItem("user");

      window.location.href = "/";
    }

    return Promise.reject(error);
  }
);

export const authAPI = {

  register: (
    fullName: string,
    email: string,
    password: string
  ) =>
    api.post("/api/auth/register", {
      fullName,
      email,
      password,
    }),

  login: (
    email: string,
    password: string
  ) =>
    api.post("/api/auth/login", {
      email,
      password,
    }),

  logout: (email: string) =>
    api.post(
      "/api/auth/logout",
      null,
      {
        params: { email },
      }
    ),
};

export const emailAPI = {

  generate: (
    emailContent: string,
    tone: string
  ) =>
    api.post("/api/email/generate", {
      emailContent,
      tone,
    }),
};

export const historyAPI = {

  getHistory: (
    email: string,
    page: number = 0,
    size: number = 10
  ) =>
    api.get("/api/history", {
      params: {
        email,
        page,
        size,
      },
    }),
};

export default api;