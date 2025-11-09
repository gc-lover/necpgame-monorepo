import { createTheme, ThemeOptions } from '@mui/material/styles'

/**
 * Киберпанк тема для Material UI
 */
export const cyberpunkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#00f7ff',
      light: '#33f9ff',
      dark: '#00b8c4',
      contrastText: '#000000',
    },
    secondary: {
      main: '#ff2a6d',
      light: '#ff5a8d',
      dark: '#cc2156',
      contrastText: '#ffffff',
    },
    error: {
      main: '#ff2a6d',
      light: '#ff5a8d',
      dark: '#cc2156',
    },
    warning: {
      main: '#fef86c',
      light: '#fffa99',
      dark: '#cac250',
    },
    info: {
      main: '#00f7ff',
      light: '#33f9ff',
      dark: '#00b8c4',
    },
    success: {
      main: '#05ffa1',
      light: '#37ffb3',
      dark: '#04cc81',
    },
    background: {
      default: '#050812',
      paper: '#1a1f3a',
    },
    text: {
      primary: '#ffffff',
      secondary: '#b8c5e0',
      disabled: '#7a8ab0',
    },
  },
  typography: {
    fontFamily: '"Orbitron", "Rajdhani", "Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontFamily: '"Orbitron", sans-serif',
      fontWeight: 900,
      letterSpacing: '0.1em',
    },
    h2: {
      fontFamily: '"Orbitron", sans-serif',
      fontWeight: 900,
      letterSpacing: '0.1em',
    },
    h3: {
      fontFamily: '"Orbitron", sans-serif',
      fontWeight: 700,
      letterSpacing: '0.05em',
    },
    h4: {
      fontFamily: '"Orbitron", sans-serif',
      fontWeight: 700,
      letterSpacing: '0.05em',
    },
    h5: {
      fontFamily: '"Orbitron", sans-serif',
      fontWeight: 600,
      letterSpacing: '0.05em',
    },
    h6: {
      fontFamily: '"Orbitron", sans-serif',
      fontWeight: 600,
      letterSpacing: '0.05em',
    },
    button: {
      fontFamily: '"Orbitron", sans-serif',
      fontWeight: 700,
      textTransform: 'uppercase',
      letterSpacing: '0.1em',
    },
  },
  shape: {
    borderRadius: 0, // Убираем скругления для киберпанк стиля
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 0,
          textTransform: 'uppercase',
          fontWeight: 700,
          letterSpacing: '0.1em',
          padding: '12px 24px',
          boxShadow: '0 4px 8px rgba(0, 0, 0, 0.3)',
          '&:hover': {
            boxShadow: '0 6px 12px rgba(0, 247, 255, 0.3)',
            transform: 'translateY(-2px)',
          },
        },
        contained: {
          background: 'linear-gradient(135deg, rgba(0, 247, 255, 0.4) 0%, rgba(0, 247, 255, 0.2) 100%)',
          border: '2px solid #00f7ff',
          '&:hover': {
            background: 'linear-gradient(135deg, rgba(0, 247, 255, 0.6) 0%, rgba(0, 247, 255, 0.4) 100%)',
          },
        },
        outlined: {
          border: '2px solid #00f7ff',
          color: '#00f7ff',
          '&:hover': {
            border: '2px solid #00f7ff',
            background: 'rgba(0, 247, 255, 0.1)',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          background: 'linear-gradient(135deg, rgba(26, 31, 58, 0.95) 0%, rgba(26, 31, 58, 0.85) 50%, rgba(10, 14, 39, 0.95) 100%)',
          border: '2px solid rgba(0, 247, 255, 0.4)',
          borderRadius: 0,
          boxShadow: '0 8px 16px rgba(0, 0, 0, 0.5), 0 4px 8px rgba(0, 0, 0, 0.3), 0 0 30px rgba(0, 247, 255, 0.08), inset 0 2px 4px rgba(0, 247, 255, 0.1)',
          clipPath: 'polygon(0 0, calc(100% - 15px) 0, 100% 15px, 100% 100%, 15px 100%, 0 calc(100% - 15px))',
          color: '#ffffff',
          '&:hover': {
            boxShadow: '0 12px 24px rgba(0, 0, 0, 0.6), 0 6px 12px rgba(0, 0, 0, 0.4), 0 0 40px rgba(0, 247, 255, 0.2)',
            transform: 'translateY(-4px) scale(1.01)',
          },
          transition: 'all 0.3s ease',
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          background: 'linear-gradient(135deg, rgba(26, 31, 58, 0.95) 0%, rgba(26, 31, 58, 0.85) 50%, rgba(10, 14, 39, 0.95) 100%)',
          border: '2px solid rgba(0, 247, 255, 0.4)',
          borderRadius: 0,
          color: '#ffffff',
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 0,
            background: 'rgba(26, 31, 58, 0.5)',
            color: '#ffffff',
            '& fieldset': {
              border: '2px solid rgba(0, 247, 255, 0.4)',
              borderRadius: 0,
            },
            '&:hover fieldset': {
              border: '2px solid rgba(0, 247, 255, 0.6)',
            },
            '&.Mui-focused fieldset': {
              border: '2px solid #00f7ff',
              boxShadow: '0 0 20px rgba(0, 247, 255, 0.3)',
            },
            '& .MuiInputBase-input': {
              color: '#ffffff',
              fontFamily: '"Share Tech Mono", monospace',
            },
          },
          '& .MuiInputLabel-root': {
            color: '#b8c5e0',
            '&.Mui-focused': {
              color: '#00f7ff',
            },
          },
        },
      },
    },
    MuiFormControl: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 0,
            background: 'rgba(26, 31, 58, 0.5)',
            color: '#ffffff',
            '& fieldset': {
              border: '2px solid rgba(0, 247, 255, 0.4)',
              borderRadius: 0,
            },
            '&:hover fieldset': {
              border: '2px solid rgba(0, 247, 255, 0.6)',
            },
            '&.Mui-focused fieldset': {
              border: '2px solid #00f7ff',
              boxShadow: '0 0 20px rgba(0, 247, 255, 0.3)',
            },
          },
          '& .MuiInputLabel-root': {
            color: '#b8c5e0',
            '&.Mui-focused': {
              color: '#00f7ff',
            },
          },
        },
      },
    },
  },
} as ThemeOptions)

