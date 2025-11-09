import { ReactNode, useEffect } from 'react'
import { Box, Card, CardContent, Typography, IconButton, Divider } from '@mui/material'

/**
 * Пропсы компонента Modal
 */
export interface ModalProps {
  /** Открыто ли модальное окно */
  isOpen: boolean
  /** Callback при закрытии */
  onClose: () => void
  /** Заголовок */
  title?: string
  /** Дочерние элементы */
  children: ReactNode
  /** Размер модального окна */
  size?: 'sm' | 'md' | 'lg' | 'xl' | 'full'
  /** Показывать кнопку закрытия */
  showCloseButton?: boolean
  /** Закрывать при клике на backdrop */
  closeOnBackdropClick?: boolean
}

/**
 * Компонент модального окна в киберпанк стиле
 */
export function Modal({
  isOpen,
  onClose,
  title,
  children,
  size = 'md',
  showCloseButton = true,
  closeOnBackdropClick = true,
}: ModalProps) {
  // Блокируем скролл body когда модалка открыта
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden'
    } else {
      document.body.style.overflow = 'unset'
    }
    
    return () => {
      document.body.style.overflow = 'unset'
    }
  }, [isOpen])
  
  // Закрытие по ESC
  useEffect(() => {
    const handleEsc = (e: KeyboardEvent) => {
      if (e.key === 'Escape' && isOpen) {
        onClose()
      }
    }
    
    window.addEventListener('keydown', handleEsc)
    return () => window.removeEventListener('keydown', handleEsc)
  }, [isOpen, onClose])
  
  if (!isOpen) return null
  
  const sizeClasses = {
    sm: 'max-w-md',
    md: 'max-w-2xl',
    lg: 'max-w-4xl',
    xl: 'max-w-6xl',
    full: 'max-w-full mx-4',
  }
  
  return (
    <Box
      sx={{
        position: 'fixed',
        inset: 0,
        zIndex: 1300,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        p: 2,
      }}
    >
      {/* Backdrop - улучшенный фон с размытием */}
      <Box
        sx={{
          position: 'absolute',
          inset: 0,
          bgcolor: 'rgba(0, 0, 0, 0.9)',
          backdropFilter: 'blur(10px)',
          background: 'radial-gradient(circle at center, rgba(0, 0, 0, 0.95) 0%, rgba(0, 0, 0, 0.85) 100%)',
        }}
        onClick={closeOnBackdropClick ? onClose : undefined}
      />
      
      {/* Modal */}
      <Box
        sx={{
          position: 'relative',
          width: '100%',
          maxWidth: sizeClasses[size],
          maxHeight: '90vh',
          display: 'flex',
          flexDirection: 'column',
        }}
      >
        <Card
          sx={{
            overflow: 'hidden',
            display: 'flex',
            flexDirection: 'column',
            maxHeight: '100%',
            border: '2px solid',
            borderColor: 'primary.main',
            boxShadow: `
              0 0 20px rgba(0, 247, 255, 0.3),
              0 0 40px rgba(0, 247, 255, 0.2),
              0 0 60px rgba(0, 247, 255, 0.1),
              inset 0 0 20px rgba(0, 247, 255, 0.05)
            `,
            background: 'linear-gradient(135deg, rgba(26, 31, 58, 0.98) 0%, rgba(10, 14, 39, 0.98) 100%)',
            position: 'relative',
            animation: 'scaleUp 0.3s ease-out',
          }}
        >
          {/* Neon рамка сверху */}
          <Box
            sx={{
              position: 'absolute',
              top: 0,
              left: 0,
              right: 0,
              height: 4,
              background: 'linear-gradient(90deg, transparent 0%, #00f7ff 50%, transparent 100%)',
              boxShadow: '0 0 10px #00f7ff, 0 0 20px #00f7ff',
            }}
          />
          
          {/* Header */}
          {(title || showCloseButton) && (
            <Box
              sx={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                borderBottom: '2px solid',
                borderColor: 'divider',
                pb: 2,
                mb: 2,
                px: 3,
                pt: 3,
              }}
            >
              {title && (
                <Typography
                  variant="h5"
                  sx={{
                    color: 'primary.main',
                    textTransform: 'uppercase',
                    letterSpacing: '0.1em',
                    fontWeight: 'bold',
                    textShadow: '0 0 10px #00f7ff, 0 0 20px rgba(0, 247, 255, 0.25)',
                  }}
                >
                  {title}
                </Typography>
              )}
              {showCloseButton && (
                <IconButton
                  onClick={onClose}
                  sx={{
                    ml: 'auto',
                    color: 'primary.main',
                    '&:hover': {
                      color: 'secondary.main',
                    },
                    textShadow: '0 0 5px currentColor',
                  }}
                  aria-label="Закрыть"
                >
                  ✕
                </IconButton>
              )}
            </Box>
          )}
          
          {/* Content */}
          <Box sx={{ flex: 1, overflowY: 'auto', px: 3 }}>
            {children}
          </Box>
        </Card>
      </Box>
    </Box>
  )
}

/**
 * Подвал модального окна
 */
export function ModalFooter({ children, className = '' }: { children: ReactNode; className?: string }) {
  return (
    <Box
      sx={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        gap: 2,
        borderTop: '2px solid',
        borderColor: 'divider',
        pt: 2,
        mt: 2,
        px: 3,
        pb: 3,
      }}
      className={className}
    >
      {children}
    </Box>
  )
}

