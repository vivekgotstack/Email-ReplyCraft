import { useEffect, useState } from "react";
import { historyAPI } from "@/lib/api";
import { getUser } from "@/lib/auth";

import {
  Card,
  CardHeader,
  CardTitle,
  CardContent,
} from "@/components/ui/card";

import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";

interface HistoryItem {
  id: number;
  prompt: string;
  response: string;
  tone: string;
  model: string;
  createdAt: string;
}

export default function HistoryPanel() {
  const [history, setHistory] = useState<HistoryItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const [expandedItems, setExpandedItems] =
    useState<Set<number>>(new Set());

  const user = getUser();

  useEffect(() => {
    if (user?.email) {
      loadHistory();
    }
  }, [page, user?.email]);

  const loadHistory = async () => {
    try {
      setLoading(true);

      const res = await historyAPI.getHistory(
        user?.email || "",
        page,
        3
      );

      setHistory(res.data.content || []);
      setTotalPages(res.data.totalPages || 0);
    } catch (err) {
      console.error("Failed to load history", err);
    } finally {
      setLoading(false);
    }
  };

  const toggleExpand = (id: number) => {
    setExpandedItems((prev) => {
      const updated = new Set(prev);

      if (updated.has(id)) {
        updated.delete(id);
      } else {
        updated.add(id);
      }

      return updated;
    });
  };

  if (!user) return null;

  return (
    <Card className="border border-slate-800 bg-slate-950/70 backdrop-blur-2xl rounded-2xl">
      <CardHeader>
        <CardTitle className="text-2xl text-white">
          Generation History
        </CardTitle>
      </CardHeader>

      <CardContent>
        {loading ? (
          <div className="py-10 text-center text-slate-400">
            Loading history...
          </div>
        ) : history.length === 0 ? (
          <div className="py-10 text-center text-slate-400">
            No history found.
          </div>
        ) : (
          <div className="space-y-5">
            {history.map((item) => (
              <div
                key={item.id}
                className="rounded-2xl border border-slate-800 bg-slate-900/60 p-5 transition-all hover:border-slate-700"
              >
                <div className="flex items-center justify-between mb-4">
                  <Badge className="bg-indigo-500/15 text-indigo-300 border border-indigo-500/20">
                    {item.tone}
                  </Badge>

                  <span className="text-xs text-slate-500">
                    {new Date(item.createdAt + "Z").toLocaleString("en-IN", {
                      timeZone: "Asia/Kolkata",
                    })}
                  </span>
                </div>

                <div className="space-y-4">
                  <div>
                    <p className="text-xs uppercase tracking-wider text-slate-500 mb-2">
                      Original Email
                    </p>

                    <p className="text-slate-300 text-sm leading-6">
                      {item.prompt}
                    </p>
                  </div>

                  <div>
                    <p className="text-xs uppercase tracking-wider text-slate-500 mb-2">
                      Generated Reply
                    </p>

                    <p className="text-slate-200 text-sm whitespace-pre-wrap leading-7">
                      {expandedItems.has(item.id)
                        ? item.response
                        : `${item.response?.substring(0, 220)}...`}
                    </p>

                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => toggleExpand(item.id)}
                      className="mt-3 text-indigo-300 hover:text-white hover:bg-slate-800"
                    >
                      {expandedItems.has(item.id)
                        ? "Show Less"
                        : "Show More"}
                    </Button>
                  </div>
                </div>
              </div>
            ))}

            <div className="flex justify-between pt-6">
              <Button
                onClick={() => setPage((p) => Math.max(0, p - 1))}
                disabled={page === 0}
                className="bg-slate-900 border border-slate-700 hover:bg-slate-800 text-slate-100 cursor-pointer"
              >
                Previous
              </Button>

              <Button
                onClick={() => setPage((p) => p + 1)}
                disabled={page >= totalPages - 1}
                className="bg-indigo-600 hover:bg-indigo-500 text-white cursor-pointer"
              >
                Next
              </Button>
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
