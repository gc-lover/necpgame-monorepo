# Character Status Feature - Статус и характеристики персонажа

Feature модуль для просмотра детальной информации о персонаже в NECPGAME.

## 📋 Описание

Детальная информация о персонаже:
- Статус (здоровье, энергия, человечность, уровень)
- Характеристики (сила, рефлексы, интеллект, технические, хладнокровие)
- Навыки (уровень навыков, прогресс)

## 🗂️ Структура

```
features/character/status/
├── components/
│   ├── StatusOverview.tsx           # Обзор статуса
│   ├── CharacterStatsDisplay.tsx    # Характеристики
│   ├── SkillsListDisplay.tsx        # Навыки
│   ├── index.ts
│   └── __tests__/
├── pages/
│   ├── CharacterStatusPage.tsx     # Страница статуса
│   └── index.ts
└── README.md
```

## 🎨 Компоненты (Material UI)

### StatusOverview
Обзор основного статуса персонажа

**OpenAPI тип:** `CharacterStatus`

**Отображает:**
- Здоровье (HP)
- Энергия
- Человечность
- Уровень и опыт

### CharacterStatsDisplay
Отображение характеристик персонажа (SPECIAL-like)

**OpenAPI тип:** `CharacterStats`

**Характеристики:**
- Сила (Strength)
- Рефлексы (Reflexes)
- Интеллект (Intelligence)
- Технические навыки (Technical)
- Хладнокровие (Cool)

### SkillsListDisplay
Список навыков с прогрессом

**OpenAPI тип:** `Skill[]`

## 📄 Страница

### CharacterStatusPage
Полноценная страница персонажа

**Роут:** `/game/character`

**Структура (3 колонки):**
- Левая: Навигация (Статус/Характеристики/Навыки)
- Центр: Контент выбранного раздела
- Правая: Краткая информация о персонаже

## 🔌 API (Orval Generated)

**Queries:**
- `GET /characters/{characterId}/status` → `useGetCharacterStatus`
- `GET /characters/{characterId}/stats` → `useGetCharacterStats`
- `GET /characters/{characterId}/skills` → `useGetCharacterSkills`

**Mutations:**
- `POST /characters/{characterId}/status/update` → `useUpdateCharacterStatus`

## ✅ OpenAPI Compliance

**Все данные из OpenAPI!**

| Компонент | Тип | Hardcoded? |
|-----------|-----|------------|
| StatusOverview | CharacterStatus | ❌ НЕТ |
| CharacterStatsDisplay | CharacterStats | ❌ НЕТ |
| SkillsListDisplay | Skill[] | ❌ НЕТ |

## 🚀 Использование

Навигация:
```typescript
navigate('/game/character')
```

Загрузка:
```typescript
const { data: status } = useGetCharacterStatus({ characterId })
const { data: stats } = useGetCharacterStats({ characterId })
const { data: skills } = useGetCharacterSkills({ characterId })
```

## 🎯 Интеграция

**Доступ:**
- GameplayPage → Меню → "Персонаж"
- Прямой переход: `/game/character`

## 📝 Источники

- **OpenAPI:** `API-SWAGGER/api/v1/characters/status.yaml`
- **Task:** API-TASK-035

