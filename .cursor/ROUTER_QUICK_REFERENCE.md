# ⚡ Chi Router - Quick Reference

**Chi - ЕДИНСТВЕННЫЙ роутер для ВСЕХ сервисов**

## Простое правило

```
НОВЫЙ сервис → Chi (chi-server)
СУЩЕСТВУЮЩИЙ с Chi → OK
СУЩЕСТВУЮЩИЙ с Gorilla → МИГРИРУЙ на Chi!
```

## Новый сервис

```makefile
# Makefile
ROUTER_TYPE := chi-server
```

```go
// server/http_server.go
import "github.com/go-chi/chi/v5"
router := chi.NewRouter()
```

## Существующий с Gorilla

**ОБЯЗАТЕЛЬНО мигрируй!** См. `.cursor/rules/agent-backend.mdc` секция "Миграция с Gorilla на Chi"

## Детали

**Все инструкции, примеры и таблицы различий:**  
`.cursor/rules/agent-backend.mdc`

