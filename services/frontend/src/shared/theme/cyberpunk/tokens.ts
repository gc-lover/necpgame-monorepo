/**
 * Cyberpunk Design Tokens
 * 
 * Все размеры, цвета, шрифты и эффекты для MMORPG UI
 * 
 * КРИТИЧНО:
 * - Размеры панелей: 380px (левая) | flex (центр) | 320px (правая)
 * - Шрифты: 0.65rem - 0.875rem (МАЛЕНЬКИЕ для компактности!)
 * - Всё помещается на 1 экран (height: 100vh)
 */

export const cyberpunkTokens = {
  /**
   * Размеры панелей (НЕ ТРОГАТЬ!)
   * Фиксированная 3-колоночная сетка MMORPG
   */
  sizes: {
    leftPanel: 380,          // Левая панель (меню, действия)
    rightPanel: 320,         // Правая панель (персонаж, статы)
    maxWidth: 1400,          // Макс ширина центрального контента
    headerHeight: 64,        // Высота header
  },

  /**
   * Шрифты (МАЛЕНЬКИЕ!)
   * Важно для компактности и 1 экрана
   */
  fonts: {
    // Размеры (компактные!)
    xs: '0.65rem',           // Очень мелкий текст (labels, badges)
    sm: '0.75rem',           // Обычный текст (основной размер)
    md: '0.875rem',          // Средний текст (заголовки карточек)
    lg: '1rem',              // Крупный текст (заголовки секций)
    xl: '1.25rem',           // Очень крупный (главные заголовки)
    
    // Семейства шрифтов
    primary: "'Rajdhani', sans-serif",           // Основной шрифт
    mono: "'Share Tech Mono', monospace",        // Монопространственный
  },

  /**
   * Цвета (Киберпанк палитра)
   */
  colors: {
    // Неоновые акценты
    neonCyan: '#00F7FF',       // Основной неоновый (primary)
    neonPink: '#ff2a6d',       // Розовый неон
    neonGreen: '#05ffa1',      // Зелёный неон (success)
    neonPurple: '#d817ff',     // Фиолетовый неон
    neonYellow: '#fef86c',     // Жёлтый неон (warning)
    
    // Фоны (тёмные)
    darkBg: '#0A0E27',         // Основной тёмный фон (rgba(10, 14, 39, 1))
    darkBg2: '#050812',        // Ещё темнее (rgba(5, 8, 18, 1))
    cardBg: '#1A1F3A',         // Фон карточек (rgba(26, 31, 58, 1))
    
    // Прозрачные версии
    darkBgAlpha: 'rgba(10, 14, 39, 0.95)',
    darkBg2Alpha: 'rgba(5, 8, 18, 0.98)',
    cardBgAlpha: 'rgba(26, 31, 58, 0.8)',
  },

  /**
   * Эффекты (неон, свечение, тени)
   */
  effects: {
    // Неоновое свечение
    neonGlow: '0 0 10px currentColor, 0 0 20px currentColor',
    neonGlowStrong: '0 0 8px currentColor, 0 0 15px currentColor',
    neonGlowWeak: '0 0 6px currentColor',
    
    // Тени для карточек
    boxShadowCard: '0 2px 8px rgba(0, 0, 0, 0.4), 0 1px 4px rgba(0, 0, 0, 0.3), inset 0 1px 1px rgba(255, 255, 255, 0.08), inset 0 -1px 1px rgba(0, 0, 0, 0.3)',
    boxShadowCardHover: '0 4px 12px rgba(0, 0, 0, 0.5), 0 2px 6px rgba(0, 0, 0, 0.4), 0 0 15px rgba(0, 247, 255, 0.12), inset 0 1px 1px rgba(255, 255, 255, 0.12), inset 0 -1px 1px rgba(0, 0, 0, 0.4)',
    
    // Тени для активных элементов
    boxShadowActive: '0 4px 12px rgba(0, 247, 255, 0.3), 0 2px 6px rgba(0, 0, 0, 0.5), inset 0 1px 2px rgba(0, 247, 255, 0.2), inset 0 -1px 2px rgba(0, 0, 0, 0.3)',
    boxShadowActiveHover: '0 6px 16px rgba(0, 247, 255, 0.4), 0 3px 8px rgba(0, 0, 0, 0.6), inset 0 1px 2px rgba(0, 247, 255, 0.3), inset 0 -1px 2px rgba(0, 0, 0, 0.4)',
    
    // Тени для панелей
    boxShadowPanelLeft: '4px 0 12px rgba(0, 0, 0, 0.5), inset -2px 0 4px rgba(0, 247, 255, 0.05)',
    boxShadowPanelRight: '-4px 0 12px rgba(0, 0, 0, 0.5), inset 2px 0 4px rgba(0, 247, 255, 0.05)',
    
    // Backdrop blur
    backdropBlur: 'blur(10px)',
  },

  /**
   * Градиенты (для глубины и стиля)
   */
  gradients: {
    // Фоны панелей
    panelBg: 'linear-gradient(180deg, rgba(10, 14, 39, 0.95) 0%, rgba(5, 8, 18, 0.98) 100%)',
    
    // Фоны карточек
    cardBg: 'linear-gradient(135deg, rgba(26, 31, 58, 0.9) 0%, rgba(10, 14, 39, 0.95) 100%)',
    
    // Активная кнопка
    activeButton: 'linear-gradient(135deg, rgba(0, 247, 255, 0.2) 0%, rgba(0, 247, 255, 0.05) 50%, rgba(10, 14, 39, 0.8) 100%)',
    
    // Обычная кнопка
    normalButton: 'linear-gradient(135deg, rgba(26, 31, 58, 0.8) 0%, rgba(10, 14, 39, 0.9) 100%)',
    
    // Глубина (для наложения поверх)
    depth: 'linear-gradient(135deg, rgba(255, 255, 255, 0.04) 0%, transparent 50%, rgba(0, 0, 0, 0.2) 100%)',
    depthActive: 'linear-gradient(135deg, rgba(0, 247, 255, 0.25) 0%, transparent 50%, rgba(0, 0, 0, 0.2) 100%)',
  },

  /**
   * MMORPG стиль - скошенные углы (clipPath)
   */
  clipPath: {
    corner: 'polygon(0 0, calc(100% - 8px) 0, 100% 8px, 100% 100%, 8px 100%, 0 calc(100% - 8px))',
    cornerSmall: 'polygon(0 0, calc(100% - 4px) 0, 100% 4px, 100% 100%, 4px 100%, 0 calc(100% - 4px))',
  },

  /**
   * Spacing (отступы)
   * Используется для padding, margin, gap
   */
  spacing: {
    xs: 4,    // 0.25rem
    sm: 8,    // 0.5rem
    md: 12,   // 0.75rem
    lg: 16,   // 1rem
    xl: 24,   // 1.5rem
    xxl: 32,  // 2rem
  },

  /**
   * Border radius (скругления)
   */
  borderRadius: {
    none: 0,
    sm: 3,
    md: 4,
    lg: 6,
  },

  /**
   * Transitions (анимации)
   */
  transitions: {
    fast: '0.15s cubic-bezier(0.4, 0, 0.2, 1)',
    normal: '0.3s cubic-bezier(0.4, 0, 0.2, 1)',
    slow: '0.5s cubic-bezier(0.4, 0, 0.2, 1)',
  },

  /**
   * Z-index слои
   */
  zIndex: {
    base: 1,
    dropdown: 1000,
    sticky: 1100,
    fixed: 1200,
    modal: 1300,
    popover: 1400,
    tooltip: 1500,
  },
} as const;

/**
 * Типы для TypeScript
 */
export type CyberpunkTokens = typeof cyberpunkTokens;
export type ColorName = keyof typeof cyberpunkTokens.colors;
export type FontSize = keyof typeof cyberpunkTokens.fonts;

