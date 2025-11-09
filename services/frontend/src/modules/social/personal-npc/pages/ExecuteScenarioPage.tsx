import { FormEvent, useEffect, useState } from 'react'
import { useNavigate, useParams, Link as RouterLink } from 'react-router-dom'
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { usePersonalNpcQueries } from '@/modules/social/personal-npc/hooks/usePersonalNpcQueries'
import { usePersonalNpcStore } from '@/modules/social/personal-npc/state/usePersonalNpcStore'

type ExecuteScenarioFormState = {
  npcId: string
  priority: number
  scheduledAt: string
  parameters: string
}

export const ExecuteScenarioPage = () => {
  const navigate = useNavigate()
  const { blueprintId } = useParams<{ blueprintId: string }>()
  const { selectBlueprint } = usePersonalNpcStore((state) => ({
    selectBlueprint: state.selectBlueprint,
  }))
  const { executeScenario, detailQuery } = usePersonalNpcQueries()
  const [formState, setFormState] = useState<ExecuteScenarioFormState>({
    npcId: '',
    priority: 5,
    scheduledAt: '',
    parameters: '{}',
  })
  const [formError, setFormError] = useState<string | null>(null)

  useEffect(() => {
    selectBlueprint(blueprintId)
    return () => {
      selectBlueprint(undefined)
    }
  }, [blueprintId, selectBlueprint])

  const blueprintName = detailQuery.data?.data.summary.name

  const handleChange =
    (field: keyof ExecuteScenarioFormState) => (event: React.ChangeEvent<HTMLInputElement>) => {
      const value = field === 'priority' ? Number(event.target.value) : event.target.value
      setFormState((prev) => ({
        ...prev,
        [field]: value,
      }))
    }

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setFormError(null)

    if (!blueprintId) {
      setFormError('Не выбран блупринт сценария.')
      return
    }

    if (!formState.npcId) {
      setFormError('Укажите идентификатор NPC.')
      return
    }

    let parsedParameters: Record<string, unknown> | undefined

    if (formState.parameters.trim().length > 0) {
      try {
        parsedParameters = JSON.parse(formState.parameters)
      } catch (error) {
        setFormError('Параметры должны быть корректным JSON.')
        return
      }
    }

    const priority = Number.isNaN(formState.priority) ? 5 : Math.min(Math.max(1, formState.priority), 10)
    const scheduledAt =
      formState.scheduledAt.trim().length > 0
        ? new Date(formState.scheduledAt).toISOString()
        : undefined

    executeScenario.mutate(
      {
        npcId: formState.npcId,
        data: {
          blueprintId,
          parameters: parsedParameters,
          priority,
          scheduledAt,
        },
      },
      {
        onSuccess: () => {
          setFormState({
            npcId: '',
            priority: 5,
            scheduledAt: '',
            parameters: '{}',
          })
        },
      }
    )
  }

  if (!blueprintId) {
    return (
      <Stack spacing={3} sx={{ px: 4, py: 3 }}>
        <Typography variant="h5" color="text.secondary">
          Не удалось определить идентификатор блупринта.
        </Typography>
        <Button variant="contained" component={RouterLink} to="/game/personal-npc-scenarios">
          Вернуться к списку
        </Button>
      </Stack>
    )
  }

  return (
    <Stack spacing={3} sx={{ px: 4, py: 3 }}>
      <Stack direction="row" alignItems="center" justifyContent="space-between">
        <Box>
          <Typography variant="h4" color="text.primary">
            Запуск сценария
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            {blueprintName ?? `Блупринт ${blueprintId}`}
          </Typography>
        </Box>
        <Button variant="outlined" component={RouterLink} to={`/game/personal-npc-scenarios/${blueprintId}`}>
          К деталям блупринта
        </Button>
      </Stack>

      <Card variant="outlined">
        <CardContent>
          <form onSubmit={handleSubmit}>
            <Stack spacing={3}>
              <TextField
                required
                label="NPC ID"
                value={formState.npcId}
                onChange={handleChange('npcId')}
                helperText="UUID или идентификатор персонального NPC"
              />
              <TextField
                label="Приоритет"
                type="number"
                inputProps={{ min: 1, max: 10 }}
                value={formState.priority}
                onChange={handleChange('priority')}
              />
              <TextField
                label="Запланировать на"
                type="datetime-local"
                value={formState.scheduledAt}
                onChange={handleChange('scheduledAt')}
                InputLabelProps={{ shrink: true }}
              />
              <TextField
                label="Параметры запуска (JSON)"
                multiline
                minRows={4}
                value={formState.parameters}
                onChange={handleChange('parameters')}
              />

              {formError && (
                <Alert severity="error" onClose={() => setFormError(null)}>
                  {formError}
                </Alert>
              )}

              {executeScenario.isSuccess && (
                <Alert severity="success">
                  Сценарий поставлен в очередь. Инстанс: {executeScenario.data.instanceId}
                </Alert>
              )}

              {executeScenario.isError && (
                <Alert severity="error">
                  Не удалось запустить сценарий. Проверьте данные и попробуйте снова.
                </Alert>
              )}

              <Stack direction="row" spacing={2}>
                <Button
                  type="submit"
                  variant="contained"
                  disabled={executeScenario.isPending || executeScenario.isSuccess}
                  startIcon={executeScenario.isPending ? <CircularProgress size={20} /> : undefined}
                >
                  Запустить
                </Button>
                <Button
                  variant="outlined"
                  disabled={executeScenario.isPending}
                  onClick={() => navigate(`/game/personal-npc-scenarios/${blueprintId}`)}
                >
                  Отмена
                </Button>
              </Stack>
            </Stack>
          </form>
        </CardContent>
      </Card>
    </Stack>
  )
}

