// Issue: #1928 - Meta Mechanics Service dependencies
module github.com/gc-lover/necpgame-monorepo/services/meta-mechanics-service-go

go 1.21

require (
	github.com/go-chi/chi/v5 v5.0.10
	github.com/google/uuid v1.6.0
	github.com/jackc/pgx/v5 v5.5.4
	github.com/redis/go-redis/v9 v9.4.0
	github.com/sirupsen/logrus v1.9.3
	go.uber.org/goleak v1.3.0
)
