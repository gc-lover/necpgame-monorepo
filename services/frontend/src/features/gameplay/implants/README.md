# Implants Feature - Управление имплантами

Feature модуль для системы имплантов в NECPGAME.

## 📋 Описание

Этот модуль отвечает за:
- Просмотр доступных слотов имплантов (по типам)
- Отображение лимитов установки имплантов
- Мониторинг энергетического пула
- Управление имплантами персонажа

## 🗂️ Структура модуля

```
features/gameplay/implants/
├── components/
│   ├── EnergyPoolDisplay.tsx    # Энергетический пул
│   ├── ImplantLimitInfo.tsx     # Лимиты имплантов
│   ├── ImplantSlotItem.tsx      # Один слот импланта
│   ├── ImplantSlotsList.tsx     # Список слотов
│   ├── index.ts
│   └── __tests__/              # Тесты
├── pages/
│   ├── ImplantsPage.tsx         # Страница управления имплантами
│   └── index.ts
└── README.md
```

## 🎨 UI Компоненты (все Material UI)

### ImplantLimitInfo
Отображает лимиты установки имплантов

**OpenAPI тип:** `ImplantLimits`

**Данные из API:**
- `base_limit` - базовый лимит
- `bonus_from_class` - бонус от класса
- `bonus_from_progression` - бонус от прокачки
- `humanity_penalty` - штраф от человечности
- `current_limit` - текущий лимит
- `used_slots` - использовано слотов
- `available_slots` - доступно слотов

### EnergyPoolDisplay
Отображает энергетический пул

**OpenAPI тип:** `EnergyPoolInfo`

**Данные из API:**
- `total_pool` - общий пул энергии
- `used` - использовано
- `available` - доступно
- `regen_rate` - скорость регенерации
- `current_level` - текущий уровень
- `max_level` - максимальный уровень

### ImplantSlotItem
Один слот импланта (visual)

**OpenAPI тип:** `SlotInfo`

**Данные из API:**
- `slot_id` - ID слота
- `is_occupied` - занят ли
- `installed_implant_id` - ID установленного импланта
- `can_install` - можно ли установить

### ImplantSlotsList
Список слотов по типу

**OpenAPI тип:** `SlotInfo[]`

## 📄 Страницы

### ImplantsPage
Страница управления имплантами

**Роут:** `/game/implants`

**Защита:** Требуется выбранный персонаж

**Структура (3 колонки):**
- Левая панель: Меню типов (combat, tactical, defensive, mobility, os)
- Центр: Сетка слотов выбранного типа
- Правая панель: Лимиты + Энергия

## 🔌 API Интеграция

### Endpoints (сгенерированы Orval)

**Slots:**
- `GET /gameplay/combat/implants/{playerId}/slots` → `useGetImplantSlots`

**Limits:**
- `GET /gameplay/combat/implants/{playerId}/limits` → `useGetImplantLimits`
- `GET /gameplay/combat/implants/{playerId}/limit` → `useGetImplantLimit`

**Energy:**
- `GET /gameplay/combat/implants/{playerId}/energy` → `useGetEnergyPool`
- `GET /gameplay/combat/implants/{playerId}/energy/limits` → `useGetIndividualEnergyLimits`

**Mutations:**
- `useCheckCompatibility` - проверка совместимости
- `useCalculateImplantLimit` - расчет лимита
- `useCalculateEnergyConsumption` - расчет энергии
- `useRestoreEnergy` - восстановление энергии
- `useValidateInstall` - валидация установки

## OK OpenAPI Compliance

**ВАЖНО:** Все данные берутся ТОЛЬКО из OpenAPI!

**Flow данных:**
```
API (OpenAPI) → Orval Generated Hooks → React Components
```

**Проверено:**
- OK ImplantLimitInfo - использует `ImplantLimits` из OpenAPI
- OK EnergyPoolDisplay - использует `EnergyPoolInfo` из OpenAPI
- OK ImplantSlotItem - использует `SlotInfo` из OpenAPI
- OK ImplantSlotsList - использует `SlotInfo[]` из OpenAPI
- OK ImplantsPage - использует сгенерированные хуки

**Нет hardcoded данных!** OK

## 🔐 Защищенный роут

Роут `/game/implants` защищен через `ProtectedRoute`:
```typescript
<ProtectedRoute requireCharacter={true}>
  <ImplantsPage />
</ProtectedRoute>
```

## 🧪 Тестирование

Тесты: 2 файла (10+ тестов)

**Запуск:**
```bash
npm test
```

## 🚀 Использование

### Навигация на страницу имплантов

```typescript
import { useNavigate } from 'react-router-dom'

const navigate = useNavigate()
navigate('/game/implants')
```

### Загрузка данных

```typescript
import { useGetImplantSlots } from '@/api/generated/gameplay/combat/combat/combat'

const { data, isLoading } = useGetImplantSlots(playerId, undefined, {
  query: { enabled: !!playerId }
})
```

## 📦 Зависимости

- **Material UI (MUI)** - UI компоненты
- **React Router** - роутинг
- **React Query** - управление серверным состоянием (хуки сгенерированы Orval)
- **TypeScript** - типы из OpenAPI

## 🎯 Интеграция

**Доступ из GameplayPage:**
- Левая панель → кнопка "Импланты" 
- Переход на `/game/implants`

## 📝 Источники

- **OpenAPI:** `API-SWAGGER/api/v1/gameplay/combat/implants-limits.yaml`
- **.BRAIN:** `02-gameplay/combat/combat-implants-limits.md`
- **Task:** API-TASK-003

