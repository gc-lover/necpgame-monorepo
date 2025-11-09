# Тесты для implants компонентов

Покрытие: 50%+

## Тестируемые компоненты

- ✅ ImplantLimitInfo - лимиты имплантов из OpenAPI
- ✅ EnergyPoolDisplay - энергетический пул из OpenAPI

## Запуск тестов

```bash
npm test
```

## OpenAPI Compliance

Все тесты проверяют использование данных из OpenAPI типов:
- `ImplantLimits` (OpenAPI)
- `EnergyPoolInfo` (OpenAPI)
- `SlotInfo` (OpenAPI)

Нет hardcoded значений! ✅

