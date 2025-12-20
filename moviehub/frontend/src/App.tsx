// @ts-ignore
import React, { useEffect, useState } from "react";

type Movie = {
    id: number;
    title: string;
    director: string;
    releaseYear: number;
    rtScore: number;
    genres: string[];
};

function App() {
    const [movies, setMovies] = useState<Movie[]>([]);
    const [error, setError] = useState<string>("");

    // login UI
    const [username, setUsername] = useState("admin");
    const [password, setPassword] = useState("");
    const [syncInfo, setSyncInfo] = useState<string>("");

    async function loadMovies() {
        setError("");
        try {
            const r = await fetch("http://localhost:8080/api/movies");
            if (!r.ok) throw new Error(await r.text());
            setMovies(await r.json());
        } catch (e: any) {
            setError(String(e));
        }
    }

    useEffect(() => {
        loadMovies();
    }, []);

    async function triggerSync() {
        setSyncInfo("Sync…");
        try {
            const r = await fetch("http://localhost:8080/api/admin/sync", {
                method: "POST",
                headers: {
                    Authorization: "Basic " + btoa(`${username}:${password}`),
                },
            });

            if (!r.ok) throw new Error(await r.text());
            const data = await r.json();
            setSyncInfo(`Sync OK. Upserts=${data.upserts}`);

            await loadMovies();
        } catch (e: any) {
            setSyncInfo("Sync FAIL: " + String(e));
        }
    }

    return (
        <div style={{fontFamily: "system-ui", padding: 16}}>
            <h1>MovieHub SPA</h1>

            {/* LOGIN PANEL */}
            <div style={{marginBottom: 12, padding: 12, border: "1px solid #ddd"}}>
                <b>Admin login (Basic Auth)</b>
                <div style={{marginTop: 8}}>
                    <label>
                        Login:&nbsp;
                        <input
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            style={{marginRight: 8}}
                        />
                    </label>

                    <label>
                        Hasło:&nbsp;
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            style={{marginRight: 8}}
                        />
                    </label>

                    <button onClick={triggerSync}>SYNC (admin)</button>
                    <button onClick={loadMovies} style={{marginLeft: 8}}>
                        Odśwież
                    </button>
                </div>

                {syncInfo && <p style={{marginTop: 8}}>{syncInfo}</p>}
            </div>

            {error && (
                <div style={{padding: 12, border: "1px solid #ccc"}}>
                    <b>Błąd:</b> {error}
                    <p>
                        Sprawdź czy backend działa na <code>http://localhost:8080</code>.
                    </p>
                </div>
            )}

            {!error && movies.length === 0 && <p>Ładowanie…</p>}

            {movies.length > 0 && (
                <table cellPadding={8} style={{borderCollapse: "collapse"}}>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tytuł</th>
                        <th>Reżyser</th>
                        <th>Rok</th>
                        <th>RT</th>
                        <th>Gatunki</th>
                    </tr>
                    </thead>
                    <tbody>
                    {movies.map((m) => (
                        <tr key={m.id}>
                            <td>{m.id}</td>
                            <td>{m.title}</td>
                            <td>{m.director}</td>
                            <td>{m.releaseYear}</td>
                            <td>{m.rtScore}</td>
                            <td>{(m.genres ?? []).join(", ")}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default App
