import { useMemo, useState, type ChangeEvent, type FormEvent } from 'react'
import {
  Alert,
  Grid,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { CompactCard } from '@/shared/ui/cards/CompactCard'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { useUpdateCharacterStatus } from '@/api/generated/character-status/characters/characters'
import type { UpdateCharacterStatusBody } from '@/api/generated/character-status/models'

interface StatusAdjustmentCardProps {
  characterId: string
  onUpdated: () => void
}

type DeltaField = 'healthDelta' | 'energyDelta' | 'humanityDelta' | 'experienceDelta'

const fieldLabels: Record<DeltaField, string> = {
  healthDelta: 'Здоровье Δ',
  energyDelta: 'Энергия Δ',
  humanityDelta: 'Человечность Δ',
  experienceDelta: 'Опыт Δ',
}

export function StatusAdjustmentCard({ characterId, onUpdated }: StatusAdjustmentCardProps) {
  const updateMutation = useUpdateCharacterStatus()
  const [form, setForm] = useState<Record<DeltaField, string>>({
    healthDelta: '',
    energyDelta: '',
    humanityDelta: '',
    experienceDelta: '',
  })
  const [feedback, setFeedback] = useState<{ type: 'success' | 'error'; message: string } | null>(null)

  const hasAnyValue = useMemo(
    () => Object.values(form).some((value) => value.trim().length > 0),
    [form]
  )

  const handleChange = (field: DeltaField) => (event: ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value
    if (value === '' || /^-?\d{0,4}$/.test(value)) {
      setForm((prev) => ({ ...prev, [field]: value }))
    }
  }

  const handleReset = () => {
    setForm({
      healthDelta: '',
      energyDelta: '',
      humanityDelta: '',
      experienceDelta: '',
    })
  }

  const buildPayload = (): UpdateCharacterStatusBody => {
    const payload: UpdateCharacterStatusBody = {}
    ;(Object.keys(form) as DeltaField[]).forEach((field) => {
      const value = form[field].trim()
      if (value !== '') {
        const numeric = Number(value)
        if (!Number.isNaN(numeric)) {
          payload[field] = numeric
        }
      }
    })
    return payload
  }

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setFeedback(null)

    if (!hasAnyValue) {
      setFeedback({ type: 'error', message: 'Укажите изменения перед применением' })
      return
    }

    const payload = buildPayload()

    updateMutation.mutate(
      { characterId, data: payload },
      {
        onSuccess: () => {
          setFeedback({ type: 'success', message: 'Статус обновлён' })
          handleReset()
          onUpdated()
        },
        onError: (error) => {
          const axiosLike = error as { response?: { data?: unknown } }
          const apiMessage =
            axiosLike.response?.data && typeof axiosLike.response.data === 'object' && axiosLike.response.data !== null
              ? (axiosLike.response.data as Record<string, unknown>).message
              : undefined
          setFeedback({
            type: 'error',
            message: typeof apiMessage === 'string' ? apiMessage : 'Не удалось обновить статус',
          })
        },
      }
    )
  }

  return (
    <CompactCard color="green" glowIntensity="strong">
      <form onSubmit={handleSubmit}>
        <Stack spacing={2}>
          <BoxHeader />
          {feedback && (
            <Alert severity={feedback.type} variant="outlined" onClose={() => setFeedback(null)}>
              {feedback.message}
            </Alert>
          )}
          <Grid container spacing={1.5}>
            {(Object.keys(form) as DeltaField[]).map((field) => (
              <Grid key={field} item xs={6}>
                <TextField
                  label={fieldLabels[field]}
                  value={form[field]}
                  onChange={handleChange(field)}
                  size="small"
                  fullWidth
                  inputProps={{ inputMode: 'numeric', pattern: '-?[0-9]*', maxLength: 4 }}
                  placeholder="0"
                />
              </Grid>
            ))}
          </Grid>
          <Stack direction="row" spacing={1}>
            <CyberpunkButton
              type="submit"
              variant="primary"
              size="medium"
              disabled={updateMutation.isPending}
            >
              {updateMutation.isPending ? 'Применение...' : 'Применить'}
            </CyberpunkButton>
            <CyberpunkButton
              type="button"
              variant="outlined"
              size="medium"
              onClick={handleReset}
              disabled={!hasAnyValue || updateMutation.isPending}
            >
              Сбросить
            </CyberpunkButton>
          </Stack>
        </Stack>
      </form>
    </CompactCard>
  )
}

function BoxHeader() {
  return (
    <Stack spacing={0.5}>
      <Typography variant="h6" sx={{ fontSize: '0.95rem', fontWeight: 'bold', color: 'primary.main' }}>
        Быстрое изменение статуса
      </Typography>
      <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.7rem' }}>
        Введите дельты по характеристикам (положительные или отрицательные значения). Пустые поля будут
        проигнорированы.
      </Typography>
    </Stack>
  )
}

