import { useEffect, useState } from 'react'
import { Box, Typography, Fade, Zoom } from '@mui/material'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'

/**
 * Пропсы для анимации успеха
 */
interface SuccessAnimationProps {
  /** Сообщение об успехе */
  message?: string
  /** Показывать ли анимацию */
  show: boolean
  /** Callback при завершении анимации */
  onComplete?: () => void
}

/**
 * Анимация успеха в киберпанк стиле
 * 
 * Показывает анимацию успешного выполнения операции
 */
export function SuccessAnimation({ message = 'Успешно!', show, onComplete }: SuccessAnimationProps) {
  const [visible, setVisible] = useState(false)

  useEffect(() => {
    if (show) {
      setVisible(true)
      // Автоматически скрываем через 1.2 секунды (более короткая анимация)
      const timer = setTimeout(() => {
        setVisible(false)
        // Вызываем callback после завершения fade out анимации
        setTimeout(() => {
          onComplete?.()
        }, 400) // Задержка для завершения fade out
      }, 1200)

      return () => clearTimeout(timer)
    }
  }, [show, onComplete])

  if (!show && !visible) return null

  return (
    <Fade in={visible} timeout={{ enter: 300, exit: 400 }}>
      <Box
        sx={{
          position: 'fixed',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          zIndex: 9999,
          pointerEvents: 'none',
        }}
      >
        <Zoom in={visible} timeout={{ enter: 400, exit: 300 }}>
          <Box
            sx={{
              bgcolor: 'rgba(26, 31, 58, 0.95)',
              border: '3px solid',
              borderColor: 'success.main',
              borderRadius: 2,
              p: 4,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              gap: 2,
              minWidth: 300,
              boxShadow: '0 8px 32px rgba(0, 0, 0, 0.8), 0 0 40px rgba(5, 255, 161, 0.4), inset 0 0 20px rgba(5, 255, 161, 0.1)',
              backdropFilter: 'blur(10px)',
            }}
          >
            {/* Иконка успеха с анимацией */}
            <Box
              sx={{
                position: 'relative',
              }}
            >
              <CheckCircleIcon
                sx={{
                  fontSize: 80,
                  color: 'success.main',
                  filter: 'drop-shadow(0 0 20px currentColor)',
                  animation: 'glow 1.2s ease-in-out 1',
                  '@keyframes glow': {
                    '0%, 100%': {
                      filter: 'drop-shadow(0 0 20px currentColor)',
                      transform: 'scale(1)',
                    },
                    '50%': {
                      filter: 'drop-shadow(0 0 30px currentColor) drop-shadow(0 0 40px currentColor)',
                      transform: 'scale(1.05)',
                    },
                  },
                }}
              />
            </Box>

            {/* Сообщение */}
            <Typography
              variant="h6"
              sx={{
                color: 'success.main',
                textShadow: '0 0 10px currentColor',
                fontWeight: 'bold',
                textTransform: 'uppercase',
                letterSpacing: '0.1em',
                textAlign: 'center',
                transition: 'opacity 0.4s ease-out',
              }}
            >
              {message}
            </Typography>
          </Box>
        </Zoom>
      </Box>
    </Fade>
  )
}

