# OGEN vs CHI - Анализ и рекомендации

## 🔍 Что делает каждый компонент

### OGEN Router
- **Статический сгенерированный роутер** (switch-case)
- Реализует `http.Handler` интерфейс
- **НЕ зависит от chi**
- Обрабатывает только OpenAPI routes
- Максимальная производительность (статический код)

### CHI Router
- Динамический роутер с middleware
- Используется для:
  1. Middleware (Logger, Recoverer, RequestID, CORS)
  2. Health check endpoints (`/health`, `/metrics`)
  3. Монтирования ogen server

## ⚡ Производительность

**OGEN router:** ~50-100ns на route matching (статический switch-case)  
**CHI router:** ~200-500ns на route matching (динамический)  
**http.ServeMux:** ~300-800ns на route matching (стандартный)

**Вывод:** OGEN router быстрее, но chi используется только для:
- Монтирования ogen (1 раз при старте)
- Health checks (редкие запросы)
- Middleware (небольшой overhead)

## ✅ Можно ли убрать CHI?

**ДА!** Можно заменить на стандартный `http.ServeMux`:

```go
// Вместо chi
mux := http.NewServeMux()

// Middleware wrapper
handler := withMiddleware(ogenServer, 
    loggingMiddleware,
    metricsMiddleware,
    corsMiddleware,
)

// Монтирование
mux.Handle("/api/v1/", handler)
mux.HandleFunc("/health", healthCheck)
mux.HandleFunc("/metrics", metricsHandler)
```

## 📊 Рекомендация

**Для наших сервисов:**

1. **OGEN routes** (hot path) → ogen router (уже максимально быстрый)
2. **Health/Metrics** (cold path) → можно через стандартный mux
3. **Middleware** → можно сделать вручную (легко)

**Вывод:** CHI можно убрать, но:
- ✅ Производительность: минимальный gain (только на health/metrics)
- ❌ Удобство: потеряем удобные middleware
- ⚠️ Код: больше boilerplate для middleware

## 🎯 Рекомендация

**Оставить CHI** потому что:
1. Overhead минимальный (только на health/metrics, не на hot path)
2. Удобные middleware (Logger, Recoverer, RequestID)
3. Чистый код (меньше boilerplate)
4. OGEN routes уже максимально быстрые (не проходят через chi)

**Или убрать CHI** если:
- Нужна максимальная производительность на health/metrics
- Готовы писать middleware вручную
- Хотите уменьшить зависимости

## 📝 Пример без CHI

```go
func NewHTTPServer(addr string, service *Service) *HTTPServer {
    // OGEN server (fast router)
    handlers := NewHandlers(service)
    secHandler := &SecurityHandler{}
    ogenServer, _ := api.NewServer(handlers, secHandler)
    
    // Standard mux
    mux := http.NewServeMux()
    
    // Middleware wrapper
    handler := withMiddleware(ogenServer,
        loggingMiddleware,
        metricsMiddleware,
        corsMiddleware,
    )
    
    // Mount
    mux.Handle("/api/v1/", handler)
    mux.HandleFunc("/health", healthCheck)
    mux.HandleFunc("/metrics", metricsHandler)
    
    return &HTTPServer{
        server: &http.Server{
            Addr:    addr,
            Handler: mux,
        },
    }
}

func withMiddleware(h http.Handler, mws ...func(http.Handler) http.Handler) http.Handler {
    for i := len(mws) - 1; i >= 0; i-- {
        h = mws[i](h)
    }
    return h
}
```

