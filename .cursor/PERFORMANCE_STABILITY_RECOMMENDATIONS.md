# Рекомендации: Производительность и Стабильность

## 🔍 OGEN vs CHI - Вывод

**OGEN Router:**
- Статический switch-case, ~50-100ns
- НЕ проходит через chi (hot path максимально быстрый)

**CHI Router:**
- Динамический, ~200-500ns
- Используется только для health/metrics (cold path, 1% трафика)

**Рекомендация:**
- **Оставить CHI** - overhead минимальный (только на health/metrics)
- OGEN routes уже максимально быстрые (не проходят через chi)
- Удобные middleware (Logger, Recoverer, RequestID)

**Или убрать CHI** если нужна максимальная производительность на health/metrics (gain: -10-20% latency, -50KB memory).

**Вывод:** CHI можно убрать, но gain минимальный. OGEN routes уже максимально быстрые.

---

## 📊 Реализация без CHI (опционально)

```go
func NewHTTPServer(addr string, service *Service) *HTTPServer {
    handlers := NewHandlers(service)
    secHandler := &SecurityHandler{}
    ogenServer, _ := api.NewServer(handlers, secHandler)
    
    mux := http.NewServeMux()
    handler := chainMiddleware(ogenServer,
        recoveryMiddleware,
        requestIDMiddleware,
        loggingMiddleware,
        metricsMiddleware,
        corsMiddleware,
    )
    
    mux.Handle("/api/v1/", handler)
    mux.HandleFunc("/health", healthCheck)
    mux.HandleFunc("/metrics", metricsHandler)
    
    return &HTTPServer{
        server: &http.Server{
            Addr:         addr,
            Handler:      mux,
            ReadTimeout:  15 * time.Second,
            WriteTimeout: 15 * time.Second,
            IdleTimeout:  60 * time.Second,
        },
    }
}

func chainMiddleware(h http.Handler, mws ...func(http.Handler) http.Handler) http.Handler {
    for i := len(mws) - 1; i >= 0; i-- {
        h = mws[i](h)
    }
    return h
}
```
