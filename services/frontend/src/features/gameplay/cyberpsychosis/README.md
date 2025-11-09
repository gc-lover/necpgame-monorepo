# Cyberpsychosis Feature - Система киберпсихоза

Feature модуль для системы киберпсихоза в NECPGAME.

## 📋 Описание

Мониторинг человечности персонажа и отслеживание симптомов киберпсихоза.

**Функции:**
- Просмотр текущего уровня человечности
- Отображение активных симптомов
- Информация о стадиях киберпсихоза
- Мониторинг прогрессии

## 🗂️ Структура

```
features/gameplay/cyberpsychosis/
├── components/
│   ├── HumanityDisplay.tsx      # Уровень человечности
│   ├── StageInfoCard.tsx        # Информация о стадии
│   ├── SymptomsList.tsx         # Список симптомов
│   ├── index.ts
│   └── __tests__/
├── pages/
│   ├── CyberpsychosisPage.tsx   # Страница мониторинга
│   └── index.ts
└── README.md
```

## 🎨 Компоненты (Material UI)

### HumanityDisplay
Отображает уровень человечности

**OpenAPI тип:** `HumanityInfo`

**Данные из API:**
- `current` - текущий уровень (0-100)
- `max` - максимальный уровень
- `loss_percentage` - процент потери
- `stage` - текущая стадия (stable/anxious/dissociative/cyberpsycho)

### SymptomsList
Список активных симптомов

**OpenAPI тип:** `Symptom[]`

**Данные из API:**
- `symptom_id` - ID симптома
- `name` - название
- `description` - описание
- `severity` - серьезность (minor/moderate/severe/critical)
- `effects` - эффекты симптома
- `duration` - длительность (если временный)

### StageInfoCard
Информация о стадии киберпсихоза

**OpenAPI тип:** `StageInfo`

**Данные из API:**
- `name` - название стадии
- `humanity_range` - диапазон человечности
- `symptoms` - возможные симптомы
- `effects` - эффекты стадии
- `consequences` - последствия

## 📄 Страница

### CyberpsychosisPage
Страница мониторинга киберпсихоза

**Роут:** `/game/cyberpsychosis`

**Защита:** Требуется выбранный персонаж

**Структура (3 колонки):**
- Левая панель: Меню разделов (Обзор, Симптомы, Прогрессия, Информация)
- Центр: Контент выбранного раздела
- Правая панель: Уровень человечности (HumanityDisplay)

## 🔌 API Интеграция (Orval Generated)

### GET Endpoints

**Humanity:**
- `GET /gameplay/combat/cyberpsychosis/{playerId}/humanity` → `useGetHumanity`

**Stage:**
- `GET /gameplay/combat/cyberpsychosis/{playerId}/stage` → `useGetCyberpsychosisStage`
- `GET /gameplay/combat/cyberpsychosis/stages/{stage}` → `useGetStageInfo`

**Symptoms:**
- `GET /gameplay/combat/cyberpsychosis/{playerId}/symptoms` → `useGetSymptoms`

**Progression:**
- `GET /gameplay/combat/cyberpsychosis/{playerId}/progression` → `useGetProgression`

**Effects:**
- `GET /gameplay/combat/cyberpsychosis/{playerId}/consequences` → `useGetConsequences`
- `GET /gameplay/combat/cyberpsychosis/{playerId}/stat-penalties` → `useGetStatPenalties`
- `GET /gameplay/combat/cyberpsychosis/{playerId}/social-effects` → `useGetSocialEffects`

### Mutations (доступны но не используются пока)

- `useCalculateHumanityLoss` - расчет потери человечности
- `useApplyHumanityLoss` - применить потерю
- `useCalculateCyberpsychosisProgression` - расчет прогрессии
- `useTriggerProgression` - триггер прогрессии
- `useApplyPrevention` - применить профилактику
- `useApplyTreatment` - применить лечение
- `useApplySymptomManagement` - управление симптомами
- `useApplySocialSupport` - социальная поддержка

## ✅ OpenAPI Compliance

**Все данные из OpenAPI!**

**Flow:**
```
API (OpenAPI spec)
  ↓
Orval Generated Hooks
  ↓
React Components
```

**Компоненты и типы:**
| Компонент | OpenAPI Тип | Hardcoded? |
|-----------|-------------|------------|
| HumanityDisplay | HumanityInfo | ❌ НЕТ |
| SymptomsList | Symptom[] | ❌ НЕТ |
| StageInfoCard | StageInfo | ❌ НЕТ |

**100% OpenAPI!** ✅

## 🔐 Защищенный роут

```typescript
<ProtectedRoute requireCharacter={true}>
  <CyberpsychosisPage />
</ProtectedRoute>
```

## 🧪 Тестирование

- ✅ HumanityDisplay.test.tsx (4 теста)

## 🚀 Использование

### Навигация

```typescript
import { useNavigate } from 'react-router-dom'

const navigate = useNavigate()
navigate('/game/cyberpsychosis')
```

### Загрузка данных

```typescript
import { useGetHumanity } from '@/api/generated/gameplay/cyberpsychosis/combat/combat'

const { data, isLoading } = useGetHumanity(playerId, {
  query: { enabled: !!playerId }
})
```

## 🎯 Интеграция

**Доступ:**
- GameplayPage → Левая панель → кнопка "Киберпсихоз"
- Прямой переход: `/game/cyberpsychosis`

## 📝 Источники

- **OpenAPI:** `API-SWAGGER/api/v1/gameplay/combat/cyberpsychosis.yaml`
- **.BRAIN:** `02-gameplay/combat/combat-cyberpsychosis.md`
- **Task:** API-TASK-004

## 📦 Зависимости

- Material UI (MUI) - UI компоненты
- React Router - роутинг
- React Query - хуки (сгенерированы Orval)
- TypeScript - типы из OpenAPI

