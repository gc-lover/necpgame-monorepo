# Промпт для рефакторинга на ogen-go (краткая версия)

## Роли
- **Основной:** Backend Developer
- **Вспомогательные:** API Designer (если нужен), Performance Engineer (валидация)

## Документация (обязательно)
1. `.cursor/OGEN_MIGRATION_GUIDE.md` - главный гайд
2. `.cursor/ogen/02-MIGRATION-STEPS.md` - пошаговая инструкция
3. `.cursor/ogen/03-TROUBLESHOOTING.md` - решение проблем
4. `.cursor/CODE_GENERATION_TEMPLATE.md` - шаблоны Makefile
5. `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - валидация

## Reference
- `services/combat-combos-service-ogen-go/` - **полный пример**

## Процесс
1. Проверь OpenAPI: `redocly lint proto/openapi/{service}.yaml`
2. Обнови Makefile (см. CODE_GENERATION_TEMPLATE.md)
3. Сгенерируй код: `make generate-api`
4. Мигрируй handlers на typed responses (см. 02-MIGRATION-STEPS.md Phase 3)
5. Реализуй SecurityHandler
6. Создай benchmarks
7. Валидируй: `/backend-validate-optimizations #{issue}`

## Success Criteria
- [ ] Build passes
- [ ] Tests pass
- [ ] Benchmarks >70% improvement
- [ ] Typed responses (нет interface{})
- [ ] SecurityHandler реализован

## Промпт для агента
```
Рефакторинг {service-name} на ogen-go.

Роль: Backend Developer
Документация: .cursor/OGEN_MIGRATION_GUIDE.md, .cursor/ogen/02-MIGRATION-STEPS.md
Reference: services/combat-combos-service-ogen-go/

Требования:
1. Генерация ogen кода
2. Миграция handlers на typed responses
3. SecurityHandler
4. Benchmarks
5. Валидация

Issue: #{number}
```

