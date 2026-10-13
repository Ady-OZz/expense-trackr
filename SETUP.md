# Setup (INR / ₹ edition)

## Option A — one command (needs Docker Desktop)
    cp .env.example .env        # optional, edit if you like
    docker compose up --build
Open http://localhost:3000

Admin login: admin@example.com / Admin@123 (change in .env). Default categories are created automatically.
Sign-up skips email OTP by default (SKIP_EMAIL_VERIFICATION=true). To use real emails set it to false and
fill MAIL_USERNAME / MAIL_PASSWORD (Gmail App Password).

## Option B — without Docker
Prereqs: JDK 21, Maven, Node 18+, MySQL 8.
1. Start MySQL (database is auto-created).
2. Backend:   cd backend && DB_PASSWORD=yourpass SKIP_EMAIL_VERIFICATION=true ./mvnw spring-boot:run
3. Frontend:  cd frontend && npm install && npm start     -> http://localhost:3000

## Config (env vars, backend)
DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET, CORS_ALLOWED_ORIGINS, SKIP_EMAIL_VERIFICATION,
MAIL_USERNAME, MAIL_PASSWORD, ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PASSWORD, PORT
Frontend: REACT_APP_API_URL (e.g. https://your-api.example.com/mywallet) — set at build time.

## Currency
All amounts display as ₹ with Indian grouping (₹1,25,000). See frontend/src/components/utils/currency.js.
