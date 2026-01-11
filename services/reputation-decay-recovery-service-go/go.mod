module github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go

go 1.21

require (
	github.com/go-faster/errors v1.0.1
	github.com/google/uuid v1.6.0
	github.com/gorilla/mux v1.8.1
	github.com/jackc/pgx/v5 v5.5.5
	github.com/redis/go-redis/v9 v9.5.1
	go.opentelemetry.io/otel v1.24.0
	go.opentelemetry.io/otel/exporters/prometheus v0.47.0
	go.opentelemetry.io/otel/metric v1.24.0
	go.opentelemetry.io/otel/sdk/metric v1.24.0
	go.uber.org/zap v1.27.0
	golang.org/x/sync v0.7.0
	gonum.org/v1/gonum v0.15.1
)