import { useState } from "react";
import { authAPI } from "@/lib/api";
import { setToken, setUser } from "@/lib/auth";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "./ui/input";

interface AuthFormProps {
  onAuthSuccess: () => void;
}

export default function AuthForm({ onAuthSuccess }: AuthFormProps) {
  const [isLogin, setIsLogin] = useState(true);
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      if (isLogin) {
        const res = await authAPI.login(email, password);
        const token = res.data?.data?.accessToken || res.data?.accessToken;
        if (!token) {
          throw new Error("No token received from server");
        }
        setToken(token);
        setUser({ email, fullName: "" });
        onAuthSuccess();
      } else {
        const res = await authAPI.register(fullName, email, password);
        const token = res.data?.data?.accessToken || res.data?.accessToken;
        if (!token) {
          throw new Error("No token received from server");
        }
        setToken(token);
        setUser({ email, fullName });
        onAuthSuccess();
      }
    } catch (err: any) {
  console.error("Auth error:", err);

  const status = err.response?.status;
  const backendMsg =
    err.response?.data?.message ||
    err.response?.data?.error ||
    err.message ||
    "";

  let errorMsg = "No account found. Please sign up first.";

  if (isLogin) {
    if (
      status === 404 ||
      backendMsg.toLowerCase().includes("not found") ||
      backendMsg.toLowerCase().includes("user does not exist") ||
      backendMsg.toLowerCase().includes("no user")
    ) {
      errorMsg = "No account found. Please sign up first.";
    } else if (
      status === 401 ||
      backendMsg.toLowerCase().includes("invalid credentials")
    ) {
      errorMsg = "Incorrect email or password.";
    }
  } else {
    if (status === 409 || backendMsg.toLowerCase().includes("already exists")) {
      errorMsg = "Account already exists. Please sign in.";
    }
  }

  setError(errorMsg);
} finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-linear-to-br from-slate-950 via-slate-900 to-slate-950 p-6">
      <Card className="w-full max-w-md border-slate-800 bg-slate-950/50 backdrop-blur-xl text-white shadow-2xl">
        <CardHeader className="space-y-1">
          <CardTitle className="text-3xl font-bold tracking-tight">
            ReplyCraft
          </CardTitle>
          <CardDescription className="text-slate-400">
            {isLogin ? "Enter your credentials to access your account" : "Create your account to get started"}
          </CardDescription>
        </CardHeader>

        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {!isLogin && (
              <div className="space-y-2">
                <Label htmlFor="fullName" className="text-slate-300">Full Name</Label>
                <Input
                  id="fullName"
                  type="text"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  placeholder="John Doe"
                  required={!isLogin}
                  className="bg-slate-900/50 border-slate-700 text-white placeholder:text-slate-500 focus:border-slate-500 focus:ring-slate-500"
                />
              </div>
            )}

            <div className="space-y-2">
              <Label htmlFor="email" className="text-slate-300">Email</Label>
              <Input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="you@example.com"
                required
                className="bg-slate-900/50 border-slate-700 text-white placeholder:text-slate-500 focus:border-slate-500 focus:ring-slate-500"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="password" className="text-slate-300">Password</Label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                required
                minLength={6}
                className="bg-slate-900/50 border-slate-700 text-white placeholder:text-slate-500 focus:border-slate-500 focus:ring-slate-500"
              />
            </div>

            {error && (
              <div className="bg-red-950/50 border border-red-800 text-red-400 px-4 py-3 rounded-md text-sm">
                {error}
              </div>
            )}

            <Button
              type="submit"
              disabled={loading}
              className="w-full bg-slate-100 hover:bg-slate-200 text-slate-900 font-semibold cursor-pointer"
            >
              {loading ? "Processing..." : isLogin ? "Sign In" : "Create Account"}
            </Button>
          </form>

          <div className="mt-6 text-center">
            <button
              type="button"
              onClick={() => setIsLogin(!isLogin)}
              className="text-slate-400 hover:text-white text-sm font-medium transition-colors cursor-pointer"
            >
              {isLogin ? "Don't have an account? " : "Already have an account? "}
              <span className="text-slate-100 hover:underline">
                {isLogin ? "Sign up" : "Sign in"}
              </span>
            </button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
