# 🎯 Chi Router Standard

**Chi - единый стандарт роутера для всех новых Go сервисов**

---

## 📋 Политика

### ✅ ДЛЯ НОВЫХ СЕРВИСОВ (создаешь с нуля)

**ОБЯЗАТЕЛЬНО используй Chi:**
- `ROUTER_TYPE := chi-server` в Makefile
- `github.com/go-chi/chi/v5` в go.mod
- Chi - это наш **стандарт** для всех новых сервисов!

### 🔄 ДЛЯ СУЩЕСТВУЮЩИХ СЕРВИСОВ (уже есть код)

**Используй то что уже есть:**
- Если Chi → оставляй Chi
- Если Gorilla → оставляй Gorilla
- **НЕ мигрируй** с Gorilla на Chi (разные API!)
- Сохраняй working code без изменений

---

## ❓ Почему Chi?

### Преимущества Chi:

✅ **Современный** - активная разработка, актуальные фичи  
✅ **Легковесный** - минимальные зависимости  
✅ **Быстрый** - отличная производительность  
✅ **Middleware** - мощная система middleware  
✅ **oapi-codegen** - нативная интеграция  
✅ **Context** - нативная работа с context.Context  
✅ **Community** - большое активное сообщество  

### Почему НЕ Gorilla для новых сервисов:

⚠️ **Deprecated** - проект в режиме поддержки (maintenance mode)  
⚠️ **Legacy** - старый подход к роутингу  
⚠️ **Heavy** - больше зависимостей  
⚠️ **Slower** - медленнее Chi в бенчмарках  

---

## 🔄 Chi vs Gorilla: Различия API

**КРИТИЧЕСКИ ВАЖНО:** Chi и Gorilla имеют **несовместимый API**!

### Создание роутера

**Chi:**
```go
router := chi.NewRouter()
```

**Gorilla:**
```go
router := mux.NewRouter()
```

### Тип роутера

**Chi:**
```go
var router chi.Router
```

**Gorilla:**
```go
var router *mux.Router
```

### Методы

**Chi:**
```go
router.Get("/users", handler)
router.Post("/users", handler)
router.Delete("/users/{id}", handler)
```

**Gorilla:**
```go
router.HandleFunc("/users", handler).Methods("GET")
router.HandleFunc("/users", handler).Methods("POST")
router.HandleFunc("/users/{id}", handler).Methods("DELETE")
```

### Subrouter

**Chi:**
```go
router.Route("/api", func(r chi.Router) {
    r.Get("/users", handler)
})
```

**Gorilla:**
```go
api := router.PathPrefix("/api").Subrouter()
api.HandleFunc("/users", handler).Methods("GET")
```

### Middleware

**Chi:**
```go
router.Use(middleware.Logger)
router.Use(middleware.Recoverer)
```

**Gorilla:**
```go
router.Use(loggingMiddleware)
router.Use(recoveryMiddleware)
```

### Интеграция с oapi-codegen

**Chi:**
```go
api.HandlerWithOptions(handlers, api.ChiServerOptions{
    BaseURL:    "/api/v1",
    BaseRouter: router,
})
```

**Gorilla:**
```go
api.HandlerFromMux(handlers, router)
```

---

## 🛠️ Как использовать Chi

### 1. Создание нового сервиса

**Makefile:**
```makefile
SERVICE_NAME := my-new-service
ROUTER_TYPE := chi-server  # ✅ Chi by default
```

**go.mod:**
```go
require (
    github.com/go-chi/chi/v5 v5.0.12
    github.com/oapi-codegen/runtime v1.1.1
)
```

### 2. HTTP Server setup

```go
// Issue: #123
package server

import (
    "net/http"
    "github.com/go-chi/chi/v5"
    "github.com/go-chi/chi/v5/middleware"
    "{org}/necpgame/services/my-service-go/pkg/api"
)

type HTTPServer struct {
    addr    string
    router  chi.Router
    service Service
}

func NewHTTPServer(addr string, service Service) *HTTPServer {
    router := chi.NewRouter()
    
    // Standard middleware
    router.Use(middleware.Logger)
    router.Use(middleware.Recoverer)
    router.Use(middleware.RequestID)
    
    // Custom middleware
    router.Use(corsMiddleware)
    router.Use(metricsMiddleware)
    
    // Handlers
    handlers := NewHandlers(service)
    
    // oapi-codegen integration
    api.HandlerWithOptions(handlers, api.ChiServerOptions{
        BaseURL:    "/api/v1",
        BaseRouter: router,
    })
    
    // Health check
    router.Get("/health", healthCheckHandler)
    router.Get("/metrics", metricsHandler)
    
    return &HTTPServer{
        addr:   addr,
        router: router,
        service: service,
    }
}

func (s *HTTPServer) Start() error {
    return http.ListenAndServe(s.addr, s.router)
}
```

### 3. Handlers

```go
// Issue: #123
package server

import (
    "net/http"
    "{org}/necpgame/services/my-service-go/pkg/api"
)

type Handlers struct {
    service Service
}

func NewHandlers(service Service) *Handlers {
    return &Handlers{service: service}
}

// Реализация api.ServerInterface (сгенерирован oapi-codegen)
func (h *Handlers) GetUser(w http.ResponseWriter, r *http.Request, userID string) {
    user, err := h.service.GetUser(r.Context(), userID)
    if err != nil {
        respondError(w, http.StatusNotFound, "User not found")
        return
    }
    
    respondJSON(w, http.StatusOK, user)
}
```

---

## 🚫 НЕ мигрируй существующие сервисы

**ЕСЛИ сервис уже использует Gorilla:**

❌ **НЕ ДЕЛАЙ:**
```bash
# НЕ меняй ROUTER_TYPE
ROUTER_TYPE := gorilla-server  # Оставь как есть!

# НЕ меняй импорты
import "github.com/gorilla/mux"  # Не трогай!

# НЕ переписывай код
router := mux.NewRouter()  # Working code!
```

✅ **ДЕЛАЙ:**
```bash
# Оставь всё как есть
ROUTER_TYPE := gorilla-server

# Сохрани working code
# Миграция займет много времени и не даст пользы
```

**Причина:**
- Chi и Gorilla имеют **разный API**
- Миграция требует **полной перезаписи** http_server.go
- Риск **внести баги** в working code
- **Нет выгоды** для существующего сервиса

---

## ✅ Чек-лист для агентов

### API Designer:

- [ ] Знаю что Backend использует Chi для новых сервисов
- [ ] НЕ беспокоюсь о типе роутера (Backend настроит)
- [ ] Фокусируюсь на качестве OpenAPI спецификации

### Backend Developer:

#### Для НОВОГО сервиса:
- [ ] Использую `ROUTER_TYPE := chi-server` в Makefile
- [ ] Добавляю `github.com/go-chi/chi/v5` в go.mod
- [ ] Создаю HTTP server с Chi API
- [ ] Использую `api.HandlerWithOptions()` для интеграции

#### Для СУЩЕСТВУЮЩЕГО сервиса:
- [ ] Проверяю какой роутер уже используется
- [ ] Если Chi → продолжаю использовать Chi
- [ ] Если Gorilla → оставляю Gorilla (НЕ мигрирую!)
- [ ] Сохраняю working code без изменений

---

## 📚 Дополнительные материалы

**Документация Chi:**
- GitHub: https://github.com/go-chi/chi
- Docs: https://go-chi.io/

**В проекте:**
- `.cursor/rules/agent-backend.mdc` - полные правила Backend агента
- `.cursor/CODE_GENERATION_TEMPLATE.md` - шаблон Makefile с Chi
- `.cursor/SOLID_CODE_GENERATION_GUIDE.md` - гайд по генерации кода

---

## 🎯 Итог

**Chi - единый стандарт для ВСЕХ новых Go сервисов в проекте!**

- ✅ Новые сервисы → Chi обязательно
- 🔄 Существующие сервисы → оставляй как есть
- ❌ НЕ мигрируй с Gorilla на Chi

**Вопросы?** См. `.cursor/rules/agent-backend.mdc`

