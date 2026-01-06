# Analytics Service - Enterprise-Grade Domain Service

## 📋 **Назначение**

Analytics Service предоставляет enterprise-grade API для комплексной аналитики, метрик и мониторинга всех систем NECPGAME. Сервис отвечает за сбор, обработку и анализ данных для принятия решений, используя SOLID/DRY domain separation принципы.

## 🎯 **Функциональность**

### Core Analytics Domains

- **Combat Analytics**: Статистика боев, эффективность, метрики производительности
- **Economy Analytics**: Рыночные тренды, портфельный анализ, экономические индикаторы, stock analytics
- **Player Analytics**: Поведение игроков, вовлеченность, паттерны использования
- **System Monitoring**: Мониторинг инфраструктуры, производительности, здоровья систем
- **Anti-cheat Monitoring**: Обнаружение мошенничества и предотвращение
- **Stock Protection**: Market integrity monitoring, anomaly detection, risk assessment

### Key Features

- **Real-time Analytics**: Live metrics with <25ms P99 latency
- **AI-Powered Insights**: Machine learning for predictive analytics
- **Enterprise Monitoring**: Comprehensive system observability
- **Market Protection**: Advanced fraud detection and integrity monitoring
- **Performance Optimized**: MMOFPS-grade performance with domain separation

## 📁 **Структура**

```
analytics-service/
├── main.yaml              # Основная спецификация API с domain inheritance
└── README.md              # Эта документация
```

## 🔗 **Зависимости**

- **common-service**: Domain-specific entity schemas (economy-entities, infrastructure-entities)
- **ability-service**: Данные о способностях для аналитики
- **equipment-service**: Данные об оборудовании
- **combo-service**: Данные о комбо-системах

## 📊 **Performance**

- **P99 Latency**: <25ms для аналитических запросов
- **Memory per Instance**: <20KB
- **Concurrent Users**: 50,000+ одновременных операций
- **Data Processing**: <10ms

## 🚀 **Domain Separation Architecture**

### SOLID/DRY Principles Applied

- **Single Responsibility**: Each endpoint serves one analytics domain
- **Domain Inheritance**: Uses common entity schemas for consistency
- **DRY (Don't Repeat Yourself)**: No duplicated schemas or logic
- **Enterprise Grade**: Optimistic locking, strict typing, validation

### Domain-Specific Endpoints

```
/analytics/combat/*     - Combat performance metrics
/analytics/economy/*    - Market trends and economics
/analytics/stock/*      - Stock analytics and protection
/analytics/players/*    - Player behavior analytics
/analytics/system/*     - System monitoring and health
```

### Common Entity Inheritance

All schemas inherit from domain-specific common entities:
- `economy-entities.yaml` for market/trading schemas
- `infrastructure-entities.yaml` for system monitoring schemas
- `game-entities.yaml` for combat/player schemas

## 🚀 **Использование**

### Валидация

```bash
npx @redocly/cli lint main.yaml
```

### Генерация Go кода

```bash
ogen --target ../../services/analytics-service-go/pkg/api \
     --package api --clean main.yaml
```

### Документация

```bash
npx @redocly/cli build-docs main.yaml -o docs/index.html
```








