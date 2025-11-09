import {
  Alert,
  Card,
  CardContent,
  CardHeader,
  Chip,
  CircularProgress,
  Stack,
  Typography,
} from '@mui/material'
import { useGetAbilitySynergies } from '@/api/generated/abilities/abilities/abilities'

type AbilitySynergiesPanelProps = {
  characterId?: string | null
}

export function AbilitySynergiesPanel({ characterId }: AbilitySynergiesPanelProps) {
  const {
    data,
    isLoading,
    error,
  } = useGetAbilitySynergies(
    { character_id: characterId ?? '' },
    { query: { enabled: !!characterId } }
  )

  return (
    <Card variant="outlined">
      <CardHeader
        title="Синергии"
        subheader="Бонусы от комбинаций способностей"
        titleTypographyProps={{ fontSize: '0.95rem', fontWeight: 600 }}
        subheaderTypographyProps={{ fontSize: '0.75rem' }}
      />
      <CardContent>
        {!characterId ? (
          <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
            Выберите персонажа, чтобы увидеть синергии.
          </Alert>
        ) : isLoading ? (
          <Stack direction="row" justifyContent="center" py={2}>
            <CircularProgress size={24} />
          </Stack>
        ) : error ? (
          <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
            Не удалось загрузить синергии
          </Alert>
        ) : !data?.synergies?.length ? (
          <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
            Для текущего набора не найдено синергий.
          </Alert>
        ) : (
          <Stack spacing={1.5}>
            {data.synergies.map((synergy, index) => (
              <Stack key={index} spacing={0.5}>
                <Stack direction="row" alignItems="center" justifyContent="space-between">
                  <Typography variant="subtitle2" fontSize="0.8rem" fontWeight={600}>
                    {synergy.name ?? 'Без названия'}
                  </Typography>
                  {synergy.type && (
                    <Chip
                      size="small"
                      color="secondary"
                      label={synergy.type}
                      sx={{ fontSize: '0.65rem', textTransform: 'uppercase' }}
                    />
                  )}
                </Stack>
                {synergy.description && (
                  <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
                    {synergy.description}
                  </Typography>
                )}

                {synergy.required_abilities && synergy.required_abilities.length > 0 && (
                  <Typography variant="caption" color="primary" fontSize="0.65rem">
                    Требуются: {synergy.required_abilities.join(', ')}
                  </Typography>
                )}

                {synergy.bonus_effects && synergy.bonus_effects.length > 0 && (
                  <Stack spacing={0.25} sx={{ pl: 1.5 }}>
                    {synergy.bonus_effects.map((effect, effectIndex) => (
                      <Typography key={effectIndex} variant="caption" fontSize="0.65rem" color="success.main">
                        • {effect.description ?? 'Бонус'}
                      </Typography>
                    ))}
                  </Stack>
                )}
              </Stack>
            ))}
          </Stack>
        )}
      </CardContent>
    </Card>
  )
}

import { Card, CardContent, CardHeader, Chip, List, ListItem, Stack, Typography } from '@mui/material'
import type { AbilitySynergy } from '@/api/generated/abilities/models'

type AbilitySynergiesPanelProps = {
  synergies?: AbilitySynergy[]
}

const SYNERGY_LABELS: Record<string, string> = {
  set_bonus: 'Сет',
  brand_bonus: 'Бренд',
  class_bonus: 'Класс',
  combo_bonus: 'Комбо',
}

export const AbilitySynergiesPanel = ({ synergies }: AbilitySynergiesPanelProps) => {
  return (
    <Card variant="outlined">
      <CardHeader
        title="Синергии"
        titleTypographyProps={{ fontSize: '1rem', fontWeight: 600 }}
        subheader="Бонусы от комбинирования способностей"
        subheaderTypographyProps={{ fontSize: '0.75rem' }}
      />
      <CardContent sx={{ maxHeight: 280, overflowY: 'auto' }}>
        {synergies && synergies.length > 0 ? (
          <List dense disablePadding>
            {synergies.map((synergy, index) => (
              <ListItem
                key={`${synergy.name ?? 'synergy'}-${index}`}
                disableGutters
                sx={{ alignItems: 'flex-start', mb: 1 }}
              >
                <Stack spacing={0.5} sx={{ width: '100%' }}>
                  <Stack direction="row" spacing={1} alignItems="center">
                    <Typography variant="subtitle2" sx={{ fontSize: '0.85rem', fontWeight: 600 }}>
                      {synergy.name ?? 'Без названия'}
                    </Typography>
                    {synergy.type && (
                      <Chip
                        label={SYNERGY_LABELS[synergy.type] ?? synergy.type}
                        size="small"
                        color="primary"
                        sx={{ height: 18, fontSize: '0.65rem' }}
                      />
                    )}
                  </Stack>
                  {synergy.description && (
                    <Typography variant="caption" color="text.secondary">
                      {synergy.description}
                    </Typography>
                  )}
                  {synergy.required_abilities && synergy.required_abilities.length > 0 && (
                    <Typography variant="caption" color="text.secondary">
                      Требуются: {synergy.required_abilities.join(', ')}
                    </Typography>
                  )}
                  {synergy.bonus_effects && synergy.bonus_effects.length > 0 && (
                    <Typography variant="caption" color="success.main">
                      Бонусы:{' '}
                      {synergy.bonus_effects
                        .map((effect) => `${effect.effect_type ?? 'эффект'} +${effect.value ?? 0}`)
                        .join(', ')}
                    </Typography>
                  )}
                </Stack>
              </ListItem>
            ))}
          </List>
        ) : (
          <Typography variant="caption" color="text.secondary">
            Синергии не найдены
          </Typography>
        )}
      </CardContent>
    </Card>
  )
}

export default AbilitySynergiesPanel

