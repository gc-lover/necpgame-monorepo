import { useState } from 'react'
import {
  Alert,
  Button,
  Card,
  CardContent,
  Grid,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import type { CharacterAttributes } from '@/api/generated/progression/backend/models'

type Distribution = Record<string, number>

interface AttributesPanelProps {
  attributes?: CharacterAttributes
  onSpendPoints: (distribution: Distribution) => Promise<void>
  isMutating: boolean
  error?: unknown
}

const ATTRIBUTE_LABELS: Record<string, string> = {
  BODY: 'Body',
  REFLEXES: 'Reflexes',
  TECHNICAL_ABILITY: 'Technical',
  INTELLIGENCE: 'Intelligence',
  COOL: 'Cool',
}

export const AttributesPanel = ({
  attributes,
  onSpendPoints,
  isMutating,
  error,
}: AttributesPanelProps) => {
  const [distribution, setDistribution] = useState<Distribution>({})

  const handleChange = (key: string, value: number) => {
    setDistribution(prev => ({ ...prev, [key]: value }))
  }

  const handleSubmit = async () => {
    const filtered = Object.fromEntries(
      Object.entries(distribution).filter(([, value]) => value && value > 0)
    )
    if (Object.keys(filtered).length === 0) {
      return
    }
    await onSpendPoints(filtered)
    setDistribution({})
  }

  const coreAttributes = attributes?.attributes ?? {}

  return (
    <Card variant="outlined">
      <CardContent>
        <Stack spacing={2}>
          <Typography variant="h6" fontSize="1rem" fontWeight={600}>
            Атрибуты
          </Typography>
          {attributes ? (
            <>
              <Typography variant="body2" color="text.secondary">
                Свободные очки: {attributes.unspent_points ?? 0}
              </Typography>
              <Grid container spacing={2}>
                {Object.entries(coreAttributes).map(([key, value]) => (
                  <Grid item xs={12} md={4} key={key}>
                    <Stack spacing={0.5}>
                      <Typography variant="subtitle2" fontWeight={600}>
                        {ATTRIBUTE_LABELS[key] ?? key}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        Текущее значение: {value?.value ?? 0} (база {value?.base_value ?? 0})
                      </Typography>
                      <TextField
                        label="Добавить"
                        size="small"
                        type="number"
                        value={distribution[key] ?? ''}
                        onChange={event =>
                          handleChange(key, Number.parseInt(event.target.value, 10) || 0)
                        }
                        inputProps={{ min: 0 }}
                      />
                    </Stack>
                  </Grid>
                ))}
              </Grid>
            </>
          ) : (
            <Typography variant="body2" color="text.secondary">
              Данные об атрибутах недоступны
            </Typography>
          )}

          {error && (
            <Alert severity="error">
              Не удалось распределить очки. Убедитесь, что значения не превышают доступный запас.
            </Alert>
          )}

          <Button variant="contained" size="small" onClick={handleSubmit} disabled={isMutating}>
            Распределить очки
          </Button>
        </Stack>
      </CardContent>
    </Card>
  )
}







