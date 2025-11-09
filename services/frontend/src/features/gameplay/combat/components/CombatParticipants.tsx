import { Card, CardContent, Typography, LinearProgress, Stack, Chip, Box } from '@mui/material'
import type { CombatParticipant } from '@/api/generated/combat-system/models'

interface CombatParticipantsProps {
  participants: CombatParticipant[]
  currentTurnId?: string
}

export function CombatParticipants({ participants, currentTurnId }: CombatParticipantsProps) {
  if (!participants.length) {
    return (
      <Card variant="outlined">
        <CardContent sx={{ p: 2 }}>
          <Typography variant="caption" sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
            Участники боя пока не загружены.
          </Typography>
        </CardContent>
      </Card>
    )
  }

  return (
    <Stack spacing={1.5}>
      {participants.map((participant) => {
        const hpPercent = Math.max(0, Math.min(100, (participant.health / participant.maxHealth) * 100))
        const isCurrentTurn = participant.id === currentTurnId

        return (
          <Card
            key={participant.id}
            variant="outlined"
            sx={{
              borderColor: isCurrentTurn ? 'primary.main' : participant.isAlive === false ? 'error.light' : 'divider',
              opacity: participant.isAlive === false ? 0.6 : 1,
              position: 'relative',
            }}
          >
            {isCurrentTurn && (
              <Chip
                label="Ход"
                color="primary"
                size="small"
                sx={{ position: 'absolute', top: 10, right: 10, height: 18, fontSize: '0.6rem' }}
              />
            )}
            <CardContent sx={{ p: 2, '&:last-child': { pb: 2 } }}>
              <Stack spacing={0.75}>
                <Box display="flex" justifyContent="space-between" alignItems="center">
                  <Typography variant="subtitle2" sx={{ fontSize: '0.9rem', fontWeight: 600 }}>
                    {participant.name}
                  </Typography>
                  <Chip
                    label={participant.type}
                    size="small"
                    color="secondary"
                    sx={{ height: 18, fontSize: '0.6rem', textTransform: 'uppercase' }}
                  />
                </Box>

                <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
                  HP: {participant.health}/{participant.maxHealth}
                </Typography>
                <LinearProgress
                  variant="determinate"
                  value={hpPercent}
                  color={hpPercent > 50 ? 'success' : hpPercent > 20 ? 'warning' : 'error'}
                  sx={{ height: 6, borderRadius: 4 }}
                />

                <Stack direction="row" spacing={1}>
                  {participant.energy !== undefined && participant.energy !== null && (
                    <Chip
                      label={`Энергия: ${participant.energy}`}
                      size="small"
                      sx={{ height: 18, fontSize: '0.6rem' }}
                    />
                  )}
                  {participant.armor !== undefined && (
                    <Chip
                      label={`Броня: ${participant.armor}`}
                      size="small"
                      sx={{ height: 18, fontSize: '0.6rem' }}
                    />
                  )}
                </Stack>
              </Stack>
            </CardContent>
          </Card>
        )
      })}
    </Stack>
  )
}

