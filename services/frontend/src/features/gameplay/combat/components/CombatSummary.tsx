import { Card, CardContent, Typography, Stack, Chip, Divider } from '@mui/material'
import type { CombatResult, CombatState } from '@/api/generated/combat-system/models'

interface CombatSummaryProps {
  combatState?: CombatState
  combatResult?: CombatResult
}

const STATUS_LABELS: Record<CombatState['status'], string> = {
  active: 'Идёт бой',
  ended: 'Бой завершён',
  fled: 'Бой прерван',
}

export function CombatSummary({ combatState, combatResult }: CombatSummaryProps) {
  if (!combatState) {
    return (
      <Card variant="outlined">
        <CardContent sx={{ p: 2 }}>
          <Typography variant="caption" sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
            Создайте бой или введите ID, чтобы увидеть сводку.
          </Typography>
        </CardContent>
      </Card>
    )
  }

  const statusLabel = STATUS_LABELS[combatState.status] ?? combatState.status
  const isFinished = combatState.status !== 'active'

  return (
    <Card variant="outlined">
      <CardContent sx={{ p: 2 }}>
        <Stack spacing={1.5}>
          <Stack direction="row" spacing={1} alignItems="center">
            <Typography variant="subtitle2" sx={{ fontSize: '0.85rem', fontWeight: 600 }}>
              Состояние боя
            </Typography>
            <Chip
              label={statusLabel}
              size="small"
              color={combatState.status === 'active' ? 'primary' : 'success'}
              sx={{ height: 18, fontSize: '0.6rem' }}
            />
          </Stack>

          <Stack spacing={0.5}>
            <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
              Раунд: {combatState.round ?? 1}
            </Typography>
            <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
              Ход: {combatState.currentTurn}
            </Typography>
            <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
              ID боя: {combatState.id}
            </Typography>
          </Stack>

          {isFinished && combatResult && (
            <>
              <Divider />
              <Stack spacing={0.5}>
                <Typography variant="subtitle2" sx={{ fontSize: '0.8rem', fontWeight: 600 }}>
                  Результат
                </Typography>
                <Chip
                  label={combatResult.victory ? 'Победа' : 'Поражение'}
                  size="small"
                  color={combatResult.victory ? 'success' : 'error'}
                  sx={{ height: 18, fontSize: '0.65rem', width: 'fit-content' }}
                />
                {combatResult.rewards && (
                  <Stack spacing={0.3} sx={{ mt: 0.5 }}>
                    <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
                      Опыт: {combatResult.rewards.experience ?? 0}
                    </Typography>
                    <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
                      Валюта: {combatResult.rewards.currency ?? 0}
                    </Typography>
                    {combatResult.rewards.items && combatResult.rewards.items.length > 0 && (
                      <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
                        Предметы: {combatResult.rewards.items.join(', ')}
                      </Typography>
                    )}
                  </Stack>
                )}
                {combatResult.penalties && (
                  <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'error.main' }}>
                    Есть штрафы, проверьте логи боя.
                  </Typography>
                )}
              </Stack>
            </>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

