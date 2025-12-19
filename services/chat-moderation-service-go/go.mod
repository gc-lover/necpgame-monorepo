module github.com/gc-lover/necpgame-monorepo/services/chat-moderation-service-go

go 1.21

require (
	github.com/gc-lover/necpgame-monorepo/services/chat-moderation-service-ogen-go v0.0.0
	github.com/google/uuid v1.6.0
	github.com/lib/pq v1.10.9
	github.com/prometheus/client_golang v1.19.1
	github.com/sirupsen/logrus v1.9.3
)

replace github.com/gc-lover/necpgame-monorepo/services/chat-moderation-service-ogen-go => ./

require (
	github.com/beorn7/perks v1.0.1 // indirect
	github.com/cespare/xxhash/v2 v2.2.0 // indirect
	github.com/go-faster/errors v0.7.1 // indirect
	github.com/go-faster/jx v1.1.0 // indirect
	github.com/go-faster/otel v0.1.0 // indirect
	github.com/go-faster/yaml v0.4.6 // indirect
	github.com/golang/protobuf v1.5.3 // indirect
	github.com/matttproud/golang_protobuf_extensions/v2 v2.0.0 // indirect
	github.com/prometheus/client_model v0.5.0 // indirect
	github.com/prometheus/common v0.48.0 // indirect
	github.com/prometheus/procfs v0.12.0 // indirect
	github.com/segmentio/asm v1.2.0 // indirect
	github.com/valyala/bytebufferpool v1.0.0 // indirect
	github.com/valyala/fasthttp v1.51.0 // indirect
)