module github.com/gc-lover/necpgame/services/battle-pass-service-go

go 1.24.0

require (
	github.com/go-faster/errors v0.7.1
	github.com/go-faster/jx v1.2.0
	github.com/golang-jwt/jwt/v5 v5.2.1
	github.com/google/uuid v1.6.0
	github.com/jackc/pgx/v5 v5.7.2
	github.com/lib/pq v1.10.9
	github.com/ogen-go/ogen v1.18.0
	github.com/redis/go-redis/v9 v9.17.2
	go.opentelemetry.io/otel v1.39.0
	go.opentelemetry.io/otel/exporters/jaeger v1.17.0
	go.opentelemetry.io/otel/metric v1.39.0
	go.opentelemetry.io/otel/sdk v1.39.0
	go.opentelemetry.io/otel/trace v1.39.0
	go.uber.org/zap v1.27.1
)