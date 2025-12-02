# ⚡ Router Quick Reference

**Быстрая шпаргалка: Chi vs Gorilla**

---

## 🎯 Простое правило

```
НОВЫЙ сервис → Chi ✅
СУЩЕСТВУЮЩИЙ с Chi → всё ОК ✅
СУЩЕСТВУЮЩИЙ с Gorilla → МИГРИРУЙ на Chi! 🔄
```

**Gorilla ЗАПРЕЩЕН - все сервисы ДОЛЖНЫ быть на Chi!**

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

# Chi найден → всё ОК, продолжай работу
# Chi НЕ найден → проверь gorilla

grep "github.com/gorilla/mux" services/{service}-go/server/

# Gorilla найден → ОБЯЗАТЕЛЬНО мигрируй на Chi!
# См. .cursor/rules/agent-backend.mdc секция "Миграция с Gorilla на Chi"
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
A: ❌ НЕТ. Chi - единственный стандарт. Gorilla ЗАПРЕЩЕН.

**Q: Нужно мигрировать существующий Gorilla сервис на Chi?**  
A: ✅ ДА! ОБЯЗАТЕЛЬНО мигрируй на Chi. См. инструкции в agent-backend.mdc.

**Q: В чем разница между Chi и Gorilla?**  
A: Разный API (см. `.cursor/CHI_ROUTER_STANDARD.md` таблицу различий)

**Q: Почему Chi, а не Gorilla?**  
A: Chi современнее, легче, быстрее. Gorilla deprecated и больше не используется.

**Q: Сложно ли мигрировать с Gorilla на Chi?**  
A: Нет, основная работа - переписать `http_server.go`. Подробная инструкция в документации.

---

## 📚 Подробнее

- `.cursor/CHI_ROUTER_STANDARD.md` - полный гайд
- `.cursor/rules/agent-backend.mdc` - правила Backend агента
- `.cursor/CODE_GENERATION_TEMPLATE.md` - шаблон Makefile

