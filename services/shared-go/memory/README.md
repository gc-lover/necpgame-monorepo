# Memory Pool Library

## Overview

Enterprise-grade memory pooling library for reducing GC pressure in hot paths. Designed for MMOFPS game servers requiring zero allocations in critical paths.

## Issue: #1954

## Features

- **Response Pool**: Pooled HTTP response objects
- **Buffer Pool**: Pooled byte buffers for temporary operations
- **BytesBuffer Pool**: Pooled bytes.Buffer for JSON/text operations
- **StringBuilder Pool**: Pooled strings.Builder for string concatenation
- **Map Pool**: Pooled maps for temporary key-value operations

## Usage

### Response Pool

```go
import "necpgame/services/shared-go/memory"

func HandleRequest() {
    resp := memory.ResponsePool.Get().(*memory.Response)
    defer memory.ResponsePool.Put(resp)
    
    resp.Reset() // Reset for reuse
    resp.Status = 200
    resp.Data = append(resp.Data, "response"...)
    
    return resp
}
```

### Buffer Pool

```go
import "necpgame/services/shared-go/memory"

func ProcessData(data []byte) []byte {
    buf := memory.GetBuffer()
    defer memory.PutBuffer(buf)
    
    // Use buffer
    buf = append(buf, data...)
    
    // Copy result (buffer will be reused)
    result := make([]byte, len(buf))
    copy(result, buf)
    return result
}
```

### BytesBuffer Pool

```go
import "necpgame/services/shared-go/memory"

func MarshalJSON(v interface{}) ([]byte, error) {
    buf := memory.GetBytesBuffer()
    defer memory.PutBytesBuffer(buf)
    
    encoder := json.NewEncoder(buf)
    if err := encoder.Encode(v); err != nil {
        return nil, err
    }
    
    result := make([]byte, buf.Len())
    copy(result, buf.Bytes())
    return result, nil
}
```

### StringBuilder Pool

```go
import "necpgame/services/shared-go/memory"

func BuildMessage(parts []string) string {
    sb := memory.GetStringBuilder()
    defer memory.PutStringBuilder(sb)
    
    for _, part := range parts {
        sb.WriteString(part)
    }
    
    return sb.String()
}
```

### Map Pool

```go
import "necpgame/services/shared-go/memory"

func ProcessMetadata(metadata map[string]interface{}) {
    temp := memory.GetMap()
    defer memory.PutMap(temp)
    
    // Use temporary map
    temp["key"] = "value"
    
    // Process
    process(temp)
}
```

## Best Practices

1. **Always Reset**: Reset pooled objects before use
2. **Always Put**: Use defer to ensure objects are returned to pool
3. **Copy Results**: Copy data from pooled buffers before returning
4. **Profile First**: Use benchmarks to identify hot paths
5. **Pool Hot Objects**: Only pool objects allocated >1000 times/sec

## Performance Targets

- **Allocations**: 0 allocs/op in hot paths
- **Latency**: <100μs per operation
- **Memory**: Minimal overhead from pooling

## References

- Memory Allocation Optimization Guide: `.cursor/guides/MEMORY_ALLOCATION_OPTIMIZATION.md`
- Performance Agent Rules: `.cursor/rules/agent-performance.mdc`
- Performance Enforcement: `.cursor/PERFORMANCE_ENFORCEMENT.md`
