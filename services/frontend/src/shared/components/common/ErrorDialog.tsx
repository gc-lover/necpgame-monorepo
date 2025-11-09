import { Dialog, DialogTitle, DialogContent, DialogActions, Button, Typography, Stack } from '@mui/material'
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline'

/**
 * Пропсы для диалога ошибки
 */
interface ErrorDialogProps {
  /** Показывать ли диалог */
  open: boolean
  /** Заголовок диалога */
  title?: string
  /** Сообщение об ошибке */
  message: string
  /** Текст кнопки закрытия */
  buttonText?: string
  /** Callback при закрытии */
  onClose: () => void
}

/**
 * Диалог ошибки в киберпанк стиле
 *
 * Используется для отображения ошибок вместо стандартного alert
 */
export function ErrorDialog({
  open,
  title = 'Ошибка',
  message,
  buttonText = 'Закрыть',
  onClose,
}: ErrorDialogProps) {
  return (
    <Dialog
      open={open}
      onClose={onClose}
      maxWidth="sm"
      fullWidth
      PaperProps={{
        sx: {
          bgcolor: 'rgba(26, 31, 58, 0.98)',
          background: 'linear-gradient(135deg, rgba(26, 31, 58, 0.98) 0%, rgba(10, 14, 39, 0.98) 100%)',
          border: '2px solid',
          borderColor: 'error.main',
          boxShadow:
            '0 8px 32px rgba(0, 0, 0, 0.8), 0 0 40px rgba(211, 47, 47, 0.4), inset 0 0 20px rgba(211, 47, 47, 0.1)',
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
          color: 'error.main',
          textShadow: '0 0 10px currentColor',
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
          {/* Иконка ошибки */}
          <ErrorOutlineIcon
            sx={{
              color: 'error.main',
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

      <DialogActions sx={{ px: 3, pb: 2.5 }}>
        <Button
          onClick={onClose}
          variant="contained"
          color="error"
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
            boxShadow:
              '0 3px 12px rgba(211, 47, 47, 0.4), 0 1px 6px rgba(0, 0, 0, 0.4), inset 0 1px 2px rgba(255, 255, 255, 0.15), inset 0 -1px 2px rgba(0, 0, 0, 0.3)',
            '&:hover': {
              boxShadow:
                '0 5px 16px rgba(211, 47, 47, 0.5), 0 2px 8px rgba(0, 0, 0, 0.5), inset 0 1px 2px rgba(255, 255, 255, 0.2), inset 0 -1px 2px rgba(0, 0, 0, 0.4)',
              transform: 'translateY(-1px)',
            },
          }}
        >
          {buttonText}
        </Button>
      </DialogActions>
    </Dialog>
  )
}

