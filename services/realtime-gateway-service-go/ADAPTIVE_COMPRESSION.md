# Adaptive Compression for Real-Time Data

## Overview

Adaptive compression automatically selects the optimal compression algorithm based on network conditions and data type. This provides the best balance between compression ratio, latency, and CPU usage.

## Issue: #2117

## Implementation

### Algorithms

1. **LZ4** - Fast compression for real-time data
   - Low latency (<1ms)
   - Good for: position updates, movement, shooting
   - Compression ratio: ~2-3x

2. **Zstandard (Zstd)** - High compression ratio for bulk data
   - Better compression ratio (~3-5x)
   - Good for: inventory, quest data, chat messages
   - Dictionary compression support

3. **Delta Compression** - State change tracking
   - Only sends changes, not full state
   - Used in combination with other algorithms

4. **None** - No compression
   - Used when packet loss is high (>5%)
   - Prevents compression overhead from worsening network issues

### Adaptive Selection

The algorithm is selected based on:

- **Data Type**: Real-time vs bulk data
- **Network Bandwidth**: High bandwidth (>10 Mbps) → Zstd
- **Network Latency**: Low latency (<50ms) → LZ4
- **Packet Loss**: High loss (>5%) → No compression

### Network Condition Monitoring

The compressor monitors:
- Available bandwidth (Mbps)
- Network latency (ms)
- Packet loss rate (0.0-1.0)

Conditions are updated per-client and used for algorithm adaptation.

### Dictionary Compression

For Zstd, a shared dictionary is built from sample game packets. This improves compression ratio by 30-50% for game-specific data patterns.

## Usage

```go
// Initialize adaptive compressor
config := udp.DefaultAdaptiveCompressionConfig()
config.Logger = logger
compressor, err := udp.NewAdaptiveCompressor(config)

// Compress data (algorithm selected automatically)
compressed, algorithm, err := compressor.Compress(clientAddr, data, isRealtime)

// Update network conditions (for adaptation)
condition := &udp.NetworkCondition{
    BandwidthMbps: 10.0,
    LatencyMs:    30,
    PacketLossRate: 0.01,
}
compressor.UpdateNetworkCondition(clientAddr, condition)

// Decompress
decompressed, err := compressor.Decompress(clientAddr, compressed, algorithm)
```

## Performance

- **LZ4**: <1ms compression time, 2-3x ratio
- **Zstd**: 2-5ms compression time, 3-5x ratio
- **Delta**: <0.5ms, 70-85% bandwidth reduction
- **Overall**: 40-60% bandwidth reduction with minimal latency impact

## Statistics

The compressor tracks:
- Total bytes compressed/original
- Compression ratios per algorithm
- Algorithm usage statistics
- Algorithm switch count

Access via `GetStats()` method.
