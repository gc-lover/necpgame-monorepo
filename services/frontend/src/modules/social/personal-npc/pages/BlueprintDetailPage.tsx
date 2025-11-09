import { useEffect, useMemo } from 'react'
import { useNavigate, useParams, Link as RouterLink } from 'react-router-dom'
import {
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Divider,
  Grid,
  Link,
  List,
  ListItem,
  ListItemText,
  Stack,
  Typography,
} from '@mui/material'
import { ScenarioBlueprintPublishRequestVisibilityScope } from '@/api/generated/social/personal-npc-scenarios/models/personal-npc-scenarios-models/scenarioBlueprintPublishRequestVisibilityScope'
import { usePersonalNpcQueries } from '@/modules/social/personal-npc/hooks/usePersonalNpcQueries'
import { usePersonalNpcStore } from '@/modules/social/personal-npc/state/usePersonalNpcStore'

export const BlueprintDetailPage = () => {
  const navigate = useNavigate()
  const { blueprintId } = useParams<{ blueprintId: string }>()
  const { selectBlueprint } = usePersonalNpcStore((state) => ({
    selectBlueprint: state.selectBlueprint,
  }))
  const {
    detailQuery,
    instancesQuery,
    publishBlueprint,
    deleteBlueprint,
  } = usePersonalNpcQueries()

  useEffect(() => {
    selectBlueprint(blueprintId)
    return () => {
      selectBlueprint(undefined)
    }
  }, [blueprintId, selectBlueprint])

  const blueprint = detailQuery.data?.data
  const instances = instancesQuery.data?.data ?? []

  const isLoading = detailQuery.isLoading || instancesQuery.isLoading
  const isMutating = publishBlueprint.isPending || deleteBlueprint.isPending

  const summary = blueprint?.summary
  const steps = blueprint?.steps ?? []

  const automationHints = blueprint?.automationHints ?? []
  const verificationNotes = blueprint?.verificationNotes

  const handleTogglePublish = () => {
    if (!summary || !blueprintId) {
      return
    }
    publishBlueprint.mutate({
      blueprintId,
      data: {
        publish: !summary.isPublic,
        price: summary.price,
        visibilityScope: summary.isPublic
          ? ScenarioBlueprintPublishRequestVisibilityScope.private
          : ScenarioBlueprintPublishRequestVisibilityScope.marketplace,
      },
    })
  }

  const handleDelete = () => {
    if (!blueprintId) {
      return
    }
    deleteBlueprint.mutate(
      { blueprintId },
      {
        onSuccess: () => {
          navigate('/game/personal-npc-scenarios')
        },
      }
    )
  }

  const formatDateTime = (value?: string) => {
    if (!value) {
      return '—'
    }
    const date = new Date(value)
    if (Number.isNaN(date.getTime())) {
      return '—'
    }
    return new Intl.DateTimeFormat('ru-RU', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date)
  }

  const latestExecution = useMemo(() => {
    const toTimestamp = (value?: string) => {
      if (!value) {
        return 0
      }
      const timestamp = new Date(value).getTime()
      return Number.isNaN(timestamp) ? 0 : timestamp
    }

    return instances
      .slice()
      .sort((a, b) => {
        const left = toTimestamp(a.startedAt)
        const right = toTimestamp(b.startedAt)
        return right - left
      })
      .at(0)
  }, [instances])

  if (isLoading) {
    return (
      <Stack alignItems="center" justifyContent="center" sx={{ height: '100vh' }}>
        <CircularProgress />
      </Stack>
    )
  }

  if (!summary || !blueprintId) {
    return (
      <Stack spacing={3} sx={{ px: 4, py: 3 }}>
        <Typography variant="h5" color="text.secondary">
          Блупринт не найден.
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
            {summary.name}
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            {summary.description ?? 'Описание отсутствует'}
          </Typography>
        </Box>
        <Stack direction="row" spacing={2}>
          <Button variant="outlined" href={`/game/personal-npc-scenarios/${blueprintId}/execute`}>
            Запустить сценарий
          </Button>
          <Button
            variant="outlined"
            color={summary.isPublic ? 'secondary' : 'primary'}
            disabled={isMutating}
            onClick={handleTogglePublish}
          >
            {summary.isPublic ? 'Сделать приватным' : 'Опубликовать'}
          </Button>
          <Button variant="text" color="error" disabled={isMutating} onClick={handleDelete}>
            Удалить
          </Button>
        </Stack>
      </Stack>

      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Card variant="outlined">
            <CardContent>
              <Stack spacing={1.5}>
                <Typography variant="h6" color="text.primary">
                  Общая информация
                </Typography>
                <Stack direction="row" spacing={2} flexWrap="wrap">
                  <Typography variant="body2" color="text.secondary">
                    Категория: {summary.category}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Автор: {summary.authorId}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Версия: {summary.version}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Доступ: {summary.isPublic ? 'Публичный' : 'Приватный'}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Проверено: {summary.isVerified ? 'Да' : 'Нет'}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Стоимость: {summary.price ?? 0}
                  </Typography>
                </Stack>
                <Typography variant="body2" color="text.secondary">
                  Роли доступа: {summary.requiredRoles.join(', ')}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Создан: {formatDateTime(summary.createdAt)}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Обновлён: {formatDateTime(summary.updatedAt)}
                </Typography>
                {verificationNotes && (
                  <Typography variant="body2" color="text.secondary">
                    Заметки проверки: {verificationNotes}
                  </Typography>
                )}
                {automationHints.length > 0 && (
                  <Box>
                    <Typography variant="body2" color="text.primary">
                      Подсказки автоматизации:
                    </Typography>
                    <List dense>
                      {automationHints.map((hint) => (
                        <ListItem key={hint} sx={{ py: 0 }}>
                          <ListItemText primary={hint} />
                        </ListItem>
                      ))}
                    </List>
                  </Box>
                )}
              </Stack>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} md={6}>
          <Card variant="outlined">
            <CardContent>
              <Stack spacing={1.5}>
                <Typography variant="h6" color="text.primary">
                  Последний запуск
                </Typography>
                {latestExecution ? (
                  <Stack spacing={0.5}>
                    <Typography variant="body2" color="text.secondary">
                      Статус: {latestExecution.status}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      NPC: {latestExecution.npcId}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Текущий шаг: {latestExecution.currentStep ?? 0}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Начат: {formatDateTime(latestExecution.startedAt)}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Завершён: {formatDateTime(latestExecution.completedAt)}
                    </Typography>
                  </Stack>
                ) : (
                  <Typography variant="body2" color="text.secondary">
                    Запусков ещё не было.
                  </Typography>
                )}
                <Divider />
                <Typography variant="body2" color="text.primary">
                  Активные инстансы: {instances.length}
                </Typography>
                <Link component={RouterLink} to="/game/personal-npc-scenarios" underline="hover">
                  Вернуться к списку блупринтов
                </Link>
              </Stack>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6" color="text.primary">
              Шаги сценария
            </Typography>
            <List>
              {steps.map((step) => (
                <ListItem key={step.id} alignItems="flex-start">
                  <ListItemText
                    primary={`${step.order}. ${step.type.toUpperCase()} — ${step.action}`}
                    secondary={
                      step.parameters
                        ? JSON.stringify(step.parameters, null, 2)
                        : 'Параметры отсутствуют'
                    }
                  />
                </ListItem>
              ))}
            </List>
            {steps.length === 0 && (
              <Typography variant="body2" color="text.secondary">
                Шаги не определены.
              </Typography>
            )}
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6" color="text.primary">
              Инстансы сценария
            </Typography>
            <List>
              {instances.map((instance) => (
                <ListItem key={instance.id} alignItems="flex-start">
                  <ListItemText
                    primary={`NPC ${instance.npcId} — статус: ${instance.status}`}
                    secondary={
                      <>
                        <Typography variant="body2" color="text.secondary">
                          Шаг: {instance.currentStep ?? 0} • Длительность: {instance.duration ?? 0} сек.
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                          Начат: {formatDateTime(instance.startedAt)}
                        </Typography>
                      </>
                    }
                  />
                </ListItem>
              ))}
            </List>
            {instances.length === 0 && (
              <Typography variant="body2" color="text.secondary">
                Инстансы отсутствуют. Запустите сценарий, чтобы увидеть прогресс.
              </Typography>
            )}
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )
}

