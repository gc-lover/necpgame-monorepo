# NPCs Feature - Система взаимодействия с NPC

Feature модуль для NPCs и диалогов в NECPGAME.

## 📋 Описание

Управление взаимодействием с NPC:
- Просмотр списка NPCs в локации
- Фильтрация по типу NPC (торговцы, квестодатели, граждане, враги)
- Детальная информация о NPC
- Диалоги с NPC
- Взаимодействия (торговля, квесты, и т.д.)

## 🗂️ Структура

```
features/gameplay/npcs/
├── components/
│   ├── NPCCard.tsx              # Карточка NPC
│   ├── DialogueBox.tsx          # Диалоговое окно
│   ├── NPCDetailsPanel.tsx      # Панель деталей NPC
│   ├── index.ts
│   └── __tests__/
├── pages/
│   ├── NPCsPage.tsx            # Страница NPCs
│   └── index.ts
└── README.md
```

## 🎨 Компоненты (Material UI)

### NPCCard
Компактная карточка NPC с иконкой типа

**OpenAPI тип:** `Npc`

**Функции:**
- Отображение имени, типа, уровня
- Индикация фракции
- Враждебность (враг/союзник)
- Клик для открытия деталей

### DialogueBox
Диалоговое окно с текстом NPC и вариантами ответов

**OpenAPI тип:** `NPCDialogue`

**Функции:**
- Отображение текста NPC
- Варианты ответов (DialogueOption)
- Выбор ответа

### NPCDetailsPanel
Панель с подробной информацией о NPC

**OpenAPI тип:** `Npc`

## 📄 Страница

### NPCsPage
Полноценная страница для взаимодействия с NPCs

**Роут:** `/game/npcs`

**Структура (3 колонки):**
- Левая: Фильтры по типу (Все, Торговцы, Квестодатели, Граждане, Враги)
- Центр: Список NPCs (сетка карточек) или диалоговое окно
- Правая: Детали выбранного NPC, кнопки действий

**Режимы:**
- Список NPCs (по умолчанию)
- Диалог с NPC (при клике "Поговорить")

## 🔌 API (Orval Generated)

**Queries:**
- `GET /npcs?characterId&type` → `useGetNPCs`
- `GET /npcs/location/{locationId}?characterId` → `useGetNPCsByLocation`
- `GET /npcs/{npcId}?characterId` → `useGetNPCDetails`
- `GET /npcs/{npcId}/dialogue?characterId` → `useGetNPCDialogue`

**Mutations:**
- `POST /npcs/{npcId}/interact` → `useInteractWithNPC`
- `POST /npcs/{npcId}/dialogue/respond` → `useRespondToDialogue`

## OK OpenAPI Compliance

**Все данные из OpenAPI!**

| Компонент | Тип | Hardcoded? |
|-----------|-----|------------|
| NPCCard | Npc | ❌ НЕТ |
| DialogueBox | NPCDialogue | ❌ НЕТ |
| NPCDetailsPanel | Npc | ❌ НЕТ |

## 🚀 Использование

Навигация:
```typescript
navigate('/game/npcs')
```

Загрузка:
```typescript
const { data } = useGetNPCs({ characterId, type })
const { data: dialogue } = useGetNPCDialogue({ npcId, characterId })
```

Действия:
```typescript
const { mutate: interact } = useInteractWithNPC()
interact({ npcId, data: { characterId, action: 'trade' } })

const { mutate: respond } = useRespondToDialogue()
respond({ npcId, data: { characterId, optionId } })
```

## 🎯 Интеграция

**Доступ:**
- GameplayPage → Меню → "NPCs"
- Прямой переход: `/game/npcs`

**Связи:**
- Квесты (quest_giver NPCs)
- Торговля (trader NPCs)
- Диалоги (все NPCs)

## 📝 Источники

- **OpenAPI:** `API-SWAGGER/api/v1/npcs/npcs.yaml`
- **Task:** API-TASK-031

