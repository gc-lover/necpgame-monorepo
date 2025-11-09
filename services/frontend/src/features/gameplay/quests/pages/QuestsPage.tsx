import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Box,
  CircularProgress,
  Alert,
  Typography,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Paper,
  Chip,
  Stack,
} from '@mui/material'
import ListAltIcon from '@mui/icons-material/ListAlt'
import PlaylistAddCheckIcon from '@mui/icons-material/PlaylistAddCheck'
import AllInclusiveIcon from '@mui/icons-material/AllInclusive'
import {
  useGetAvailableQuests,
  useGetActiveQuests,
} from '@/api/generated/quests/quests/quests'
import { QuestListItem, QuestProgressItem } from '../components'
import { useGameState } from '@/features/game/hooks/useGameState'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import type { Quest, QuestProgress } from '@/api/generated/quests/models'

type QuestFilter = 'available' | 'active' | 'all'

export function QuestsPage() {
  const navigate = useNavigate()
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)
  const [filter, setFilter] = useState<QuestFilter>('active')
  const [_selectedQuest, setSelectedQuest] = useState<Quest | QuestProgress | null>(null)

  // Загрузка доступных квестов из OpenAPI
  const {
    data: availableData,
    isLoading: isLoadingAvailable,
  } = useGetAvailableQuests(
    { characterId: selectedCharacterId || '' },
    { query: { enabled: !!selectedCharacterId && (filter === 'available' || filter === 'all') } }
  )

  // Загрузка активных квестов из OpenAPI
  const {
    data: activeData,
    isLoading: isLoadingActive,
    error,
  } = useGetActiveQuests(
    { characterId: selectedCharacterId || '' },
    { query: { enabled: !!selectedCharacterId && (filter === 'active' || filter === 'all') } }
  )

  useEffect(() => {
    if (!selectedCharacterId) {
      navigate('/characters')
    }
  }, [selectedCharacterId, navigate])

  const handleQuestClick = (quest: Quest | QuestProgress) => {
    setSelectedQuest(quest)
  }

  // Левая панель - Фильтры
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
        Квесты
      </Typography>

      <List dense>
        <ListItem disablePadding>
          <ListItemButton
            selected={filter === 'active'}
            onClick={() => setFilter('active')}
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
            <ListItemText
              primary={
                <Stack direction="row" spacing={0.5} alignItems="center">
                  <PlaylistAddCheckIcon sx={{ fontSize: '1rem' }} />
                  <span style={{ fontSize: '0.875rem' }}>Активные</span>
                </Stack>
              }
              secondary={activeData?.quests?.length || 0}
              secondaryTypographyProps={{ fontSize: '0.7rem' }}
            />
          </ListItemButton>
        </ListItem>

        <ListItem disablePadding>
          <ListItemButton
            selected={filter === 'available'}
            onClick={() => setFilter('available')}
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
            <ListItemText
              primary={
                <Stack direction="row" spacing={0.5} alignItems="center">
                  <ListAltIcon sx={{ fontSize: '1rem' }} />
                  <span style={{ fontSize: '0.875rem' }}>Доступные</span>
                </Stack>
              }
              secondary={availableData?.quests?.length || 0}
              secondaryTypographyProps={{ fontSize: '0.7rem' }}
            />
          </ListItemButton>
        </ListItem>

        <ListItem disablePadding>
          <ListItemButton
            selected={filter === 'all'}
            onClick={() => setFilter('all')}
            sx={{
              borderRadius: 1,
              '&.Mui-selected': {
                bgcolor: 'rgba(0, 247, 255, 0.15)',
                borderLeft: '3px solid',
                borderColor: 'primary.main',
              },
            }}
          >
            <ListItemText
              primary={
                <Stack direction="row" spacing={0.5} alignItems="center">
                  <AllInclusiveIcon sx={{ fontSize: '1rem' }} />
                  <span style={{ fontSize: '0.875rem' }}>Все</span>
                </Stack>
              }
            />
          </ListItemButton>
        </ListItem>
      </List>
    </Box>
  )

  // Правая панель - Статистика
  const rightPanel = (
    <StatsPanel>
      <Paper elevation={2} sx={{ p: 1.5 }}>
        <Typography
          variant="subtitle2"
          sx={{
            color: 'primary.main',
            fontSize: '0.75rem',
            textTransform: 'uppercase',
            mb: 1,
          }}
        >
          Статистика
        </Typography>
        <Stack spacing={1}>
          <Stack direction="row" justifyContent="space-between">
            <Typography variant="caption" sx={{ fontSize: '0.7rem' }}>Активных:</Typography>
            <Chip label={activeData?.quests?.length || 0} size="small" color="info" sx={{ height: 18, fontSize: '0.65rem' }} />
          </Stack>
          <Stack direction="row" justifyContent="space-between">
            <Typography variant="caption" sx={{ fontSize: '0.7rem' }}>Доступных:</Typography>
            <Chip label={availableData?.quests?.length || 0} size="small" color="success" sx={{ height: 18, fontSize: '0.65rem' }} />
          </Stack>
        </Stack>
      </Paper>
    </StatsPanel>
  )

  if (isLoadingActive || isLoadingAvailable) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1 }}>
          <CircularProgress size={60} />
        </Box>
      </Box>
    )
  }

  if (error) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1, p: 3 }}>
          <Alert severity="error" sx={{ maxWidth: 600 }}>
            <Typography variant="h6">Ошибка загрузки квестов</Typography>
            <Typography variant="body2">
              {(error as unknown as Error)?.message || 'Не удалось загрузить квесты'}
            </Typography>
          </Alert>
        </Box>
      </Box>
    )
  }

  const renderQuests = () => {
    if (filter === 'active') {
      return (
        <Stack spacing={1.5}>
          {activeData?.quests && activeData.quests.length > 0 ? (
            activeData.quests.map((quest) => (
              <QuestProgressItem key={quest.id} quest={quest} onClick={() => handleQuestClick(quest)} />
            ))
          ) : (
            <Paper elevation={1} sx={{ p: 2, textAlign: 'center' }}>
              <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.75rem' }}>
                Нет активных квестов
              </Typography>
            </Paper>
          )}
        </Stack>
      )
    }

    if (filter === 'available') {
      return (
        <List dense>
          {availableData?.quests && availableData.quests.length > 0 ? (
            availableData.quests.map((quest) => (
              <QuestListItem key={quest.id} quest={quest} onClick={() => handleQuestClick(quest)} />
            ))
          ) : (
            <Paper elevation={1} sx={{ p: 2, textAlign: 'center' }}>
              <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.75rem' }}>
                Нет доступных квестов
              </Typography>
            </Paper>
          )}
        </List>
      )
    }

    // all
    return (
      <Stack spacing={2}>
        {activeData?.quests && activeData.quests.length > 0 && (
          <Box>
            <Typography variant="h6" sx={{ fontSize: '0.9rem', mb: 1 }}>Активные</Typography>
            <Stack spacing={1}>
              {activeData.quests.map((quest) => (
                <QuestProgressItem key={quest.id} quest={quest} onClick={() => handleQuestClick(quest)} />
              ))}
            </Stack>
          </Box>
        )}
        {availableData?.quests && availableData.quests.length > 0 && (
          <Box>
            <Typography variant="h6" sx={{ fontSize: '0.9rem', mb: 1 }}>Доступные</Typography>
            <List dense>
              {availableData.quests.map((quest) => (
                <QuestListItem key={quest.id} quest={quest} onClick={() => handleQuestClick(quest)} />
              ))}
            </List>
          </Box>
        )}
      </Stack>
    )
  }

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      <Header />
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2, minHeight: 0, overflowY: 'auto', p: 2 }}>
          <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 'bold', fontSize: '1.1rem' }}>
            Журнал квестов
          </Typography>
          {renderQuests()}
        </Box>
      </GameLayout>
    </Box>
  )
}

