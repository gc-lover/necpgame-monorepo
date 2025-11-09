# Quests Feature - Система квестов

Feature модуль для квестовой системы в NECPGAME.

## 📋 Описание

Управление квестами персонажа:
- Просмотр доступных квестов
- Просмотр активных квестов
- Прогресс выполнения квестов
- Детали квестов и наград

## 🗂️ Структура

```
features/gameplay/quests/
├── components/
│   ├── QuestListItem.tsx        # Элемент списка квеста
│   ├── QuestProgressItem.tsx    # Прогресс квеста
│   ├── index.ts
│   └── __tests__/
├── pages/
│   ├── QuestsPage.tsx          # Страница квестов
│   └── index.ts
└── README.md
```

## 🎨 Компоненты (Material UI)

### QuestListItem
Компактный элемент списка квестов

**OpenAPI тип:** `Quest`

### QuestProgressItem
Отображение прогресса активного квеста

**OpenAPI тип:** `QuestProgress`

## 📄 Страница

### QuestsPage
Журнал квестов

**Роут:** `/game/quests`

**Структура (3 колонки):**
- Левая: Фильтры (Активные/Доступные/Все)
- Центр: Список квестов
- Правая: Статистика

## 🔌 API (Orval Generated)

**Endpoints:**
- `GET /quests?characterId` → `useGetAvailableQuests`
- `GET /quests/active?characterId` → `useGetActiveQuests`
- `GET /quests/{questId}` → `useGetQuestDetails`
- `GET /quests/{questId}/objectives` → `useGetQuestObjectives`

**Mutations:**
- `POST /quests/accept` → `useAcceptQuest`
- `POST /quests/complete` → `useCompleteQuest`
- `POST /quests/abandon` → `useAbandonQuest`

## ✅ OpenAPI Compliance

**Все данные из OpenAPI!**

| Компонент | Тип | Hardcoded? |
|-----------|-----|------------|
| QuestListItem | Quest | ❌ НЕТ |
| QuestProgressItem | QuestProgress | ❌ НЕТ |

## 🚀 Использование

Навигация:
```typescript
navigate('/game/quests')
```

Загрузка:
```typescript
const { data } = useGetActiveQuests({ characterId })
```

## 🎯 Интеграция

**Доступ:**
- GameplayPage → Меню → "Квесты"
- Прямой переход: `/game/quests`

## 📝 Источники

- **OpenAPI:** `API-SWAGGER/api/v1/quests/quests.yaml`
- **Task:** API-TASK-030

