module github.com/necp-game/player-orders-service-go

go 1.21

require (
	github.com/go-chi/chi/v5 v5.0.10
	github.com/google/uuid v1.6.0
	github.com/jackc/pgx/v5 v5.5.4
	github.com/segmentio/kafka-go v0.4.47
	github.com/stretchr/testify v1.8.4
	go.uber.org/zap v1.27.0
	gopkg.in/yaml.v3 v3.0.1
)

// Issue: #140894810
// Player Orders World Impact Service
// Implements mechanics for tracking and calculating effects of player orders on game world
