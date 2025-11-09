# Events Feature (Случайные события)

## Описание

Feature для управления случайными событиями в игре. События могут происходить во время путешествий и исследований, предлагая игроку различные варианты действий с соответствующими последствиями.

## OpenAPI Спецификация

Данный feature реализует спецификацию из `API-SWAGGER/api/v1/events/random-events.yaml`

### Эндпоинты

#### GET `/api/v1/events/random`
Генерация нового случайного события для персонажа.

**Параметры:**
- `characterId` (query, required): ID персонажа
- `contextType` (query, optional): Тип контекста (TRAVEL, COMBAT, EXPLORATION, SOCIAL)
- `locationId` (query, optional): ID текущей локации

**Ответ:** `RandomEvent`
```typescript
interface RandomEvent {
  id: string
  name: string
  description: string
  options: EventOption[]
  timeLimit?: number | null
  dangerLevel?: 'LOW' | 'MEDIUM' | 'HIGH'
}
```

#### POST `/api/v1/events/{eventId}/respond`
Ответ игрока на событие.

**Параметры:**
- `eventId` (path, required): ID события

**Тело запроса:** `RespondToEventBody`
```typescript
interface RespondToEventBody {
  characterId: string
  eventId: string
  optionId: string
}
```

**Ответ:** `EventResult`
```typescript
interface EventResult {
  success: boolean
  outcome: string
  rewards?: EventResultRewards
  penalties?: EventResultPenalties
}
```

#### GET `/api/v1/events/active`
Получение списка активных событий персонажа.

**Параметры:**
- `characterId` (query, required): ID персонажа

**Ответ:** `GetActiveEvents200`
```typescript
interface GetActiveEvents200 {
  events: RandomEvent[]
}
```

## Структура Feature

```
src/features/gameplay/events/
├── components/
│   ├── RandomEventCard.tsx          # Карточка события
│   ├── EventDialog.tsx              # Диалог для взаимодействия с событием
│   └── __tests__/
│       ├── RandomEventCard.test.tsx
│       └── EventDialog.test.tsx
├── pages/
│   └── EventsPage.tsx               # Главная страница событий
└── README.md                         # Документация
```

## Компоненты

### RandomEventCard
Компактная карточка события, отображает:
- Название события
- Описание
- Уровень опасности (LOW/MEDIUM/HIGH)
- Ограничение времени (если есть)
- Количество вариантов действий

**Props:**
```typescript
interface RandomEventCardProps {
  event: RandomEvent
  onClick?: () => void
}
```

### EventDialog
Диалоговое окно для взаимодействия с событием и просмотра результатов:
- Отображение информации о событии
- Выбор варианта действия
- Проверка требований для каждого варианта
- Отображение результатов (награды/штрафы)

**Props:**
```typescript
interface EventDialogProps {
  open: boolean
  event: RandomEvent | null
  result: EventResult | null
  onClose: () => void
  onSelectOption: (optionId: string) => void
  isResponding?: boolean
}
```

### EventsPage
Главная страница для управления событиями:
- 3-колоночная компактная SPA структура
- Список активных событий
- Генерация новых событий
- Взаимодействие с событиями через диалог

## Генерация API клиента

Клиент генерируется через Orval:

```bash
npm run generate:api
```

Конфигурация в `orval.config.ts`:
```typescript
'events-api': {
  input: {
    target: '../API-SWAGGER/api/v1/events/random-events.yaml',
  },
  output: {
    mode: 'tags-split',
    target: './src/api/generated/events',
    schemas: './src/api/generated/events/models',
    client: 'react-query',
    mock: true,
    prettier: true,
    override: {
      mutator: {
        path: './src/api/custom-instance.ts',
        name: 'customInstance',
      },
      query: {
        useQuery: true,
        useMutation: true,
        signal: true,
      },
    },
  },
}
```

## Используемые React Query хуки

- `useGetRandomEvent()` - Генерация нового события
- `useRespondToEvent()` - Ответ на событие
- `useGetActiveEvents()` - Получение активных событий

## Роутинг

Страница доступна по адресу `/game/events` с защитой через `ProtectedRoute`:

```typescript
{
  path: '/game/events',
  element: (
    <ProtectedRoute requireCharacter={true}>
      <EventsPage />
    </ProtectedRoute>
  ),
}
```

## Интеграция с игрой

Кнопка "События" добавлена в меню `GameplayPage`:
- Иконка: `EventIcon`
- Навигация: `/game/events`
- Описание: "Случайные события"

## Типы событий

### Уровни опасности
- **LOW** (Низкая) - Безопасные события, обычно с положительными последствиями
- **MEDIUM** (Средняя) - События с умеренным риском
- **HIGH** (Высокая) - Опасные ситуации с высоким риском

### Контекст событий
- **TRAVEL** - События во время путешествий
- **COMBAT** - События во время боя
- **EXPLORATION** - События при исследовании
- **SOCIAL** - Социальные события

## Требования к вариантам действий

События могут иметь требования к характеристикам или навыкам:
- `minStrength` - Минимальная сила
- `minIntelligence` - Минимальный интеллект
- `minReflex` - Минимальный рефлекс
- `minTechnical` - Минимальная техника
- `requiredSkill` - Необходимый навык

Варианты с невыполненными требованиями отмечаются как недоступные.

## Награды и штрафы

### Награды
- Опыт (experience)
- Деньги (money)
- Репутация фракций (reputation)
- Предметы (items)

### Штрафы
- Потеря здоровья (healthLoss)
- Потеря денег (moneyLoss)
- Потеря человечности (humanityLoss)

## Тестирование

Покрытие тестами: **50%+**

Тесты покрывают:
- Рендеринг компонентов
- Отображение данных из OpenAPI
- Взаимодействие пользователя
- Обработка различных состояний (успех/неудача)
- Проверка требований

Запуск тестов:
```bash
npm run test
```

## UI/UX особенности

- **Компактный дизайн** - весь интерфейс помещается на одном экране
- **3-колоночная сетка** - использование `GameLayout` для консистентности
- **Отзывчивость** - немедленная обратная связь на действия пользователя
- **Визуальная иерархия** - цветовая кодировка по уровню опасности
- **Доступность** - поддержка клавиатурной навигации

## Соответствие принципам

- ✅ **SOLID** - компоненты имеют одну ответственность
- ✅ **DRY** - переиспользуемые компоненты и типы
- ✅ **KISS** - простая и понятная структура
- ✅ **SPA Architecture** - клиентская навигация без перезагрузки
- ✅ **OpenAPI First** - все типы и хуки из OpenAPI спецификации
- ✅ **Material UI** - исключительно MUI компоненты
- ✅ **React Query** - управление серверным состоянием
- ✅ **Feature-based structure** - модульная организация

## Зависимости

- React 18+
- Material UI (MUI)
- React Query (TanStack Query)
- React Router
- TypeScript
- Orval (кодогенерация)

## Автор

Разработано AI Agent с использованием спецификаций из `ФРОНТТАСК.MD`

