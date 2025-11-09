/**
 * Страница мониторинга киберпсихоза
 * 
 * SPA архитектура с компактной 3-колоночной сеткой:
 * - Левая панель: Меню разделов
 * - Центр: Информация о стадии и симптомы
 * - Правая панель: Уровень человечности
 */
import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Box,
  CircularProgress,
  Alert,
  Typography,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Card,
  CardHeader,
  CardContent,
  Stack,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
  Button,
} from '@mui/material'
import FavoriteIcon from '@mui/icons-material/Favorite'
import WarningAmberIcon from '@mui/icons-material/WarningAmber'
import TrendingDownIcon from '@mui/icons-material/TrendingDown'
import InfoIcon from '@mui/icons-material/Info'
import {
  useGetHumanity,
  useGetCyberpsychosisStage,
  useGetSymptoms,
  useGetProgression,
  useApplyTreatment,
  useGetStageInfo,
} from '@/api/generated/gameplay/cyberpsychosis/combat/combat'
import {
  HumanityDisplay,
  StageInfoCard,
  SymptomsList,
} from '../components'
import { useGameState } from '@/features/game/hooks/useGameState'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import { ApplyTreatmentRequestTreatmentType } from '@/api/generated/gameplay/cyberpsychosis/models'

const treatmentOptionLabels: Record<ApplyTreatmentRequestTreatmentType, string> = {
  therapy: 'Терапия (психолог)',
  medication: 'Медикаментозное лечение',
  implant_removal: 'Удаление импланта',
  detoxification: 'Детоксикация',
}

type Section = 'overview' | 'symptoms' | 'progression' | 'info'

export function CyberpsychosisPage() {
  const navigate = useNavigate()
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)
  const [selectedSection, setSelectedSection] = useState<Section>('overview')
  const [treatmentType, setTreatmentType] = useState<ApplyTreatmentRequestTreatmentType>(
    ApplyTreatmentRequestTreatmentType.therapy,
  )
  const [treatmentNpcId, setTreatmentNpcId] = useState('')
  const [treatmentMessage, setTreatmentMessage] = useState<string | null>(null)
  const [treatmentError, setTreatmentError] = useState<string | null>(null)

  const treatmentOptions = useMemo(() => Object.values(ApplyTreatmentRequestTreatmentType), [])

  // Загрузка данных из OpenAPI
  const {
    data: humanityData,
    isLoading: isLoadingHumanity,
    error: humanityError,
  } = useGetHumanity(selectedCharacterId || '', {
    query: { enabled: !!selectedCharacterId },
  })

  const {
    data: stageData,
    isLoading: isLoadingStage,
  } = useGetCyberpsychosisStage(selectedCharacterId || '', {
    query: { enabled: !!selectedCharacterId },
  })

  const {
    data: symptomsData,
    isLoading: isLoadingSymptoms,
  } = useGetSymptoms(selectedCharacterId || '', {
    query: { enabled: !!selectedCharacterId },
  })

  const stageName = humanityData?.stage && ['early', 'middle', 'late', 'cyberpsychosis'].includes(humanityData.stage)
    ? humanityData.stage
    : 'early'

  const {
    data: stageInfoData,
  } = useGetStageInfo(stageName as 'early' | 'middle' | 'late' | 'cyberpsychosis', {
    query: { enabled: !!humanityData?.stage },
  })

  const progressionQuery = useGetProgression(selectedCharacterId || '', {
    query: { enabled: !!selectedCharacterId },
  })

  const applyTreatmentMutation = useApplyTreatment({
    mutation: {
      onSuccess: (result) => {
        setTreatmentMessage(
          `Восстановлено человечности: ${result.humanity_restored}. Стоимость: ${result.cost} кредитов.`,
        )
        setTreatmentError(null)
        progressionQuery.refetch?.()
      },
      onError: (error) => {
        setTreatmentError(error instanceof Error ? error.message : 'Не удалось применить лечение')
      },
    },
  })

  const handleApplyTreatment = () => {
    if (!selectedCharacterId) {
      return
    }

    setTreatmentMessage(null)
    setTreatmentError(null)

    applyTreatmentMutation.mutate({
      playerId: selectedCharacterId,
      data: {
        treatment_type: treatmentType,
        npc_id: treatmentNpcId.trim() ? treatmentNpcId.trim() : null,
      },
    })
  }

  useEffect(() => {
    if (!selectedCharacterId) {
      navigate('/characters')
    }
  }, [selectedCharacterId, navigate])

  // Левая панель - Меню разделов
  const leftPanel = (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, height: '100%', minHeight: 0 }}>
      <Typography
        variant="h6"
        sx={{
          color: 'primary.main',
          textShadow: '0 0 8px currentColor',
          fontWeight: 'bold',
          fontSize: '0.875rem',
          textTransform: 'uppercase',
          letterSpacing: '0.1em',
        }}
      >
        Киберпсихоз
      </Typography>

      <Box sx={{ flex: 1, minHeight: 0, overflowY: 'auto' }}>
        <List dense>
          <ListItem disablePadding>
            <ListItemButton
              selected={selectedSection === 'overview'}
              onClick={() => setSelectedSection('overview')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&.Mui-selected': {
                  bgcolor: 'rgba(0, 247, 255, 0.15)',
                  borderLeft: '3px solid',
                  borderColor: 'primary.main',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <FavoriteIcon />
              </ListItemIcon>
              <ListItemText
                primary="Обзор"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
              />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding>
            <ListItemButton
              selected={selectedSection === 'symptoms'}
              onClick={() => setSelectedSection('symptoms')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&.Mui-selected': {
                  bgcolor: 'rgba(0, 247, 255, 0.15)',
                  borderLeft: '3px solid',
                  borderColor: 'primary.main',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <WarningAmberIcon />
              </ListItemIcon>
              <ListItemText
                primary="Симптомы"
                secondary={symptomsData?.length || 0}
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.7rem' }}
              />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding>
            <ListItemButton
              selected={selectedSection === 'progression'}
              onClick={() => setSelectedSection('progression')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&.Mui-selected': {
                  bgcolor: 'rgba(0, 247, 255, 0.15)',
                  borderLeft: '3px solid',
                  borderColor: 'primary.main',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <TrendingDownIcon />
              </ListItemIcon>
              <ListItemText
                primary="Прогрессия"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
              />
            </ListItemButton>
          </ListItem>

          <ListItem disablePadding>
            <ListItemButton
              selected={selectedSection === 'info'}
              onClick={() => setSelectedSection('info')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&.Mui-selected': {
                  bgcolor: 'rgba(0, 247, 255, 0.15)',
                  borderLeft: '3px solid',
                  borderColor: 'primary.main',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <InfoIcon />
              </ListItemIcon>
              <ListItemText
                primary="Информация"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
              />
            </ListItemButton>
          </ListItem>
        </List>
      </Box>
    </Box>
  )

  // Правая панель - Человечность
  const rightPanel = (
    <StatsPanel>
      {humanityData && <HumanityDisplay humanity={humanityData} />}
    </StatsPanel>
  )

  if (isLoadingHumanity || isLoadingStage || isLoadingSymptoms) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1 }}>
          <CircularProgress size={60} />
        </Box>
      </Box>
    )
  }

  if (humanityError || !humanityData) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1, p: 3 }}>
          <Alert severity="error" sx={{ maxWidth: 600 }}>
            <Typography variant="h6">Ошибка загрузки данных</Typography>
            <Typography variant="body2">
              {(humanityError as unknown as Error)?.message || 'Не удалось загрузить данные киберпсихоза'}
            </Typography>
          </Alert>
        </Box>
      </Box>
    )
  }

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      <Header />
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        {/* Центр - контент по разделам */}
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2, minHeight: 0, overflowY: 'auto', p: 2 }}>
          <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 'bold', fontSize: '1.1rem', mb: 1 }}>
            Мониторинг киберпсихоза
          </Typography>

          {selectedSection === 'overview' && stageData && (
            <Box>
              <Typography variant="h6" sx={{ fontSize: '0.9rem', mb: 1.5 }}>
                Текущая стадия
              </Typography>
              {stageInfoData && <StageInfoCard stage={stageInfoData} />}
            </Box>
          )}

          {selectedSection === 'symptoms' && (
            <Box>
              <Typography variant="h6" sx={{ fontSize: '0.9rem', mb: 1.5 }}>
                Активные симптомы
              </Typography>
              <SymptomsList symptoms={symptomsData || []} />
            </Box>
          )}

          {selectedSection === 'progression' && (
            <Stack spacing={2}>
              <Card variant="outlined">
                <CardHeader
                  title="Прогрессия киберпсихоза"
                  subheader="Текущие факторы риска и триггеры"
                  titleTypographyProps={{ fontSize: '0.95rem', fontWeight: 600 }}
                  subheaderTypographyProps={{ fontSize: '0.75rem' }}
                />
                <CardContent>
                  {progressionQuery.isLoading ? (
                    <Box display="flex" justifyContent="center" py={2}>
                      <CircularProgress size={28} />
                    </Box>
                  ) : progressionQuery.error ? (
                    <Alert severity="error" sx={{ fontSize: '0.8rem' }}>
                      {(progressionQuery.error as Error).message}
                    </Alert>
                  ) : progressionQuery.data ? (
                    <Stack spacing={1.5}>
                      <Typography variant="body2" fontWeight={600} fontSize="0.8rem">
                        Скорость прогрессии: {progressionQuery.data.current_progression_rate ?? 0} %/день
                      </Typography>
                      {progressionQuery.data.next_check_time && (
                        <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
                          Следующая проверка: {progressionQuery.data.next_check_time}
                        </Typography>
                      )}
                      <Stack spacing={1}>
                        <Typography variant="subtitle2" fontSize="0.8rem">
                          Активные факторы
                        </Typography>
                        {progressionQuery.data.factors.length > 0 ? (
                          progressionQuery.data.factors.map((factor, index) => (
                            <Typography key={index} variant="caption" color="text.secondary" fontSize="0.7rem">
                              • {JSON.stringify(factor)}
                            </Typography>
                          ))
                        ) : (
                          <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
                            Активных факторов нет
                          </Typography>
                        )}
                      </Stack>
                      <Stack spacing={1}>
                        <Typography variant="subtitle2" fontSize="0.8rem">
                          Триггеры
                        </Typography>
                        {progressionQuery.data.triggers.length > 0 ? (
                          progressionQuery.data.triggers.map((trigger, index) => (
                            <Typography key={index} variant="caption" color="text.secondary" fontSize="0.7rem">
                              • {JSON.stringify(trigger)}
                            </Typography>
                          ))
                        ) : (
                          <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
                            Активных триггеров нет
                          </Typography>
                        )}
                      </Stack>
                    </Stack>
                  ) : (
                    <Typography variant="caption" color="text.secondary">
                      Нет данных о прогрессии
                    </Typography>
                  )}
                </CardContent>
              </Card>

              <Card variant="outlined">
                <CardHeader
                  title="Лечение"
                  subheader="Снижение риска и восстановление человечности"
                  titleTypographyProps={{ fontSize: '0.95rem', fontWeight: 600 }}
                  subheaderTypographyProps={{ fontSize: '0.75rem' }}
                />
                <CardContent>
                  <Stack spacing={2}>
                    <FormControl fullWidth size="small">
                      <InputLabel sx={{ fontSize: '0.75rem' }}>Тип лечения</InputLabel>
                      <Select
                        value={treatmentType}
                        label="Тип лечения"
                        onChange={(event) =>
                          setTreatmentType(event.target.value as ApplyTreatmentRequestTreatmentType)
                        }
                        sx={{ fontSize: '0.75rem' }}
                      >
                        {treatmentOptions.map((option) => (
                          <MenuItem key={option} value={option} sx={{ fontSize: '0.75rem' }}>
                            {treatmentOptionLabels[option as ApplyTreatmentRequestTreatmentType] ?? option}
                          </MenuItem>
                        ))}
                      </Select>
                    </FormControl>

                    <TextField
                      label="ID NPC лечащего врача"
                      size="small"
                      value={treatmentNpcId}
                      onChange={(event) => setTreatmentNpcId(event.target.value)}
                      helperText="Опционально. Укажите, если лечение проводит конкретный NPC."
                    />

                    <Button
                      variant="contained"
                      size="small"
                      onClick={handleApplyTreatment}
                      disabled={applyTreatmentMutation.isPending || !selectedCharacterId}
                    >
                      {applyTreatmentMutation.isPending ? 'Применение...' : 'Применить лечение'}
                    </Button>

                    {treatmentError && (
                      <Alert severity="error" sx={{ fontSize: '0.8rem' }}>
                        {treatmentError}
                      </Alert>
                    )}

                    {treatmentMessage && (
                      <Alert severity="success" sx={{ fontSize: '0.8rem' }}>
                        {treatmentMessage}
                      </Alert>
                    )}
                  </Stack>
                </CardContent>
              </Card>
            </Stack>
          )}

          {selectedSection === 'info' && stageInfoData && (
            <Box>
              <Typography variant="h6" sx={{ fontSize: '0.9rem', mb: 1.5 }}>
                Информация о стадиях
              </Typography>
              <StageInfoCard stage={stageInfoData} />
            </Box>
          )}
        </Box>
      </GameLayout>
    </Box>
  )
}

