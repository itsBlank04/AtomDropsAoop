# AtomDrops Marketplace OS

Full-stack, multi-vertical e-commerce platform for Bangladesh — **new products**, **used listings**, **repair services**, and **live auctions** in one codebase.

| Layer | Technology |
|---|---|
| Backend | Java 21 · Spring Boot 4.0.6 · Maven |
| Database | PostgreSQL 15 (Supabase) |
| Frontend | React 19 · TypeScript · Vite 8 · Tailwind CSS 4 |
| Real-time | STOMP WebSocket + SSE |

## Repository layout

```
oopproject/
├── backend/                    # Spring Boot API (Maven)
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd         # Maven wrapper
│   └── src/
│       ├── main/java/com/atomdrops/       # controllers, services, entities, repos
│       ├── main/resources/     # application.properties, schema.sql
│       └── test/java/          # JUnit tests
├── frontend/                   # React + Vite SPA
│   ├── package.json
│   └── src/                    # pages, components, contexts, hooks, lib
├── docs/                       # architecture docs & specs
│   ├── master.md               # full architecture overview
│   ├── auction.md              # auction deep-dive
│   ├── projectoverview.md      # project overview & metrics
│   ├── schema.md               # database design (71 tables)
│   └── images/                 # diagrams / screenshots
├── scripts/                    # QA & utility scripts
│   ├── package.json            # deps for QA scripts (pg)
│   ├── qa-e2e-smoke.mjs        # end-to-end smoke test
│   ├── qa-seed-countdown-auction.mjs
│   ├── qa-test-vendor-shops.mjs
│   ├── query-orders.mjs
│   ├── reset-database.sql
│   └── test-upgrade.ps1        # role upgrade flow test
└── AGENTS.md                   # operational guide (auth, quirks, testing)
```

## Quick start

**Backend** (runs on :8080):
```bash
cd backend
./mvnw spring-boot:run          # Windows: .\mvnw.cmd spring-boot:run
```

**Frontend** (runs on :5173):
```bash
cd frontend
npm install
npm run dev
```

**QA scripts** (from repo root, backend must be running):
```bash
node scripts/qa-e2e-smoke.mjs
node scripts/qa-seed-countdown-auction.mjs
./scripts/test-upgrade.ps1
```

## Documentation

- [`docs/master.md`](docs/master.md) — full architecture: verticals, auth, payments, trust scoring, state machines
- [`docs/auction.md`](docs/auction.md) — auction lifecycle, bidding rules, fraud detection
- [`docs/projectoverview.md`](docs/projectoverview.md) — stack, metrics, testing strategy
- [`docs/schema.md`](docs/schema.md) — database design
- [`AGENTS.md`](AGENTS.md) — commands, auth model, architecture quirks for AI agents
