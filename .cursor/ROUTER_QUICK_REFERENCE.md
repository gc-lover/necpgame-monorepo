# ⚡ Router Quick Reference

**Быстрая шпаргалка: Chi vs Gorilla**

---

## 🎯 Простое правило

```
НОВЫЙ сервис → Chi ✅
СУЩЕСТВУЮЩИЙ сервис → оставь как есть 🔄
```

---

## 📋 Чек-лист для Backend Developer

### Создаю НОВЫЙ сервис:

```bash
# Makefile
ROUTER_TYPE := chi-server  # ✅

# go.mod
require github.com/go-chi/chi/v5 v5.0.12

# server/http_server.go
import "github.com/go-chi/chi/v5"
router := chi.NewRouter()
```

### Работаю с СУЩЕСТВУЮЩИМ сервисом:

```bash
# Проверь что уже используется
grep "github.com/go-chi/chi" services/{service}-go/server/

# Chi найден → используй chi-server
# Chi НЕ найден → проверь gorilla

grep "github.com/gorilla/mux" services/{service}-go/server/

# Gorilla найден → используй gorilla-server
# НЕ меняй на chi!
```

---

## 🔧 Примеры кода

### Chi (для НОВЫХ сервисов):

```go
import (
    "github.com/go-chi/chi/v5"
    "github.com/go-chi/chi/v5/middleware"
)

router := chi.NewRouter()
router.Use(middleware.Logger)
router.Get("/users", handler)

api.HandlerWithOptions(handlers, api.ChiServerOptions{
    BaseURL:    "/api/v1",
    BaseRouter: router,
})
```

### Gorilla (только для LEGACY):

```go
import "github.com/gorilla/mux"

router := mux.NewRouter()
router.Use(loggingMiddleware)
router.HandleFunc("/users", handler).Methods("GET")

api.HandlerFromMux(handlers, router)
```

---

## ❓ FAQ

**Q: Можно использовать Gorilla для нового сервиса?**  
A: ❌ НЕТ. Chi - единственный стандарт для новых сервисов.

**Q: Нужно мигрировать существующий Gorilla сервис на Chi?**  
A: ❌ НЕТ. Оставь working code как есть.

**Q: В чем разница между Chi и Gorilla?**  
A: Разный API (см. `.cursor/CHI_ROUTER_STANDARD.md` таблицу различий)

**Q: Почему Chi, а не Gorilla?**  
A: Chi современнее, легче, быстрее. Gorilla deprecated.

---

## 📚 Подробнее

- `.cursor/CHI_ROUTER_STANDARD.md` - полный гайд
- `.cursor/rules/agent-backend.mdc` - правила Backend агента
- `.cursor/CODE_GENERATION_TEMPLATE.md` - шаблон Makefile

