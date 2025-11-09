/**
 * Cyberpunk MUI Theme
 * 
 * Material UI тема с киберпанк стилем на основе Design Tokens
 * 
 * Использует маленькие шрифты для компактности!
 */

import { createTheme, ThemeOptions } from '@mui/material/styles';
import { cyberpunkTokens } from './tokens';

/**
 * Создание темы с киберпанк стилем
 */
export const cyberpunkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: cyberpunkTokens.colors.neonCyan,
      light: '#33F8FF',
      dark: '#00C4CC',
    },
    secondary: {
      main: cyberpunkTokens.colors.neonPink,
      light: '#FF5789',
      dark: '#CC1E56',
    },
    success: {
      main: cyberpunkTokens.colors.neonGreen,
      light: '#37FFB4',
      dark: '#04CC81',
    },
    warning: {
      main: cyberpunkTokens.colors.neonYellow,
      light: '#FEF986',
      dark: '#CBBB56',
    },
    error: {
      main: '#FF0055',
      light: '#FF3377',
      dark: '#CC0044',
    },
    info: {
      main: cyberpunkTokens.colors.neonCyan,
    },
    background: {
      default: cyberpunkTokens.colors.darkBg,
      paper: cyberpunkTokens.colors.cardBg,
    },
    text: {
      primary: '#FFFFFF',
      secondary: 'rgba(255, 255, 255, 0.7)',
      disabled: 'rgba(255, 255, 255, 0.38)',
    },
    divider: 'rgba(255, 255, 255, 0.12)',
  },

  /**
   * Типография - МАЛЕНЬКИЕ шрифты!
   */
  typography: {
    fontFamily: cyberpunkTokens.fonts.primary,
    fontSize: 12, // Базовый размер (маленький!)
    
    // Заголовки
    h1: {
      fontSize: cyberpunkTokens.fonts.xl,
      fontWeight: 700,
      letterSpacing: '0.05em',
      textTransform: 'uppercase',
    },
    h2: {
      fontSize: cyberpunkTokens.fonts.lg,
      fontWeight: 700,
      letterSpacing: '0.05em',
      textTransform: 'uppercase',
    },
    h3: {
      fontSize: cyberpunkTokens.fonts.md,
      fontWeight: 700,
      letterSpacing: '0.05em',
      textTransform: 'uppercase',
    },
    h4: {
      fontSize: cyberpunkTokens.fonts.sm,
      fontWeight: 700,
      letterSpacing: '0.05em',
      textTransform: 'uppercase',
    },
    h5: {
      fontSize: cyberpunkTokens.fonts.sm,
      fontWeight: 600,
    },
    h6: {
      fontSize: cyberpunkTokens.fonts.xs,
      fontWeight: 600,
    },
    
    // Текст
    subtitle1: {
      fontSize: cyberpunkTokens.fonts.md,
      fontWeight: 600,
    },
    subtitle2: {
      fontSize: cyberpunkTokens.fonts.sm,
      fontWeight: 600,
    },
    body1: {
      fontSize: cyberpunkTokens.fonts.sm,
      fontWeight: 400,
    },
    body2: {
      fontSize: cyberpunkTokens.fonts.sm,
      fontWeight: 400,
    },
    caption: {
      fontSize: cyberpunkTokens.fonts.xs,
      fontWeight: 400,
    },
    button: {
      fontSize: cyberpunkTokens.fonts.sm,
      fontWeight: 700,
      textTransform: 'uppercase',
      letterSpacing: '0.05em',
    },
    overline: {
      fontSize: cyberpunkTokens.fonts.xs,
      fontWeight: 600,
      textTransform: 'uppercase',
      letterSpacing: '0.08em',
    },
  },

  /**
   * Компоненты - переопределения стилей
   */
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          scrollbarWidth: 'thin',
          scrollbarColor: `${cyberpunkTokens.colors.neonCyan} ${cyberpunkTokens.colors.darkBg}`,
          '&::-webkit-scrollbar': {
            width: '8px',
            height: '8px',
          },
          '&::-webkit-scrollbar-track': {
            background: cyberpunkTokens.colors.darkBg,
          },
          '&::-webkit-scrollbar-thumb': {
            background: cyberpunkTokens.colors.neonCyan,
            borderRadius: '4px',
            '&:hover': {
              background: '#33F8FF',
            },
          },
        },
      },
    },

    MuiButton: {
      styleOverrides: {
        root: {
          fontSize: cyberpunkTokens.fonts.sm,
          padding: '8px 16px',
          borderRadius: cyberpunkTokens.borderRadius.sm,
          transition: cyberpunkTokens.transitions.normal,
        },
        sizesmall: {
          fontSize: cyberpunkTokens.fonts.xs,
          padding: '4px 12px',
        },
        sizeMedium: {
          fontSize: cyberpunkTokens.fonts.sm,
          padding: '8px 16px',
        },
        sizeLarge: {
          fontSize: cyberpunkTokens.fonts.md,
          padding: '12px 24px',
        },
      },
    },

    MuiCard: {
      styleOverrides: {
        root: {
          backgroundImage: cyberpunkTokens.gradients.cardBg,
          border: `2px solid rgba(255, 255, 255, 0.1)`,
          boxShadow: cyberpunkTokens.effects.boxShadowCard,
          transition: cyberpunkTokens.transitions.normal,
          '&:hover': {
            boxShadow: cyberpunkTokens.effects.boxShadowCardHover,
          },
        },
      },
    },

    MuiChip: {
      styleOverrides: {
        root: {
          fontSize: cyberpunkTokens.fonts.xs,
          height: '20px',
        },
        sizeSmall: {
          fontSize: '0.6rem',
          height: '16px',
        },
      },
    },

    MuiTypography: {
      defaultProps: {
        variantMapping: {
          h1: 'h1',
          h2: 'h2',
          h3: 'h3',
          h4: 'h4',
          h5: 'h5',
          h6: 'h6',
          subtitle1: 'h6',
          subtitle2: 'h6',
          body1: 'p',
          body2: 'p',
        },
      },
    },

    MuiListItemText: {
      styleOverrides: {
        primary: {
          fontSize: cyberpunkTokens.fonts.sm,
        },
        secondary: {
          fontSize: cyberpunkTokens.fonts.xs,
        },
      },
    },

    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiInputBase-input': {
            fontSize: cyberpunkTokens.fonts.sm,
          },
          '& .MuiInputLabel-root': {
            fontSize: cyberpunkTokens.fonts.sm,
          },
        },
      },
    },

    MuiSelect: {
      styleOverrides: {
        select: {
          fontSize: cyberpunkTokens.fonts.sm,
        },
      },
    },

    MuiMenuItem: {
      styleOverrides: {
        root: {
          fontSize: cyberpunkTokens.fonts.sm,
        },
      },
    },
  },

  /**
   * Spacing - используем значения из tokens
   */
  spacing: 8, // Базовая единица (8px)
} as ThemeOptions);

export default cyberpunkTheme;

