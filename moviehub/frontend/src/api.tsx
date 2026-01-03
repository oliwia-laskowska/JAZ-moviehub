export type Movie = {
    id: number; // ID filmu
    title: string; // tytuł
    director?: string | null; // reżyser (opcjonalnie)
    releaseYear?: number | null; // rok wydania (opcjonalnie)
    rtScore?: number | null; // ocena (opcjonalnie)
    description?: string | null; // opis (opcjonalnie)
};

const API_BASE = "http://localhost:8080"; // base URL backendu

// Pobiera listę filmów z API
export async function fetchMovies(): Promise<Movie[]> {
    const res = await fetch(`${API_BASE}/api/movies`, {
        credentials: "include", // wysyła cookies (sesja / auth)
    });

    // Jeśli backend zwróci błąd HTTP, rzuca wyjątek
    if (!res.ok) throw new Error(`API error: ${res.status}`);

    // Parsuje JSON -> Movie[]
    return res.json();
}
