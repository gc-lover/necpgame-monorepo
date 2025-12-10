package server

import (
	"context"
	"os"
	"testing"
	"time"

	"go.uber.org/goleak"

	"github.com/testcontainers/testcontainers-go/modules/postgres"
	"github.com/testcontainers/testcontainers-go/modules/redis"
)

var (
	pgContainer    *postgres.PostgresContainer
	redisContainer *redis.RedisContainer
	testPgConnStr  string
	testRedisAddr  string
)

func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("github.com/Microsoft/go-winio.ioCompletionProcessor"),
		goleak.IgnoreTopFunction("github.com/testcontainers/testcontainers-go.(*Reaper).connect.func1"),
		goleak.IgnoreAnyFunction("github.com/Microsoft/go-winio.ioCompletionProcessor"),
	)

	ctx, cancel := context.WithTimeout(context.Background(), 2*time.Minute)
	defer cancel()

	pgContainer = startPostgresForSuite(ctx)
	redisContainer = startRedisForSuite(ctx)

	code := m.Run()

	shutdownSuite(ctx)
	os.Exit(code)
}

func startPostgresForSuite(ctx context.Context) *postgres.PostgresContainer {
	container, err := postgres.Run(ctx,
		"postgres:16-alpine",
		postgres.WithDatabase("matchmaking"),
		postgres.WithUsername("postgres"),
		postgres.WithPassword("postgres"),
	)
	if err != nil {
		panic(err)
	}

	connStr, err := container.ConnectionString(ctx, "sslmode=disable")
	if err != nil {
		panic(err)
	}
	testPgConnStr = connStr

	// Ensure ready
	db := connectWithRetryCtx(ctx, connStr)
	if db == nil {
		panic("postgres not ready")
	}
	if err := db.PingContext(ctx); err != nil {
		panic(err)
	}
	_ = db.Close()

	return container
}

func startRedisForSuite(ctx context.Context) *redis.RedisContainer {
	container, err := redis.Run(ctx, "redis:7-alpine")
	if err != nil {
		panic(err)
	}

	host, err := container.Host(ctx)
	if err != nil {
		panic(err)
	}
	port, err := container.MappedPort(ctx, "6379/tcp")
	if err != nil {
		panic(err)
	}
	testRedisAddr = host + ":" + port.Port()
	return container
}

func shutdownSuite(ctx context.Context) {
	if pgContainer != nil {
		_ = pgContainer.Terminate(ctx)
	}
	if redisContainer != nil {
		_ = redisContainer.Terminate(ctx)
	}
}
