import { useEffect, useMemo, useState } from "react";
import { fetchMovies, Movie } from "./api";

type SortKey = "title" | "director" | "releaseYear" | "rtScore";
type SortDir = "asc" | "desc";

function compare(a: any, b: any) {
    if (a == null && b == null) return 0;
    if (a == null) return 1; // nulls last
    if (b == null) return -1;
    if (typeof a === "number" && typeof b === "number") return a - b;
    return String(a).localeCompare(String(b), "pl", { sensitivity: "base" });
}

export default function MoviesPage() {
    const [movies, setMovies] = useState<Movie[]>([]);
    const [loading, setLoading] = useState(true);
    const [err, setErr] = useState<string | null>(null);

    const [q, setQ] = useState("");
    const [sortKey, setSortKey] = useState<SortKey>("title");
    const [sortDir, setSortDir] = useState<SortDir>("asc");

    useEffect(() => {
        (async () => {
            try {
                setLoading(true);
                const data = await fetchMovies();
                setMovies(data);
            } catch (e: any) {
                setErr(e?.message ?? "Nie udało się pobrać filmów");
            } finally {
                setLoading(false);
            }
        })();
    }, []);

    const filteredSorted = useMemo(() => {
        const query = q.trim().toLowerCase();

        const filtered = query
            ? movies.filter((m) => (m.title ?? "").toLowerCase().includes(query))
            : movies;

        const sorted = [...filtered].sort((a, b) => {
            const av = (a as any)[sortKey];
            const bv = (b as any)[sortKey];
            const c = compare(av, bv);
            return sortDir === "asc" ? c : -c;
        });

        return sorted;
    }, [movies, q, sortKey, sortDir]);

    function toggleSort(key: SortKey) {
        if (key === sortKey) {
            setSortDir((d) => (d === "asc" ? "desc" : "asc"));
        } else {
            setSortKey(key);
            setSortDir("asc");
        }
    }

    const sortArrow = (key: SortKey) =>
        sortKey === key ? (sortDir === "asc" ? " ↑" : " ↓") : "";

    return (
        <div style={styles.page}>
            <div style={styles.container}>
                <header style={styles.header}>
                    <div>
                        <h1 style={styles.h1}>Movies</h1>
                        <div style={styles.muted}>
                            Wyniki: <b>{filteredSorted.length}</b> / {movies.length}
                            {q.trim() ? (
                                <>
                                    {" "}
                                    • filtr: <b>{q.trim()}</b>
                                </>
                            ) : null}
                        </div>
                    </div>

                    <div style={{ display: "flex", gap: 10, flexWrap: "wrap" }}>
                        <a style={styles.pill} href="http://localhost:8080" target="_blank" rel="noreferrer">
                            Backend
                        </a>
                        <a style={styles.pill} href="http://localhost:8080/api/movies" target="_blank" rel="noreferrer">
                            API /api/movies
                        </a>
                    </div>
                </header>

                <div style={styles.card}>
                    <div style={styles.toolbar}>
                        <div style={styles.search}>
                            <span style={styles.muted}>🔎</span>
                            <input
                                style={styles.input}
                                value={q}
                                onChange={(e) => setQ(e.target.value)}
                                placeholder="Szukaj po tytule…"
                            />
                            {q ? (
                                <button style={styles.btnSecondary} onClick={() => setQ("")}>
                                    Wyczyść
                                </button>
                            ) : null}
                        </div>

                        <div style={{ display: "flex", gap: 10, flexWrap: "wrap", alignItems: "center" }}>
                            <span style={styles.chip}>Sort: <b>{sortKey}</b> ({sortDir})</span>
                        </div>
                    </div>

                    {loading ? (
                        <div style={styles.empty}>Ładowanie…</div>
                    ) : err ? (
                        <div style={styles.empty}>
                            <b>ERROR:</b> {err}
                        </div>
                    ) : filteredSorted.length === 0 ? (
                        <div style={styles.empty}>Brak wyników.</div>
                    ) : (
                        <div style={styles.tableWrap}>
                            <table style={styles.table}>
                                <thead>
                                <tr>
                                    <th style={{ ...styles.th, width: 90 }} onClick={() => toggleSort("id" as any)}>
                                        ID
                                    </th>
                                    <th style={styles.thClickable} onClick={() => toggleSort("title")}>
                                        Tytuł{sortArrow("title")}
                                    </th>
                                    <th style={styles.thClickable} onClick={() => toggleSort("director")}>
                                        Reżyser{sortArrow("director")}
                                    </th>
                                    <th style={{ ...styles.thClickable, width: 120 }} onClick={() => toggleSort("releaseYear")}>
                                        Rok{sortArrow("releaseYear")}
                                    </th>
                                    <th style={{ ...styles.thClickable, width: 140 }} onClick={() => toggleSort("rtScore")}>
                                        RT score{sortArrow("rtScore")}
                                    </th>
                                </tr>
                                </thead>

                                <tbody>
                                {filteredSorted.map((m) => {
                                    const score = m.rtScore ?? null;
                                    const dotStyle =
                                        score == null
                                            ? styles.dotNone
                                            : score < 70
                                                ? styles.dotLow
                                                : styles.dotOk;

                                    return (
                                        <tr key={m.id} style={styles.tr}>
                                            <td style={styles.tdMono}>{m.id}</td>

                                            <td style={styles.td}>
                                                <div style={styles.title}>{m.title}</div>
                                                {m.description ? (
                                                    <div style={styles.sub}>
                                                        {m.description.length > 120 ? m.description.slice(0, 120) + "…" : m.description}
                                                    </div>
                                                ) : null}
                                            </td>

                                            <td style={styles.td}>{m.director ?? "—"}</td>
                                            <td style={styles.tdMono}>{m.releaseYear ?? "—"}</td>

                                            <td style={styles.td}>
                          <span style={styles.badge}>
                            <span style={dotStyle} />
                            <span style={styles.tdMono}>{score ?? "—"}</span>
                          </span>
                                            </td>
                                        </tr>
                                    );
                                })}
                                </tbody>
                            </table>
                        </div>
                    )}

                    <footer style={styles.footer}>
                        <span style={styles.muted}>MovieHub SPA</span>

                    </footer>
                </div>
            </div>
        </div>
    );
}

const styles: Record<string, any> = {
    page: {
        minHeight: "100vh",
        padding: 24,
        background:
            "radial-gradient(900px 600px at 20% 0%, #f1f5ff 0%, transparent 60%), radial-gradient(900px 600px at 80% 10%, #fff7ed 0%, transparent 55%), #fafafa",
        color: "#111",
        fontFamily:
            'ui-sans-serif, system-ui, -apple-system, "Segoe UI", Roboto, Arial',
    },
    container: { maxWidth: 1100, margin: "0 auto" },
    header: { display: "flex", justifyContent: "space-between", gap: 16, flexWrap: "wrap", marginBottom: 14 },
    h1: { margin: 0, fontSize: 22 },
    muted: { color: "#6b7280", fontSize: 13 },
    pill: {
        display: "inline-flex",
        alignItems: "center",
        gap: 8,
        padding: "10px 12px",
        borderRadius: 999,
        border: "1px solid #e5e7eb",
        background: "#fff",
        color: "#111",
        textDecoration: "none",
        fontSize: 13,
    },
    card: {
        background: "#fff",
        border: "1px solid #e5e7eb",
        borderRadius: 16,
        boxShadow: "0 10px 30px rgba(0,0,0,.06)",
        overflow: "hidden",
    },
    toolbar: {
        padding: 14,
        borderBottom: "1px solid #e5e7eb",
        display: "flex",
        gap: 12,
        flexWrap: "wrap",
        justifyContent: "space-between",
        alignItems: "center",
    },
    search: {
        display: "flex",
        alignItems: "center",
        gap: 10,
        border: "1px solid #e5e7eb",
        borderRadius: 999,
        padding: "10px 12px",
        minWidth: 320,
        background: "#fff",
    },
    input: {
        border: 0,
        outline: 0,
        width: "100%",
        fontSize: 14,
        background: "transparent",
    },
    btnSecondary: {
        border: "1px solid #e5e7eb",
        background: "#fff",
        padding: "8px 10px",
        borderRadius: 999,
        cursor: "pointer",
        fontSize: 13,
    },
    chip: {
        display: "inline-flex",
        alignItems: "center",
        gap: 6,
        padding: "10px 12px",
        borderRadius: 999,
        border: "1px solid #e5e7eb",
        background: "#fff",
        fontSize: 13,
    },
    tableWrap: { overflow: "auto" },
    table: { width: "100%", borderCollapse: "separate", borderSpacing: 0, minWidth: 900 },
    th: {
        position: "sticky",
        top: 0,
        background: "#f9fafb",
        textAlign: "left",
        padding: "12px 14px",
        borderBottom: "1px solid #e5e7eb",
        fontSize: 12,
        textTransform: "uppercase",
        letterSpacing: ".04em",
        color: "#374151",
        whiteSpace: "nowrap",
    },
    thClickable: {
        position: "sticky",
        top: 0,
        background: "#f9fafb",
        textAlign: "left",
        padding: "12px 14px",
        borderBottom: "1px solid #e5e7eb",
        fontSize: 12,
        textTransform: "uppercase",
        letterSpacing: ".04em",
        color: "#374151",
        cursor: "pointer",
        userSelect: "none",
        whiteSpace: "nowrap",
    },
    tr: {},
    td: { padding: "12px 14px", borderBottom: "1px solid #e5e7eb", fontSize: 14, verticalAlign: "top" },
    tdMono: { padding: "12px 14px", borderBottom: "1px solid #e5e7eb", fontVariantNumeric: "tabular-nums", fontSize: 14 },
    title: { fontWeight: 650, lineHeight: 1.2 },
    sub: { marginTop: 4, fontSize: 12, color: "#6b7280" },
    badge: {
        display: "inline-flex",
        alignItems: "center",
        gap: 8,
        padding: "6px 10px",
        borderRadius: 999,
        border: "1px solid #e5e7eb",
        background: "#fff",
    },
    dotOk: { width: 8, height: 8, borderRadius: 999, background: "#22c55e", boxShadow: "0 0 0 3px rgba(34,197,94,.15)" },
    dotLow: { width: 8, height: 8, borderRadius: 999, background: "#f59e0b", boxShadow: "0 0 0 3px rgba(245,158,11,.18)" },
    dotNone:{ width: 8, height: 8, borderRadius: 999, background: "#9ca3af", boxShadow: "0 0 0 3px rgba(156,163,175,.20)" },
    empty: { padding: 22, color: "#6b7280", fontSize: 14 },
    footer: { padding: 14, display: "flex", justifyContent: "space-between", flexWrap: "wrap", gap: 10 },
};
