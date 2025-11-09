import {
  Paper,
  Typography,
  List,
  ListItem,
  ListItemText,
  Stack,
  Chip,
  Box,
} from '@mui/material'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import BlockIcon from '@mui/icons-material/Block'
import type { LocationAction } from '@/api/generated/locations/models'

interface LocationActionsListProps {
  actions: LocationAction[]
}

const actionTypeLabels: Record<LocationAction['actionType'], string> = {
  exploration: 'Исследование',
  interaction: 'Взаимодействие',
  combat: 'Бой',
  trade: 'Торговля',
  quest: 'Квест',
  travel: 'Путешествие',
}

export function LocationActionsList({ actions }: LocationActionsListProps) {
  if (actions.length === 0) {
    return (
      <Paper elevation={1} sx={{ p: 2 }}>
        <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.75rem' }}>
          Доступные действия не найдены.
        </Typography>
      </Paper>
    )
  }

  return (
    <Paper elevation={2} sx={{ p: 1.5 }}>
      <Typography variant="subtitle2" sx={{ fontSize: '0.8rem', mb: 1, color: 'primary.main' }}>
        Доступные действия
      </Typography>

      <List dense sx={{ py: 0 }}>
        {actions.map((action) => (
          <ListItem
            key={action.id}
            sx={{
              mb: 0.8,
              px: 0,
              alignItems: 'flex-start',
            }}
          >
            <ListItemText
              primary={
                <Stack direction="row" spacing={0.8} alignItems="center">
                  {action.enabled ? (
                    <CheckCircleIcon color="success" sx={{ fontSize: '0.9rem' }} />
                  ) : (
                    <BlockIcon color="error" sx={{ fontSize: '0.9rem' }} />
                  )}
                  <Typography variant="body2" sx={{ fontSize: '0.85rem', fontWeight: 600 }}>
                    {action.label}
                  </Typography>
                  <Chip
                    size="small"
                    label={actionTypeLabels[action.actionType]}
                    color="secondary"
                    sx={{ height: 16, fontSize: '0.6rem' }}
                  />
                </Stack>
              }
              secondary={
                <Stack spacing={0.6} sx={{ mt: 0.5 }}>
                  <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
                    {action.description}
                  </Typography>

                  {!action.enabled && action.disabledReason && (
                    <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'error.main' }}>
                      Причина: {action.disabledReason}
                    </Typography>
                  )}

                  {!action.enabled && action.requirements && (
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 0.3 }}>
                      {action.requirements.minLevel && (
                        <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
                          - Требуемый уровень: {action.requirements.minLevel}
                        </Typography>
                      )}
                      {action.requirements.requiredSkills && action.requirements.requiredSkills.length > 0 && (
                        <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
                          - Навыки: {action.requirements.requiredSkills.join(', ')}
                        </Typography>
                      )}
                      {action.requirements.requiredItems && action.requirements.requiredItems.length > 0 && (
                        <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
                          - Предметы: {action.requirements.requiredItems.join(', ')}
                        </Typography>
                      )}
                      {action.requirements.requiredQuests && action.requirements.requiredQuests.length > 0 && (
                        <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
                          - Квесты: {action.requirements.requiredQuests.join(', ')}
                        </Typography>
                      )}
                    </Box>
                  )}
                </Stack>
              }
            />
          </ListItem>
        ))}
      </List>
    </Paper>
  )
}

