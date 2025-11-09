/**
 * Диалог с результатом действия
 * Данные из OpenAPI: результаты actions
 */
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Typography,
  Stack,
  Chip,
  Box,
  List,
  ListItem,
  ListItemText,
} from '@mui/material'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import ErrorIcon from '@mui/icons-material/Error'

interface ActionResultDialogProps {
  open: boolean
  onClose: () => void
  title: string
  success: boolean
  result?: {
    description?: string
    healthRestored?: number
    energyRestored?: number
    timePassed?: number
    pointsOfInterest?: string[]
    hiddenObjects?: string[]
    dataAccessed?: string[]
    reward?: any
  }
}

export function ActionResultDialog({ open, onClose, title, success, result }: ActionResultDialogProps) {
  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        {success ? (
          <CheckCircleIcon color="success" />
        ) : (
          <ErrorIcon color="error" />
        )}
        <Typography variant="h6" sx={{ fontSize: '0.95rem' }}>
          {title}
        </Typography>
      </DialogTitle>
      
      <DialogContent>
        <Stack spacing={2}>
          {result?.description && (
            <Typography variant="body2" sx={{ fontSize: '0.85rem' }}>
              {result.description}
            </Typography>
          )}

          {/* Восстановление здоровья/энергии */}
          {(result?.healthRestored || result?.energyRestored) && (
            <Box>
              <Typography variant="caption" sx={{ fontSize: '0.75rem', fontWeight: 'bold', display: 'block', mb: 0.5 }}>
                Восстановлено:
              </Typography>
              <Stack direction="row" spacing={1}>
                {result.healthRestored && (
                  <Chip
                    label={`+${result.healthRestored} HP`}
                    size="small"
                    color="error"
                    sx={{ fontSize: '0.7rem' }}
                  />
                )}
                {result.energyRestored && (
                  <Chip
                    label={`+${result.energyRestored} Energy`}
                    size="small"
                    color="primary"
                    sx={{ fontSize: '0.7rem' }}
                  />
                )}
              </Stack>
            </Box>
          )}

          {/* Время */}
          {result?.timePassed && (
            <Typography variant="caption" sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
              Прошло времени: {result.timePassed} минут
            </Typography>
          )}

          {/* Точки интереса */}
          {result?.pointsOfInterest && result.pointsOfInterest.length > 0 && (
            <Box>
              <Typography variant="caption" sx={{ fontSize: '0.75rem', fontWeight: 'bold', display: 'block', mb: 0.5 }}>
                Точки интереса:
              </Typography>
              <List dense>
                {result.pointsOfInterest.map((poi, index) => (
                  <ListItem key={index} sx={{ py: 0.3, px: 0 }}>
                    <ListItemText
                      primary={poi}
                      primaryTypographyProps={{ fontSize: '0.75rem' }}
                    />
                  </ListItem>
                ))}
              </List>
            </Box>
          )}

          {/* Скрытые объекты */}
          {result?.hiddenObjects && result.hiddenObjects.length > 0 && (
            <Box>
              <Typography variant="caption" sx={{ fontSize: '0.75rem', fontWeight: 'bold', color: 'warning.main', display: 'block', mb: 0.5 }}>
                Найдено:
              </Typography>
              <List dense>
                {result.hiddenObjects.map((obj, index) => (
                  <ListItem key={index} sx={{ py: 0.3, px: 0 }}>
                    <ListItemText
                      primary={obj}
                      primaryTypographyProps={{ fontSize: '0.75rem', color: 'warning.main' }}
                    />
                  </ListItem>
                ))}
              </List>
            </Box>
          )}

          {/* Полученные данные после взлома */}
          {result?.dataAccessed && result.dataAccessed.length > 0 && (
            <Box>
              <Typography
                variant="caption"
                sx={{ fontSize: '0.75rem', fontWeight: 'bold', color: 'info.main', display: 'block', mb: 0.5 }}
              >
                Полученные данные:
              </Typography>
              <List dense>
                {result.dataAccessed.map((data, index) => (
                  <ListItem key={index} sx={{ py: 0.3, px: 0 }}>
                    <ListItemText
                      primary={data}
                      primaryTypographyProps={{ fontSize: '0.75rem', color: 'info.main' }}
                    />
                  </ListItem>
                ))}
              </List>
            </Box>
          )}

          {/* Награда */}
          {result?.reward && (
            <Chip
              label="Получена награда!"
              size="small"
              color="success"
              sx={{ fontSize: '0.7rem', alignSelf: 'flex-start' }}
            />
          )}
        </Stack>
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose} variant="contained" size="small" sx={{ fontSize: '0.75rem' }}>
          Закрыть
        </Button>
      </DialogActions>
    </Dialog>
  )
}

