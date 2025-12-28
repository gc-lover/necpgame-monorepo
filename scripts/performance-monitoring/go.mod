module github.com/your-org/necpgame/scripts/performance-monitoring

go 1.21

require (
	github.com/prometheus/client_golang v1.18.0
	go.uber.org/zap v1.26.0

	github.com/your-org/necpgame/scripts/core/error-handling v0.0.0
)

require (
	github.com/beorn7/perks v1.0.1 // indirect
	github.com/cespare/xxhash/v2 v2.2.0 // indirect
	github.com/golang/protobuf v1.5.3 // indirect
	github.com/matttproud/golang_protobuf_extensions v1.0.4 // indirect
	github.com/prometheus/client_model v0.5.0 // indirect
	github.com/prometheus/common v0.45.0 // indirect
	github.com/prometheus/procfs v0.12.0 // indirect
	go.uber.org/multierr v1.11.0 // indirect
)

replace github.com/your-org/necpgame/scripts/core/error-handling => ../error-handling
