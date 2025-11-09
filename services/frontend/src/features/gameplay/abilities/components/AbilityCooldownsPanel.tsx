import {
  Alert,
  Card,
  CardContent,
  CardHeader,
  CircularProgress,
  LinearProgress,
  Stack,
  Typography,
} from '@mui/material'
import { useGetAbilityCooldowns } from '@/api/generated/abilities/abilities/abilities'

type AbilityCooldownsPanelProps = {
  characterId?: string | null
}

export function AbilityCooldownsPanel({ characterId }: AbilityCooldownsPanelProps) {
  const {
    data,
    isLoading,
    error,
    refetch,
  } = useGetAbilityCooldowns(
    { character_id: characterId ?? '' },
    {
      query: {
        enabled: !!characterId,
        refetchInterval: 10_000,
        onSuccess: () => {
          // noop
        },
      },
    }
  )

  return (
    <Card variant="outlined">
      <CardHeader
        title="Кулдауны"
        subheader="Текущее состояние способностей"
        titleTypographyProps={{ fontSize: '0.95rem', fontWeight: 600 }}
        subheaderTypographyProps={{ fontSize: '0.75rem' }}
        action={
          <Typography
            component="button"
            onClick={() => refetch()}
            sx={{
              border: 'none',
              background: 'transparent',
              color: 'primary.main',
              fontSize: '0.7rem',
              cursor: 'pointer',
            }}
          >
            Обновить
          </Typography>
        }
      />
      <CardContent>
        {!characterId ? (
          <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
            Авторизуйтесь и выберите персонажа, чтобы отслеживать кулдауны.
          </Alert>
        ) : isLoading ? (
          <Stack direction="row" justifyContent="center" py={2}>
            <CircularProgress size={24} />
          </Stack>
        ) : error ? (
          <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
            Не удалось загрузить кулдауны
          </Alert>
        ) : !data?.cooldowns?.length ? (
          <Alert severity="success" sx={{ fontSize: '0.75rem' }}>
            Все способности готовы к использованию.
          </Alert>
        ) : (
          <Stack spacing={1.5}>
            {data.cooldowns.map((cooldown, index) => {
              const remaining = cooldown.remaining_time ?? 0
              const total = cooldown.total_time ?? 1
              const progress = total > 0 ? 100 - Math.min(100, (remaining / total) * 100) : 100

              return (
                <Stack key={`${cooldown.ability_id ?? index}`} spacing={0.5}>
                  <Stack direction="row" justifyContent="space-between" alignItems="center">
                    <Typography variant="subtitle2" fontSize="0.8rem" fontWeight={600}>
                      {cooldown.ability_id ?? 'Способность'}
                    </Typography>
                    <Typography
                      variant="caption"
                      fontSize="0.65rem"
                      color={cooldown.is_ready ? 'success.main' : 'text.secondary'}
                    >
                      {cooldown.is_ready ? 'Готово' : `${remaining.toFixed(1)} с`}
                    </Typography>
                  </Stack>
                  <LinearProgress
                    variant="determinate"
                    value={progress}
                    sx={{ height: 6, borderRadius: 999 }}
                    color={cooldown.is_ready ? 'success' : 'primary'}
                  />
                </Stack>
              )
            })}
          </Stack>
        )}
      </CardContent>
    </Card>
  )
}

import { Card, CardContent, CardHeader, LinearProgress, Stack, Typography } from '@mui/material'
import type { AbilityCooldown } from '@/api/generated/abilities/models'

type AbilityCooldownsPanelProps = {
  cooldowns?: AbilityCooldown[]
}

export const AbilityCooldownsPanel = ({ cooldowns }: AbilityCooldownsPanelProps) => {
  return (
    <Card variant="outlined">
      <CardHeader
        title="Кулдауны"
        titleTypographyProps={{ fontSize: '1rem', fontWeight: 600 }}
        subheader="Оставшееся время перезарядки"
        subheaderTypographyProps={{ fontSize: '0.75rem' }}
      />
      <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: 1.5 }}>
        {cooldowns && cooldowns.length > 0 ? (
          cooldowns.map((cooldown) => {
            const remaining = cooldown.remaining_time ?? 0
            const total = cooldown.total_time ?? 1
            const value = Math.min(100, Math.max(0, ((total - remaining) / total) * 100))
            return (
              <Stack key={cooldown.ability_id ?? Math.random()} spacing={0.5}>
                <Stack direction="row" justifyContent="space-between" alignItems="center">
                  <Typography variant="caption" fontWeight={600}>
                    {cooldown.ability_id ?? 'Способность'}
                  </Typography>
                  <Typography variant="caption" color={cooldown.is_ready ? 'success.main' : 'text.secondary'}>
                    {cooldown.is_ready ? 'Готово' : `${remaining.toFixed(1)}с`}
                  </Typography>
                </Stack>
                <LinearProgress
                  variant="determinate"
                  value={value}
                  color={cooldown.is_ready ? 'success' : 'primary'}
                  sx={{ height: 6, borderRadius: 1 }}
                />
              </Stack>
            )
          })
        ) : (
          <Typography variant="caption" color="text.secondary">
            Кулдауны отсутствуют
          </Typography>
        )}
      </CardContent>
    </Card>
  )
}

export default AbilityCooldownsPanel

