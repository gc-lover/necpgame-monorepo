// Issue: #1612 - Adaptive Compression
// LZ4 для real-time (fast!), Zstandard для bulk data (best ratio)
package server

import (
	"fmt"

	"github.com/klauspost/compress/zstd"
	"github.com/pierrec/lz4/v4"
)

// AdaptiveCompressor выбирает compression в зависимости от типа данных
type AdaptiveCompressor struct {
	zstdEncoder *zstd.Encoder
	zstdDecoder *zstd.Decoder
	threshold   int // Минимальный размер для compression
}

// NewAdaptiveCompressor создает новый adaptive compressor
func NewAdaptiveCompressor() (*AdaptiveCompressor, error) {
	zstdEncoder, err := zstd.NewWriter(nil, zstd.WithEncoderLevel(zstd.SpeedDefault))
	if err != nil {
		return nil, fmt.Errorf("failed to create zstd encoder: %w", err)
	}

	zstdDecoder, err := zstd.NewReader(nil)
	if err != nil {
		return nil, fmt.Errorf("failed to create zstd decoder: %w", err)
	}

	return &AdaptiveCompressor{
		zstdEncoder: zstdEncoder,
		zstdDecoder: zstdDecoder,
		threshold:   100, // <100 bytes: no compression (overhead > gain)
	}, nil
}

// Compress сжимает данные адаптивно
// Issue: #1612 - Adaptive compression (LZ4 для real-time, Zstandard для bulk)
func (ac *AdaptiveCompressor) Compress(data []byte, isRealtime bool) ([]byte, error) {
	// Small data: no compression (overhead > gain)
	if len(data) < ac.threshold {
		return data, nil
	}

	// Real-time data (position updates, game state): LZ4 (fast!)
	if isRealtime {
		buf := make([]byte, lz4.CompressBlockBound(len(data)))
		n, err := lz4.CompressBlock(data, buf, nil)
		if err != nil {
			return nil, fmt.Errorf("lz4 compression failed: %w", err)
		}
		return buf[:n], nil
	}

	// Bulk data (inventory, stats, logs): Zstandard (best ratio)
	compressed := ac.zstdEncoder.EncodeAll(data, nil)
	return compressed, nil
}

// Decompress распаковывает данные
func (ac *AdaptiveCompressor) Decompress(data []byte, isRealtime bool) ([]byte, error) {
	// Try LZ4 first (real-time)
	if isRealtime {
		decompressed := make([]byte, len(data)*4) // LZ4 ratio ~2-3x
		n, err := lz4.UncompressBlock(data, decompressed)
		if err == nil {
			return decompressed[:n], nil
		}
		// Fallback to zstd if LZ4 fails
	}

	// Zstandard decompression
	decompressed, err := ac.zstdDecoder.DecodeAll(data, nil)
	if err != nil {
		return nil, fmt.Errorf("decompression failed: %w", err)
	}
	return decompressed, nil
}

// Close освобождает ресурсы
func (ac *AdaptiveCompressor) Close() error {
	ac.zstdEncoder.Close()
	ac.zstdDecoder.Close()
	return nil
}

// isRealtimeData определяет, является ли данные real-time
func isRealtimeData(data []byte) bool {
	// Real-time: position updates, game state, shooting events
	// Bulk: inventory, stats, logs, quest data
	// Эвристика: маленький размер + частые обновления = real-time
	return len(data) < 1024 // <1KB обычно real-time
}

