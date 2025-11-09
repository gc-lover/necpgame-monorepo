/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // Киберпанк цветовая схема
        cyber: {
          // Основные цвета
          dark: '#0a0e27',
          darker: '#050812',
          // Неоновые акценты
          neon: {
            cyan: '#00f7ff',
            pink: '#ff2a6d',
            purple: '#d817ff',
            blue: '#1b03a3',
            green: '#05ffa1',
            yellow: '#fef86c',
          },
          // UI цвета
          surface: {
            DEFAULT: '#1a1f3a',
            hover: '#252b4a',
            active: '#2d3456',
          },
          border: {
            DEFAULT: '#00f7ff40',
            hover: '#00f7ff80',
            bright: '#00f7ff',
          },
            text: {
              primary: '#ffffff',
              secondary: '#b8c5e0',
              muted: '#7a8ab0',
            },
        },
      },
      fontFamily: {
        'cyber': ['"Orbitron"', '"Rajdhani"', 'ui-sans-serif', 'system-ui', 'sans-serif'],
        'mono-cyber': ['"Share Tech Mono"', '"Courier New"', 'monospace'],
      },
      boxShadow: {
        'neon-cyan': '0 0 10px #00f7ff, 0 0 20px #00f7ff40',
        'neon-pink': '0 0 10px #ff2a6d, 0 0 20px #ff2a6d40',
        'neon-purple': '0 0 10px #d817ff, 0 0 20px #d817ff40',
        'neon-green': '0 0 10px #05ffa1, 0 0 20px #05ffa140',
      },
      animation: {
        'pulse-slow': 'pulse 3s cubic-bezier(0.4, 0, 0.6, 1) infinite',
        'glow': 'glow 2s ease-in-out infinite alternate',
        'scan': 'scan 8s linear infinite',
        'scale-up': 'scaleUp 0.2s ease-out',
      },
      keyframes: {
        glow: {
          '0%': { filter: 'brightness(1) drop-shadow(0 0 5px currentColor)' },
          '100%': { filter: 'brightness(1.2) drop-shadow(0 0 15px currentColor)' },
        },
        scan: {
          '0%, 100%': { transform: 'translateY(-100%)' },
          '50%': { transform: 'translateY(100%)' },
        },
        scaleUp: {
          '0%': { transform: 'scale(0.95)', opacity: '0' },
          '100%': { transform: 'scale(1)', opacity: '1' },
        },
      },
      backgroundImage: {
        'grid-pattern': 'linear-gradient(#00f7ff10 1px, transparent 1px), linear-gradient(90deg, #00f7ff10 1px, transparent 1px)',
      },
      backgroundSize: {
        'grid': '50px 50px',
      },
    },
  },
  plugins: [require('daisyui')],
  daisyui: {
    themes: [
      {
        cyberpunk: {
          "primary": "#00f7ff",
          "primary-content": "#000000",
          "secondary": "#ff2a6d",
          "secondary-content": "#ffffff",
          "accent": "#d817ff",
          "accent-content": "#ffffff",
          "neutral": "#1a1f3a",
          "neutral-content": "#ffffff",
          "base-100": "#050812",
          "base-200": "#0a0e27",
          "base-300": "#1a1f3a",
          "base-content": "#ffffff",
          "info": "#00f7ff",
          "info-content": "#000000",
          "success": "#05ffa1",
          "success-content": "#000000",
          "warning": "#fef86c",
          "warning-content": "#000000",
          "error": "#ff2a6d",
          "error-content": "#ffffff",
        },
      },
    ],
    darkTheme: "cyberpunk",
    base: true,
    styled: true,
    utils: true,
  },
}





