# Combat Damage Service - Enterprise-Grade Domain Service

## Назначение

Combat Damage Service предоставляет enterprise-grade API для расчета и обработки урона в NECPGAME. Сервис отвечает
за все аспекты damage calculation, включая критические удары, mitigation, damage-over-time эффекты и валидацию.

## Функциональность

- **Расчет урона**: Real-time damage calculation с модификаторами и резистами
- **Критические удары**: Динамический расчет crit chance и damage multipliers
- **Damage mitigation**: Armor, shields, resistance calculations
- **DOT эффекты**: Damage-over-time с stacking и refresh механиками
- **Damage analytics**: Статистика и метрики для анализа боя
- **Anti-cheat валидация**: Защита от читов и эксплойтов
- **AI оптимизация**: AI-powered damage prediction и optimization

## Структура

```
combat-damage-service/
├── main.yaml              # Основная спецификация API
└── README.md              # Эта документация
```

## Зависимости

- **common**: Общие схемы и ответы
- **combat-service**: Интеграция с боевой системой
- **effect-service**: Синергия с эффектами и баффами
- **analytics-service**: Аналитика damage статистики

## Performance

- **P99 Latency**: <5ms для damage операций
- **Memory per Instance**: <8KB
- **Concurrent Users**: 75,000+ одновременных расчетов
- **Calculation Time**: <2ms

## Использование

### Валидация

```bash
npx @redocly/cli lint main.yaml
```

### Генерация Go кода

```bash
ogen --target ../../services/combat-damage-service-go/pkg/api \
     --package api --clean main.yaml
```

### Документация

```bash
npx @redocly/cli build-docs main.yaml -o docs/index.html
```

## API Endpoints

### Core Operations

- `POST /combat/damage/calculate` - Основной расчет урона
- `POST /combat/damage/critical` - Расчет критических ударов
- `POST /combat/damage/mitigation` - Расчет mitigation
- `POST /combat/damage/dot` - Применение DOT эффектов

### Analytics & Monitoring

- `GET /combat/damage/analytics/{session_id}` - Статистика сессии
- `POST /combat/damage/validate` - Валидация расчетов

### Health & System

- `GET /health` - Проверка здоровья сервиса

## Damage Types

Сервис поддерживает следующие типы урона:

- `physical` - Физический урон
- `energy` - Энергетический урон
- `chemical` - Химический урон
- `thermal` - Термический урон
- `electrical` - Электрический урон
- `cybernetic` - Кибернетический урон
- `explosive` - Взрывной урон

## Critical Hit System

- Динамический расчет crit chance на основе:
  - Базовых характеристик оружия
  - Навыков атакующего
  - Дебаффов цели
  - Экологических факторов

- Критический урон multipliers: 1.5x - 5.0x

## Damage Mitigation

Комплексная система mitigation включает:

- **Armor**: Базовая защита от физического урона
- **Shields**: Активная защита с перезарядкой
- **Resistances**: Специфические резисты к типам урона
- **Environmental**: Защита от окружающей среды

## DOT Effects

Damage-over-time эффекты с поддержкой:

- **Stacking**: Накопление эффектов
- **Refresh**: Обновление длительности
- **Types**: Burn, Poison, Bleed, Corrosion, Radiation, Electric, Cyber

## Anti-Cheat Protection

- **Cryptographic validation**: Проверка расчетов с хэшами
- **Discrepancy detection**: Обнаружение несоответствий
- **Session monitoring**: Отслеживание подозрительной активности
- **Real-time validation**: Проверка в реальном времени

## Analytics & Metrics

Сервис предоставляет детальную аналитику:

- **Damage breakdown**: Разбор урона по компонентам
- **Critical statistics**: Статистика критических ударов
- **Mitigation analysis**: Анализ эффективности защиты
- **Performance metrics**: Метрики производительности

## Integration Points

- **Combat Service**: Основная интеграция с боевой системой
- **Effect Service**: Синхронизация с баффами и дебаффами
- **Analytics Service**: Передача статистики для анализа
- **Anti-Cheat Service**: Валидация и обнаружение читов

## Configuration

### Environment Variables

```bash
COMBAT_DAMAGE_SERVICE_PORT=8080
COMBAT_DAMAGE_DB_HOST=localhost
COMBAT_DAMAGE_REDIS_HOST=redis:6379
COMBAT_DAMAGE_METRICS_ENABLED=true
```

### Database Schema

Сервис использует оптимизированную схему с индексами для:

- Быстрого поиска damage расчетов
- Агрегации статистики по сессиям
- Анализа паттернов damage

### Caching Strategy

- **Redis** для hot data (активные сессии)
- **In-memory LRU** для часто используемых модификаторов
- **Precomputed tables** для crit chance расчетов

## Monitoring & Observability

### Metrics

- `combat_damage_calculations_total` - Общее количество расчетов
- `combat_damage_calculation_duration` - Время расчета (P50, P95, P99)
- `combat_damage_critical_hits` - Количество критических ударов
- `combat_damage_validation_failures` - Неудачи валидации

### Health Checks

- Database connectivity
- Redis connectivity
- External service dependencies
- Performance thresholds

## Security Considerations

- **Input validation**: Строгая валидация всех входных данных
- **Rate limiting**: Защита от DDoS атак
- **Audit logging**: Полное логирование всех операций
- **Encryption**: Шифрование чувствительных данных

## Development Guidelines

### Code Generation

Используйте OpenAPI генерацию для:

- Server stubs (Go)
- Client SDKs (TypeScript, C++)
- Documentation
- Tests

### Testing Strategy

- **Unit tests** для математических расчетов
- **Integration tests** для внешних зависимостей
- **Load tests** для performance validation
- **Security tests** для anti-cheat validation

### Performance Optimization

- SIMD операции для массивов damage
- Precomputed lookup tables для crit calculations
- Memory pooling для частых объектов
- Zero-allocation hot paths

## Deployment

### Docker

```dockerfile
FROM golang:1.21-alpine AS builder
WORKDIR /app
COPY . .
RUN go build -o combat-damage-service ./cmd

FROM alpine:latest
RUN apk --no-cache add ca-certificates
COPY --from=builder /app/combat-damage-service /usr/local/bin/
EXPOSE 8080
CMD ["combat-damage-service"]
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: combat-damage-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: combat-damage-service
  template:
    spec:
      containers:
      - name: combat-damage-service
        image: necpgame/combat-damage-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: SERVICE_PORT
          value: "8080"
        resources:
          requests:
            memory: "128Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

## Troubleshooting

### Common Issues

1. **High latency**: Проверить database indexes и Redis connectivity
2. **Memory leaks**: Проверить DOT effect cleanup и object pooling
3. **Validation failures**: Проверить cryptographic keys и hash algorithms

### Debug Mode

```bash
export COMBAT_DAMAGE_DEBUG=true
export COMBAT_DAMAGE_LOG_LEVEL=debug
```

## Contributing

### Code Style

- Go: Standard Go formatting (`gofmt`)
- YAML: 2-space indentation, consistent naming
- Comments: English only, comprehensive documentation

### PR Requirements

- All tests passing
- Performance benchmarks included
- Security review completed
- Documentation updated

## License

Proprietary - NECPGAME Internal Use Only




