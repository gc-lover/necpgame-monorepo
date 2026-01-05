# Ability Service - Enterprise-Grade Domain Service

## Назначение

Ability Service предоставляет enterprise-grade API для управления способностями персонажей в NECPGAME. Сервис отвечает
за активацию способностей, управление cooldown'ами и отслеживание ресурсов.

## Функциональность

- **Активация способностей**: Real-time активация с валидацией
- **Cooldown управление**: Динамическое отслеживание перезарядок
- **Отслеживание ресурсов**: Mana, energy, resource consumption
- **Синергия способностей**: Комбо взаимодействия и цепочки
- **Anti-cheat валидация**: Защита от читов и эксплойтов

## Структура

```
ability-service/
├── main.yaml              # Основная спецификация API
└── README.md              # Эта документация
```

## Зависимости

- **common**: Общие схемы и ответы
- **combo-service**: Синергия с комбо-системой
- **combat-service**: Интеграция с боевой системой

## Performance

- **P99 Latency**: <15ms для операций со способностями
- **Memory per Instance**: <12KB
- **Concurrent Users**: 45,000+ одновременных операций
- **Activation Time**: <5ms

## Использование

### Валидация

```bash
npx @redocly/cli lint main.yaml
```

### Генерация Go кода

```bash
ogen --target ../../services/ability-service-go/pkg/api \
     --package api --clean main.yaml
```

### Документация

```bash
npx @redocly/cli build-docs main.yaml -o docs/index.html
```
