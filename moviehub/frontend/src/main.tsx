// frontend/src/main.tsx

// @ts-ignore
import React from "react";
// @ts-ignore
import ReactDOM from "react-dom/client";
import MoviesPage from "./MoviesPage"; // główny komponent aplikacji (strona z filmami)

// Renderuje aplikację React do elementu #root w index.html
ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
        {/* StrictMode pomaga wykrywać problemy w dev (może podwójnie wywoływać efekty) */}
        <MoviesPage />
    </React.StrictMode>
);
