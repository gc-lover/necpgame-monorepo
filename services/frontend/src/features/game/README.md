# Game Feature - Начало игры

Feature модуль для функциональности начала игры в NECPGAME.

## 📋 Описание

Этот модуль отвечает за:
- Приветственный экран перед началом игры
- Запуск игры для выбранного персонажа
- Отображение начального состояния игры (локация, NPC, квесты)
- Туториал для новых игроков
- Управление игровым состоянием

## 🗂️ Структура модуля

```
features/game/
├── components/           # UI компоненты
│   ├── ActionButtons.tsx       # Кнопки действий в игре
│   ├── CharacterState.tsx      # Состояние персонажа
│   ├── GameStartButton.tsx     # Кнопка начала игры
│   ├── LocationInfo.tsx        # Информация о локации
│   ├── NPCList.tsx            # Список NPC
│   ├── QuestCard.tsx          # Карточка квеста
│   ├── StartingEquipment.tsx  # Стартовое снаряжение
│   ├── TutorialSteps.tsx      # Шаги туториала (MUI Stepper)
│   ├── WelcomeScreen.tsx      # Приветственный экран
│   ├── index.ts               # Экспорты компонентов
│   └── __tests__/             # Тесты компонентов
│       ├── ActionButtons.test.tsx
│       ├── CharacterState.test.tsx
│       ├── LocationInfo.test.tsx
│       ├── QuestCard.test.tsx
│       └── README.md
├── pages/                # Страницы
│   ├── WelcomePage.tsx         # Страница приветствия
│   ├── GameplayPage.tsx        # Основная игровая страница
│   ├── index.ts                # Экспорты страниц
│   └── __tests__/              # Тесты страниц
├── hooks/                # Кастомные хуки
│   ├── useGameStart.ts         # Хук для запуска игры
│   ├── useGameState.ts         # Хук для управления состоянием
│   └── index.ts                # Экспорты хуков
└── README.md             # Этот файл
```

## 🎨 UI Компоненты

### ActionButtons
Отображает доступные действия в игре (осмотреть, поговорить, переместиться и т.д.)

**Props:**
- `actions: GameAction[]` - список доступных действий
- `onActionClick?: (action: GameAction) => void` - обработчик клика

**Использование:**
```tsx
import { ActionButtons } from '@/features/game/components'

<ActionButtons 
  actions={gameState.availableActions}
  onActionClick={handleActionClick}
/>
```

### CharacterState
Отображает текущее состояние персонажа (здоровье, энергия, деньги и т.д.)

**Props:**
- `state: GameCharacterState` - состояние персонажа

**Использование:**
```tsx
import { CharacterState } from '@/features/game/components'

<CharacterState state={characterState} />
```

### LocationInfo
Отображает информацию о текущей локации

**Props:**
- `location: GameLocation` - информация о локации

**Использование:**
```tsx
import { LocationInfo } from '@/features/game/components'

<LocationInfo location={currentLocation} />
```

### NPCList
Отображает список доступных NPC в локации

**Props:**
- `npcs: GameNPC[]` - список NPC
- `onSelectNPC?: (npc: GameNPC) => void` - обработчик выбора NPC

**Использование:**
```tsx
import { NPCList } from '@/features/game/components'

<NPCList 
  npcs={availableNPCs}
  onSelectNPC={handleNPCSelect}
/>
```

### QuestCard
Отображает карточку квеста с наградами

**Props:**
- `quest: GameQuest` - информация о квесте
- `onSelect?: (quest: GameQuest) => void` - обработчик выбора

**Использование:**
```tsx
import { QuestCard } from '@/features/game/components'

<QuestCard 
  quest={firstQuest}
  onSelect={handleQuestSelect}
/>
```

### TutorialSteps
Отображает шаги туториала с использованием MUI Stepper

**Props:**
- `data: TutorialStepsResponse` - данные туториала
- `onComplete?: () => void` - обработчик завершения
- `onSkip?: () => void` - обработчик пропуска

**Использование:**
```tsx
import { TutorialSteps } from '@/features/game/components'

<TutorialSteps 
  data={tutorialData}
  onComplete={handleComplete}
  onSkip={handleSkip}
/>
```

### WelcomeScreen
Отображает приветственный экран перед началом игры

**Props:**
- `data: WelcomeScreenResponse` - данные приветственного экрана
- `onStartGame: (skipTutorial: boolean) => void` - обработчик начала игры
- `loading?: boolean` - состояние загрузки

**Использование:**
```tsx
import { WelcomeScreen } from '@/features/game/components'

<WelcomeScreen 
  data={welcomeData}
  onStartGame={handleStartGame}
  loading={isStarting}
/>
```

## 📄 Страницы

### WelcomePage
Страница приветствия перед началом игры

**Роут:** `/game/welcome?characterId={id}`

**Защита:** Требуется выбранный персонаж

**Функциональность:**
- Загружает приветственный экран через API
- Отображает информацию о персонаже
- Предоставляет кнопки "Начать игру" и "Пропустить туториал"
- Запускает игру при нажатии кнопки

### GameplayPage
Основная игровая страница

**Роут:** `/game/play`

**Защита:** Требуется выбранный персонаж

**Функциональность:**
- Загружает начальное состояние игры
- Отображает информацию о локации
- Показывает список доступных NPC
- Отображает первый квест
- Показывает доступные действия
- Отображает туториал (если включен)
- Показывает состояние персонажа

## 🎣 Хуки

### useGameStart
Хук для запуска игры

**Возвращает:**
```typescript
{
  startGame: (characterId: string, skipTutorial: boolean, onSuccess?: (sessionId: string) => void) => void
  isLoading: boolean
  isError: boolean
  error: Error | null
  data: GameStartResponse | undefined
}
```

**Использование:**
```tsx
const { startGame, isLoading } = useGameStart()

startGame(characterId, false, (sessionId) => {
  console.log('Game started:', sessionId)
})
```

### useGameState
Zustand store для управления игровым состоянием

**Состояние:**
```typescript
{
  gameSessionId: string | null
  selectedCharacterId: string | null
  tutorialEnabled: boolean
  tutorialStep: number
  tutorialCompleted: boolean
}
```

**Actions:**
```typescript
{
  setGameSession: (sessionId: string) => void
  setSelectedCharacter: (characterId: string) => void
  setTutorialEnabled: (enabled: boolean) => void
  setTutorialStep: (step: number) => void
  completeTutorial: () => void
  resetGame: () => void
}
```

**Использование:**
```tsx
const gameSessionId = useGameState((state) => state.gameSessionId)
const setGameSession = useGameState((state) => state.setGameSession)
```

### useSelectedCharacter
Helper хук для получения ID выбранного персонажа

**Использование:**
```tsx
const characterId = useSelectedCharacter()
```

### useTutorialState
Helper хук для получения состояния туториала

**Возвращает:**
```typescript
{
  enabled: boolean
  currentStep: number
  completed: boolean
}
```

## 🔌 API Интеграция

### Endpoints

**Game Start API:**
- `POST /game/start` - запуск игры (useStartGame)
- `GET /game/welcome` - приветственный экран (useGetWelcomeScreen)
- `POST /game/return` - возврат в игру (useReturnToGame)

**Initial State API:**
- `GET /game/initial-state` - начальное состояние (useGetInitialState)
- `GET /game/tutorial-steps` - шаги туториала (useGetTutorialSteps)

### Типы данных

Все типы импортируются из `@/api/generated/game/models`:

```typescript
import type {
  GameLocation,
  GameNPC,
  GameQuest,
  GameAction,
  GameCharacterState,
  GameStartRequest,
  GameStartResponse,
  WelcomeScreenResponse,
  InitialStateResponse,
  TutorialStepsResponse,
} from '@/api/generated/game/models'
```

## 🔐 Защищенные роуты

Роуты `/game/welcome` и `/game/play` защищены компонентом `ProtectedRoute`, который проверяет наличие выбранного персонажа. Если персонаж не выбран - происходит редирект на `/characters`.

```typescript
<ProtectedRoute requireCharacter={true}>
  <WelcomePage />
</ProtectedRoute>
```

## ✅ OpenAPI Compliance

**ВАЖНО:** Все данные берутся из OpenAPI спецификаций, не hardcoded!

См. полную проверку: [OPENAPI-COMPLIANCE-CHECK.md](./OPENAPI-COMPLIANCE-CHECK.md)

**Flow данных:**
```
API (OpenAPI) → Orval Generated Hooks → Zustand Store → React Components
```

**Проверено:**
- ✅ CharacterState - из POST /game/start → store
- ✅ StartingEquipment - из POST /game/start → store  
- ✅ LocationInfo - из GET /game/initial-state
- ✅ NPC List - из GET /game/initial-state
- ✅ QuestCard - из GET /game/initial-state
- ✅ Actions - из GET /game/initial-state
- ✅ Tutorial - из GET /game/tutorial-steps

## 🧪 Тестирование

Тесты написаны с использованием Vitest и Testing Library.

**Запуск тестов:**
```bash
npm test
```

**Покрытие:**
```bash
npm run test:coverage
```

**Тестируемые компоненты:**
- ✅ CharacterState
- ✅ QuestCard
- ✅ LocationInfo
- ✅ ActionButtons

Покрытие: 50%+

## 🚀 Использование

### 1. Навигация на приветственный экран

```tsx
import { useNavigate } from 'react-router-dom'

const navigate = useNavigate()
navigate(`/game/welcome?characterId=${characterId}`)
```

### 2. Запуск игры

```tsx
import { useGameStart } from '@/features/game/hooks'

const { startGame } = useGameStart()
startGame(characterId, false) // false = с туториалом
```

### 3. Загрузка игрового состояния

```tsx
import { useGetInitialState } from '@/api/generated/game/game-initial-state/game-initial-state'

const { data, isLoading } = useGetInitialState({
  characterId: selectedCharacterId || ''
})
```

## 📦 Зависимости

- **Material UI (MUI)** - UI компоненты
- **React Router** - роутинг
- **React Query** - управление серверным состоянием
- **Zustand** - клиентское состояние
- **TypeScript** - типизация

## 🎯 Следующие шаги

- [ ] Реализовать диалоги с NPC
- [ ] Добавить систему квестов
- [ ] Реализовать перемещение между локациями
- [ ] Добавить инвентарь
- [ ] Реализовать систему действий
- [ ] Добавить сохранение/загрузку игры

## 📝 Примечания

- Все компоненты используют Material UI (MUI)
- Текстовая MVP версия - без графики
- Темная цветовая схема (киберпанк стиль)
- Все файлы не превышают 400 строк
- Соблюдаются принципы SOLID, DRY, KISS

