import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Alert,
  Box,
  Button,
  Divider,
  FormControlLabel,
  Grid,
  Paper,
  Stack,
  Switch,
  TextField,
  Typography,
} from '@mui/material'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useInitiateCombat,
  useGetCombatState,
  useGetAvailableActions,
  usePerformCombatAction,
  useFleeCombat,
  useGetCombatResult,
} from '@/api/generated/combat-system/combat/combat'
import type {
  CombatAction,
  CombatResult,
  CombatState,
  PerformCombatActionBodyActionType,
} from '@/api/generated/combat-system/models'
import { CombatParticipants, CombatLog, CombatActionsList, CombatSummary } from '../components'

const ACTION_TYPE_OPTIONS: PerformCombatActionBodyActionType[] = ['attack', 'defend', 'use_item', 'ability']

interface FeedbackState {
  type: 'success' | 'error'
  message: string
}

export function CombatPage() {
  const navigate = useNavigate()
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)
  const hasCharacter = Boolean(selectedCharacterId)
  const characterId = selectedCharacterId ?? ''

  const [combatId, setCombatId] = useState('')
  const [pendingCombatId, setPendingCombatId] = useState('')
  const [targetId, setTargetId] = useState('')
  const [locationId, setLocationId] = useState('')
  const [autoRefresh, setAutoRefresh] = useState(true)

  const [selectedActionType, setSelectedActionType] = useState<PerformCombatActionBodyActionType | ''>('')
  const [actionTargetId, setActionTargetId] = useState('')
  const [actionItemId, setActionItemId] = useState('')
  const [actionAbilityId, setActionAbilityId] = useState('')

  const [feedback, setFeedback] = useState<FeedbackState | null>(null)

  useEffect(() => {
    if (!hasCharacter) {
      navigate('/characters')
    }
  }, [hasCharacter, navigate])

  const combatStateQuery = useGetCombatState(
    combatId || 'placeholder',
    {
      query: {
        enabled: Boolean(combatId),
        refetchInterval: combatId && autoRefresh ? 4000 : undefined,
      },
    }
  )

  const combatState: CombatState | undefined = combatStateQuery.data

  const availableActionsQuery = useGetAvailableActions(
    combatId || 'placeholder',
    { characterId },
    {
      query: {
        enabled: Boolean(combatId && hasCharacter),
      },
    }
  )

  const availableActions: CombatAction[] = availableActionsQuery.data?.actions ?? []

  useEffect(() => {
    if (!availableActions.length) {
      return
    }

    if (!selectedActionType || !availableActions.some((action) => action.type === selectedActionType)) {
      setSelectedActionType(availableActions[0].type)
    }
  }, [availableActions, selectedActionType])

  const isCombatFinished = combatState?.status && combatState.status !== 'active'

  const combatResultQuery = useGetCombatResult(
    combatId || 'placeholder',
    {
      query: {
        enabled: Boolean(combatId && isCombatFinished),
      },
    }
  )

  const combatResult: CombatResult | undefined = combatResultQuery.data

  const initiateCombatMutation = useInitiateCombat()
  const performActionMutation = usePerformCombatAction()
  const fleeCombatMutation = useFleeCombat()

  const actionsLoading =
    initiateCombatMutation.isPending ||
    performActionMutation.isPending ||
    fleeCombatMutation.isPending ||
    combatStateQuery.isFetching

  const handleInitiateCombat = () => {
    if (!characterId || !targetId.trim()) {
      setFeedback({ type: 'error', message: 'Укажите ID цели для начала боя.' })
      return
    }

    initiateCombatMutation.mutate(
      {
        data: {
          characterId,
          targetId: targetId.trim(),
          locationId: locationId.trim() || undefined,
        },
      },
      {
        onSuccess: (result) => {
          setCombatId(result.id)
          setPendingCombatId(result.id)
          setFeedback({ type: 'success', message: 'Бой успешно начат.' })
          setTargetId('')
          setLocationId('')
        },
        onError: () => {
          setFeedback({ type: 'error', message: 'Не удалось начать бой. Проверьте параметры.' })
        },
      }
    )
  }

  const handleLoadCombat = () => {
    if (!pendingCombatId.trim()) {
      setFeedback({ type: 'error', message: 'Введите ID боя для загрузки.' })
      return
    }
    setCombatId(pendingCombatId.trim())
    setFeedback(null)
  }

  const handlePerformAction = () => {
    if (!combatId || !characterId || !selectedActionType) {
      setFeedback({ type: 'error', message: 'Выберите действие и убедитесь, что бой активен.' })
      return
    }

    performActionMutation.mutate(
      {
        combatId,
        data: {
          characterId,
          actionType: selectedActionType,
          targetId: actionTargetId.trim() || undefined,
          itemId: actionItemId.trim() || undefined,
          abilityId: actionAbilityId.trim() || undefined,
        },
      },
      {
        onSuccess: () => {
          setFeedback({ type: 'success', message: 'Действие выполнено.' })
          combatStateQuery.refetch()
          availableActionsQuery.refetch()
          combatResultQuery.refetch()
        },
        onError: () => {
          setFeedback({ type: 'error', message: 'Не удалось выполнить действие. Проверьте параметры.' })
        },
      }
    )
  }

  const handleFleeCombat = () => {
    if (!combatId || !characterId) {
      return
    }

    fleeCombatMutation.mutate(
      {
        combatId,
        data: { characterId },
      },
      {
        onSuccess: () => {
          setFeedback({ type: 'success', message: 'Попытка побега выполнена.' })
          combatStateQuery.refetch()
          availableActionsQuery.refetch()
          combatResultQuery.refetch()
        },
        onError: () => {
          setFeedback({ type: 'error', message: 'Не удалось сбежать из боя.' })
        },
      }
    )
  }

  const centerContent = (
    <Stack spacing={2} height="100%">
      {feedback && (
        <Alert
          severity={feedback.type}
          onClose={() => setFeedback(null)}
          sx={{ fontSize: '0.8rem' }}
        >
          {feedback.message}
        </Alert>
      )}

      <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 700, fontSize: '1.2rem' }}>
        Боевая система
      </Typography>

      <Grid container spacing={2} sx={{ flex: 1, minHeight: 0 }}>
        <Grid item xs={12} md={6}>
          <CombatParticipants
            participants={combatState?.participants ?? []}
            currentTurnId={combatState?.currentTurn}
          />
        </Grid>
        <Grid item xs={12} md={6}>
          <CombatLog entries={combatState?.log} />
        </Grid>
      </Grid>
    </Stack>
  )

  const leftPanel = (
    <Stack spacing={2} height="100%">
      <Typography variant="subtitle2" sx={{ fontWeight: 600, fontSize: '0.9rem', color: 'primary.main' }}>
        Инициация боя
      </Typography>
      <TextField
        label="ID цели"
        size="small"
        value={targetId}
        onChange={(event) => setTargetId(event.target.value)}
        helperText="Кого атакуем (required)"
      />
      <TextField
        label="ID локации"
        size="small"
        value={locationId}
        onChange={(event) => setLocationId(event.target.value)}
        helperText="Опционально"
      />
      <Button variant="contained" size="small" onClick={handleInitiateCombat} disabled={initiateCombatMutation.isPending}>
        Начать бой
      </Button>

      <Divider />

      <Typography variant="subtitle2" sx={{ fontWeight: 600, fontSize: '0.9rem', color: 'primary.main' }}>
        Управление боем
      </Typography>
      <TextField
        label="ID боя"
        size="small"
        value={pendingCombatId}
        onChange={(event) => setPendingCombatId(event.target.value)}
        helperText="Введите ID существующего боя"
      />
      <Button variant="outlined" size="small" onClick={handleLoadCombat} disabled={!pendingCombatId.trim()}>
        Загрузить бой
      </Button>
      <FormControlLabel
        control={
          <Switch
            size="small"
            checked={autoRefresh}
            onChange={() => setAutoRefresh((prev) => !prev)}
          />
        }
        label="Автообновление"
        sx={{ '.MuiTypography-root': { fontSize: '0.75rem' } }}
      />

      <Divider />

      <Typography variant="subtitle2" sx={{ fontWeight: 600, fontSize: '0.9rem', color: 'primary.main' }}>
        Доступные действия
      </Typography>
      <CombatActionsList actions={availableActions} selectedActionType={selectedActionType} onSelect={setSelectedActionType} />

      <Paper variant="outlined" sx={{ p: 2 }}>
        <Stack spacing={1.5}>
          <Typography variant="subtitle2" sx={{ fontSize: '0.85rem', fontWeight: 600 }}>
            Выполнить действие
          </Typography>
          <TextField
            select
            size="small"
            label="Тип действия"
            value={selectedActionType}
            onChange={(event) => setSelectedActionType(event.target.value as PerformCombatActionBodyActionType)}
            SelectProps={{
              native: true,
            }}
            helperText="Выберите доступное действие"
          >
            <option value="" />
            {ACTION_TYPE_OPTIONS.map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </TextField>
          <TextField
            label="ID цели (опц.)"
            size="small"
            value={actionTargetId}
            onChange={(event) => setActionTargetId(event.target.value)}
          />
          <TextField
            label="ID предмета (опц.)"
            size="small"
            value={actionItemId}
            onChange={(event) => setActionItemId(event.target.value)}
          />
          <TextField
            label="ID способности (опц.)"
            size="small"
            value={actionAbilityId}
            onChange={(event) => setActionAbilityId(event.target.value)}
          />
          <Button
            variant="contained"
            size="small"
            onClick={handlePerformAction}
            disabled={actionsLoading || !selectedActionType}
          >
            Выполнить
          </Button>
        </Stack>
      </Paper>
    </Stack>
  )

  const rightPanel = (
    <StatsPanel>
      <CombatSummary combatState={combatState} combatResult={combatResult} />

      <Paper variant="outlined" sx={{ p: 2 }}>
        <Stack spacing={1}>
          <Typography variant="subtitle2" sx={{ fontWeight: 600, fontSize: '0.85rem' }}>
            Управление
          </Typography>
          <Button
            variant="outlined"
            size="small"
            onClick={() => {
              combatStateQuery.refetch()
              availableActionsQuery.refetch()
              combatResultQuery.refetch()
            }}
          >
            Обновить данные
          </Button>
          <Button
            variant="outlined"
            color="warning"
            size="small"
            onClick={handleFleeCombat}
            disabled={fleeCombatMutation.isPending || !combatId}
          >
            Сбежать из боя
          </Button>
        </Stack>
      </Paper>
    </StatsPanel>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

