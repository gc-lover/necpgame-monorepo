# 📖 Go Performance Bible - Part 7A

**PostgreSQL Advanced Optimizations**

---

# POSTGRESQL ADVANCED

## 🔴 CRITICAL: pgBouncer

```yaml
# pgbouncer.ini
[databases]
gamedb = host=postgres port=5432 dbname=game

[pgbouncer]
pool_mode = transaction
max_client_conn = 10000
default_pool_size = 25
reserve_pool_size = 5
```

```go
db, _ := sql.Open("postgres", "host=pgbouncer port=6432")
```

**Gains:** 10k connections → 25 real, Latency ↓30%

---

## 🟡 HIGH: LISTEN/NOTIFY

```go
import "github.com/lib/pq"

type EventListener struct {
    listener *pq.Listener
}

func NewEventListener(connStr string) *EventListener {
    listener := pq.NewListener(connStr, 10*time.Second, time.Minute, nil)
    listener.Listen("player_events")
    return &EventListener{listener: listener}
}

func (el *EventListener) Start() {
    go func() {
        for notification := range el.listener.Notify {
            el.handleEvent(notification.Extra)
        }
    }()
}
```

**SQL Trigger:**
```sql
CREATE FUNCTION notify_player_event() RETURNS TRIGGER AS $$
BEGIN
  PERFORM pg_notify('player_events', row_to_json(NEW)::text);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

**Gains:** 0 polling, instant events

---

## 🟡 HIGH: JSONB Optimization

```sql
-- GIN index для JSONB
CREATE INDEX idx_inventory_gin 
ON players USING GIN (inventory jsonb_path_ops);

-- Fast query
SELECT * FROM players WHERE inventory @> '{"weapon": "AK47"}';

-- Update field
UPDATE players 
SET inventory = jsonb_set(inventory, '{health}', '100')
WHERE id = $1;
```

```go
func (r *Repo) UpdateInventoryItem(playerID, itemID string, qty int) error {
    return r.db.Exec(`UPDATE players SET inventory = jsonb_set(inventory, $1, $2::jsonb) WHERE id = $3`,
        fmt.Sprintf("{items,%s}", itemID), qty, playerID).Err()
}
```

---

## 🟢 MEDIUM: Unlogged Tables

```sql
-- Temp data (no WAL)
CREATE UNLOGGED TABLE active_sessions (
    session_id VARCHAR(64) PRIMARY KEY,
    player_id BIGINT,
    data JSONB
);
```

**Gains:** Write ↑300-500%  
**WARNING:** Данные теряются при crash!

---

## 🟡 HIGH: WAL Tuning

```sql
-- postgresql.conf
wal_level = replica
wal_buffers = 16MB
max_wal_size = 4GB
checkpoint_timeout = 15min
checkpoint_completion_target = 0.9

-- Performance (с риском)
synchronous_commit = off  # +50% write
```

**Trade-off:** Можно потерять 200ms при crash

---

## 🟡 HIGH: Prepared Cache

```go
type PreparedCache struct {
    stmts sync.Map
    db    *sql.DB
}

func (pc *PreparedCache) Exec(query string, args ...interface{}) error {
    var stmt *sql.Stmt
    
    if s, ok := pc.stmts.Load(query); ok {
        stmt = s.(*sql.Stmt)
    } else {
        stmt, _ = pc.db.Prepare(query)
        pc.stmts.Store(query, stmt)
    }
    
    _, err := stmt.Exec(args...)
    return err
}
```

**Gains:** Query planning ↓100%

---

## 🟢 MEDIUM: Parallel Queries

```sql
-- postgresql.conf
max_parallel_workers_per_gather = 4
max_parallel_workers = 8

-- Auto parallel
SELECT COUNT(*) FROM players WHERE level > 50;
-- Parallel Seq Scan (workers=4)
```

---

## 🟢 MEDIUM: Autovacuum Tuning

```sql
-- Частый vacuum для game tables
ALTER TABLE players SET (
    autovacuum_vacuum_scale_factor = 0.01,
    autovacuum_analyze_scale_factor = 0.005
);
```

---

## 🟡 HIGH: Connection Settings

```sql
-- postgresql.conf
max_connections = 200       # Low (pgbouncer!)
shared_buffers = 8GB       # 25% RAM
effective_cache_size = 24GB # 75% RAM
work_mem = 64MB
maintenance_work_mem = 2GB
```

---

**Next:** [Part 7B - Redis Advanced](./07b-redis-advanced.md)  
**Previous:** [Part 6](./06-resilience-compression.md)

