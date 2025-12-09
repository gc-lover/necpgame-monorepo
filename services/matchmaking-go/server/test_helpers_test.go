package server

import (
	"context"
	"database/sql"
	"fmt"
	"testing"
	"time"

	_ "github.com/lib/pq"
	"github.com/stretchr/testify/require"
	"github.com/testcontainers/testcontainers-go/modules/postgres"
	"github.com/testcontainers/testcontainers-go/modules/redis"
)

func connectWithRetry(t *testing.T, connStr string) *sql.DB {
	t.Helper()

	var db *sql.DB
	var err error

	for i := 0; i < 20; i++ {
		db, err = sql.Open("postgres", connStr)
		if err == nil {
			ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
			err = db.PingContext(ctx)
			cancel()
		}

		if err == nil {
			return db
		}

		if db != nil {
			_ = db.Close()
		}
		time.Sleep(500 * time.Millisecond)
	}

	require.NoError(t, err, "failed to connect to postgres after retries")
	return db
}

func startRedisContainer(t *testing.T) (*redis.RedisContainer, string) {
	t.Helper()

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	t.Cleanup(cancel)

	container, err := redis.Run(ctx, "redis:7-alpine")
	require.NoError(t, err)

	host, err := container.Host(ctx)
	require.NoError(t, err)
	port, err := container.MappedPort(ctx, "6379/tcp")
	require.NoError(t, err)
	addr := fmt.Sprintf("%s:%s", host, port.Port())

	return container, addr
}

func startPostgresContainer(t *testing.T) (*postgres.PostgresContainer, string) {
	t.Helper()

	ctx, cancel := context.WithTimeout(context.Background(), 45*time.Second)
	t.Cleanup(cancel)

	container, err := postgres.Run(ctx,
		"postgres:16-alpine",
		postgres.WithDatabase("matchmaking"),
		postgres.WithUsername("postgres"),
		postgres.WithPassword("postgres"),
	)
	require.NoError(t, err)

	connStr, err := container.ConnectionString(ctx, "sslmode=disable")
	require.NoError(t, err)

	// Ensure DB is ready before returning connection string
	db := connectWithRetry(t, connStr)
	require.NoError(t, db.PingContext(ctx))
	_ = db.Close()

	return container, connStr
}

func prepareMatchmakingSchema(t *testing.T, connStr string) {
	t.Helper()

	ctx, cancel := context.WithTimeout(context.Background(), 20*time.Second)
	defer cancel()

	db := connectWithRetry(t, connStr)
	defer db.Close()

	schema := `
CREATE TABLE IF NOT EXISTS matchmaking_queues (
	id UUID PRIMARY KEY,
	player_id UUID NOT NULL,
	party_id UUID NULL,
	activity_type TEXT NOT NULL,
	queue_status TEXT NOT NULL,
	entered_at TIMESTAMP NOT NULL,
	rating INT NOT NULL,
	rating_range_min INT NOT NULL,
	rating_range_max INT NOT NULL,
	priority INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS player_ratings (
	player_id UUID NOT NULL,
	activity_type TEXT NOT NULL,
	season_id TEXT NOT NULL,
	current_rating INT NOT NULL,
	peak_rating INT NOT NULL DEFAULT 0,
	wins INT NOT NULL DEFAULT 0,
	losses INT NOT NULL DEFAULT 0,
	draws INT NOT NULL DEFAULT 0,
	current_streak INT NOT NULL DEFAULT 0,
	tier TEXT NOT NULL,
	league INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS players (
	id UUID PRIMARY KEY,
	username TEXT NOT NULL
);`

	var execErr error
	for i := 0; i < 5; i++ {
		_, execErr = db.ExecContext(ctx, schema)
		if execErr == nil {
			break
		}
		time.Sleep(500 * time.Millisecond)
	}
	require.NoError(t, execErr)
}

func truncateMatchmakingTables(t *testing.T, db *sql.DB) {
	t.Helper()

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	_, err := db.ExecContext(ctx, `TRUNCATE TABLE matchmaking_queues, player_ratings, players`)
	require.NoError(t, err)
}

func newTestRepository(t *testing.T) (*Repository, func()) {
	t.Helper()

	pgContainer, connStr := startPostgresContainer(t)
	prepareMatchmakingSchema(t, connStr)

	repo, err := NewRepository(connStr)
	require.NoError(t, err)

	cleanup := func() {
		_ = repo.Close()
		_ = pgContainer.Terminate(context.Background())
	}

	return repo, cleanup
}

func newTestCache(t *testing.T) (*CacheManager, func()) {
	t.Helper()

	redisContainer, addr := startRedisContainer(t)
	cache := NewCacheManager(addr)

	cleanup := func() {
		_ = cache.Close()
		_ = redisContainer.Terminate(context.Background())
	}

	return cache, cleanup
}
