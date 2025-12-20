# Тестирование сервисов NECPGAME

## Быстрый тест

```bash
# Проверить health checks
curl http://localhost:8085/health  # Inventory
curl http://localhost:8087/health  # Character
curl http://localhost:8086/health  # Movement

# Проверить статус контейнеров
docker-compose ps inventory-service character-service movement-service
```

## Полное тестирование

```bash
# Запустить скрипт тестирования
powershell -ExecutionPolicy Bypass -File scripts/test/test-all-services.ps1
```

## Проверка БД

```bash
# Проверить таблицы
docker exec necpgame-postgres-1 psql -U postgres -d necpgame -c "\dt mvp_core.*"

# Проверить данные
docker exec necpgame-postgres-1 psql -U postgres -d necpgame -c "SELECT COUNT(*) FROM mvp_core.player_account;"
docker exec necpgame-postgres-1 psql -U postgres -d necpgame -c "SELECT COUNT(*) FROM mvp_core.character;"
```

## API тесты

### Character Service

```bash
# Создать аккаунт
curl -X POST http://localhost:8087/api/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{"nickname":"testuser"}'

# Создать персонажа
curl -X POST http://localhost:8087/api/v1/characters \
  -H "Content-Type: application/json" \
  -d '{"account_id":"<account_id>","name":"Hero1","level":1}'
```

### Inventory Service

```bash
# Получить инвентарь
curl http://localhost:8085/api/v1/inventory/<character_id>

# Добавить предмет
curl -X POST http://localhost:8085/api/v1/inventory/<character_id>/items \
  -H "Content-Type: application/json" \
  -d '{"item_id":"weapon_pistol_mk1","stack_count":1}'
```

### Movement Service

```bash
# Получить позицию
curl http://localhost:8086/api/v1/movement/<character_id>/position
```

## Метрики

- Inventory: http://localhost:9094/metrics
- Character: http://localhost:9096/metrics
- Movement: http://localhost:9095/metrics

