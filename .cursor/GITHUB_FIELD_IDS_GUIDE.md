# Получение ID полей GitHub Project

## Как получить ID для полей TYPE и CHECK

### Шаг 1: Открыть GitHub Project

1. Перейди в GitHub repository: `gc-lover/necpgame-monorepo`
2. Открой Projects вкладку
3. Выбери проект "NECPGAME Development"

### Шаг 2: Найти ID поля

1. Нажми на `+` для добавления новой задачи
2. В поле TYPE выбери любое значение
3. Открой Developer Tools в браузере (F12)
4. Перейди на вкладку Network
5. Выполни действие добавления/обновления задачи
6. Найди GraphQL запрос в Network
7. В запросе найди `fieldId` для TYPE и CHECK полей

### Шаг 3: Обновить конфигурацию

После получения ID обнови `.cursor/GITHUB_PROJECT_CONFIG.md`:

```javascript
// Найденные ID (примеры)
TYPE_FIELD_ID: 'PVTSSF_...',    // Реальный ID поля TYPE
CHECK_FIELD_ID: 'PVTSSF_...',   // Реальный ID поля CHECK

// Option IDs для TYPE
TYPE_OPTIONS: {
  API: 'PVTSSFO_...',         // Реальный ID опции API
  MIGRATION: 'PVTSSFO_...',   // Реальный ID опции MIGRATION
  DATA: 'PVTSSFO_...',        // Реальный ID опции DATA
  BACKEND: 'PVTSSFO_...',     // Реальный ID опции BACKEND
  UE5: 'PVTSSFO_...'          // Реальный ID опции UE5
}

// CHECK options (обычно просто '0' и '1')
CHECK_OPTIONS: {
  '0': '0',  // NOT_CHECKED
  '1': '1'   // CHECKED
}
```

### Шаг 4: Обновить скрипты

После обновления конфига обнови `scripts/update-github-fields.py` с реальными ID.

### Шаг 5: Протестировать

```bash
# Тест обновления полей
python scripts/update-github-fields.py --item-id 123 --type API --check 1
```

## Важно!

- ID полей начинаются с `PVTSSF_` (Single Select Field)
- ID опций начинаются с `PVTSSFO_` (Single Select Field Option)
- CHECK поле использует простые значения '0' и '1'
