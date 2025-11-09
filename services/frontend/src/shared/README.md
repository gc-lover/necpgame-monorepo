# Shared Library - Библиотека переиспользуемых компонентов

**Версия:** 1.0.0  
**Дата:** 2025-11-06

---

## 📚 Обзор

Библиотека переиспользуемых компонентов для всего проекта.

**Что содержит:**
- **UI Kit** - переиспользуемые UI компоненты (GameLayout, карточки, кнопки...)
- **Theme** - Design System (Design Tokens + MUI Theme)
- **Forms** - готовые игровые формы (в будущем)
- **Hooks** - общие хуки (в будущем)
- **Utils** - утилиты (в будущем)

---

## 🎯 Структура

```
src/shared/
├── ui/                          # UI Kit ⭐
│   ├── layout/                 # Layout компоненты
│   │   └── GameLayout/         # 3-колоночная сетка MMORPG
│   │       ├── GameLayout.tsx
│   │       ├── MenuPanel.tsx
│   │       ├── StatsPanel.tsx
│   │       ├── MenuItem.tsx
│   │       ├── StatCard.tsx
│   │       └── index.ts
│   └── index.ts
│
├── theme/                       # Design System ⭐
│   └── cyberpunk/
│       ├── tokens.ts           # Design Tokens (цвета, размеры, шрифты)
│       ├── theme.ts            # MUI Theme
│       └── index.ts
│
├── forms/                       # Готовые формы (в будущем)
├── hooks/                       # Переиспользуемые хуки (в будущем)
├── utils/                       # Утилиты (в будущем)
└── index.ts                     # Главный экспорт
```

---

## 🚀 Использование

### UI Components

```typescript
// Импорт компонентов
import { GameLayout, MenuItem, StatCard } from '@/shared/ui/layout';

// Использование
<GameLayout
  leftPanel={
    <MenuPanel>
      <MenuItem label="Explore" active />
      <MenuItem label="Inventory" />
    </MenuPanel>
  }
  rightPanel={
    <StatsPanel>
      <StatCard label="HP" value="100" color="cyan" />
      <StatCard label="ENERGY" value="85" color="green" />
    </StatsPanel>
  }
>
  {/* Основной контент */}
</GameLayout>
```

### Design Tokens

```typescript
// Импорт токенов
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

// Использование в sx
<Box 
  sx={{
    fontSize: cyberpunkTokens.fonts.sm,         // 0.75rem
    color: cyberpunkTokens.colors.neonCyan,     // #00F7FF
    boxShadow: cyberpunkTokens.effects.neonGlow,
    clipPath: cyberpunkTokens.clipPath.corner,
  }}
/>
```

### MUI Theme

```typescript
// Импорт темы
import { cyberpunkTheme } from '@/shared/theme/cyberpunk';
import { ThemeProvider } from '@mui/material/styles';

// Использование
<ThemeProvider theme={cyberpunkTheme}>
  <App />
</ThemeProvider>
```

---

## 📊 Design Tokens (cyberpunkTokens)

### Размеры

```typescript
sizes: {
  leftPanel: 380,          // Левая панель
  rightPanel: 320,         // Правая панель
  maxWidth: 1400,          // Макс ширина центра
}
```

### Шрифты (МАЛЕНЬКИЕ!) ⭐

```typescript
fonts: {
  xs: '0.65rem',           // Очень мелкий
  sm: '0.75rem',           // Обычный (основной)
  md: '0.875rem',          // Средний
  lg: '1rem',              // Крупный
  xl: '1.25rem',           // Очень крупный
}
```

### Цвета

```typescript
colors: {
  neonCyan: '#00F7FF',     // Основной неон
  neonPink: '#ff2a6d',
  neonGreen: '#05ffa1',
  neonPurple: '#d817ff',
  neonYellow: '#fef86c',
  darkBg: '#0A0E27',       // Основной фон
  cardBg: '#1A1F3A',       // Фон карточек
}
```

### Эффекты

```typescript
effects: {
  neonGlow: '0 0 10px currentColor, 0 0 20px currentColor',
  boxShadowCard: '...',
  backdropBlur: 'blur(10px)',
}
```

### MMORPG стиль - скос углов

```typescript
clipPath: {
  corner: 'polygon(0 0, calc(100% - 8px) 0, 100% 8px, 100% 100%, 8px 100%, 0 calc(100% - 8px))',
}
```

---

## 🎨 Компоненты

### GameLayout

3-колоночная сетка MMORPG:
- Левая панель (380px) - меню, действия
- Центр (flex) - основной контент
- Правая панель (320px) - персонаж, статы

**Критично:**
- Всё помещается на 1 экран (height: 100%)
- Маленькие шрифты (0.65rem - 0.875rem)
- Киберпанк стиль

### MenuItem

Кнопка меню с киберпанк стилем:
- Маленький шрифт (0.75rem)
- MMORPG стиль (скос углов)
- Неоновое свечение при активности

### StatCard

Карточка статистики:
- Маленькие шрифты (0.65rem для label)
- Неоновое свечение по цвету
- MMORPG стиль (скос углов)

---

## WARNING Критичные требования

**НЕ МЕНЯТЬ:**
- OK Размеры: 380px (левая) | flex (центр) | 320px (правая)
- OK Шрифты: 0.65rem - 0.875rem (МАЛЕНЬКИЕ!)
- OK Киберпанк стиль (неон, свечение, скосы)
- OK Всё на 1 экран (height: 100vh)

---

## 📖 Документация

**Планы:**
- [REFACTORING-PLAN.md](../../../docs/REFACTORING-PLAN.md) - детальный план
- [QUICK-START-REFACTORING.md](../../../docs/QUICK-START-REFACTORING.md) - быстрый старт

**Концепции:**
- [ФРОНТТАСК-LIBRARIES.md](../../../docs/ФРОНТТАСК-LIBRARIES.md) - библиотеки
- [DESIGN-SYSTEM.md](../../../docs/libraries/DESIGN-SYSTEM.md) - Design System

---

## 🚀 Следующие шаги

1. OK Создана структура `src/shared/`
2. OK Создан Design System (tokens + theme)
3. OK Перенесен GameLayout
4. OK Созданы базовые компоненты (CompactCard, CyberpunkButton, HealthBar, ProgressBar)
5. ⏳ Создать дополнительные компоненты (CharacterCard, ItemCard, NPCCard...)
6. ⏳ Создать готовые формы (CharacterCreationForm, TradeForm...)
7. ⏳ Обновить импорты во всех features (опционально)

---

**Версия:** 1.0.0  
**Дата:** 2025-11-06  
**Статус:** В разработке (Этап 1)

