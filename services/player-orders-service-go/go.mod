module github.com/necp-game/player-orders-service-go

go 1.21

require (
	github.com/go-chi/chi/v5 v5.0.10
	github.com/google/uuid v1.6.0
	github.com/segmentio/kafka-go v0.4.47
	go.uber.org/zap v1.27.0
)

require (
	github.com/klauspost/compress v1.15.9 // indirect
	github.com/pierrec/lz4/v4 v4.1.15 // indirect
	github.com/stretchr/testify v1.8.4 // indirect
	go.uber.org/multierr v1.10.0 // indirect
	golang.org/x/text v0.14.0 // indirect
)

// Issue: #140894810
// Player Orders World Impact Service
// Implements mechanics for tracking and calculating effects of player orders on game world
