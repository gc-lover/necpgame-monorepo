# 🔄 Pre-commit Hook Update - Benchmark Results Collection

**Обновление:** Pre-commit hook теперь сохраняет результаты бенчмарков в `.benchmarks/results/`

---

## ✅ Что изменилось

### До обновления:
- ❌ Pre-commit hook запускал только `bench-quick` для проверки
- ❌ Результаты не сохранялись
- ❌ Не было истории локальных запусков

### После обновления:
- ✅ Pre-commit hook запускает `bench-quick` с JSON output
- ✅ Сохраняет результаты в `.benchmarks/results/pre-commit_TIMESTAMP.json`
- ✅ Результаты доступны в дашборде
- ✅ История локальных запусков сохраняется

---

## 📋 Формат сохраненных результатов

```json
{
  "timestamp": "20251204_143022",
  "source": "pre-commit",
  "services": [
    {
      "service": "loot-service-go",
      "benchmarks": [
        {
          "name": "server/BenchmarkGetPlayerLootHistory",
          "ns_per_op": 200.2,
          "allocs_per_op": 5,
          "bytes_per_op": 320
        }
      ]
    }
  ]
}
```

**Отличия от полных бенчмарков:**
- `source: "pre-commit"` - метка источника
- `benchtime=100ms` - быстрые бенчмарки (меньше точность)
- Только измененные сервисы

---

## 🚀 Использование

### Автоматически:
```bash
# При коммите автоматически запускается и сохраняется
git add services/loot-service-go/server/handlers.go
git commit -m "feat: update handler"
# → Создается .benchmarks/results/pre-commit_YYYYMMDD_HHMMSS.json
```

### Пропустить сохранение:
```bash
SKIP_BENCHMARKS=1 git commit -m "message"
```

---

## 📊 Просмотр в дашборде

Результаты pre-commit автоматически отображаются в дашборде:
- Откройте `http://localhost:8080`
- Фильтруйте по сервисам
- Смотрите тренды (включая pre-commit результаты)

---

## ⚠️ Требования

- `jq` должен быть установлен для парсинга JSON
- Если `jq` не найден, hook все равно работает, но не сохраняет JSON

**Установка jq:**
```bash
# Ubuntu/Debian
sudo apt-get install jq

# macOS
brew install jq

# Windows (PowerShell)
choco install jq
```

---

## 🔍 Отличия от полных бенчмарков

| Характеристика | Pre-commit | Полные (CI) |
|----------------|------------|-------------|
| Длительность | 100ms | Полная |
| Точность | Ниже | Высокая |
| Сервисы | Только измененные | Все |
| Источник | `pre-commit` | `benchmarks.yml` |
| Файл | `pre-commit_*.json` | `benchmarks_*.json` |

**Рекомендация:** Используйте pre-commit для быстрой проверки, полные бенчмарки - для точных измерений.

---

**См. также:**
- `AUTOMATIC-BENCHMARKS-GUIDE.md` - полное руководство
- `.githooks/pre-commit` - код hook'а

