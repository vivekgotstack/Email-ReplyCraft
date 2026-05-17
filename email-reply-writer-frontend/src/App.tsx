import { useState, useEffect } from "react";
import { emailAPI, authAPI } from "@/lib/api";
import {
  isAuthenticated,
  removeToken,
  removeUser,
  getUser,
} from "@/lib/auth";

import AuthForm from "@/components/AuthForm";
import HistoryPanel from "@/components/HistoryPanel";

import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
} from "@/components/ui/card";

import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

import { Badge } from "@/components/ui/badge";
import { Label } from "@/components/ui/label";

export default function App() {
  const [authenticated, setAuthenticated] = useState(false);

  const [emailContent, setEmailContent] = useState("");
  const [tone, setTone] = useState("professional");

  const [response, setResponse] = useState("");
  const [loading, setLoading] = useState(false);

  const [showHistory, setShowHistory] = useState(false);

  useEffect(() => {
    setAuthenticated(isAuthenticated());
  }, []);

  const handleAuthSuccess = () => {
    setAuthenticated(true);
  };

  const handleLogout = async () => {
    try {
      const user = getUser();

      if (user) {
        await authAPI.logout(user.email);
      }
    } catch (err) {
      console.error("Logout error:", err);
    } finally {
      removeToken();
      removeUser();

      setAuthenticated(false);
      setResponse("");
      setEmailContent("");
    }
  };

  const generate = async () => {
    if (!emailContent.trim()) return;

    setLoading(true);
    setResponse("");

    try {
      const res = await emailAPI.generate(
        emailContent,
        tone || "professional"
      );

      const data = res.data?.data || res.data;

      setResponse(
        typeof data === "string"
          ? data
          : JSON.stringify(data, null, 2)
      );
    } catch (e: any) {
      console.error("Generation error:", e);

      const errorMsg =
        e.response?.data?.message ||
        e.response?.data?.error ||
        e.message ||
        "Failed to generate response";

      setResponse(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  if (!authenticated) {
    return <AuthForm onAuthSuccess={handleAuthSuccess} />;
  }

  const user = getUser();

  return (
    <div className="min-h-screen bg-[#020617] text-white">
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_top,rgba(99,102,241,0.12),transparent_30%)] pointer-events-none" />

      <div className="relative max-w-5xl mx-auto px-6 py-10">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-5 mb-8">
          <div>
            <h1 className="text-4xl font-bold tracking-tight">
              ReplyCraft
            </h1>

            <p className="text-slate-400 mt-2 text-sm">
              Generate polished AI-powered email replies instantly
            </p>
          </div>

          <div className="flex items-center gap-4">
            <div className="hidden sm:flex flex-col items-end">
              <span className="text-xs text-slate-500">
                Logged in as
              </span>

              <span className="text-sm text-slate-300">
                {user?.email}
              </span>
            </div>

            <Button
              onClick={handleLogout}
              className="bg-slate-800 border border-slate-700 hover:bg-slate-700 text-slate-100 shadow-lg"
            >
              Logout
            </Button>
          </div>
        </div>

        <Card className="border border-slate-800 bg-slate-950/70 backdrop-blur-2xl shadow-[0_0_40px_rgba(15,23,42,0.7)] rounded-2xl">
          <CardHeader className="pb-2">
            <CardTitle className="text-2xl text-white">
              Generate Reply
            </CardTitle>

            <CardDescription className="text-slate-400">
              Paste the email content and generate a professional response.
            </CardDescription>
          </CardHeader>

          <CardContent className="space-y-6">
            <div className="space-y-2">
              <Label
                htmlFor="emailContent"
                className="text-slate-300"
              >
                Email Content
              </Label>

              <Textarea
                id="emailContent"
                placeholder="Paste the email you want to reply to..."
                value={emailContent}
                onChange={(e) => setEmailContent(e.target.value)}
                className="min-h-44 resize-none rounded-xl border-slate-800 bg-slate-900/70 text-slate-100 placeholder:text-slate-500 focus-visible:ring-1 focus-visible:ring-indigo-500"
              />
            </div>

            <div className="flex flex-col sm:flex-row gap-4 sm:items-end">
              <div className="w-full sm:w-64 space-y-2">
                <Label className="text-slate-300">
                  Response Tone
                </Label>

                <Select value={tone} onValueChange={setTone}>
                  <SelectTrigger className="border-slate-800 bg-slate-900/70 text-slate-100 rounded-xl">
                    <SelectValue />
                  </SelectTrigger>

                  <SelectContent className="bg-slate-950 border-slate-800 text-slate-100">
                    <SelectItem value="professional">
                      Professional
                    </SelectItem>

                    <SelectItem value="friendly">
                      Friendly
                    </SelectItem>

                    <SelectItem value="concise">
                      Concise
                    </SelectItem>

                    <SelectItem value="persuasive">
                      Persuasive
                    </SelectItem>

                    <SelectItem value="sarcastic">
                      Sarcastic
                    </SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <Badge className="bg-indigo-500/15 text-indigo-300 border border-indigo-500/20 h-fit px-3 py-1 rounded-lg">
                {tone}
              </Badge>
            </div>

            <Button
              onClick={generate}
              disabled={loading || !emailContent.trim()}
              className="w-full h-12 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold transition-all shadow-lg shadow-indigo-950/40"
            >
              {loading ? "Generating..." : "Generate Reply"}
            </Button>

            {response && (
              <div className="space-y-3">
                <Label className="text-slate-300">
                  Generated Response
                </Label>

                <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-6 whitespace-pre-wrap text-slate-200 leading-7">
                  {response}
                </div>

                <Button
                  onClick={() => navigator.clipboard.writeText(response)}
                  className="w-full rounded-xl bg-slate-800 hover:bg-slate-700 border border-slate-700 text-slate-100"
                >
                  Copy to Clipboard
                </Button>
              </div>
            )}
          </CardContent>
        </Card>

        <div className="flex justify-center mt-8">
          <Button
            onClick={() => setShowHistory(!showHistory)}
            className="bg-slate-900 border border-slate-700 hover:bg-slate-800 text-slate-100 rounded-xl px-8"
          >
            {showHistory ? "Hide History" : "View History"}
          </Button>
        </div>

        {showHistory && (
          <div className="mt-8">
            <HistoryPanel />
          </div>
        )}
      </div>
    </div>
  );
}