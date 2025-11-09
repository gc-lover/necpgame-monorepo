# Inventory Feature - Система инвентаря и экипировки

Feature модуль для инвентаря персонажа в NECPGAME.

## 📋 Описание

Управление инвентарем и экипировкой персонажа:
- Просмотр предметов в инвентаре
- Фильтрация по категориям
- Экипировка предметов
- Использование consumables
- Управление весом
- Отображение слотов экипировки и бонусов

## 🗂️ Структура

```
features/gameplay/inventory/
├── components/
│   ├── ItemCard.tsx                  # Карточка предмета
│   ├── InventoryGrid.tsx             # Сетка предметов
│   ├── EquipmentSlotDisplay.tsx      # Слот экипировки
│   ├── index.ts
│   └── __tests__/
├── pages/
│   ├── InventoryPage.tsx             # Страница инвентаря
│   └── index.ts
└── README.md
```

## 🎨 Компоненты (Material UI)

### ItemCard
Компактная карточка предмета с цветовой индикацией редкости

**OpenAPI тип:** `InventoryItem`

**Функции:**
- Отображение информации о предмете
- Кнопки: Экипировать / Использовать / Выбросить
- Индикация редкости цветом
- Tooltip с описанием

### InventoryGrid
Адаптивная сетка предметов

**OpenAPI тип:** `InventoryItem[]`

### EquipmentSlotDisplay
Отображение слота экипировки с бонусами

**OpenAPI тип:** `EquipmentSlot`

## 📄 Страница

### InventoryPage
Полноценный инвентарь с экипировкой

**Роут:** `/game/inventory`

**Структура (3 колонки):**
- Левая: Фильтры (категории), индикатор веса
- Центр: Сетка предметов (InventoryGrid)
- Правая: Слоты экипировки, суммарные бонусы

## 🔌 API (Orval Generated)

**Queries:**
- `GET /inventory?characterId&category` → `useGetInventory`
- `GET /inventory/equipment?characterId` → `useGetEquipment`

**Mutations:**
- `POST /inventory/equip` → `useEquipItem`
- `POST /inventory/unequip` → `useUnequipItem`
- `POST /inventory/use` → `useUseItem`
- `POST /inventory/drop` → `useDropItem`

## OK OpenAPI Compliance

**Все данные из OpenAPI!**

| Компонент | Тип | Hardcoded? |
|-----------|-----|------------|
| ItemCard | InventoryItem | ❌ НЕТ |
| EquipmentSlotDisplay | EquipmentSlot | ❌ НЕТ |
| InventoryGrid | InventoryItem[] | ❌ НЕТ |

## 🚀 Использование

Навигация:
```typescript
navigate('/game/inventory')
```

Загрузка:
```typescript
const { data } = useGetInventory({ characterId })
const { data: equipment } = useGetEquipment({ characterId })
```

Действия:
```typescript
const { mutate: equipItem } = useEquipItem()
equipItem({ data: { characterId, itemId, slotType } })
```

## 🎯 Интеграция

**Доступ:**
- GameplayPage → Меню → "Открыть инвентарь"
- Прямой переход: `/game/inventory`

## 📝 Источники

- **OpenAPI:** `API-SWAGGER/api/v1/economy/inventory/inventory.yaml`
- **Task:** API-TASK-029


