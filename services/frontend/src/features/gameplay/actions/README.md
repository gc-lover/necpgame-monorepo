# Actions Feature - Система игровых действий

Feature модуль для игровых действий в локациях NECPGAME.

## 📋 Описание

Выполнение игровых действий:
- Осмотр локации (explore)
- Отдых для восстановления (rest)
- Использование объектов (use)
- Взлом систем (hack)

## 🗂️ Структура

```
features/gameplay/actions/
├── components/
│   ├── ActionResultDialog.tsx   # Диалог результата
│   └── __tests__/
└── README.md
```

**Примечание:** Actions интегрированы напрямую в GameplayPage, поэтому нет отдельной страницы.

## 🎨 Компоненты (Material UI)

### ActionResultDialog
Диалог для отображения результатов действий

**OpenAPI типы:**
- `exploreLocation` response
- `restAction` response
- `useObject` response
- `hackSystem` response

**Отображает:**
- Описание результата
- Восстановленное здоровье/энергию
- Найденные точки интереса
- Скрытые объекты
- Награды

## 🔌 API (Orval Generated)

**Mutations:**
- `POST /gameplay/actions/explore` → `useExploreLocation`
- `POST /gameplay/actions/rest` → `useRestAction`
- `POST /gameplay/actions/use` → `useUseObject`
- `POST /gameplay/actions/hack` → `useHackSystem`

## OK OpenAPI Compliance

**Все данные из OpenAPI!**

| Действие | Хук | Hardcoded? |
|----------|-----|------------|
| Осмотр локации | useExploreLocation | ❌ НЕТ |
| Отдых | useRestAction | ❌ НЕТ |
| Использование объекта | useUseObject | ❌ НЕТ |
| Взлом | useHackSystem | ❌ НЕТ |

## 🚀 Использование

Интеграция в GameplayPage:

```typescript
import {
  useExploreLocation,
  useRestAction,
} from '@/api/generated/actions/gameplay/gameplay'

const { mutate: exploreLocation } = useExploreLocation()
const { mutate: restAction } = useRestAction()

// Осмотр локации
exploreLocation(
  { data: { characterId, locationId } },
  {
    onSuccess: (result) => {
      // Показать результат (точки интереса, скрытые объекты)
      console.log(result.pointsOfInterest, result.hiddenObjects)
    }
  }
)

// Отдых
restAction(
  { data: { characterId, duration: 60 } },
  {
    onSuccess: (result) => {
      // Показать восстановление HP/Energy
      console.log(`+${result.healthRestored} HP, +${result.energyRestored} Energy`)
    }
  }
)
```

## 🎯 Интеграция

**Где используется:**
- GameplayPage → Кнопки действий в левой панели

**Действия:**
- "Осмотреть окрестности" → `useExploreLocation`
- "Отдохнуть" → `useRestAction`
- "Использовать объект" → `useUseObject` (будущее)
- "Взломать" → `useHackSystem` (будущее)

## 📝 Источники

- **OpenAPI:** `API-SWAGGER/api/v1/gameplay/actions/actions.yaml`
- **Task:** API-TASK-034

