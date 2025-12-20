export type Movie = {
    id: number;
    title: string;
    director?: string | null;
    releaseYear?: number | null;
    rtScore?: number | null;
    description?: string | null;
};

const API_BASE = "http://localhost:8080";

export async function fetchMovies(): Promise<Movie[]> {
    const res = await fetch(`${API_BASE}/api/movies`, { credentials: "include" });
    if (!res.ok) throw new Error(`API error: ${res.status}`);
    return res.json();
}
