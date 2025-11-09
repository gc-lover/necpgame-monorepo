import { useEffect, useMemo, useState } from 'react'
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  CircularProgress,
  FormControl,
  InputLabel,
  LinearProgress,
  MenuItem,
  Select,
  Stack,
  Typography,
} from '@mui/material'
import {
  useGetAbilityLoadout,
  useUpdateAbilityLoadout,
} from '@/api/generated/abilities/loadout/loadout'
import type { Ability, AbilityLoadout } from '@/api/generated/abilities/models'

type AbilityLoadoutPanelProps = {
  characterId?: string | null
  abilities: Ability[]
}

type SlotKey = 'q_slot' | 'e_slot' | 'r_slot'

const slotMeta: Record<SlotKey, { label: string; description: string }> = {
  q_slot: { label: 'Q', description: 'Тактическая способность (быстрое восстановление)' },
  e_slot: { label: 'E', description: 'Сигнатурная способность (бесплатная, но с кулдауном)' },
  r_slot: { label: 'R', description: 'Ультимативная способность (требует зарядки)' },
}

export function AbilityLoadoutPanel({ characterId, abilities }: AbilityLoadoutPanelProps) {
  const [draft, setDraft] = useState<AbilityLoadout | null>(null)

  const {
    data: loadout,
    isLoading,
    error,
    refetch,
  } = useGetAbilityLoadout(
    { character_id: characterId ?? '' },
    { query: { enabled: !!characterId } }
  )

  const updateMutation = useUpdateAbilityLoadout({
    mutation: {
      onSuccess: (updated) => {
        setDraft(updated)
      },
    },
  })

  useEffect(() => {
    if (loadout) {
      setDraft(loadout)
    }
  }, [loadout])

  const abilityOptionsBySlot = useMemo(() => {
    return {
      q_slot: abilities.filter((ability) => ability.slot === 'Q'),
      e_slot: abilities.filter((ability) => ability.slot === 'E'),
      r_slot: abilities.filter((ability) => ability.slot === 'R'),
      passive: abilities.filter((ability) => ability.slot === 'passive'),
      cyberdeck: abilities.filter((ability) => ability.slot === 'cyberdeck'),
    }
  }, [abilities])

  const handleSlotChange = (slot: SlotKey, value: string) => {
    setDraft((prev) =>
      prev
        ? {
            ...prev,
            [slot]: value === 'none' ? undefined : value,
          }
        : prev
    )
  }

  const handleMultiSlotChange = (slot: 'passive_slots' | 'cyberdeck_slots', value: string[]) => {
    setDraft((prev) =>
      prev
        ? {
            ...prev,
            [slot]: value,
          }
        : prev
    )
  }

  const handleSave = () => {
    if (!characterId || !draft) {
      return
    }

    updateMutation.mutate({
      data: {
        ...draft,
        character_id: characterId,
      },
    })
  }

  if (!characterId) {
    return (
      <Card variant="outlined">
        <CardContent>
          <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
            Выберите персонажа, чтобы управлять набором способностей.
          </Alert>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card variant="outlined" sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardHeader
        title="Набор способностей"
        subheader="Конфигурация Q/E/R и пассивов"
        titleTypographyProps={{ fontSize: '0.95rem', fontWeight: 600 }}
        subheaderTypographyProps={{ fontSize: '0.75rem' }}
        action={
          <Button
            size="small"
            variant="contained"
            onClick={handleSave}
            disabled={updateMutation.isPending || !draft}
            sx={{ fontSize: '0.7rem' }}
          >
            {updateMutation.isPending ? 'Сохранение...' : 'Сохранить'}
          </Button>
        }
      />

      <CardContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, flex: 1, overflow: 'auto' }}>
        {isLoading ? (
          <Box display="flex" justifyContent="center" py={4}>
            <CircularProgress size={28} />
          </Box>
        ) : error ? (
          <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
            Не удалось загрузить набор способностей
          </Alert>
        ) : (
          <>
            <Stack spacing={1.5}>
              {(['q_slot', 'e_slot', 'r_slot'] as SlotKey[]).map((slot) => (
                <FormControl key={slot} fullWidth size="small">
                  <InputLabel sx={{ fontSize: '0.75rem' }}>{slotMeta[slot].label}</InputLabel>
                  <Select
                    value={draft?.[slot] ?? 'none'}
                    label={slotMeta[slot].label}
                    onChange={(event) => handleSlotChange(slot, event.target.value)}
                    sx={{ fontSize: '0.75rem' }}
                  >
                    <MenuItem value="none">
                      <em>Пусто</em>
                    </MenuItem>
                    {abilityOptionsBySlot[slot].map((ability) => (
                      <MenuItem key={ability.id} value={ability.id} sx={{ fontSize: '0.75rem' }}>
                        {ability.name}
                      </MenuItem>
                    ))}
                  </Select>
                  <Typography variant="caption" color="text.secondary" fontSize="0.65rem">
                    {slotMeta[slot].description}
                  </Typography>
                </FormControl>
              ))}
            </Stack>

            <Stack spacing={1.5}>
              <FormControl fullWidth size="small">
                <InputLabel sx={{ fontSize: '0.75rem' }}>Пассивные способности</InputLabel>
                <Select
                  multiple
                  value={draft?.passive_slots ?? []}
                  label="Пассивные способности"
                  renderValue={(selected) =>
                    selected
                      .map(
                        (value) => abilityOptionsBySlot.passive.find((ability) => ability.id === value)?.name ?? value
                      )
                      .join(', ')
                  }
                  onChange={(event) =>
                    handleMultiSlotChange(
                      'passive_slots',
                      typeof event.target.value === 'string' ? event.target.value.split(',') : event.target.value
                    )
                  }
                  sx={{ fontSize: '0.75rem' }}
                >
                  {abilityOptionsBySlot.passive.map((ability) => (
                    <MenuItem key={ability.id} value={ability.id} sx={{ fontSize: '0.75rem' }}>
                      {ability.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>

              <FormControl fullWidth size="small">
                <InputLabel sx={{ fontSize: '0.75rem' }}>Кибердека</InputLabel>
                <Select
                  multiple
                  value={draft?.cyberdeck_slots ?? []}
                  label="Кибердека"
                  renderValue={(selected) =>
                    selected
                      .map(
                        (value) => abilityOptionsBySlot.cyberdeck.find((ability) => ability.id === value)?.name ?? value
                      )
                      .join(', ')
                  }
                  onChange={(event) =>
                    handleMultiSlotChange(
                      'cyberdeck_slots',
                      typeof event.target.value === 'string' ? event.target.value.split(',') : event.target.value
                    )
                  }
                  sx={{ fontSize: '0.75rem' }}
                >
                  {abilityOptionsBySlot.cyberdeck.map((ability) => (
                    <MenuItem key={ability.id} value={ability.id} sx={{ fontSize: '0.75rem' }}>
                      {ability.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Stack>

            <Box>
              <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
                Энергобюджет {draft?.energy_budget_used ?? 0}/{draft?.energy_budget_max ?? 0}
              </Typography>
              <LinearProgress
                variant="determinate"
                value={
                  draft?.energy_budget_max
                    ? Math.min(100, ((draft?.energy_budget_used ?? 0) / draft.energy_budget_max) * 100)
                    : 0
                }
                sx={{ height: 6, borderRadius: 999, mt: 0.5 }}
                color={
                  draft?.energy_budget_max && draft.energy_budget_used && draft.energy_budget_used > draft.energy_budget_max
                    ? 'error'
                    : 'primary'
                }
              />
              {draft?.heat_level !== undefined && (
                <Typography variant="caption" color="warning.main" fontSize="0.65rem" display="block" mt={0.5}>
                  Уровень перегрева: {draft.heat_level}%
                </Typography>
              )}
            </Box>

            {updateMutation.error && (
              <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
                Не удалось сохранить набор: {String(updateMutation.error)}
              </Alert>
            )}
          </>
        )}
      </CardContent>
    </Card>
  )
}

import { useEffect, useMemo, useState } from 'react'
import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Chip,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  SelectChangeEvent,
  Stack,
  Typography,
} from '@mui/material'
import type { Ability, AbilityLoadout } from '@/api/generated/abilities/models'

type AbilityLoadoutPanelProps = {
  characterId: string
  abilities: Ability[]
  loadout?: AbilityLoadout
  isSaving: boolean
  onSave: (payload: AbilityLoadout) => void
}

const SLOT_LABELS: Record<string, string> = {
  Q: 'Q',
  E: 'E',
  R: 'R',
  passive: 'Пассивные',
  cyberdeck: 'Кибердека',
}

const normalizeLoadout = (payload: AbilityLoadout | undefined): AbilityLoadout | undefined => {
  if (!payload) {
    return undefined
  }
  return {
    ...payload,
    passive_slots: [...(payload.passive_slots ?? [])].sort(),
    cyberdeck_slots: [...(payload.cyberdeck_slots ?? [])].sort(),
  }
}

export const AbilityLoadoutPanel = ({
  characterId,
  abilities,
  loadout,
  isSaving,
  onSave,
}: AbilityLoadoutPanelProps) => {
  const [draft, setDraft] = useState<AbilityLoadout>({
    character_id: characterId,
    passive_slots: [],
    cyberdeck_slots: [],
  })

  useEffect(() => {
    setDraft(
      normalizeLoadout(
        loadout ?? {
          character_id: characterId,
          passive_slots: [],
          cyberdeck_slots: [],
        }
      )!
    )
  }, [characterId, loadout])

  const slotOptions = useMemo(() => {
    return {
      Q: abilities.filter((ability) => ability.slot === 'Q'),
      E: abilities.filter((ability) => ability.slot === 'E'),
      R: abilities.filter((ability) => ability.slot === 'R'),
      passive: abilities.filter((ability) => ability.slot === 'passive'),
      cyberdeck: abilities.filter((ability) => ability.slot === 'cyberdeck'),
    }
  }, [abilities])

  const BASIC_SLOTS: Array<{ key: 'q_slot' | 'e_slot' | 'r_slot'; slot: 'Q' | 'E' | 'R' }> = [
    { key: 'q_slot', slot: 'Q' },
    { key: 'e_slot', slot: 'E' },
    { key: 'r_slot', slot: 'R' },
  ]

  const hasChanges =
    JSON.stringify(normalizeLoadout(draft)) !== JSON.stringify(normalizeLoadout(loadout))

  const handleBasicSlotChange = (slotKey: 'q_slot' | 'e_slot' | 'r_slot') => (value: string) => {
    setDraft((current) => ({
      ...current,
      [slotKey]: value || undefined,
    }))
  }

  const handleMultiSlotChange =
    (slotKey: 'passive_slots' | 'cyberdeck_slots') =>
    (event: SelectChangeEvent<string[]>) => {
      const value = event.target.value
      const nextValue = typeof value === 'string' ? value.split(',') : value
      setDraft((current) => ({
        ...current,
        [slotKey]: nextValue,
      }))
    }

  const renderMultiValue = (selected: string[]) => (
    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
      {selected.map((id) => {
        const ability = abilities.find((item) => item.id === id)
        return <Chip key={id} label={ability?.name ?? id} size="small" />
      })}
    </Box>
  )

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardHeader
        title="Loadout"
        titleTypographyProps={{ fontSize: '1rem', fontWeight: 600 }}
        subheader="Настройка Q/E/R и дополнительных слотов"
        subheaderTypographyProps={{ fontSize: '0.75rem' }}
      />
      <CardContent>
        <Stack spacing={2}>
          <Stack spacing={1}>
            {BASIC_SLOTS.map(({ key, slot }) => (
              <FormControl key={key} fullWidth size="small">
                <InputLabel>{SLOT_LABELS[slot]}</InputLabel>
                <Select
                  value={(draft as Record<string, string | undefined>)[key] ?? ''}
                  label={SLOT_LABELS[slot]}
                  onChange={(event) => handleBasicSlotChange(key)(event.target.value)}
                >
                  <MenuItem value="">
                    <em>Пусто</em>
                  </MenuItem>
                  {slotOptions[slot].map((ability) => (
                    <MenuItem key={ability.id} value={ability.id}>
                      {ability.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            ))}
          </Stack>

          <FormControl fullWidth size="small">
            <InputLabel>{SLOT_LABELS.passive}</InputLabel>
            <Select
              multiple
              value={draft.passive_slots ?? []}
              label={SLOT_LABELS.passive}
              onChange={handleMultiSlotChange('passive_slots')}
              renderValue={renderMultiValue}
            >
              {slotOptions.passive.map((ability) => (
                <MenuItem key={ability.id} value={ability.id}>
                  {ability.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <FormControl fullWidth size="small">
            <InputLabel>{SLOT_LABELS.cyberdeck}</InputLabel>
            <Select
              multiple
              value={draft.cyberdeck_slots ?? []}
              label={SLOT_LABELS.cyberdeck}
              onChange={handleMultiSlotChange('cyberdeck_slots')}
              renderValue={renderMultiValue}
            >
              {slotOptions.cyberdeck.map((ability) => (
                <MenuItem key={ability.id} value={ability.id}>
                  {ability.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <Stack spacing={0.5}>
            <Typography variant="caption" color="text.secondary">
              Энергия: {draft.energy_budget_used ?? 0} / {draft.energy_budget_max ?? 0}
            </Typography>
            <Typography variant="caption" color="text.secondary">
              Перегрев: {draft.heat_level ?? 0}
            </Typography>
          </Stack>

          <Button
            variant="contained"
            size="small"
            onClick={() => onSave(draft)}
            disabled={isSaving || !hasChanges}
          >
            {isSaving ? 'Сохранение...' : 'Сохранить loadout'}
          </Button>
        </Stack>
      </CardContent>
    </Card>
  )
}

export default AbilityLoadoutPanel

