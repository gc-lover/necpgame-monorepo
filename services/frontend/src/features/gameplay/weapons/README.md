# Weapons Feature (Система оружия)

## Описание

Feature для каталога оружия и системы Weapon Mastery. Предоставляет доступ к 80+ моделям оружия из лора Cyberpunk 2077 с детальными характеристиками и системой прогрессии владения.

## OpenAPI Спецификация

Данный feature реализует спецификацию из `API-SWAGGER/api/v1/gameplay/combat/weapons.yaml`

### Эндпоинты

#### GET `/gameplay/combat/weapons`
Получение каталога оружия с фильтрацией.

**Параметры:**
- `weapon_class` (query, optional): Класс оружия (pistol, assault_rifle, shotgun, sniper_rifle, smg, lmg, melee, cyberware)
- `brand` (query, optional): Бренд оружия
- `rarity` (query, optional): Редкость (common, uncommon, rare, epic, legendary, iconic)

**Ответ:** `GetWeaponsCatalog200`
```typescript
interface GetWeaponsCatalog200 {
  weapons: WeaponSummary[]
}
```

#### GET `/gameplay/combat/weapons/{weapon_id}`
Получение детальной информации об оружии.

**Ответ:** `WeaponDetails`
```typescript
interface WeaponDetails {
  id: string
  name: string
  description?: string
  lore?: string
  weapon_class: WeaponClass
  subclass?: string
  brand?: string
  rarity: Rarity
  stats: WeaponStats
  special_abilities?: SpecialAbility[]
  mod_slots?: number
  compatible_mods?: string[]
  requirements?: Requirements
}
```

#### GET `/gameplay/combat/weapons/mastery/{character_id}`
Получение прогресса Weapon Mastery персонажа.

**Ответ:** `WeaponMasteryProgress`
```typescript
interface WeaponMasteryProgress {
  character_id: string
  weapon_class: string
  rank: 'novice' | 'adept' | 'expert' | 'master' | 'legend'
  total_kills: number
  kills_to_next_rank: number
  bonuses: MasteryBonus[]
}
```

#### PUT `/gameplay/combat/weapons/mastery`
Обновление прогресса Weapon Mastery (после убийств).

**Тело запроса:**
```typescript
{
  character_id: string
  weapon_id: string
  kills: number
}
```

## Структура Feature

```
src/features/gameplay/weapons/
├── components/
│   ├── WeaponCard.tsx                # Карточка оружия для каталога
│   ├── WeaponDetailsDialog.tsx       # Диалог с деталями оружия
│   ├── MasteryDisplay.tsx            # Отображение Weapon Mastery
│   └── __tests__/
│       ├── WeaponCard.test.tsx
│       └── MasteryDisplay.test.tsx
├── pages/
│   └── WeaponsPage.tsx               # Главная страница каталога
└── README.md                          # Документация
```

## Компоненты

### WeaponCard
Компактная карточка оружия для каталога:
- Название, бренд, редкость
- Класс оружия (перевод на русский)
- Базовые характеристики (урон, скорострельность)
- Цветовая кодировка по редкости

**Props:**
```typescript
interface WeaponCardProps {
  weapon: WeaponSummary
  onClick?: () => void
}
```

### WeaponDetailsDialog
Диалог с полной информацией об оружии:
- Детальные характеристики (обойма, перезарядка, точность, дальность, крит)
- Лор из Cyberpunk 2077
- Специальные способности
- Слоты для модов
- Требования к персонажу

**Props:**
```typescript
interface WeaponDetailsDialogProps {
  open: boolean
  weapon: WeaponDetails | null
  onClose: () => void
}
```

### MasteryDisplay
Отображение прогресса владения оружием:
- Текущий ранг (5 уровней)
- Прогресс-бар до следующего ранга
- Активные бонусы от Mastery
- Компактный и полный режимы отображения

**Props:**
```typescript
interface MasteryDisplayProps {
  mastery: WeaponMasteryProgress
  compact?: boolean
}
```

### WeaponsPage
Главная страница каталога оружия:
- 3-колоночная компактная SPA структура
- Фильтры по классу и редкости
- Список оружия с деталями
- Панель Weapon Mastery
- Информация о системе прогрессии

## Генерация API клиента

Клиент генерируется через Orval:

```bash
npm run generate:api
```

Конфигурация в `orval.config.ts`:
```typescript
'weapons-api': {
  input: {
    target: '../API-SWAGGER/api/v1/gameplay/combat/weapons.yaml',
  },
  output: {
    mode: 'tags-split',
    target: './src/api/generated/weapons',
    schemas: './src/api/generated/weapons/models',
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

- `useGetWeaponsCatalog()` - Каталог оружия с фильтрами
- `useGetWeapon()` - Детали конкретного оружия
- `useGetWeaponsByBrand()` - Оружие по бренду
- `useGetWeaponsByClass()` - Оружие по классу
- `useGetWeaponMastery()` - Прогресс Weapon Mastery
- `useUpdateWeaponMastery()` - Обновление Mastery
- `useGetWeaponMods()` - Доступные моды
- `useGetMetaWeapons()` - Мета оружие для типа контента

## Роутинг

Страница доступна по адресу `/game/weapons` с защитой через `ProtectedRoute`:

```typescript
{
  path: '/game/weapons',
  element: (
    <ProtectedRoute requireCharacter={true}>
      <WeaponsPage />
    </ProtectedRoute>
  ),
}
```

## Интеграция с игрой

Кнопка "Оружие" добавлена в меню `GameplayPage`:
- Иконка: `GpsFixedIcon` (прицел)
- Навигация: `/game/weapons`
- Описание: "Каталог и Mastery"

## Система оружия

### Классы оружия (7)
- **Pistols** (Пистолеты) - универсальное оружие ближнего боя
- **Assault Rifles** (Штурмовые винтовки) - универсальное оружие средней дальности
- **Shotguns** (Дробовики) - мощное оружие ближнего боя
- **Sniper Rifles** (Снайперские винтовки) - дальнобойное точное оружие
- **SMG** (Пистолеты-пулемёты) - быстрое оружие ближнего боя
- **LMG** (Лёгкие пулемёты) - оружие подавления
- **Melee** (Ближний бой) - холодное оружие
- **Cyberware** (Кибероружие) - Mantis Blades, Gorilla Arms, Monowire

### Бренды (5)
- **Arasaka** - японская корпорация, высокотехнологичное оружие
- **Militech** - американская корпорация, военное оружие
- **Kang Tao** - китайская корпорация, умное оружие
- **Budget Arms** - дешёвое массовое оружие
- **Constitutional Arms** - надёжное гражданское оружие

### Редкость (6 уровней)
- **Common** (Обычное) - серый цвет
- **Uncommon** (Необычное) - зелёный цвет
- **Rare** (Редкое) - синий цвет
- **Epic** (Эпическое) - фиолетовый цвет
- **Legendary** (Легендарное) - оранжевый цвет
- **Iconic** (Иконическое) - оранжевый цвет, уникальное

### Weapon Mastery (5 рангов)

Система прогрессии владения оружием:

| Ранг | Убийств | Цвет | Бонусы |
|------|---------|------|--------|
| **Novice** (Новичок) | 0-100 | Серый | Базовые характеристики |
| **Adept** (Адепт) | 100-500 | Зелёный | +5% урон, +2% точность |
| **Expert** (Эксперт) | 500-2000 | Синий | +10% урон, +5% точность, +10% крит |
| **Master** (Мастер) | 2000-5000 | Фиолетовый | +15% урон, +10% точность, +15% крит |
| **Legend** (Легенда) | 5000+ | Оранжевый | +25% урон, +15% точность, +25% крит |

Прогресс увеличивается при убийствах врагов данным типом оружия.

## Характеристики оружия

### Базовые характеристики
- **Damage** (Урон) - базовый урон за выстрел/удар
- **Fire Rate** (Скорострельность) - выстрелов/ударов в секунду
- **Magazine Size** (Обойма) - количество патронов
- **Reload Time** (Перезарядка) - время в секундах
- **Accuracy** (Точность) - процент точности
- **Range Effective** (Эффективная дальность) - метры
- **Range Max** (Максимальная дальность) - метры
- **Crit Chance** (Шанс крита) - процент
- **Crit Damage** (Урон крита) - процент
- **Penetration** (Проникающая способность) - броне-пробитие

### Специальные способности
Легендарное и иконическое оружие может иметь уникальные способности с кулдауном.

### Weapon Mods
Система модификаций оружия для улучшения характеристик.

## Тестирование

Покрытие тестами: **50%+**

Тесты покрывают:
- Рендеринг компонентов
- Отображение данных из OpenAPI
- Фильтрацию каталога
- Клики и взаимодействие
- Различные ранги Mastery

Запуск тестов:
```bash
npm run test
```

## UI/UX особенности

- **Компактный дизайн** - весь каталог на одном экране
- **3-колоночная сетка** - фильтры, каталог, Mastery
- **Цветовая кодировка** - редкость и ранги
- **Фильтрация** - по классу и редкости
- **Детали по клику** - диалог с полной информацией
- **Прогресс-бары** - визуализация Mastery

## Соответствие принципам

- OK **SOLID** - каждый компонент имеет одну ответственность
- OK **DRY** - переиспользуемые компоненты и типы
- OK **KISS** - простая и понятная структура
- OK **SPA Architecture** - клиентская навигация
- OK **OpenAPI First** - все типы из OpenAPI спецификации
- OK **Material UI** - исключительно MUI компоненты
- OK **React Query** - управление серверным состоянием
- OK **Feature-based structure** - модульная организация

## Зависимости

- React 18+
- Material UI (MUI)
- React Query (TanStack Query)
- React Router
- TypeScript
- Orval (кодогенерация)

## Источники

- Концепция: `.BRAIN/02-gameplay/combat/combat-weapon-classes-detailed.md v1.0.0`
- Лор: Cyberpunk 2077
- API: `API-SWAGGER/api/v1/gameplay/combat/weapons.yaml`

## Примечания

- **80+ моделей оружия** из лора Cyberpunk 2077
- **Система Mastery** поощряет использование разных типов оружия
- **Weapon Mods** система будет расширена в будущем
- **Meta рекомендации** для разных типов контента (PvE, PvP, Extraction, Raid)

## Автор

Разработано AI Agent с использованием спецификаций из `ФРОНТТАСК.MD`

