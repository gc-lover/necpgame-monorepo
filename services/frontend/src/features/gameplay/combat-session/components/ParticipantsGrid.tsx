import { Card, CardContent, Chip, Grid, LinearProgress, Stack, Typography } from '@mui/material'
import type { Participant } from '@/api/generated/gameplay/combat-session/models'

interface ParticipantsGridProps {
  participants?: Participant[]
  activeParticipantId?: string | null
}

const getHpPercent = (participant: Participant) => {
  if (!participant.max_hp || participant.max_hp <= 0 || participant.hp == null) {
    return 0
  }

  return Math.max(0, Math.min(100, Math.round((participant.hp / participant.max_hp) * 100)))
}

export const ParticipantsGrid = ({
  participants = [],
  activeParticipantId,
}: ParticipantsGridProps) => {
  if (!participants.length) {
    return (
      <Card variant="outlined">
        <CardContent>
          <Typography variant="body2" color="text.secondary">
            Участники не найдены
          </Typography>
        </CardContent>
      </Card>
    )
  }

  return (
    <Grid container spacing={1}>
      {participants.map(participant => {
        const hpPercent = getHpPercent(participant)
        const isActive = participant.id === activeParticipantId

        return (
          <Grid item xs={12} md={6} key={`${participant.id}-${participant.team}`}>
            <Card
              variant="outlined"
              sx={theme => ({
                borderColor: isActive ? theme.palette.primary.main : undefined,
                backgroundColor: isActive ? theme.palette.action.hover : undefined,
              })}
            >
              <CardContent>
                <Stack spacing={1}>
                  <Stack direction="row" justifyContent="space-between" alignItems="center">
                    <Typography variant="subtitle2" fontWeight={600}>
                      {participant.character_name ?? participant.id ?? 'Неизвестный'}
                    </Typography>
                    <Stack direction="row" spacing={1}>
                      {participant.team && <Chip label={`Team ${participant.team}`} size="small" />}
                      {participant.type && <Chip label={participant.type} size="small" color="info" />}
                      {participant.status && (
                        <Chip
                          label={participant.status}
                          size="small"
                          color={participant.status === 'DEAD' ? 'error' : 'success'}
                        />
                      )}
                    </Stack>
                  </Stack>

                  <Stack spacing={0.5}>
                    <Stack direction="row" justifyContent="space-between">
                      <Typography variant="caption" color="text.secondary">
                        HP
                      </Typography>
                      <Typography variant="caption">
                        {participant.hp ?? 0}/{participant.max_hp ?? 0}
                      </Typography>
                    </Stack>
                    <LinearProgress
                      variant="determinate"
                      value={hpPercent}
                      color={hpPercent > 50 ? 'success' : hpPercent > 25 ? 'warning' : 'error'}
                    />
                  </Stack>

                  {participant.stats && (
                    <Stack direction="row" spacing={2} flexWrap="wrap">
                      <Typography variant="caption" color="text.secondary">
                        DMG:{' '}
                        <Typography component="span" variant="caption">
                          {participant.stats.damage_dealt ?? 0}
                        </Typography>
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        Taken:{' '}
                        <Typography component="span" variant="caption">
                          {participant.stats.damage_taken ?? 0}
                        </Typography>
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        Kills:{' '}
                        <Typography component="span" variant="caption">
                          {participant.stats.kills ?? 0}
                        </Typography>
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        Deaths:{' '}
                        <Typography component="span" variant="caption">
                          {participant.stats.deaths ?? 0}
                        </Typography>
                      </Typography>
                    </Stack>
                  )}

                  {!!participant.status_effects?.length && (
                    <Stack spacing={0.5}>
                      <Typography variant="caption" color="text.secondary">
                        Эффекты
                      </Typography>
                      <Stack direction="row" spacing={1} flexWrap="wrap">
                        {participant.status_effects.map(effect => (
                          <Chip
                            key={`${effect.effect_id}-${effect.type}`}
                            label={effect.name ?? effect.type}
                            size="small"
                            color={effect.type === 'BUFF' ? 'success' : 'warning'}
                          />
                        ))}
                      </Stack>
                    </Stack>
                  )}
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        )
      })}
    </Grid>
  )
}


