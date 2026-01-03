// @ts-ignore
import React, { useEffect, useState } from "react";

// Typ danych filmu zwracany z API
type Movie = {
    id: number;
    title: string;
    director: string;
    releaseYear: number;
    rtScore: number;
    genres: string[];
};

function App() {
    // Stan listy filmów + ewentualny błąd ładowania
    const [movies, setMovies] = useState<Movie[]>([]);
    const [error, setError] = useState<string>("");

    // Stan prostego panelu logowania (Basic Auth) do endpointów admina
    const [username, setUsername] = useState("admin");
    const [password, setPassword] = useState("");
    const [syncInfo, setSyncInfo] = useState<string>(""); // status synchronizacji

    // Pobiera filmy z backendu i zapisuje do stanu
    async function loadMovies() {
        setError("");
        try {
            const r = await fetch("http://localhost:8080/api/movies"); // GET movies
            if (!r.ok) throw new Error(await r.text()); // błąd HTTP -> wyjątek
            setMovies(await r.json()); // zapisuje wyniki
        } catch (e: any) {
            setError(String(e)); // zapisuje błąd do UI
        }
    }

    // Ładuje filmy raz po starcie komponentu
    useEffect(() => {
        loadMovies();
    }, []);

    // Wywołuje sync przez endpoint admina
    async function triggerSync() {
        setSyncInfo("Sync…");
        try {
            const r = await fetch("http://localhost:8080/api/admin/sync", {
                method: "POST",
                headers: {
                    Authorization: "Basic " + btoa(`${username}:${password}`), // Basic Auth
                },
            });

            if (!r.ok) throw new Error(await r.text()); // błąd -> wyjątek
            const data = await r.json(); // { upserts: number }
            setSyncInfo(`Sync OK. Upserts=${data.upserts}`);

            await loadMovies(); // po sync odświeża listę filmów
        } catch (e: any) {
            setSyncInfo("Sync FAIL: " + String(e));
        }
    }

    return (
        <div style={{ fontFamily: "system-ui", padding: 16 }}>
            <h1>MovieHub SPA</h1>

            {/* Panel do testowania sync (admin) */}
            <div style={{ marginBottom: 12, padding: 12, border: "1px solid #ddd" }}>
                <b>Admin login (Basic Auth)</b>

                <div style={{ marginTop: 8 }}>
                    {/* Login */}
                    <label>
                        Login:&nbsp;
                        <input
                            value={username}
                            onChange={(e) => setUsername(e.target.value)} // aktualizacja username
                            style={{ marginRight: 8 }}
                        />
                    </label>

                    {/* Hasło */}
                    <label>
                        Hasło:&nbsp;
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)} // aktualizacja password
                            style={{ marginRight: 8 }}
                        />
                    </label>

                    {/* Akcje */}
                    <button onClick={triggerSync}>SYNC (admin)</button>
                    <button onClick={loadMovies} style={{ marginLeft: 8 }}>
                        Odśwież
                    </button>
                </div>

                {/* Info o sync */}
                {syncInfo && <p style={{ marginTop: 8 }}>{syncInfo}</p>}
            </div>

            {/* Błąd ładowania filmów */}
            {error && (
                <div style={{ padding: 12, border: "1px solid #ccc" }}>
                    <b>Błąd:</b> {error}
                    <p>
                        Sprawdź czy backend działa na <code>http://localhost:8080</code>.
                    </p>
                </div>
            )}

            {/* Stan ładowania */}
            {!error && movies.length === 0 && <p>Ładowanie…</p>}

            {/* Tabela filmów */}
            {movies.length > 0 && (
                <table cellPadding={8} style={{ borderCollapse: "collapse" }}>
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
                            {/* Zabezpieczenie jeśli genres byłby null/undefined */}
                            <td>{(m.genres ?? []).join(", ")}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default App;
