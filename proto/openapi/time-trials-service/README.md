# Time Trials Service - Enterprise-Grade Competitive Speedrunning

## Назначение

Time Trials Service предоставляет enterprise-grade API для управления конкурентными временными испытаниями в NECPGAME. Сервис отвечает за высокоточное отслеживание времени, валидацию результатов, управление лидербордами и распределение наград за скоростное прохождение контента.

## Функциональность

- **Управление испытаниями**: CRUD операции для конфигураций временных испытаний и правил
- **Высокоточный таймер**: Server-side отслеживание времени с анти-чит защитой
- **Лидерборды**: Real-time рейтинги, персональные рекорды и сезонные соревнования
- **Система наград**: Автоматическое распределение наград по времени прохождения
- **Валидация результатов**: Anti-cheat система с server-side проверкой
- **Аналитика**: Комплексная телеметрия для конкурентного геймплея

## Структура

```
time-trials-service/
├── main.yaml              # Основная спецификация API
└── README.md              # Эта документация
```

## Зависимости

- **common**: Общие схемы и ответы
- **gameplay-service**: Конфигурации испытаний и интеграция с игровым контентом
- **leaderboard-service**: Управление рейтингами и статистикой
- **achievement-service**: Достижения за прохождение испытаний
- **analytics-service**: Сбор и анализ телеметрии соревнований
- **economy-service**: Распределение наград и валюты
- **realtime-gateway**: WebSocket синхронизация для live тайминга

## Performance

- **P99 Latency**: <30ms для операций таймера, <10ms для запросов лидербордов
- **Memory**: <5MB для конфигураций испытаний, <2KB per активную сессию
- **Concurrent Trials**: 5000+ одновременных временных испытаний
- **Leaderboard Throughput**: 10000 queries/second

## Типы испытаний

### Speedrun Raids (скоростные рейды)
- Конкурентное прохождение рейдов на время
- Групповые и соло режимы
- Валидация маршрутов и механик

### Time Attack Dungeons (атака времени в подземельях)
- Скоростные вызовы подземелий
- Тирные награды (бронза/серебро/золото/платина)
- Сложность масштабируется по времени

### Weekly Challenges (еженедельные вызовы)
- Ограниченные по времени соревнования
- Специальные правила и модификаторы
- Сезонные награды и достижения

### Seasonal Trials (сезонные испытания)
- Тематические скоростные события
- Специальный контент и награды
- Глобальные лидерборды

## API Endpoints

### Public Endpoints
- `GET /api/v1/time-trials/trials` - Список доступных испытаний
- `GET /api/v1/time-trials/trials/{trialId}` - Детали испытания
- `POST /api/v1/time-trials/trials/{trialId}/start` - Начать сессию испытания
- `POST /api/v1/time-trials/trials/{trialId}/complete` - Завершить испытание
- `GET /api/v1/time-trials/leaderboards/{trialId}` - Лидерборд испытания
- `GET /api/v1/time-trials/leaderboards/{trialId}/personal/{playerId}` - Персональный рекорд

### Analytics Endpoints
- `GET /api/v1/time-trials/analytics/trials/{trialId}/performance` - Метрики производительности

### Validation Endpoints
- `POST /api/v1/time-trials/validation/sessions/{sessionId}/report` - Репорт подозрительной сессии

### Admin Endpoints
- `POST /api/v1/admin/time-trials/trials` - Создание испытания
- `PUT /api/v1/admin/time-trials/trials/{trialId}` - Обновление испытания
- `DELETE /api/v1/admin/time-trials/trials/{trialId}` - Удаление испытания

## Использование

### Валидация

```bash
npx @redocly/cli lint main.yaml
```

### Генерация Go кода

```bash
ogen --target ../../services/time-trials-service-go/pkg/api \
     --package api --clean main.yaml
```

### Документация

```bash
npx @redocly/cli build-docs main.yaml -o docs/index.html
```

## Алгоритм таймера

1. **Server-side инициализация**: Синхронизация времени при старте сессии
2. **Real-time tracking**: Отслеживание прогресса с серверной валидацией
3. **Completion validation**: Проверка условий завершения и анти-чит фильтры
4. **Reward calculation**: Автоматический расчет наград по времени
5. **Leaderboard update**: Real-time обновление рейтингов

## Система валидации

### Server-side проверки
- Валидация маршрута прохождения
- Проверка completion percentage
- Анализ telemetry данных
- Cross-reference с другими сервисами

### Anti-cheat меры
- Server-side time tracking (zero trust)
- Route validation algorithms
- Telemetry anomaly detection
- Community reporting system

### Dispute resolution
- Automated validation pipeline
- Manual review для спорных случаев
- Appeal system для игроков

## Система наград

### Тирная система
- **Бронза**: Базовое время прохождения
- **Серебро**: Улучшенное время с бонусами
- **Золото**: Оптимальное время с premium наградами
- **Платина**: Рекордное время с эксклюзивными наградами

### Типы наград
- **Опыт**: Прогрессия персонажа
- **Валюта**: Gold и seasonal tokens
- **Предметы**: Косметика и consumables
- **Достижения**: Permanent unlocks и titles

## Лидерборды

### Типы рейтингов
- **Global**: Все-временные рекорды
- **Seasonal**: Сезонные соревнования
- **Weekly**: Еженедельные вызовы
- **Daily**: Ежедневные топы

### Функции
- Real-time обновления через WebSocket
- Personal best tracking
- Rank history и прогрессия
- Party size consideration

## Телеметрия и аналитика

Система собирает данные для балансировки и улучшения:

- **Performance metrics**: Среднее время, completion rates, difficulty distribution
- **Player behavior**: Популярность испытаний, retry patterns, time distribution
- **Cheat detection**: Anomaly patterns, validation failure rates
- **Reward optimization**: Engagement metrics, reward effectiveness

## Интеграции

### Gameplay Service
- Конфигурации испытаний и контента
- Интеграция с игровыми механиками
- Синхронизация прогресса

### Leaderboard Service
- Управление глобальными рейтингами
- Сезонная статистика
- Achievement integration

### Achievement Service
- Trial completion achievements
- Speedrun milestones
- Seasonal rewards

### Analytics Service
- Performance tracking
- Player engagement metrics
- Balance adjustments

### Economy Service
- Reward distribution
- Currency management
- Premium reward validation

### Realtime Gateway
- Live timer synchronization
- Leaderboard updates
- Real-time notifications





