// Issue: #309
package server

import (
	"context"
	"log"
	"os"
	"testing"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	tc "github.com/testcontainers/testcontainers-go"
	tcpostgres "github.com/testcontainers/testcontainers-go/modules/postgres"
	tcredis "github.com/testcontainers/testcontainers-go/modules/redis"
)

var (
	testDBURL    string
	testRedisURL string
)

func TestMain(m *testing.M) {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Minute)
	defer cancel()

	pgContainer, err := tcpostgres.RunContainer(ctx,
		tc.WithImage("postgres:16-alpine"),
		tcpostgres.WithDatabase("movement"),
		tcpostgres.WithUsername("test"),
		tcpostgres.WithPassword("test"),
	)
	if err != nil {
		log.Printf("Skipping tests: failed to start postgres container: %v", err)
		os.Exit(0)
	}
	defer func() {
		_ = pgContainer.Terminate(context.Background())
	}()

	testDBURL, err = pgContainer.ConnectionString(ctx, "sslmode=disable")
	if err != nil {
		log.Printf("Failed to get postgres connection string: %v", err)
		os.Exit(1)
	}

	redisContainer, err := tcredis.RunContainer(ctx,
		tc.WithImage("redis:7-alpine"),
	)
	if err != nil {
		log.Printf("Skipping tests: failed to start redis container: %v", err)
		os.Exit(0)
	}
	defer func() {
		_ = redisContainer.Terminate(context.Background())
	}()

	testRedisURL, err = redisContainer.ConnectionString(ctx)
	if err != nil {
		log.Printf("Failed to get redis connection string: %v", err)
		os.Exit(1)
	}

	if err := prepareMovementSchema(ctx, testDBURL); err != nil {
		log.Printf("Failed to prepare database schema: %v", err)
		os.Exit(1)
	}

	code := m.Run()

	_ = pgContainer.Terminate(context.Background())
	_ = redisContainer.Terminate(context.Background())

	os.Exit(code)
}

func prepareMovementSchema(ctx context.Context, dbURL string) error {
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return err
	}

	var pool *pgxpool.Pool
	for i := 0; i < 5; i++ {
		pool, err = pgxpool.NewWithConfig(ctx, config)
		if err == nil && pool.Ping(ctx) == nil {
			break
		}
		if pool != nil {
			pool.Close()
		}
		time.Sleep(500 * time.Millisecond)
	}
	if err != nil {
		return err
	}
	defer pool.Close()

	stmts := []string{
		`CREATE EXTENSION IF NOT EXISTS "pgcrypto";`,
		`CREATE SCHEMA IF NOT EXISTS mvp_core;`,
		`CREATE TABLE IF NOT EXISTS mvp_core.character_positions (
			id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
			character_id UUID NOT NULL UNIQUE,
			position_x DOUBLE PRECISION NOT NULL,
			position_y DOUBLE PRECISION NOT NULL,
			position_z DOUBLE PRECISION NOT NULL,
			yaw DOUBLE PRECISION NOT NULL,
			velocity_x DOUBLE PRECISION DEFAULT 0,
			velocity_y DOUBLE PRECISION DEFAULT 0,
			velocity_z DOUBLE PRECISION DEFAULT 0,
			updated_at TIMESTAMPTZ NOT NULL,
			created_at TIMESTAMPTZ NOT NULL,
			deleted_at TIMESTAMPTZ
		);`,
		`CREATE TABLE IF NOT EXISTS mvp_core.character_position_history (
			id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
			character_id UUID NOT NULL,
			position_x DOUBLE PRECISION NOT NULL,
			position_y DOUBLE PRECISION NOT NULL,
			position_z DOUBLE PRECISION NOT NULL,
			yaw DOUBLE PRECISION NOT NULL,
			velocity_x DOUBLE PRECISION DEFAULT 0,
			velocity_y DOUBLE PRECISION DEFAULT 0,
			velocity_z DOUBLE PRECISION DEFAULT 0,
			created_at TIMESTAMPTZ NOT NULL
		);`,
		`CREATE INDEX IF NOT EXISTS idx_character_positions_character_id ON mvp_core.character_positions(character_id) WHERE deleted_at IS NULL;`,
		`CREATE INDEX IF NOT EXISTS idx_character_position_history_character_id ON mvp_core.character_position_history(character_id);`,
	}

	for _, stmt := range stmts {
		if _, err := pool.Exec(ctx, stmt); err != nil {
			return err
		}
	}

	return nil
}

func requireTestDBURL(t *testing.T) string {
	t.Helper()
	if testDBURL == "" {
		t.Skip("postgres test container is not available")
	}
	return testDBURL
}

func requireTestRedisURL(t *testing.T) string {
	t.Helper()
	if testRedisURL == "" {
		t.Skip("redis test container is not available")
	}
	return testRedisURL
}

