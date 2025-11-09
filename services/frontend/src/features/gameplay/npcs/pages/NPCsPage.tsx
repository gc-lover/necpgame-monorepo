/**
 * Страница NPCs
 * 
 * SPA архитектура с компактной 3-колоночной сеткой:
 * - Левая панель: Фильтры по типу NPC
 * - Центр: Список NPC или диалог
 * - Правая панель: Детали выбранного NPC
 */
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
  Stack,
  Grid,
  Button,
} from '@mui/material'
import AllInclusiveIcon from '@mui/icons-material/AllInclusive'
import StoreIcon from '@mui/icons-material/Store'
import AssignmentIcon from '@mui/icons-material/Assignment'
import PersonIcon from '@mui/icons-material/Person'
import WarningIcon from '@mui/icons-material/Warning'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import {
  useGetNPCs,
  useGetNPCDetails,
  useGetNPCDialogue,
  useInteractWithNPC,
  useRespondToDialogue,
} from '@/api/generated/npcs/npcs/npcs'
import { NPCCard, DialogueBox, NPCDetailsPanel } from '../components'
import { useGameState } from '@/features/game/hooks/useGameState'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import type { Npc, NpcType } from '@/api/generated/npcs/models'

export function NPCsPage() {
  const navigate = useNavigate()
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)
  const [typeFilter, setTypeFilter] = useState<NpcType | undefined>(undefined)
  const [selectedNPC, setSelectedNPC] = useState<Npc | null>(null)
  const [showDialogue, setShowDialogue] = useState(false)

  // Загрузка списка NPCs из OpenAPI
  const {
    data: npcsData,
    isLoading: isLoadingNPCs,
    error: npcsError,
  } = useGetNPCs(
    { characterId: selectedCharacterId || '', type: typeFilter },
    { query: { enabled: !!selectedCharacterId } }
  )

  // Загрузка деталей выбранного NPC
  const {
    data: npcDetails,
    isLoading: isLoadingDetails,
  } = useGetNPCDetails(
    { npcId: selectedNPC?.id || '', characterId: selectedCharacterId || '' },
    { query: { enabled: !!selectedNPC && !!selectedCharacterId } }
  )

  // Загрузка диалога с NPC
  const {
    data: dialogueData,
    isLoading: isLoadingDialogue,
  } = useGetNPCDialogue(
    { npcId: selectedNPC?.id || '', characterId: selectedCharacterId || '' },
    { query: { enabled: showDialogue && !!selectedNPC && !!selectedCharacterId } }
  )

  // Mutations
  const { mutate: interactWithNPC } = useInteractWithNPC()
  const { mutate: respondToDialogue } = useRespondToDialogue()

  useEffect(() => {
    if (!selectedCharacterId) {
      navigate('/characters')
    }
  }, [selectedCharacterId, navigate])

  const handleNPCClick = (npc: Npc) => {
    setSelectedNPC(npc)
    setShowDialogue(false)
  }

  const handleStartDialogue = () => {
    if (!selectedNPC || !selectedCharacterId) return
    setShowDialogue(true)
  }

  const handleInteract = (action: string) => {
    if (!selectedNPC || !selectedCharacterId) return
    console.log('Interact with NPC:', selectedNPC.id, action)
    interactWithNPC(
      { npcId: selectedNPC.id, data: { characterId: selectedCharacterId, action } },
      {
        onSuccess: (result) => console.log('Interaction result:', result),
        onError: (err) => console.error('Interaction error:', err),
      }
    )
  }

  const handleDialogueOption = (optionId: string) => {
    if (!selectedNPC || !selectedCharacterId) return
    console.log('Dialogue option selected:', optionId)
    respondToDialogue(
      { npcId: selectedNPC.id, data: { characterId: selectedCharacterId, optionId } },
      {
        onSuccess: () => console.log('Response sent'),
        onError: (err) => console.error('Response error:', err),
      }
    )
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
        Типы NPC
      </Typography>

      <List dense>
        <ListItem disablePadding>
          <ListItemButton
            selected={!typeFilter}
            onClick={() => setTypeFilter(undefined)}
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
                  <AllInclusiveIcon sx={{ fontSize: '1rem' }} />
                  <span style={{ fontSize: '0.875rem' }}>Все</span>
                </Stack>
              }
              secondary={npcsData?.npcs?.length || 0}
              secondaryTypographyProps={{ fontSize: '0.7rem' }}
            />
          </ListItemButton>
        </ListItem>

        <ListItem disablePadding>
          <ListItemButton
            selected={typeFilter === 'trader'}
            onClick={() => setTypeFilter('trader')}
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
                  <StoreIcon sx={{ fontSize: '1rem' }} />
                  <span style={{ fontSize: '0.875rem' }}>Торговцы</span>
                </Stack>
              }
              secondaryTypographyProps={{ fontSize: '0.7rem' }}
            />
          </ListItemButton>
        </ListItem>

        <ListItem disablePadding>
          <ListItemButton
            selected={typeFilter === 'quest_giver'}
            onClick={() => setTypeFilter('quest_giver')}
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
                  <AssignmentIcon sx={{ fontSize: '1rem' }} />
                  <span style={{ fontSize: '0.875rem' }}>Квестодатели</span>
                </Stack>
              }
              secondaryTypographyProps={{ fontSize: '0.7rem' }}
            />
          </ListItemButton>
        </ListItem>

        <ListItem disablePadding>
          <ListItemButton
            selected={typeFilter === 'citizen'}
            onClick={() => setTypeFilter('citizen')}
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
                  <PersonIcon sx={{ fontSize: '1rem' }} />
                  <span style={{ fontSize: '0.875rem' }}>Граждане</span>
                </Stack>
              }
              secondaryTypographyProps={{ fontSize: '0.7rem' }}
            />
          </ListItemButton>
        </ListItem>

        <ListItem disablePadding>
          <ListItemButton
            selected={typeFilter === 'enemy'}
            onClick={() => setTypeFilter('enemy')}
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
                  <WarningIcon sx={{ fontSize: '1rem' }} color="error" />
                  <span style={{ fontSize: '0.875rem' }}>Враги</span>
                </Stack>
              }
              secondaryTypographyProps={{ fontSize: '0.7rem' }}
            />
          </ListItemButton>
        </ListItem>
      </List>
    </Box>
  )

  // Правая панель - Детали NPC
  const rightPanel = (
    <StatsPanel>
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, height: '100%', minHeight: 0, overflowY: 'auto' }}>
        {selectedNPC ? (
          <>
            <NPCDetailsPanel npc={npcDetails || selectedNPC} />
            {!showDialogue && (
              <Stack spacing={1}>
                <Button
                  variant="contained"
                  size="small"
                  onClick={handleStartDialogue}
                  sx={{ fontSize: '0.75rem' }}
                >
                  Поговорить
                </Button>
                <Button
                  variant="outlined"
                  size="small"
                  onClick={() => navigate(`/game/trading?vendorId=${selectedNPC.id}`)}
                  disabled={selectedNPC.type !== 'trader'}
                  sx={{ fontSize: '0.75rem' }}
                >
                  Торговать
                </Button>
              </Stack>
            )}
          </>
        ) : (
          <Typography variant="caption" sx={{ fontSize: '0.75rem', color: 'text.secondary', textAlign: 'center' }}>
            Выберите NPC
          </Typography>
        )}
      </Box>
    </StatsPanel>
  )

  if (isLoadingNPCs) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1 }}>
          <CircularProgress size={60} />
        </Box>
      </Box>
    )
  }

  if (npcsError) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1, p: 3 }}>
          <Alert severity="error" sx={{ maxWidth: 600 }}>
            <Typography variant="h6">Ошибка загрузки NPCs</Typography>
            <Typography variant="body2">
              {(npcsError as unknown as Error)?.message || 'Не удалось загрузить список NPCs'}
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
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2, minHeight: 0, overflowY: 'auto', p: 2 }}>
          {showDialogue && selectedNPC ? (
            <>
              <Button
                startIcon={<ArrowBackIcon />}
                onClick={() => setShowDialogue(false)}
                size="small"
                sx={{ alignSelf: 'flex-start', fontSize: '0.75rem' }}
              >
                Назад к списку
              </Button>
              {isLoadingDialogue ? (
                <CircularProgress size={40} />
              ) : dialogueData ? (
                <DialogueBox
                  dialogue={dialogueData}
                  onSelectOption={handleDialogueOption}
                  npcName={selectedNPC.name}
                />
              ) : (
                <Typography variant="body2">Нет диалога</Typography>
              )}
            </>
          ) : (
            <>
              <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 'bold', fontSize: '1.1rem' }}>
                NPCs в локации
              </Typography>
              <Grid container spacing={1.5}>
                {npcsData?.npcs && npcsData.npcs.length > 0 ? (
                  npcsData.npcs.map((npc) => (
                    <Grid item xs={12} sm={6} md={4} key={npc.id}>
                      <NPCCard npc={npc} onClick={() => handleNPCClick(npc)} />
                    </Grid>
                  ))
                ) : (
                  <Grid item xs={12}>
                    <Typography variant="caption" sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
                      Нет NPCs в этой категории
                    </Typography>
                  </Grid>
                )}
              </Grid>
            </>
          )}
        </Box>
      </GameLayout>
    </Box>
  )
}

