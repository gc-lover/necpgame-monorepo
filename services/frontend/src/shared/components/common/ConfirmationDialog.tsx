import { Dialog, DialogTitle, DialogContent, DialogActions, Button, Typography, Stack } from '@mui/material'
import WarningIcon from '@mui/icons-material/Warning'

/**
 * Пропсы для диалога подтверждения
 */
interface ConfirmationDialogProps {
  /** Показывать ли диалог */
  open: boolean
  /** Заголовок диалога */
  title?: string
  /** Сообщение для подтверждения */
  message: string
  /** Текст кнопки подтверждения */
  confirmText?: string
  /** Текст кнопки отмены */
  cancelText?: string
  /** Callback при подтверждении */
  onConfirm: () => void
  /** Callback при отмене */
  onCancel: () => void
  /** Цвет кнопки подтверждения (danger для удаления) */
  confirmColor?: 'primary' | 'error' | 'warning' | 'success'
}

/**
 * Диалог подтверждения в киберпанк стиле
 *
 * Используется для подтверждения важных действий (удаление, выход и т.д.)
 */
export function ConfirmationDialog({
  open,
  title = 'Подтвердите действие',
  message,
  confirmText = 'Подтвердить',
  cancelText = 'Отмена',
  onConfirm,
  onCancel,
  confirmColor = 'error',
}: ConfirmationDialogProps) {
  // Вычисляем цвета теней для кнопки подтверждения
  const shadowColor =
    confirmColor === 'error'
      ? 'rgba(211, 47, 47, 0.4)'
      : confirmColor === 'warning'
      ? 'rgba(255, 193, 7, 0.4)'
      : 'rgba(0, 247, 255, 0.4)'
  const hoverShadowColor =
    confirmColor === 'error'
      ? 'rgba(211, 47, 47, 0.5)'
      : confirmColor === 'warning'
      ? 'rgba(255, 193, 7, 0.5)'
      : 'rgba(0, 247, 255, 0.5)'

  return (
    <Dialog
      open={open}
      onClose={onCancel}
      maxWidth="sm"
      fullWidth
      PaperProps={{
        sx: {
          bgcolor: 'rgba(26, 31, 58, 0.98)',
          background: 'linear-gradient(135deg, rgba(26, 31, 58, 0.98) 0%, rgba(10, 14, 39, 0.98) 100%)',
          border: '2px solid',
          borderColor: confirmColor === 'error' ? 'error.main' : 'primary.main',
          boxShadow: `0 8px 32px rgba(0, 0, 0, 0.8), 0 0 40px ${
            confirmColor === 'error' ? 'rgba(211, 47, 47, 0.4)' : 'rgba(0, 247, 255, 0.4)'
          }, inset 0 0 20px ${confirmColor === 'error' ? 'rgba(211, 47, 47, 0.1)' : 'rgba(0, 247, 255, 0.1)'}`,
          backdropFilter: 'blur(10px)',
          borderRadius: 2,
        },
      }}
      BackdropProps={{
        sx: {
          bgcolor: 'rgba(0, 0, 0, 0.7)',
          backdropFilter: 'blur(4px)',
        },
      }}
    >
      <DialogTitle
        sx={{
          color: confirmColor === 'error' ? 'error.main' : 'primary.main',
          textShadow: `0 0 10px ${confirmColor === 'error' ? 'currentColor' : 'currentColor'}`,
          fontWeight: 'bold',
          textTransform: 'uppercase',
          letterSpacing: '0.1em',
          fontSize: '1rem',
          pb: 1,
        }}
      >
        {title}
      </DialogTitle>

      <DialogContent>
        <Stack direction="row" spacing={2} alignItems="flex-start">
          {/* Иконка предупреждения */}
          <WarningIcon
            sx={{
              color: confirmColor === 'error' ? 'error.main' : 'warning.main',
              fontSize: 40,
              mt: 0.5,
              filter: 'drop-shadow(0 0 10px currentColor)',
              flexShrink: 0,
            }}
          />

          {/* Сообщение */}
          <Typography
            variant="body1"
            sx={{
              color: 'text.primary',
              fontSize: '0.875rem',
              lineHeight: 1.6,
              flex: 1,
            }}
          >
            {message}
          </Typography>
        </Stack>
      </DialogContent>

      <DialogActions sx={{ px: 3, pb: 2.5, gap: 1.5 }}>
        <Button
          onClick={onCancel}
          variant="outlined"
          color="secondary"
          sx={{
            fontWeight: 'bold',
            textTransform: 'uppercase',
            border: '1px solid',
            borderColor: 'rgba(255, 255, 255, 0.2)',
            fontSize: '0.75rem',
            letterSpacing: '0.05em',
            py: 1,
            px: 2.5,
            clipPath: 'polygon(0 0, calc(100% - 8px) 0, 100% 8px, 100% 100%, 8px 100%, 0 calc(100% - 8px))',
            '&:hover': {
              borderColor: 'secondary.main',
              boxShadow: '0 4px 12px rgba(0, 0, 0, 0.5), 0 2px 6px rgba(0, 0, 0, 0.4)',
              transform: 'translateY(-1px)',
            },
          }}
        >
          {cancelText}
        </Button>
        <Button
          onClick={onConfirm}
          variant="contained"
          color={confirmColor}
          sx={{
            fontWeight: 'bold',
            textTransform: 'uppercase',
            border: '1px solid',
            borderColor: 'rgba(255, 255, 255, 0.2)',
            fontSize: '0.75rem',
            letterSpacing: '0.05em',
            py: 1,
            px: 2.5,
              clipPath: 'polygon(0 0, calc(100% - 8px) 0, 100% 8px, 100% 100%, 8px 100%, 0 calc(100% - 8px))',
              boxShadow: `0 3px 12px ${shadowColor}, 0 1px 6px rgba(0, 0, 0, 0.4), inset 0 1px 2px rgba(255, 255, 255, 0.15), inset 0 -1px 2px rgba(0, 0, 0, 0.3)`,
              '&:hover': {
                boxShadow: `0 5px 16px ${hoverShadowColor}, 0 2px 8px rgba(0, 0, 0, 0.5), inset 0 1px 2px rgba(255, 255, 255, 0.2), inset 0 -1px 2px rgba(0, 0, 0, 0.4)`,
              transform: 'translateY(-1px)',
            },
          }}
        >
          {confirmText}
        </Button>
      </DialogActions>
    </Dialog>
  )
}

