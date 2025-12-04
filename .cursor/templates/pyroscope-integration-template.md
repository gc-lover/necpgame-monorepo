# Pyroscope Continuous Profiling Template

**Issue: #1611**

## Добавление в go.mod

```go
require (
    github.com/pyroscope-io/client/pyroscope v0.40.0
)
```

## Интеграция в main.go

```go
// Issue: #1611 - Continuous Profiling
import (
    "github.com/pyroscope-io/client/pyroscope"
)

func main() {
    // Start Pyroscope continuous profiling
    pyroscope.Start(pyroscope.Config{
        ApplicationName: "necpgame.{service-name}",
        ServerAddress:   getEnv("PYROSCOPE_SERVER", "http://pyroscope:4040"),
        
        ProfileTypes: []pyroscope.ProfileType{
            pyroscope.ProfileCPU,              // CPU usage
            pyroscope.ProfileAllocObjects,      // Allocation count
            pyroscope.ProfileAllocSpace,        // Allocation size
            pyroscope.ProfileInuseObjects,      // In-use objects
            pyroscope.ProfileInuseSpace,        // In-use memory
        },
        
        // Tags for filtering
        Tags: map[string]string{
            "environment": getEnv("ENV", "development"),
            "version":     getEnv("VERSION", "unknown"),
        },
        
        // Sampling rate (1 = 100%, 0.1 = 10%)
        SampleRate: 100, // 100 Hz
        
        // Logger (optional)
        Logger: pyroscope.StandardLogger,
    })
    
    // ... rest of main
}
```

## Environment Variables

```bash
PYROSCOPE_SERVER=http://pyroscope:4040
ENV=production
VERSION=1.0.0
```

## K8s Deployment

Pyroscope уже настроен в `docker-compose.yml` и доступен по адресу `http://pyroscope:4040`.

Для K8s:
```yaml
env:
  - name: PYROSCOPE_SERVER
    value: "http://pyroscope.observability.svc.cluster.local:4040"
```

## Benefits

- OK 24/7 profiling (proactive optimization)
- OK Regression detection (compare before/after)
- OK Real-time performance insights
- OK -30% production issues

## Reference

- `.cursor/performance/03a-profiling-testing.md`
- `docker-compose.yml` (Pyroscope service)
- `infrastructure/observability/`

