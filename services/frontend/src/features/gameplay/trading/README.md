# Trading Feature - Система торговли

Feature модуль для торговли с NPC-торговцами в NECPGAME.

## 📋 Описание

Торговля с NPC-торговцами:
- Просмотр списка торговцев
- Просмотр ассортимента торговца
- Покупка предметов
- Продажа предметов
- Отображение цен и денег игрока

## 🗂️ Структура

```
features/gameplay/trading/
├── components/
│   ├── VendorCard.tsx            # Карточка торговца
│   ├── TradeItemCard.tsx         # Предмет для торговли
│   ├── index.ts
│   └── __tests__/
├── pages/
│   ├── TradingPage.tsx          # Страница торговли
│   └── index.ts
└── README.md
```

## 🎨 Компоненты (Material UI)

### VendorCard
Компактная карточка торговца

**OpenAPI тип:** `Vendor`

### TradeItemCard
Карточка предмета с ценой и кнопкой покупки/продажи

**OpenAPI тип:** `TradeItem`

**Режимы:**
- `buy` - покупка у торговца
- `sell` - продажа торговцу

## 📄 Страница

### TradingPage
Полноценная страница торговли

**Роут:** `/game/trading`

**Query params:**
- `?vendorId={id}` - автоматический выбор торговца

**Структура (3 колонки):**
- Левая: Список торговцев
- Центр: Ассортимент (вкладки: Купить/Продать)
- Правая: Деньги игрока, информация о торговце

**Режимы:**
- Купить - ассортимент торговца (VendorInventory)
- Продать - инвентарь игрока (кроме квестовых предметов)

## 🔌 API (Orval Generated)

**Queries:**
- `GET /trading/vendors?characterId` → `useGetVendors`
- `GET /trading/vendors/{vendorId}/inventory?characterId` → `useGetVendorInventory`
- `GET /trading/price?characterId&itemId` → `useGetItemPrice`

**Mutations:**
- `POST /trading/buy` → `useBuyItem`
- `POST /trading/sell` → `useSellItem`

## ✅ OpenAPI Compliance

**Все данные из OpenAPI!**

| Компонент | Тип | Hardcoded? |
|-----------|-----|------------|
| VendorCard | Vendor | ❌ НЕТ |
| TradeItemCard | TradeItem | ❌ НЕТ |

## 🚀 Использование

Навигация:
```typescript
// Прямой переход
navigate('/game/trading')

// С выбранным торговцем
navigate(`/game/trading?vendorId=${vendorId}`)
```

Загрузка:
```typescript
const { data } = useGetVendors({ characterId })
const { data: inventory } = useGetVendorInventory({ vendorId, characterId })
```

Действия:
```typescript
const { mutate: buy } = useBuyItem()
buy({ data: { characterId, vendorId, itemId, quantity: 1 } })

const { mutate: sell } = useSellItem()
sell({ data: { characterId, vendorId, itemId, quantity: 1 } })
```

## 🎯 Интеграция

**Доступ:**
- NPCsPage → Кнопка "Торговать" (для NPC типа trader)
- Прямой переход: `/game/trading`

**Связи:**
- NPCs (торговцы - это NPC типа trader)
- Inventory (продажа предметов из инвентаря)

## 📝 Источники

- **OpenAPI:** `API-SWAGGER/api/v1/trading/trading.yaml`
- **Task:** API-TASK-033

