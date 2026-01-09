# Achievement Service - Enterprise-Grade Domain Service

## Назначение

Achievement Service предоставляет enterprise-grade API для управления достижениями персонажей в NECPGAME. Сервис отвечает
за отслеживание прогресса, разблокировку достижений и распределение наград.

## Функциональность

- **Отслеживание прогресса**: Real-time обновление прогресса достижений
- **Разблокировка достижений**: Автоматическая активация при выполнении условий
- **Распределение наград**: Транзакционное распределение наград и интеграция с инвентарем
- **Синхронизация прогресса**: Кроссплатформенная синхронизация с разрешением конфликтов
- **Аналитика достижений**: Статистика и метрики вовлеченности игроков
- **Anti-cheat валидация**: Защита от читов и эксплойтов

## Структура

```
achievement-service/
├── main.yaml              # Основная спецификация API
└── README.md              # Эта документация
```

## Зависимости

- **common**: Общие схемы и ответы
- **inventory-service**: Интеграция с системой инвентаря для наград
- **analytics-service**: Аналитика прогресса игроков
- **notification-service**: Уведомления о разблокировках

## Performance

- **P99 Latency**: <10ms для операций с достижениями
- **Memory per Instance**: <8KB
- **Concurrent Users**: 75,000+ одновременных проверок достижений
- **Update Frequency**: 1000+ обновлений прогресса в секунду

## API Endpoints

### Управление достижениями
- `POST /achievements` - Создание достижения (admin)
- `GET /achievements` - Список достижений
- `GET /achievements/{id}` - Детали достижения
- `PUT /achievements/{id}` - Обновление достижения (admin)
- `DELETE /achievements/{id}` - Удаление достижения (admin)

### Прогресс и награды
- `GET /achievements/{id}/progress` - Получить прогресс
- `POST /achievements/{id}/progress` - Обновить прогресс
- `POST /achievements/{id}/unlock` - Разблокировать достижение (admin)
- `POST /achievements/{id}/rewards/claim` - Забрать награды

### Аналитика и лидерборды
- `GET /achievements/analytics/summary` - Аналитика достижений
- `GET /achievements/leaderboard` - Лидерборд достижений

### Пакетная обработка
- `POST /achievements/progress/batch` - Пакетное обновление прогресса
- `POST /achievements/events/process` - Обработка игровых событий

### AI и адаптация
- `POST /achievements/adaptive/train` - Обучение AI на данных достижений
- `GET /achievements/adaptive/recommend` - AI рекомендации достижений

## Использование

### Валидация

```bash
npx @redocly/cli lint main.yaml
```

### Генерация Go кода

```bash
ogen --target ../../services/achievement-service-go/pkg/api \
     --package api --clean main.yaml
```

### Документация

```bash
npx @redocly/cli build-docs main.yaml -o docs/index.html
```

## Категории достижений

- **combat**: Боевые достижения (убийства, комбо, стрики)
- **exploration**: Исследовательские достижения (открытие локаций, путешествия)
- **social**: Социальные достижения (друзья, гильдии, взаимодействия)
- **progression**: Прогрессионные достижения (уровни, опыт, развитие)
- **collection**: Коллекционные достижения (предметы, косметика, редкости)
- **challenge**: Челлендж достижения (сложные испытания, рекорды)

## Типы требований

- **kill_count**: Количество убийств определенного типа
- **item_collect**: Сбор определенных предметов
- **distance_traveled**: Пройденное расстояние
- **time_played**: Время игры
- **level_reached**: Достигнутый уровень
- **quest_completed**: Выполненные квесты
- **social_interaction**: Социальные взаимодействия
- **combat_streak**: Боевые серии
- **exploration_discovery**: Открытия в исследовании
- **currency_earned**: Заработанная валюта

## Типы наград

- **item**: Предметы в инвентарь
- **currency**: Валюта (евродоллары, уличный кредит)
- **experience**: Опыт персонажа
- **title**: Звания и титулы
- **cosmetic**: Косметические предметы
- **ability**: Новые способности
- **reputation**: Репутация и статус





