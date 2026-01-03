# MovieHub 

Aplikacja internetowa (projekt wielomodułowy) do przeglądania filmów i dodawania recenzji.  
Backend (Spring Boot) pobiera dane z internetu (GHIBLI + TMDB), zapisuje je w bazie, udostępnia REST API oraz widoki Thymeleaf.  
Frontend (React SPA) pokazuje listę filmów i pozwala sortować / filtrować dane.

---

## Moduły projektu

Projekt jest wielomodułowy:

- `modules/api` – backend Spring Boot (REST + Thymeleaf + Security + Scheduler + WebSocket)
- `modules/frontend` *(opcjonalnie / osobny katalog)* – React SPA (Vite)

---

## Wymagania na ocenę 3.0 

### 1) Minimum 5 encji w bazie + relacje
Encje (JPA / Hibernate):
- `User`
- `Role`
- `Movie`
- `Genre`
- `Review`
- `WatchlistItem`
- `SyncRun`

Relacje:
- `User` ↔ `Role` (ManyToMany)
- `Movie` ↔ `Genre` (ManyToMany)
- `Review` → `User` (ManyToOne)
- `Review` → `Movie` (ManyToOne)
- `WatchlistItem` → `User` + `Movie` (ManyToOne)
- `SyncRun` (historia uruchomień synchronizacji)

### 2) Warstwa bazodanowa oparta o repozytoria Spring
Repozytoria:
- `MovieRepository`
- `GenreRepository`
- `ReviewRepository`
- `UserRepository`
- `RoleRepository`
- `WatchlistRepository`
- `SyncRunRepository`

### 3) REST Controllery dla zasobów bazodanowych
REST API:
- `GET /api/movies`
- `GET /api/movies/{id}`
- `GET /api/reviews/by-movie/{movieId}`
- `POST /api/reviews` (dodawanie recenzji – wymaga zalogowania)

Admin REST:
- `POST /api/admin/sync` (sync danych)
- `GET /api/admin/logs?lines=200` (odczyt logów z pliku)

### 4) Projekt wielomodułowy
Kod podzielony na moduły (`modules/...`).

### 5) Update bazy danych danymi z internetu
Dane pobierane z:
- Ghibli API: `https://ghibliapi.vercel.app/films`
- TMDB API: `/movie/popular` + `/movie/{id}/credits`

Synchronizacja:
- ręcznie z panelu admina (UI / API)
- automatycznie przez scheduler

---

## Elementy podbijające ocenę 

### Spring Cache 
Konfiguracja cache: Caffeine  
Pliki:
- `CacheConfig`
- `MovieService` używa `@Cacheable` dla listy filmów i `getById()`
- `SyncService` po synchronizacji wykonuje `evictCaches()` (czyszczenie cache)

### Logowanie do pliku + API do odczytu logów 
Logback zapisuje logi do `logs/app.log` (konfiguracja w `logback-spring.xml`).  
API:
- `GET /api/admin/logs?lines=200`

### Spring MVC / Thymeleaf 
Widoki server-side:
- `/` (`index.html`)
- `/movies` (`movies.html`)
- `/admin` (`admin.html`)
- `/login` (`login.html`)
- `error.html`

### Frontend SPA React 
React (Vite) pobiera dane z API i wyświetla filmy:
- `GET http://localhost:8080/api/movies`
Funkcje:
- filtrowanie po tytule
- sortowanie po kolumnach
- UI w SPA


### Spring Security 
- logowanie formularzem (`/login`) dla UI
- Basic Auth dla API admina (`/api/admin/sync`)
- role: `ADMIN`, `USER`
- `RoleBasedSuccessHandler` – redirect po roli (`/admin` dla ADMIN)

### Spring Schedulers 
Scheduler automatycznie uruchamia sync danych co X czasu:
- `MovieSyncScheduler`
- parametry w `application.yml` (`fixedDelayMs`, `initialDelayMs`)

### WebSockety 
WebSocket STOMP:
- endpoint: `/ws`
- topic: `/topic/sync`

Po synchronizacji wysyłane jest powiadomienie:
- `NotificationsController.notifySync("...")`

---

## Jak uruchomić

### 1) Backend (Spring Boot)
W katalogu backendu:

```bash
 ./gradlew :modules:api:bootRun
````

Backend startuje na:

* `http://localhost:8080`

### 2) Frontend (React SPA)

W katalogu `frontend`:

```bash
npm install
npm run dev
```

Frontend:

* `http://localhost:5173`

---

## Konta startowe

Tworzone przy starcie aplikacji (`StartupDataConfig`):

* admin:

  * login: `admin`
  * hasło: `admin123`
* user:

  * login: `user`
  * hasło: `user123`

---

## Najważniejsze endpointy

### UI (Thymeleaf)

* `GET /` – strona główna
* `GET /movies` – widok filmów
* `GET /admin` – panel admina
* `POST /admin/sync` – sync z UI
* `GET /login` – logowanie

### REST API

* `GET /api/movies`
* `GET /api/movies/{id}`
* `GET /api/reviews/by-movie/{movieId}`
* `POST /api/reviews` *(wymaga zalogowania)*

### Admin API

* `POST /api/admin/sync` *(wymaga roli ADMIN)*
* `GET /api/admin/logs?lines=200` *(wymaga roli ADMIN)*

### WebSocket

* `ws://localhost:8080/ws`
* topic: `/topic/sync`

