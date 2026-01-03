import { useEffect, useMemo, useState } from "react";
import { fetchMovies, Movie } from "./api";
import "./MoviesPage.css"; // importuje style z osobnego pliku CSS

type SortKey = "title" | "director" | "releaseYear" | "rtScore" | "id";
type SortDir = "asc" | "desc";

// Porównywanie wartości do sortowania (null na koniec, liczby rosnąco, stringi locale PL)
function compare(a: any, b: any) {
    if (a == null && b == null) return 0;
    if (a == null) return 1; // nulls last
    if (b == null) return -1;
    if (typeof a === "number" && typeof b === "number") return a - b;
    return String(a).localeCompare(String(b), "pl", { sensitivity: "base" });
}

export default function MoviesPage() {
    // Dane + status ładowania
    const [movies, setMovies] = useState<Movie[]>([]);
    const [loading, setLoading] = useState(true);
    const [err, setErr] = useState<string | null>(null);

    // UI: filtr i sort
    const [q, setQ] = useState("");
    const [sortKey, setSortKey] = useState<SortKey>("title");
    const [sortDir, setSortDir] = useState<SortDir>("asc");

    // Pobranie filmów po załadowaniu komponentu
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

    // Lista po filtrowaniu i sortowaniu (memo -> nie liczy bez potrzeby)
    const filteredSorted = useMemo(() => {
        const query = q.trim().toLowerCase();

        // Filtr: tylko po tytule
        const filtered = query
            ? movies.filter((m) => (m.title ?? "").toLowerCase().includes(query))
            : movies;

        // Sortowanie wg sortKey / sortDir
        const sorted = [...filtered].sort((a, b) => {
            const av = (a as any)[sortKey];
            const bv = (b as any)[sortKey];
            const c = compare(av, bv);
            return sortDir === "asc" ? c : -c;
        });

        return sorted;
    }, [movies, q, sortKey, sortDir]);

    // Klik w nagłówek tabeli: zmienia kolumnę albo przełącza kierunek
    function toggleSort(key: SortKey) {
        if (key === sortKey) {
            setSortDir((d) => (d === "asc" ? "desc" : "asc"));
        } else {
            setSortKey(key);
            setSortDir("asc");
        }
    }

    // Strzałka przy aktywnej kolumnie
    const sortArrow = (key: SortKey) =>
        sortKey === key ? (sortDir === "asc" ? " ↑" : " ↓") : "";

    return (
        <div className="page">
            <div className="container">
                <header className="header">
                    <div>
                        <h1 className="h1">Movies</h1>

                        {/* Info o wynikach */}
                        <div className="muted">
                            Wyniki: <b>{filteredSorted.length}</b> / {movies.length}
                            {q.trim() ? (
                                <>
                                    {" "}
                                    • filtr: <b>{q.trim()}</b>
                                </>
                            ) : null}
                        </div>
                    </div>

                    {/* Linki pomocnicze */}
                    <div className="links">
                        <a className="pill" href="http://localhost:8080" target="_blank" rel="noreferrer">
                            Backend
                        </a>
                        <a className="pill" href="http://localhost:8080/api/movies" target="_blank" rel="noreferrer">
                            API /api/movies
                        </a>
                    </div>
                </header>

                <div className="card">
                    {/* Toolbar: search + sort info */}
                    <div className="toolbar">
                        <div className="search">
                            <span className="muted">🔎</span>

                            <input
                                className="input"
                                value={q}
                                onChange={(e) => setQ(e.target.value)}
                                placeholder="Szukaj po tytule…"
                            />

                            {q ? (
                                <button className="btnSecondary" onClick={() => setQ("")}>
                                    Wyczyść
                                </button>
                            ) : null}
                        </div>

                        <span className="chip">
                            Sort: <b>{sortKey}</b> ({sortDir})
                        </span>
                    </div>

                    {/* Stany: loading / error / empty / tabela */}
                    {loading ? (
                        <div className="empty">Ładowanie…</div>
                    ) : err ? (
                        <div className="empty">
                            <b>ERROR:</b> {err}
                        </div>
                    ) : filteredSorted.length === 0 ? (
                        <div className="empty">Brak wyników.</div>
                    ) : (
                        <div className="tableWrap">
                            <table className="table">
                                <thead>
                                <tr>
                                    <th className="thClickable" style={{ width: 90 }} onClick={() => toggleSort("id")}>
                                        ID{sortArrow("id")}
                                    </th>
                                    <th className="thClickable" onClick={() => toggleSort("title")}>
                                        Tytuł{sortArrow("title")}
                                    </th>
                                    <th className="thClickable" onClick={() => toggleSort("director")}>
                                        Reżyser{sortArrow("director")}
                                    </th>
                                    <th className="thClickable" style={{ width: 120 }} onClick={() => toggleSort("releaseYear")}>
                                        Rok{sortArrow("releaseYear")}
                                    </th>
                                    <th className="thClickable" style={{ width: 140 }} onClick={() => toggleSort("rtScore")}>
                                        RT score{sortArrow("rtScore")}
                                    </th>
                                </tr>
                                </thead>

                                <tbody>
                                {filteredSorted.map((m) => {
                                    // Kolorowa kropka zależna od oceny
                                    const score = m.rtScore ?? null;
                                    const dotClass =
                                        score == null ? "dotNone" : score < 70 ? "dotLow" : "dotOk";

                                    return (
                                        <tr key={m.id}>
                                            <td className="tdMono">{m.id}</td>

                                            <td className="td">
                                                <div className="title">{m.title}</div>

                                                {/* Skrócony opis */}
                                                {m.description ? (
                                                    <div className="sub">
                                                        {m.description.length > 120
                                                            ? m.description.slice(0, 120) + "…"
                                                            : m.description}
                                                    </div>
                                                ) : null}
                                            </td>

                                            <td className="td">{m.director ?? "—"}</td>
                                            <td className="tdMono">{m.releaseYear ?? "—"}</td>

                                            <td className="td">
                                                <span className="badge">
                                                    <span className={dotClass} />
                                                    <span className="tdMono">{score ?? "—"}</span>
                                                </span>
                                            </td>
                                        </tr>
                                    );
                                })}
                                </tbody>
                            </table>
                        </div>
                    )}

                    <footer className="footer">
                        <span className="muted">MovieHub SPA</span>
                    </footer>
                </div>
            </div>
        </div>
    );
}
