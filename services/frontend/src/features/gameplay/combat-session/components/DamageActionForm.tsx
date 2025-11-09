import { useState } from 'react'
import {
  Alert,
  Button,
  Card,
  CardContent,
  MenuItem,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { useApplyDamage } from '@/api/generated/gameplay/combat-session/combat-actions/combat-actions'
import type { DamageRequestDamageType } from '@/api/generated/gameplay/combat-session/models'

const DAMAGE_TYPES: DamageRequestDamageType[] = [
  'PHYSICAL',
  'ENERGY',
  'FIRE',
  'ICE',
  'ELECTRIC',
  'POISON',
  'PSYCHIC',
]

interface DamageActionFormProps {
  sessionId?: string
  onApplied?: () => void
}

export const DamageActionForm = ({ sessionId, onApplied }: DamageActionFormProps) => {
  const [attackerId, setAttackerId] = useState('')
  const [targetId, setTargetId] = useState('')
  const [amount, setAmount] = useState(50)
  const [damageType, setDamageType] = useState<DamageRequestDamageType>('PHYSICAL')
  const [isCritical, setIsCritical] = useState(false)
  const mutation = useApplyDamage()

  const handleSubmit = async () => {
    if (!sessionId) {
      return
    }
    await mutation.mutateAsync({
      session_id: sessionId,
      data: {
        attacker_id: attackerId || undefined,
        target_id: targetId || undefined,
        damage_amount: amount,
        damage_type: damageType,
        is_critical: isCritical,
      },
    })
    if (!mutation.error) {
      onApplied?.()
    }
  }

  return (
    <Card variant="outlined">
      <CardContent>
        <Stack spacing={1.5}>
          <Typography variant="h6" fontSize="1rem" fontWeight={600}>
            Нанести урон
          </Typography>
          <TextField
            label="Attacker ID"
            size="small"
            value={attackerId}
            onChange={event => setAttackerId(event.target.value)}
            helperText="Оставьте пустым, если нужно использовать активного участника"
          />
          <TextField
            label="Target ID"
            size="small"
            value={targetId}
            onChange={event => setTargetId(event.target.value)}
          />
          <TextField
            label="Damage Amount"
            type="number"
            size="small"
            value={amount}
            onChange={event => setAmount(Number.parseInt(event.target.value, 10) || 0)}
            inputProps={{ min: 0 }}
          />
          <TextField
            select
            label="Damage Type"
            size="small"
            value={damageType}
            onChange={event => setDamageType(event.target.value as DamageRequestDamageType)}
          >
            {DAMAGE_TYPES.map(type => (
              <MenuItem key={type} value={type}>
                {type}
              </MenuItem>
            ))}
          </TextField>
          <TextField
            select
            label="Critical Hit"
            size="small"
            value={isCritical ? 'YES' : 'NO'}
            onChange={event => setIsCritical(event.target.value === 'YES')}
          >
            <MenuItem value="NO">Нет</MenuItem>
            <MenuItem value="YES">Да</MenuItem>
          </TextField>

          {mutation.error && (
            <Alert severity="error">
              {(mutation.error as { message?: string })?.message ?? 'Не удалось применить урон'}
            </Alert>
          )}
          {mutation.isSuccess && (
            <Alert severity="success">Урон применён. HP цели обновятся после рефреша.</Alert>
          )}

          <Button
            variant="contained"
            color="primary"
            size="small"
            disabled={!sessionId || mutation.isPending}
            onClick={handleSubmit}
          >
            Нанести урон
          </Button>
        </Stack>
      </CardContent>
    </Card>
  )
}


