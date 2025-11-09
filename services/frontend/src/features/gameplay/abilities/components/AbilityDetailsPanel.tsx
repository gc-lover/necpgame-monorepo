import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Chip,
  CircularProgress,
  Divider,
  Stack,
  Typography,
} from '@mui/material'
import { useGetAbility } from '@/api/generated/abilities/abilities/abilities'
import type { Ability } from '@/api/generated/abilities/models'

type AbilityDetailsPanelProps = {
  abilityId?: string | null
  onUseAbility?: (ability: Ability) => void
}

export function AbilityDetailsPanel({ abilityId, onUseAbility }: AbilityDetailsPanelProps) {
  const {
    data,
    isLoading,
    error,
  } = useGetAbility(abilityId ?? '', {
    query: {
      enabled: !!abilityId,
    },
  })

  return (
    <Card variant="outlined" sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardHeader
        title="Детали способности"
        subheader={abilityId ? 'Полная информация по выбранной способности' : 'Выберите способность слева'}
        titleTypographyProps={{ fontSize: '0.95rem', fontWeight: 600 }}
        subheaderTypographyProps={{ fontSize: '0.75rem' }}
      />
      <CardContent sx={{ flex: 1, overflow: 'auto' }}>
        {!abilityId ? (
          <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
            Выберите способность, чтобы увидеть подробности.
          </Alert>
        ) : isLoading ? (
          <Box display="flex" justifyContent="center" py={4}>
            <CircularProgress size={28} />
          </Box>
        ) : error ? (
          <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
            Не удалось загрузить данные способности
          </Alert>
        ) : !data ? (
          <Alert severity="warning" sx={{ fontSize: '0.75rem' }}>
            Способность не найдена или недоступна
          </Alert>
        ) : (
          <Stack spacing={1.5}>
            <Stack direction="row" alignItems="center" spacing={1} flexWrap="wrap">
              <Typography variant="h6" fontSize="1rem" fontWeight={700}>
                {data.name}
              </Typography>
              <Chip label={data.type} size="small" sx={{ fontSize: '0.65rem', textTransform: 'uppercase' }} />
              <Chip label={`Слот ${data.slot}`} size="small" color="primary" sx={{ fontSize: '0.65rem' }} />
            </Stack>

            {data.description && (
              <Typography variant="body2" color="text.secondary" fontSize="0.75rem">
                {data.description}
              </Typography>
            )}

            <Divider flexItem />

            <Stack direction="row" spacing={2}>
              <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
                Источник: {data.source?.type ?? 'n/a'}
              </Typography>
              <Typography variant="caption" color="warning.main" fontSize="0.7rem">
                Стоимость: {data.cost?.amount ?? 0} {data.cost?.resource ?? 'energies'}
              </Typography>
              {data.cooldown !== undefined && (
                <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
                  Кулдаун: {data.cooldown}s
                </Typography>
              )}
            </Stack>

            {data.effects && data.effects.length > 0 && (
              <Stack spacing={0.5}>
                <Typography variant="subtitle2" fontSize="0.8rem" fontWeight={600}>
                  Эффекты
                </Typography>
                {data.effects.map((effect, index) => (
                  <Typography key={index} variant="caption" color="text.secondary" fontSize="0.7rem">
                    • {effect.description ?? 'Эффект'}
                  </Typography>
                ))}
              </Stack>
            )}

            {data.class_affinity && data.class_affinity.length > 0 && (
              <Stack spacing={0.5}>
                <Typography variant="subtitle2" fontSize="0.8rem" fontWeight={600}>
                  Оптимально для классов
                </Typography>
                <Stack direction="row" spacing={0.5} flexWrap="wrap">
                  {data.class_affinity.map((cls) => (
                    <Chip key={cls} label={cls} size="small" sx={{ fontSize: '0.65rem' }} />
                  ))}
                </Stack>
              </Stack>
            )}

            <Button
              variant="contained"
              size="small"
              onClick={() => onUseAbility?.(data)}
              sx={{ alignSelf: 'flex-start', fontSize: '0.75rem' }}
            >
              Использовать способность
            </Button>
          </Stack>
        )}
      </CardContent>
    </Card>
  )
}

import { Card, CardContent, CardHeader, Chip, Grid, Stack, Typography } from '@mui/material'
import type { Ability } from '@/api/generated/abilities/models'

type AbilityDetailsPanelProps = {
  ability?: Ability
  isLoading: boolean
  onUseClick: () => void
}

const TYPE_LABELS: Record<string, string> = {
  tactical: 'Тактическая',
  signature: 'Сигнатурная',
  ultimate: 'Ультимативная',
  passive: 'Пассивная',
  cyberdeck: 'Кибердека',
}

const SOURCE_LABELS: Record<string, string> = {
  equipment: 'Экипировка',
  implants: 'Импланты',
  skills: 'Навыки',
  cyberdeck: 'Кибердека',
}

export const AbilityDetailsPanel = ({ ability, isLoading, onUseClick }: AbilityDetailsPanelProps) => {
  if (isLoading) {
    return (
      <Card variant="outlined">
        <CardHeader title="Детали способности" />
        <CardContent>
          <Typography variant="body2" color="text.secondary">
            Загрузка...
          </Typography>
        </CardContent>
      </Card>
    )
  }

  if (!ability) {
    return (
      <Card variant="outlined">
        <CardHeader title="Детали способности" />
        <CardContent>
          <Typography variant="body2" color="text.secondary">
            Выберите способность для просмотра информации
          </Typography>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card variant="outlined">
      <CardHeader
        title={ability.name}
        subheader={TYPE_LABELS[ability.type] ?? ability.type}
        action={
          <Chip
            label="Использовать"
            color="primary"
            variant="filled"
            size="small"
            onClick={onUseClick}
            sx={{ cursor: 'pointer' }}
          />
        }
        titleTypographyProps={{ fontSize: '1.1rem', fontWeight: 600 }}
        subheaderTypographyProps={{ fontSize: '0.75rem' }}
      />
      <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: 1.5 }}>
        {ability.description && (
          <Typography variant="body2" color="text.secondary">
            {ability.description}
          </Typography>
        )}
        <Grid container spacing={1}>
          <Grid item xs={6}>
            <Typography variant="caption" color="text.secondary">
              Слот
            </Typography>
            <Typography variant="body2">{ability.slot.toUpperCase()}</Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography variant="caption" color="text.secondary">
              Источник
            </Typography>
            <Typography variant="body2">
              {SOURCE_LABELS[ability.source?.type ?? ''] ?? ability.source?.type ?? 'Неизвестно'}
            </Typography>
          </Grid>
        </Grid>
        <Stack spacing={0.5}>
          <Typography variant="caption" color="text.secondary">
            Стоимость
          </Typography>
          <Stack direction="row" spacing={1} flexWrap="wrap">
            {ability.cost?.energy !== undefined && (
              <Chip label={`Энергия: ${ability.cost.energy}`} size="small" color="primary" />
            )}
            {ability.cost?.health !== undefined && (
              <Chip label={`Здоровье: ${ability.cost.health}`} size="small" color="error" />
            )}
            {ability.cost?.charge !== undefined && (
              <Chip label={`Заряд: ${ability.cost.charge}`} size="small" color="secondary" />
            )}
            {ability.cost?.heat !== undefined && (
              <Chip label={`Перегрев: ${ability.cost.heat}`} size="small" color="warning" />
            )}
          </Stack>
        </Stack>
        <Grid container spacing={1}>
          <Grid item xs={6}>
            <Typography variant="caption" color="text.secondary">
              Кулдаун
            </Typography>
            <Typography variant="body2">{ability.cooldown ?? 0}с</Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography variant="caption" color="text.secondary">
              Дальность
            </Typography>
            <Typography variant="body2">{ability.range ?? 0}</Typography>
          </Grid>
        </Grid>
        {ability.class_affinity && ability.class_affinity.length > 0 && (
          <Stack spacing={0.5}>
            <Typography variant="caption" color="text.secondary">
              Оптимальные классы
            </Typography>
            <Stack direction="row" spacing={1} flexWrap="wrap">
              {ability.class_affinity.map((item) => (
                <Chip key={item} label={item} size="small" variant="outlined" />
              ))}
            </Stack>
          </Stack>
        )}
      </CardContent>
    </Card>
  )
}

export default AbilityDetailsPanel

