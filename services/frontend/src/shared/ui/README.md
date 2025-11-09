# UI Kit - Библиотека переиспользуемых UI компонентов

**Версия:** 1.0.0  
**Дата:** 2025-11-06

---

## 📚 Обзор

Библиотека переиспользуемых UI компонентов с киберпанк стилем.

**Все компоненты:**
- OK Используют `cyberpunkTokens` (Design System)
- OK Маленькие шрифты (0.65rem - 0.875rem)
- OK MMORPG стиль (скосы углов)
- OK Неоновые эффекты
- OK Компактные (для 1 экрана)

---

## 🎨 Компоненты

### Layout

**GameLayout** - 3-колоночная сетка MMORPG (380px | flex | 320px)
```typescript
import { GameLayout } from '@/shared/ui/layout';

<GameLayout leftPanel={...} rightPanel={...}>
  {/* Основной контент */}
</GameLayout>
```

**MenuItem** - кнопка меню с неоном
```typescript
import { MenuItem } from '@/shared/ui/layout';

<MenuItem label="Explore" active icon={<SearchIcon />} />
```

**StatCard** - карточка статистики
```typescript
import { StatCard } from '@/shared/ui/layout';

<StatCard label="HP" value="100" color="cyan" icon={<FavoriteIcon />} />
```

---

### Cards

**CompactCard** - базовая компактная карточка
```typescript
import { CompactCard } from '@/shared/ui/cards';

<CompactCard color="cyan" glowIntensity="normal" compact>
  {/* Содержимое */}
</CompactCard>
```

**Пропсы:**
- `color` - цвет свечения (cyan, pink, green, purple, yellow, default)
- `glowIntensity` - интенсивность (none, weak, normal, strong)
- `compact` - компактный режим (меньше padding)

---

### Buttons

**CyberpunkButton** - кнопка с неоновыми эффектами
```typescript
import { CyberpunkButton } from '@/shared/ui/buttons';

<CyberpunkButton 
  variant="primary" 
  size="medium"
  startIcon={<AttackIcon />}
>
  Атаковать
</CyberpunkButton>
```

**Пропсы:**
- `variant` - стиль (primary, secondary, success, warning, outlined)
- `size` - размер (small, medium, large)
- `fullWidth` - на всю ширину
- `startIcon`, `endIcon` - иконки

**Размеры шрифтов:**
- small: 0.65rem
- medium: 0.75rem (основной)
- large: 0.875rem

---

### Stats

**HealthBar** - полоска здоровья
```typescript
import { HealthBar } from '@/shared/ui/stats';

<HealthBar 
  current={75} 
  max={100} 
  label="HP"
  color="cyan"
  showValues
/>
```

**ProgressBar** - универсальный прогресс-бар
```typescript
import { ProgressBar } from '@/shared/ui/stats';

<ProgressBar 
  value={65} 
  label="XP to next level"
  color="green"
  showPercent
/>
```

**Пропсы:**
- `color` - цвет (cyan, pink, green, purple, yellow)
- `compact` - компактный режим (высота 6px вместо 8px)
- `showValues` / `showPercent` - показывать значения

---

## 🎯 Design Tokens

**Все компоненты используют:**
```typescript
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

// Размеры
cyberpunkTokens.sizes.leftPanel    // 380px
cyberpunkTokens.sizes.rightPanel   // 320px

// Шрифты (МАЛЕНЬКИЕ!)
cyberpunkTokens.fonts.xs           // 0.65rem
cyberpunkTokens.fonts.sm           // 0.75rem
cyberpunkTokens.fonts.md           // 0.875rem

// Цвета
cyberpunkTokens.colors.neonCyan    // #00F7FF
cyberpunkTokens.colors.neonPink    // #ff2a6d

// Эффекты
cyberpunkTokens.effects.neonGlow
cyberpunkTokens.clipPath.corner    // MMORPG скосы

// Градиенты
cyberpunkTokens.gradients.cardBg
cyberpunkTokens.gradients.activeButton
```

---

## WARNING Важные правила

### При создании новых компонентов:

1. OK **Используй cyberpunkTokens** для всех размеров/цветов/эффектов
2. OK **Маленькие шрифты** (0.65rem - 0.875rem)
3. OK **MMORPG стиль** (clipPath для скосов углов)
4. OK **Компактность** (для 1 экрана)
5. OK **Неоновые эффекты** (boxShadow с цветом)

---

## 📊 Размеры шрифтов

```typescript
xs: '0.65rem'    // Очень мелкий (labels, badges, values)
sm: '0.75rem'    // Обычный (кнопки, текст) - ОСНОВНОЙ
md: '0.875rem'   // Средний (подзаголовки)
lg: '1rem'       // Крупный (заголовки)
xl: '1.25rem'    // Очень крупный (главные заголовки)
```

**Используйте sm (0.75rem) как основной размер!**

---

## 🎨 Цветовая палитра

```typescript
neonCyan: '#00F7FF'      // Основной (primary)
neonPink: '#ff2a6d'      // Розовый (secondary)
neonGreen: '#05ffa1'     // Зелёный (success)
neonPurple: '#d817ff'    // Фиолетовый
neonYellow: '#fef86c'    // Жёлтый (warning)
```

---

## 📖 Примеры использования

### Пример 1: Компактная карточка персонажа

```typescript
import { CompactCard } from '@/shared/ui/cards';
import { Typography, Stack, Box } from '@mui/material';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

<CompactCard color="cyan" compact>
  <Stack spacing={0.5}>
    <Typography fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
      John "NetRunner" Doe
    </Typography>
    <Typography fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
      Level 15 Netrunner
    </Typography>
  </Stack>
</CompactCard>
```

### Пример 2: Кнопка действия

```typescript
import { CyberpunkButton } from '@/shared/ui/buttons';
import AttackIcon from '@mui/icons-material/GpsFixed';

<CyberpunkButton 
  variant="primary" 
  size="small"
  startIcon={<AttackIcon />}
  onClick={handleAttack}
>
  Attack
</CyberpunkButton>
```

### Пример 3: Статы персонажа

```typescript
import { HealthBar, ProgressBar } from '@/shared/ui/stats';

<Stack spacing={1}>
  <HealthBar current={75} max={100} label="HP" color="cyan" />
  <HealthBar current={60} max={100} label="Energy" color="green" />
  <ProgressBar value={65} label="XP to Level 16" color="yellow" />
</Stack>
```

---

## 🚀 Создание новых компонентов

### Шаблон компонента:

```typescript
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { Box } from '@mui/material';

export interface MyComponentProps {
  // Props
}

export function MyComponent({ ...props }: MyComponentProps) {
  return (
    <Box
      sx={{
        fontSize: cyberpunkTokens.fonts.sm,        // Маленький шрифт!
        color: cyberpunkTokens.colors.neonCyan,
        background: cyberpunkTokens.gradients.cardBg,
        boxShadow: cyberpunkTokens.effects.boxShadowCard,
        clipPath: cyberpunkTokens.clipPath.corner, // MMORPG скосы!
        transition: cyberpunkTokens.transitions.normal,
      }}
    >
      {/* Контент */}
    </Box>
  );
}
```

---

**Версия:** 1.0.0  
**Дата:** 2025-11-06  
**Компоненты:** Layout (5), Cards (1), Buttons (1), Stats (2) = **9 компонентов**

