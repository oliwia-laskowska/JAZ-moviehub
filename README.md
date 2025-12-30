# MovieHub (Spring Boot, multi-module, MariaDB Docker, REST + Thymeleaf + WebSocket + Cache + Scheduler + Logs API)
 ./gradlew :modules:api:bootRun --args='--spring.profiles.active=local'
  npm run dev
  http://localhost:8080/
## Wymagania
- Java 21
- Docker + Docker Compose
- Node 18+ (tylko dla frontendu React)

## Uruchomienie bazy
```bash
cd docker
docker compose up -d
```

## Uruchomienie backendu
```bash
./gradlew :modules:api:bootRun --args='--spring.profiles.active=local'
```

Backend: http://localhost:8080

### Konta (startowe)
- admin / admin123 (ROLE_ADMIN)
- user / user123 (ROLE_USER)

## Synchronizacja danych z internetu
- Automatycznie: scheduler co 60 minut pobiera filmy z publicznego Studio Ghibli API (bez klucza) i aktualizuje bazę.
- Ręcznie (ADMIN): `POST /api/admin/sync`

Źródło danych: Studio Ghibli API (np. endpoint `/films`) udostępniany m.in. pod `https://ghibliapi.vercel.app/` citeturn0search1turn0search4

## Logi do pliku + API
- Logi lądują w `./logs/app.log`
- Odczyt (ADMIN): `GET /api/admin/logs?lines=200`

## Frontend (React)
```bash
cd frontend
npm i
npm run dev
```
Domyślnie frontend odpytuje backend po `http://localhost:8080/api/...` (CORS włączony).

## Endpoints (skrót)
- REST:
  - `GET /api/movies`
  - `GET /api/movies/{id}`
  - `POST /api/reviews` (USER)
- MVC (Thymeleaf):
  - `GET /` (start)
  - `GET /movies` (lista)
- WebSocket (STOMP):
  - endpoint: `/ws`
  - topic: `/topic/sync` (powiadomienie po synchronizacji)
