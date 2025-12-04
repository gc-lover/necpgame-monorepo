# Рекомендации: Производительность и Стабильность

## 🔍 Текущая ситуация

### Hot Path (API routes - 99% трафика)
- OK **OGEN router** - статический switch-case, ~50-100ns
- OK **НЕ проходит через chi** - максимально быстро
- OK **Typed handlers** - нет interface{} boxing

### Cold Path (health/metrics - 1% трафика)
- WARNING **chi router** - динамический, ~200-500ns
- WARNING **Дублирование middleware** - chi.Logger + кастомный LoggingMiddleware
- WARNING **Лишняя зависимость** - chi не нужен для ogen

## ⚡ Проблемы производительности

1. **Дублирование middleware:**
   ```go
   router.Use(middleware.Logger)      // chi middleware
   router.Use(LoggingMiddleware)      // кастомный (дублирует!)
   ```

2. **chi overhead на health/metrics:**
   - Health checks: ~1 req/sec
   - Metrics: ~1 req/15sec
   - Overhead минимальный, но можно убрать

3. **chi middleware может быть медленнее:**
   - chi.Logger: форматирование строк
   - Кастомный: структурированное логирование (быстрее)

## OK Рекомендация: Убрать CHI

### Преимущества:
1. **Меньше зависимостей** - проще поддержка
2. **Нет дублирования** - один middleware chain
3. **Стандартная библиотека** - стабильнее
4. **Меньше кода** - проще понять

### Реализация:

```go
// Оптимизированный вариант БЕЗ chi
func NewHTTPServer(addr string, service *Service) *HTTPServer {
    // OGEN server (fast router)
    handlers := NewHandlers(service)
    secHandler := &SecurityHandler{}
    ogenServer, _ := api.NewServer(handlers, secHandler)
    
    // Standard mux (для health/metrics)
    mux := http.NewServeMux()
    
    // Middleware chain (один раз, без дублирования)
    handler := chainMiddleware(ogenServer,
        recoveryMiddleware,      // panic recovery
        requestIDMiddleware,      // request ID
        loggingMiddleware,         // структурированное логирование
        metricsMiddleware,         // метрики
        corsMiddleware,            // CORS
    )
    
    // Mount OGEN (hot path - максимально быстро)
    mux.Handle("/api/v1/", handler)
    
    // Health/metrics (cold path - простой mux)
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

// Chain middleware (простой и быстрый)
func chainMiddleware(h http.Handler, mws ...func(http.Handler) http.Handler) http.Handler {
    for i := len(mws) - 1; i >= 0; i-- {
        h = mws[i](h)
    }
    return h
}
```

## 📊 Ожидаемые улучшения

### Производительность:
- **Hot path:** без изменений (уже максимально быстро)
- **Cold path:** -10-20% latency (убрали chi overhead)
- **Memory:** -50KB на сервис (убрали chi из памяти)

### Стабильность:
- OK **Меньше зависимостей** - меньше точек отказа
- OK **Стандартная библиотека** - лучше тестируется
- OK **Проще код** - легче поддерживать

## 🎯 План действий

1. **Создать шаблон** без chi
2. **Мигрировать сервисы** постепенно
3. **Бенчмарки** до/после
4. **Мониторинг** в production

## WARNING Важно

**OGEN routes (hot path) НЕ затронуты** - они уже максимально быстрые!

Убираем chi только для:
- Health/metrics endpoints
- Middleware chain
- Монтирования ogen server

**Реальный impact:** минимальный на hot path, небольшой на cold path.

