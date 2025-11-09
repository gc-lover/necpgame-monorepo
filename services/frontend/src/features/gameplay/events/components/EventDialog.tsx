import React, { useState } from 'react'
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Typography,
  Box,
  Stack,
  Chip,
  Alert,
  Divider,
  List,
  ListItem,
  ListItemText,
} from '@mui/material'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import CancelIcon from '@mui/icons-material/Cancel'
import {
  RandomEvent,
  EventOption,
  EventResult,
} from '@/api/generated/events/models'

interface EventDialogProps {
  open: boolean
  event: RandomEvent | null
  result: EventResult | null
  onClose: () => void
  onSelectOption: (optionId: string) => void
  isResponding?: boolean
}

/**
 * Диалог для отображения случайного события и его результатов
 * Соответствует OpenAPI спецификации для RandomEvent и EventResult
 */
export const EventDialog: React.FC<EventDialogProps> = ({
  open,
  event,
  result,
  onClose,
  onSelectOption,
  isResponding = false,
}) => {
  const [selectedOption, setSelectedOption] = useState<string | null>(null)

  const handleSelectOption = (optionId: string) => {
    setSelectedOption(optionId)
  }

  const handleConfirm = () => {
    if (selectedOption) {
      onSelectOption(selectedOption)
    }
  }

  const handleClose = () => {
    setSelectedOption(null)
    onClose()
  }

  // Если есть результат, показываем его
  if (result) {
    return (
      <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
        <DialogTitle sx={{ pb: 1 }}>
          <Box display="flex" alignItems="center" gap={1}>
            {result.success ? (
              <CheckCircleIcon color="success" />
            ) : (
              <CancelIcon color="error" />
            )}
            <Typography variant="h6" fontSize="1rem">
              {result.success ? 'Успех!' : 'Неудача'}
            </Typography>
          </Box>
        </DialogTitle>
        <DialogContent sx={{ pt: 1 }}>
          <Stack spacing={2}>
            {/* Описание результата */}
            <Alert severity={result.success ? 'success' : 'error'} sx={{ fontSize: '0.75rem' }}>
              {result.outcome}
            </Alert>

            {/* Награды */}
            {result.rewards && (
              <Box>
                <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem" mb={1}>
                  Награды:
                </Typography>
                <List dense disablePadding>
                  {result.rewards.experience && (
                    <ListItem disableGutters>
                      <ListItemText
                        primary={`+${result.rewards.experience} опыта`}
                        primaryTypographyProps={{ fontSize: '0.75rem' }}
                      />
                    </ListItem>
                  )}
                  {result.rewards.money && (
                    <ListItem disableGutters>
                      <ListItemText
                        primary={`+${result.rewards.money} эдди`}
                        primaryTypographyProps={{ fontSize: '0.75rem' }}
                      />
                    </ListItem>
                  )}
                  {result.rewards.reputation &&
                    Object.entries(result.rewards.reputation).map(([faction, value]) => (
                      <ListItem key={faction} disableGutters>
                        <ListItemText
                          primary={`${faction}: ${value > 0 ? '+' : ''}${value} репутации`}
                          primaryTypographyProps={{ fontSize: '0.75rem' }}
                        />
                      </ListItem>
                    ))}
                  {result.rewards.items && result.rewards.items.length > 0 && (
                    <ListItem disableGutters>
                      <ListItemText
                        primary={`Получено предметов: ${result.rewards.items.length}`}
                        primaryTypographyProps={{ fontSize: '0.75rem' }}
                      />
                    </ListItem>
                  )}
                </List>
              </Box>
            )}

            {/* Штрафы */}
            {result.penalties && (
              <Box>
                <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem" mb={1}>
                  Штрафы:
                </Typography>
                <List dense disablePadding>
                  {result.penalties.healthLoss && (
                    <ListItem disableGutters>
                      <ListItemText
                        primary={`-${result.penalties.healthLoss} здоровья`}
                        primaryTypographyProps={{ fontSize: '0.75rem', color: 'error.main' }}
                      />
                    </ListItem>
                  )}
                  {result.penalties.moneyLoss && (
                    <ListItem disableGutters>
                      <ListItemText
                        primary={`-${result.penalties.moneyLoss} эдди`}
                        primaryTypographyProps={{ fontSize: '0.75rem', color: 'error.main' }}
                      />
                    </ListItem>
                  )}
                  {result.penalties.humanityLoss && (
                    <ListItem disableGutters>
                      <ListItemText
                        primary={`-${result.penalties.humanityLoss} человечности`}
                        primaryTypographyProps={{ fontSize: '0.75rem', color: 'error.main' }}
                      />
                    </ListItem>
                  )}
                </List>
              </Box>
            )}
          </Stack>
        </DialogContent>
        <DialogActions sx={{ p: 2, pt: 0 }}>
          <Button onClick={handleClose} variant="contained" size="small" sx={{ fontSize: '0.75rem' }}>
            Закрыть
          </Button>
        </DialogActions>
      </Dialog>
    )
  }

  // Если нет события, не показываем диалог
  if (!event) return null

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
      <DialogTitle sx={{ pb: 1 }}>
        <Typography variant="h6" fontSize="1rem">
          {event.name}
        </Typography>
      </DialogTitle>
      <DialogContent sx={{ pt: 1 }}>
        <Stack spacing={2}>
          {/* Описание события */}
          <Typography variant="body2" fontSize="0.75rem">
            {event.description}
          </Typography>

          {/* Ограничение по времени */}
          {event.timeLimit && (
            <Alert severity="warning" sx={{ fontSize: '0.75rem' }}>
              ⏱ У вас есть {event.timeLimit} секунд на принятие решения!
            </Alert>
          )}

          <Divider />

          {/* Варианты действий */}
          <Box>
            <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem" mb={1}>
              Выберите действие:
            </Typography>
            <Stack spacing={1}>
              {event.options.map((option: EventOption) => (
                <Button
                  key={option.id}
                  variant={selectedOption === option.id ? 'contained' : 'outlined'}
                  onClick={() => handleSelectOption(option.id)}
                  disabled={!option.available || isResponding}
                  fullWidth
                  sx={{
                    justifyContent: 'flex-start',
                    textAlign: 'left',
                    fontSize: '0.75rem',
                    py: 1,
                  }}
                >
                  <Box width="100%">
                    <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
                      {option.text}
                    </Typography>
                    {option.requirements && !option.available && (
                      <Typography variant="caption" color="error" fontSize="0.7rem">
                        Требования не выполнены
                      </Typography>
                    )}
                    {option.requirements && option.available && (
                      <Box display="flex" gap={0.5} flexWrap="wrap" mt={0.5}>
                        {option.requirements.minStrength && (
                          <Chip
                            label={`Сила ${option.requirements.minStrength}+`}
                            size="small"
                            sx={{ height: 16, fontSize: '0.65rem' }}
                          />
                        )}
                        {option.requirements.minIntelligence && (
                          <Chip
                            label={`Интеллект ${option.requirements.minIntelligence}+`}
                            size="small"
                            sx={{ height: 16, fontSize: '0.65rem' }}
                          />
                        )}
                        {option.requirements.minReflex && (
                          <Chip
                            label={`Рефлекс ${option.requirements.minReflex}+`}
                            size="small"
                            sx={{ height: 16, fontSize: '0.65rem' }}
                          />
                        )}
                        {option.requirements.minTechnical && (
                          <Chip
                            label={`Техника ${option.requirements.minTechnical}+`}
                            size="small"
                            sx={{ height: 16, fontSize: '0.65rem' }}
                          />
                        )}
                        {option.requirements.requiredSkill && (
                          <Chip
                            label={option.requirements.requiredSkill}
                            size="small"
                            color="primary"
                            sx={{ height: 16, fontSize: '0.65rem' }}
                          />
                        )}
                      </Box>
                    )}
                  </Box>
                </Button>
              ))}
            </Stack>
          </Box>
        </Stack>
      </DialogContent>
      <DialogActions sx={{ p: 2, pt: 0 }}>
        <Button onClick={handleClose} size="small" sx={{ fontSize: '0.75rem' }}>
          Отмена
        </Button>
        <Button
          onClick={handleConfirm}
          variant="contained"
          disabled={!selectedOption || isResponding}
          size="small"
          sx={{ fontSize: '0.75rem' }}
        >
          {isResponding ? 'Загрузка...' : 'Подтвердить'}
        </Button>
      </DialogActions>
    </Dialog>
  )
}

