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
import type { CharacterExperience } from '@/api/generated/progression/backend/models'

interface ExperienceOverviewProps {
  experience?: CharacterExperience
  onAwardExperience: (amount: number, source: string, multiplier?: number) => Promise<void>
  isMutating: boolean
  error?: unknown
}

export const ExperienceOverview = ({
  experience,
  onAwardExperience,
  isMutating,
  error,
}: ExperienceOverviewProps) => {
  const [amount, setAmount] = useState(500)
  const [source, setSource] = useState('quest_completion')
  const [multiplier, setMultiplier] = useState(1)

  const handleAward = async () => {
    if (amount <= 0) {
      return
    }
    await onAwardExperience(amount, source, multiplier || 1)
  }

  return (
    <Card variant="outlined">
      <CardContent>
        <Stack spacing={2}>
          <Typography variant="h6" fontSize="1rem" fontWeight={600}>
            Прогресс персонажа
          </Typography>
          {experience ? (
            <Grid container spacing={2}>
              <Grid item xs={6} md={3}>
                <Stack spacing={0.5}>
                  <Typography variant="caption" color="text.secondary">
                    Уровень
                  </Typography>
                  <Typography variant="body1" fontWeight={600}>
                    {experience.level ?? 0}
                  </Typography>
                </Stack>
              </Grid>
              <Grid item xs={6} md={3}>
                <Stack spacing={0.5}>
                  <Typography variant="caption" color="text.secondary">
                    Опыт
                  </Typography>
                  <Typography variant="body1" fontWeight={600}>
                    {experience.experience ?? 0} / {experience.experience_to_next_level ?? 0}
                  </Typography>
                </Stack>
              </Grid>
              <Grid item xs={6} md={3}>
                <Stack spacing={0.5}>
                  <Typography variant="caption" color="text.secondary">
                    Прогресс (%)
                  </Typography>
                  <Typography variant="body1" fontWeight={600}>
                    {experience.progress_to_next_level?.toFixed(1) ?? 0}
                  </Typography>
                </Stack>
              </Grid>
              <Grid item xs={6} md={3}>
                <Stack spacing={0.5}>
                  <Typography variant="caption" color="text.secondary">
                    Кап уровня
                  </Typography>
                  <Typography variant="body1" fontWeight={600}>
                    {experience.level_cap ?? 50}
                  </Typography>
                </Stack>
              </Grid>
            </Grid>
          ) : (
            <Typography variant="body2" color="text.secondary">
              Информация об опыте недоступна
            </Typography>
          )}

          <Stack spacing={1}>
            <Typography variant="subtitle2" fontWeight={600}>
              Выдать опыт
            </Typography>
            <TextField
              label="Количество"
              size="small"
              type="number"
              value={amount}
              onChange={event => setAmount(Number.parseInt(event.target.value, 10) || 0)}
              inputProps={{ min: 0 }}
            />
            <TextField
              label="Источник"
              size="small"
              value={source}
              onChange={event => setSource(event.target.value)}
            />
            <TextField
              label="Множитель"
              size="small"
              type="number"
              value={multiplier}
              onChange={event => setMultiplier(Number.parseFloat(event.target.value) || 1)}
              inputProps={{ step: 0.1, min: 0 }}
            />
            {error && (
              <Alert severity="error">Не удалось выдать опыт. Проверьте параметры запроса.</Alert>
            )}
            <Button variant="contained" size="small" onClick={handleAward} disabled={isMutating}>
              Выдать опыт
            </Button>
          </Stack>
        </Stack>
      </CardContent>
    </Card>
  )
}







