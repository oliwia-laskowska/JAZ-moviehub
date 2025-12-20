// frontend/src/main.tsx
// @ts-ignore
import React from "react";
// @ts-ignore
import ReactDOM from "react-dom/client";
import MoviesPage from './MoviesPage';

ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
        <MoviesPage />
    </React.StrictMode>
);
