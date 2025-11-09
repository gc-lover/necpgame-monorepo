import { Card, CardContent, Stack, Button, Typography, Chip, Tooltip } from '@mui/material'
import type { CombatAction } from '@/api/generated/combat-system/models'
import type { PerformCombatActionBodyActionType } from '@/api/generated/combat-system/models'

interface CombatActionsListProps {
  actions: CombatAction[]
  selectedActionType?: PerformCombatActionBodyActionType | ''
  onSelect: (actionType: PerformCombatActionBodyActionType) => void
}

export function CombatActionsList({ actions, selectedActionType, onSelect }: CombatActionsListProps) {
  if (!actions.length) {
    return (
      <Card variant="outlined">
        <CardContent sx={{ p: 2 }}>
          <Typography variant="caption" sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
            Доступные действия отсутствуют.
          </Typography>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card variant="outlined">
      <CardContent sx={{ p: 2 }}>
        <Typography variant="subtitle2" sx={{ fontSize: '0.85rem', fontWeight: 600, mb: 1 }}>
          Доступные действия
        </Typography>
        <Stack spacing={1}>
          {actions.map((action) => {
            const isSelected = selectedActionType === action.type

            return (
              <Tooltip key={action.id} title={action.description ?? ''} placement="top">
                <Button
                  variant={isSelected ? 'contained' : 'outlined'}
                  size="small"
                  color={action.available ? 'primary' : 'inherit'}
                  disabled={!action.available}
                  onClick={() => onSelect(action.type)}
                  sx={{
                    justifyContent: 'space-between',
                    fontSize: '0.75rem',
                    textTransform: 'none',
                  }}
                >
                  <Typography component="span" sx={{ fontSize: '0.75rem', fontWeight: 600 }}>
                    {action.name}
                  </Typography>
                  <Stack direction="row" spacing={0.5}>
                    <Chip
                      label={action.type}
                      size="small"
                      color={isSelected ? 'secondary' : 'default'}
                      sx={{ height: 18, fontSize: '0.6rem' }}
                    />
                    {action.cost !== undefined && action.cost !== null && (
                      <Chip label={`Стоимость: ${action.cost}`} size="small" sx={{ height: 18, fontSize: '0.6rem' }} />
                    )}
                    {action.damage !== undefined && action.damage !== null && (
                      <Chip label={`Урон: ${action.damage}`} size="small" sx={{ height: 18, fontSize: '0.6rem' }} />
                    )}
                  </Stack>
                </Button>
              </Tooltip>
            )
          })}
        </Stack>
      </CardContent>
    </Card>
  )
}

