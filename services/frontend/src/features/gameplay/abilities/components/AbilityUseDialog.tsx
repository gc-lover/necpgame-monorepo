import { useEffect, useState } from 'react'
import {
  Alert,
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Grid,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { useUseAbility } from '@/api/generated/abilities/abilities/abilities'
import type { Ability, AbilityUseResult } from '@/api/generated/abilities/models'

type AbilityUseDialogProps = {
  open: boolean
  ability?: Ability | null
  characterId?: string | null
  onClose: () => void
}

const numberOrUndefined = (value: string) => {
  const parsed = Number(value)
  return Number.isFinite(parsed) ? parsed : undefined
}

export function AbilityUseDialog({ open, ability, characterId, onClose }: AbilityUseDialogProps) {
  const [targetId, setTargetId] = useState('')
  const [position, setPosition] = useState({ x: '', y: '', z: '' })
  const [result, setResult] = useState<AbilityUseResult | null>(null)

  const { mutate, isPending, error } = useUseAbility({
    mutation: {
      onSuccess: (data) => {
        setResult(data)
      },
    },
  })

  useEffect(() => {
    if (!open) {
      setTargetId('')
      setPosition({ x: '', y: '', z: '' })
      setResult(null)
    }
  }, [open])

  const handleSubmit = () => {
    if (!ability || !characterId) {
      return
    }

    mutate({
      data: {
        character_id: characterId,
        ability_id: ability.id,
        target_id: targetId.trim() || undefined,
        target_position:
          position.x || position.y || position.z
            ? {
                x: numberOrUndefined(position.x),
                y: numberOrUndefined(position.y),
                z: numberOrUndefined(position.z),
              }
            : undefined,
      },
    })
  }

  const disabled = !ability || !characterId || isPending

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle sx={{ fontSize: '1rem', fontWeight: 700 }}>
        Использование способности {ability?.name}
      </DialogTitle>
      <DialogContent dividers>
        <Stack spacing={2}>
          {!characterId && (
            <Alert severity="warning" sx={{ fontSize: '0.75rem' }}>
              Выберите персонажа, чтобы использовать способности.
            </Alert>
          )}

          <TextField
            label="ID цели"
            value={targetId}
            onChange={(event) => setTargetId(event.target.value)}
            size="small"
            fullWidth
            helperText="Опционально. Например, ID врага или союзника."
          />

          <Box>
            <Typography variant="caption" color="text.secondary">
              Позиция цели (опционально)
            </Typography>
            <Grid container spacing={1} mt={0.5}>
              {(['x', 'y', 'z'] as const).map((axis) => (
                <Grid key={axis} item xs={4}>
                  <TextField
                    label={axis.toUpperCase()}
                    value={position[axis]}
                    onChange={(event) =>
                      setPosition((prev) => ({
                        ...prev,
                        [axis]: event.target.value,
                      }))
                    }
                    size="small"
                    fullWidth
                    type="number"
                  />
                </Grid>
              ))}
            </Grid>
          </Box>

          {error && (
            <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
              Не удалось использовать способность: {error instanceof Error ? error.message : 'Неизвестная ошибка'}
            </Alert>
          )}

          {result && (
            <Alert severity={result.success ? 'success' : 'warning'} sx={{ fontSize: '0.75rem' }}>
              {result.success ? 'Способность успешно выполнена.' : 'Использование способности не удалось.'}
              <Box component="ul" sx={{ pl: 2, mt: 0.5, mb: 0, fontSize: '0.7rem' }}>
                {result.cooldown_started !== undefined && (
                  <li>Кулдаун: {result.cooldown_started.toFixed(1)}с</li>
                )}
                {result.energy_consumed !== undefined && <li>Потрачено энергии: {result.energy_consumed}</li>}
                {result.heat_generated !== undefined && <li>Перегрев системы: +{result.heat_generated}</li>}
                {result.cyberpsychosis_risk_increase !== undefined && (
                  <li>Риск киберпсихоза: +{result.cyberpsychosis_risk_increase}%</li>
                )}
                {result.effects_applied &&
                  result.effects_applied.map((effect, index) => (
                    <li key={index}>Эффект: {effect.description ?? 'Применен эффект'}</li>
                  ))}
              </Box>
            </Alert>
          )}
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} size="small" sx={{ fontSize: '0.75rem' }}>
          Закрыть
        </Button>
        <Button onClick={handleSubmit} disabled={disabled} size="small" variant="contained" sx={{ fontSize: '0.75rem' }}>
          {isPending ? 'Использование...' : 'Использовать'}
        </Button>
      </DialogActions>
    </Dialog>
  )
}

import { useEffect, useState } from 'react'
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import type { Ability } from '@/api/generated/abilities/models'

type AbilityUseDialogProps = {
  open: boolean
  ability?: Ability
  isSubmitting: boolean
  onClose: () => void
  onSubmit: (payload: { targetId?: string }) => void
}

export const AbilityUseDialog = ({
  open,
  ability,
  isSubmitting,
  onClose,
  onSubmit,
}: AbilityUseDialogProps) => {
  const [targetId, setTargetId] = useState('')

  useEffect(() => {
    if (open) {
      setTargetId('')
    }
  }, [open])

  const handleConfirm = () => {
    onSubmit({ targetId: targetId.trim() || undefined })
  }

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Использовать способность</DialogTitle>
      <DialogContent>
        <Stack spacing={2}>
          <Typography variant="body2" color="text.secondary">
            {ability ? ability.name : 'Не выбрана способность'}
          </Typography>
          <TextField
            label="ID цели"
            size="small"
            value={targetId}
            onChange={(event) => setTargetId(event.target.value)}
            helperText="Опционально. Укажите цель, если это необходимо."
          />
          <Box>
            <Typography variant="caption" color="text.secondary">
              Для навыков с позиционным таргетом используйте отдельные инструменты боевой системы.
            </Typography>
          </Box>
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={isSubmitting}>
          Отмена
        </Button>
        <Button variant="contained" onClick={handleConfirm} disabled={isSubmitting || !ability}>
          {isSubmitting ? 'Использование...' : 'Подтвердить'}
        </Button>
      </DialogActions>
    </Dialog>
  )
}

export default AbilityUseDialog

