import { useState } from "react";
import axios from "axios";
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

export default function App() {
  const [emailContent, setEmailContent] = useState("");
  const [tone, setTone] = useState("");
  const [response, setResponse] = useState("");
  const [loading, setLoading] = useState(false);

  const generate = async () => {
    setLoading(true);
    setResponse("");

    try {
      const res = await axios.post(
        "https://email-replycraft.onrender.com/api/email/generate",
        { emailContent, tone: tone || "professional" }
      );
      setResponse(res.data.data);
    } catch (e) {
      setResponse("Backend shat itself. Check logs.");
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="min-h-svh flex items-center justify-center bg-linear-to-br from-black via-zinc-900 to-black p-6">
      <Card className="w-full max-w-xl border-zinc-800 bg-zinc-950 text-white shadow-2xl">
        <CardHeader>
          <CardTitle className="text-2xl tracking-tight">
            ReplyCraft
          </CardTitle>
          <CardDescription className="text-zinc-400">
            Write smarter replies. Choose your tone.
          </CardDescription>
        </CardHeader>

        <CardContent className="space-y-4">
          <Textarea
            placeholder="Paste the boring email here…"
            value={emailContent}
            onChange={(e) => setEmailContent(e.target.value)}
            className="min-h-30"
          />

          <div className="flex items-center gap-3">
            <Select value={tone} onValueChange={setTone}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder="Select tone" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="professional">Professional</SelectItem>
                <SelectItem value="friendly">Friendly</SelectItem>
                <SelectItem value="concise">Concise</SelectItem>
                <SelectItem value="persuasive">Persuasive</SelectItem>
                <SelectItem value="sarcastic">Sarcastic</SelectItem>
              </SelectContent>
            </Select>

            <Badge variant="destructive">{tone}</Badge>
          </div>

          <Button
            onClick={generate}
            disabled={loading || !emailContent}
            className="w-full"
          >
            {loading ? "Generating response…" : "Generate"}
          </Button>

          {response && (
            <div className="rounded-md border border-zinc-800 bg-zinc-900 p-4 text-sm whitespace-pre-wrap">
              {response}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
