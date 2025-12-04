# Adaptive Compression Template

**Issue: #1612**

## Добавление в go.mod

```go
require (
    github.com/klauspost/compress/zstd v1.17.0
    github.com/pierrec/lz4/v4 v4.1.21
)
```

## Создание compression.go

Скопировать `services/realtime-gateway-go/server/compression.go`

## Интеграция в handler/service

```go
type Service struct {
    compressor *AdaptiveCompressor // Issue: #1612
}

func NewService() *Service {
    compressor, _ := NewAdaptiveCompressor()
    return &Service{
        compressor: compressor,
    }
}

// При отправке данных
func (s *Service) SendData(data []byte, isRealtime bool) error {
    // Issue: #1612 - Adaptive compression
    if s.compressor != nil {
        compressed, err := s.compressor.Compress(data, isRealtime)
        if err == nil && len(compressed) < len(data) {
            data = compressed
        }
    }
    // Send compressed data...
}
```

## Gains

- OK Bandwidth ↓40-60%
- OK Latency minimal (LZ4 для real-time)
- OK Best ratio (Zstandard для bulk)

## Reference

- `.cursor/performance/06-resilience-compression.md`
- `services/realtime-gateway-go/server/compression.go`

