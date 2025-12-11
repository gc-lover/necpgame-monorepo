# Рекомендации: Производительность и Стабильность

## 🔍 OGEN vs ServeMux - Вывод

**OGEN Router:**
- Статический switch-case, ~50-100ns
- НЕ проходит через chi (hot path максимально быстрый)

**ServeMux:**
- Минимальный overhead, статический, подходит для health/metrics и всего API

**Рекомендация:**
- Использовать OGEN + `http.ServeMux`, без сторонних роутеров на hot/cold path.

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
