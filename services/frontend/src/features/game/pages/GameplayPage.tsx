/**
 * Основная игровая страница
 * 
 * SPA архитектура с компактной 3-колоночной сеткой:
 * - Левая панель: Действия и меню
 * - Центр: Локация и основной контент
 * - Правая панель: Персонаж и NPC
 */
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { 
  Box, 
  CircularProgress, 
  Alert, 
  Typography, 
  Button,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Divider,
  Chip,
  Collapse,
} from '@mui/material'
import SearchIcon from '@mui/icons-material/Search'
import ChatIcon from '@mui/icons-material/Chat'
import DirectionsWalkIcon from '@mui/icons-material/DirectionsWalk'
import HotelIcon from '@mui/icons-material/Hotel'
import InventoryIcon from '@mui/icons-material/Inventory'
import MemoryIcon from '@mui/icons-material/Memory'
import PsychologyAltIcon from '@mui/icons-material/PsychologyAlt'
import AssignmentIcon from '@mui/icons-material/Assignment'
import PeopleIcon from '@mui/icons-material/People'
import MapIcon from '@mui/icons-material/Map'
import PersonIcon from '@mui/icons-material/Person'
import EventIcon from '@mui/icons-material/Event'
import GpsFixedIcon from '@mui/icons-material/GpsFixed'
import FlashOnIcon from '@mui/icons-material/FlashOn'
import ExitToAppIcon from '@mui/icons-material/ExitToApp'
import BiotechIcon from '@mui/icons-material/Biotech'
import GunIcon from '@mui/icons-material/GpsFixed'
import SwordsIcon from '@mui/icons-material/GpsFixed'
import SchoolIcon from '@mui/icons-material/School'
import PublicIcon from '@mui/icons-material/Public'
import StarIcon from '@mui/icons-material/Star'
import BuildIcon from '@mui/icons-material/Build'
import AttachMoneyIcon from '@mui/icons-material/AttachMoney'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents'
import LeaderboardIcon from '@mui/icons-material/Leaderboard'
import LanguageIcon from '@mui/icons-material/Language'
import FavoriteIcon from '@mui/icons-material/Favorite'
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder'
import CloudIcon from '@mui/icons-material/Cloud'
import SecurityIcon from '@mui/icons-material/Security'
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff'
import DirectionsRunIcon from '@mui/icons-material/DirectionsRun'
import GroupsIcon from '@mui/icons-material/Groups'
import GavelIcon from '@mui/icons-material/Gavel'
import ShieldIcon from '@mui/icons-material/Shield'
import ShowChartIcon from '@mui/icons-material/ShowChart'
import CasinoIcon from '@mui/icons-material/Casino'
import PersonAddIcon from '@mui/icons-material/PersonAdd'
import PublicIcon from '@mui/icons-material/Public'
import AccountBalanceIcon from '@mui/icons-material/AccountBalance'
import CardGiftcardIcon from '@mui/icons-material/CardGiftcard'
import RouterIcon from '@mui/icons-material/Router'
import HistoryIcon from '@mui/icons-material/History'
import Brightness3Icon from '@mui/icons-material/Brightness3'
import BusinessIcon from '@mui/icons-material/Business'
import StoreIcon from '@mui/icons-material/Store'
import ListAltIcon from '@mui/icons-material/ListAlt'
import QueryStatsIcon from '@mui/icons-material/QueryStats'
import ShoppingBasketIcon from '@mui/icons-material/ShoppingBasket'
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong'
import ElectricBoltIcon from '@mui/icons-material/ElectricBolt'
import AttachMoneyIcon from '@mui/icons-material/AttachMoney'
import RouteIcon from '@mui/icons-material/Route'
import SwapHorizIcon from '@mui/icons-material/SwapHoriz'
import CategoryIcon from '@mui/icons-material/Category'
import SchoolIcon from '@mui/icons-material/School'
import FamilyRestroomIcon from '@mui/icons-material/FamilyRestroom'
import BarChartIcon from '@mui/icons-material/BarChart'
import LocalShippingIcon from '@mui/icons-material/LocalShipping'
import AccountBalanceIcon from '@mui/icons-material/AccountBalance'
import DirectionsWalkIcon from '@mui/icons-material/DirectionsWalk'
import WorkIcon from '@mui/icons-material/Work'
import MenuBookIcon from '@mui/icons-material/MenuBook'
import BadgeIcon from '@mui/icons-material/Badge'
import ShuffleIcon from '@mui/icons-material/Shuffle'
import LanguageIcon from '@mui/icons-material/Language'
import StoreIcon from '@mui/icons-material/Store'
import ListAltIcon from '@mui/icons-material/ListAlt'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore'
import ExpandLessIcon from '@mui/icons-material/ExpandLess'
import { useGetInitialState } from '@/api/generated/game/game-initial-state/game-initial-state'
import { useGetTutorialSteps } from '@/api/generated/game/game-initial-state/game-initial-state'
import {
  useExploreLocation,
  useRestAction,
} from '@/api/generated/actions/gameplay/gameplay'
import { ActionResultDialog } from '@/features/gameplay/actions/components'
import {
  LocationInfo,
  CharacterState,
  QuestCard,
  TutorialSteps,
  StartingEquipment,
} from '../components'
import { useGameState, useTutorialState } from '../hooks/useGameState'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import type { GameNPC, GameQuest, GameAction } from '@/api/generated/game/models'
import HubIcon from '@mui/icons-material/Hub'
import BalanceIcon from '@mui/icons-material/Balance'
import CrisisAlertIcon from '@mui/icons-material/CrisisAlert'
import AssignmentTurnedInIcon from '@mui/icons-material/AssignmentTurnedIn'
import AnalyticsIcon from '@mui/icons-material/Analytics'
import TimelineIcon from '@mui/icons-material/Timeline'
import DisplaySettingsIcon from '@mui/icons-material/DisplaySettings'
import AccessTimeIcon from '@mui/icons-material/AccessTime'
import GridViewIcon from '@mui/icons-material/GridView'
import SportsEsportsIcon from '@mui/icons-material/SportsEsports'
import SettingsSuggestIcon from '@mui/icons-material/SettingsSuggest'
import ReportProblemIcon from '@mui/icons-material/ReportProblem'
import SurroundSoundIcon from '@mui/icons-material/SurroundSound'
import StorageIcon from '@mui/icons-material/Storage'
import SensorsIcon from '@mui/icons-material/Sensors'

export function GameplayPage() {
  const navigate = useNavigate()
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)
  const completeTutorial = useGameState((state) => state.completeTutorial)
  const tutorialState = useTutorialState()
  // Получаем данные из store (сохранены после POST /game/start)
  const characterState = useGameState((state) => state.characterState)
  const startingEquipment = useGameState((state) => state.startingEquipment)

  // Состояние для будущих диалогов и модальных окон
  const [_selectedNPC, setSelectedNPC] = useState<GameNPC | null>(null)
  const [_selectedQuest, setSelectedQuest] = useState<GameQuest | null>(null)
  const [showNPCList, setShowNPCList] = useState(true)
  const [showQuestDetails, setShowQuestDetails] = useState(true)
  const [showTutorial, setShowTutorial] = useState(true)
  const [actionResultDialog, setActionResultDialog] = useState<{
    open: boolean
    title: string
    success: boolean
    result?: any
  }>({ open: false, title: '', success: false })

  // Загрузка начального состояния игры
  const {
    data: gameState,
    isLoading: isLoadingState,
    error: stateError,
  } = useGetInitialState(
    { characterId: selectedCharacterId || '' },
    {
      query: {
        enabled: !!selectedCharacterId,
      },
    }
  )

  // Загрузка шагов туториала (если включен)
  const {
    data: tutorialData,
    isLoading: isLoadingTutorial,
  } = useGetTutorialSteps(
    { characterId: selectedCharacterId || '' },
    {
      query: {
        enabled: !!selectedCharacterId && tutorialState.enabled && !tutorialState.completed,
      },
    }
  )

  // Редирект на выбор персонажа если не выбран персонаж
  useEffect(() => {
    if (!selectedCharacterId) {
      navigate('/characters')
    }
  }, [selectedCharacterId, navigate])

  const handleNPCSelect = (npc: GameNPC) => {
    setSelectedNPC(npc)
    console.log('Selected NPC:', npc)
    // TODO: Открыть диалог с NPC
  }

  const handleQuestSelect = (quest: GameQuest) => {
    setSelectedQuest(quest)
    console.log('Selected Quest:', quest)
    // TODO: Открыть детали квеста
  }

  // Mutations для действий
  const { mutate: exploreLocation } = useExploreLocation()
  const { mutate: restAction } = useRestAction()

  const handleActionClick = (action: GameAction) => {
    console.log('Action clicked:', action)

    if (!selectedCharacterId) return

    // Выполнение действия в зависимости от типа
    switch (action.id) {
      case 'look-around':
        exploreLocation(
          { data: { characterId: selectedCharacterId, locationId: gameState?.location?.id || '' } },
          {
            onSuccess: (result) => {
              setActionResultDialog({
                open: true,
                title: 'Осмотр локации',
                success: true,
                result,
              })
            },
            onError: (err) => console.error('Explore error:', err),
          }
        )
        break

      case 'rest':
        restAction(
          { data: { characterId: selectedCharacterId, duration: 60 } },
          {
            onSuccess: (result) => {
              setActionResultDialog({
                open: true,
                title: 'Отдых',
                success: true,
                result,
              })
            },
            onError: (err) => console.error('Rest error:', err),
          }
        )
        break

      default:
        console.log('Action not implemented:', action.id)
    }
  }

  const getActionIcon = (actionId: string) => {
    switch (actionId) {
      case 'look-around': return <SearchIcon />
      case 'talk-to-npc': return <ChatIcon />
      case 'move': return <DirectionsWalkIcon />
      case 'rest': return <HotelIcon />
      case 'inventory': return <InventoryIcon />
      default: return null
    }
  }

  const handleTutorialComplete = () => {
    completeTutorial()
    console.log('Tutorial completed!')
  }

  const handleTutorialSkip = () => {
    completeTutorial()
    console.log('Tutorial skipped!')
  }

  if (isLoadingState || isLoadingTutorial) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            flex: 1,
          }}
        >
          <CircularProgress size={60} />
        </Box>
      </Box>
    )
  }

  if (stateError || !gameState) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            flex: 1,
            p: 3,
          }}
        >
          <Alert severity="error" sx={{ maxWidth: 600 }}>
            <Typography variant="h6">Ошибка загрузки игрового состояния</Typography>
            <Typography variant="body2">
              {(stateError as unknown as Error)?.message || 'Не удалось загрузить игровое состояние'}
            </Typography>
          </Alert>
        </Box>
      </Box>
    )
  }

  // Левая панель - Действия
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
        Действия
      </Typography>

      <Box sx={{ flex: 1, minHeight: 0, overflowY: 'auto' }}>
        <List dense>
          {/* Действия из API */}
          {gameState.availableActions.map((action) => (
            <ListItem key={action.id} disablePadding>
              <ListItemButton
                onClick={() => handleActionClick(action)}
                disabled={!action.enabled}
                sx={{
                  borderRadius: 1,
                  mb: 0.5,
                  '&:hover': {
                    bgcolor: 'rgba(0, 247, 255, 0.1)',
                  },
                }}
              >
                <ListItemIcon sx={{ minWidth: 36 }}>
                  {getActionIcon(action.id)}
                </ListItemIcon>
                <ListItemText
                  primary={action.label}
                  secondary={action.description}
                  primaryTypographyProps={{ fontSize: '0.875rem' }}
                  secondaryTypographyProps={{ fontSize: '0.75rem' }}
                />
              </ListItemButton>
            </ListItem>
          ))}

          {/* Дополнительное действие - Импланты */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/implants')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <MemoryIcon />
              </ListItemIcon>
              <ListItemText
                primary="Импланты"
                secondary="Управление имплантами"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Дополнительное действие - Киберпсихоз */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/cyberpsychosis')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <PsychologyAltIcon />
              </ListItemIcon>
              <ListItemText
                primary="Киберпсихоз"
                secondary="Мониторинг человечности"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Дополнительное действие - Квесты */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/quests')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <AssignmentIcon />
              </ListItemIcon>
              <ListItemText
                primary="Квесты"
                secondary="Журнал квестов"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Дополнительное действие - NPCs */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/npcs')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <PeopleIcon />
              </ListItemIcon>
              <ListItemText
                primary="NPCs"
                secondary="Персонажи в локации"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Дополнительное действие - Локации */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/locations')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <MapIcon />
              </ListItemIcon>
              <ListItemText
                primary="Локации"
                secondary="Карта и перемещение"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Дополнительное действие - Персонаж */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/character')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <PersonIcon />
              </ListItemIcon>
              <ListItemText
                primary="Персонаж"
                secondary="Статус и характеристики"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Дополнительное действие - События */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/events')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <EventIcon />
              </ListItemIcon>
              <ListItemText
                primary="События"
                secondary="Случайные события"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Дополнительное действие - Оружие */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/weapons')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <GpsFixedIcon />
              </ListItemIcon>
              <ListItemText
                primary="Оружие"
                secondary="Каталог и Mastery"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Дополнительное действие - Способности */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/abilities')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <FlashOnIcon />
              </ListItemIcon>
              <ListItemText
                primary="Способности"
                secondary="Q/E/R система"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Abilities Catalog */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/abilities-catalog')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <FlashOnIcon />
              </ListItemIcon>
              <ListItemText
                primary="Каталог способностей"
                secondary="Все способности"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Combat Roles */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/combat-roles')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <PersonIcon />
              </ListItemIcon>
              <ListItemText
                primary="Боевые роли"
                secondary="Tank/DPS/Support"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* AI Enemies */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/enemies')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <GpsFixedIcon />
              </ListItemIcon>
              <ListItemText
                primary="AI Враги"
                secondary="Противники и боссы"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Combos & Synergies */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/combos')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <FlashOnIcon />
              </ListItemIcon>
              <ListItemText
                primary="Комбо и Синергии"
                secondary="Боевые связки"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Extraction */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/extraction')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <ExitToAppIcon />
              </ListItemIcon>
              <ListItemText
                primary="Экстракция"
                secondary="TARKOV стиль"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Implants Catalog */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/implants-catalog')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <BiotechIcon />
              </ListItemIcon>
              <ListItemText
                primary="Каталог имплантов"
                secondary="Cyberpunk импланты"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Shooting */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/shooting')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <GunIcon />
              </ListItemIcon>
              <ListItemText
                primary="Система стрельбы"
                secondary="Combat Shooting"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Combat System */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/combat-system')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <SwordsIcon />
              </ListItemIcon>
              <ListItemText
                primary="Боевая система"
                secondary="Текстовый бой"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Classes Progression */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/classes')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <SchoolIcon />
              </ListItemIcon>
              <ListItemText
                primary="Классы"
                secondary="13 классов + подклассы"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Global State System */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/global-state')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <PublicIcon />
              </ListItemIcon>
              <ListItemText
                primary="Глобальное состояние"
                secondary="Event Sourcing"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Global State Extended */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/technical/global-state-extended')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 255, 173, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <PublicIcon sx={{ color: 'success.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Global State+"
                secondary="Sync & conflicts"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'success.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* UI Systems */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/technical/ui-systems')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 255, 255, 0.12)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <GridViewIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="UI Systems"
                secondary="Login, серверы, HUD"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Disaster Recovery */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/technical/disaster-recovery')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 215, 0, 0.14)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <SecurityIcon sx={{ color: 'warning.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Disaster Recovery"
                secondary="Backups, failover"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'warning.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Configuration Management */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/technical/configuration-management')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 255, 255, 0.12)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <SettingsSuggestIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Config Management"
                secondary="Версии, секреты"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Incident Response */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/technical/incident-response')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 64, 129, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <ReportProblemIcon sx={{ color: 'error.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Incident Response"
                secondary="War-room, RCA"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'error.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* AI Algorithms */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/internal/ai-algorithms')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(224, 93, 255, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <MemoryIcon sx={{ color: 'secondary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="AI Algorithms"
                secondary="Romance, NPC, Decisions"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'secondary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Starter Content */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/narrative/starter-content')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 105, 180, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <MenuBookIcon sx={{ color: 'secondary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Стартовый лор"
                secondary="Origins, классовые квесты"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'secondary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Regional Quests */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/narrative/regional-quests')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.12)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <PublicIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Региональные квесты"
                secondary="Daily, weekly, world"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Lore Database */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/lore/database')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.12)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <MenuBookIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Лор база"
                secondary="Города, фракции, события"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Reputation Tiers */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/reputation')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <StarIcon />
              </ListItemIcon>
              <ListItemText
                primary="Репутация"
                secondary="8 тиров с фракциями"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Crafting */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/crafting')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <BuildIcon />
              </ListItemIcon>
              <ListItemText
                primary="Крафт"
                secondary="Создание предметов"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Currencies */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/currencies')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <AttachMoneyIcon />
              </ListItemIcon>
              <ListItemText
                primary="Валюты"
                secondary="12 валют"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Skills */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/skills')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <TrendingUpIcon />
              </ListItemIcon>
              <ListItemText
                primary="Навыки"
                secondary="Система прогрессии"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Perks */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/perks')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <EmojiEventsIcon />
              </ListItemIcon>
              <ListItemText
                primary="Перки"
                secondary="Пассивные бонусы"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* League System */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/league')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <LeaderboardIcon />
              </ListItemIcon>
              <ListItemText
                primary="Лиги"
                secondary="Сезоны 2020-2093"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* World State */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/world-state')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <LanguageIcon />
              </ListItemIcon>
              <ListItemText
                primary="Состояние мира"
                secondary="Player Impact"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Relationships */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/relationships')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <FavoriteIcon />
              </ListItemIcon>
              <ListItemText
                primary="Отношения"
                secondary="С NPC и игроками"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Romance */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/romance-system')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 20, 147, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <FavoriteBorderIcon sx={{ color: 'error.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Romance System"
                secondary="9 стадий, ревность, события"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'error.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Cyberspace */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/cyberspace')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <CloudIcon sx={{ color: 'primary.light' }} />
              </ListItemIcon>
              <ListItemText
                primary="Киберпространство"
                secondary="Полноценный режим"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'primary.light' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Hacking */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/hacking')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 165, 0, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <SecurityIcon sx={{ color: 'warning.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Хакинг"
                secondary="Quickhacks CP2077"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'warning.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Stealth */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/stealth')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(158, 158, 158, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <VisibilityOffIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Скрытность"
                secondary="Deus Ex / Hitman"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Freerun */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/freerun')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(76, 175, 80, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <DirectionsRunIcon sx={{ color: 'success.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Паркур"
                secondary="Mirror's Edge / AC"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'success.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Faction Wars */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/faction-wars')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(244, 67, 54, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <GroupsIcon sx={{ color: 'error.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Faction Wars"
                secondary="EVE / WoW массовые PvP"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'error.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Auction House */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/auction-house')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 193, 7, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <GavelIcon sx={{ color: 'warning.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Auction House"
                secondary="WoW / GW2 / FFXIV"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'warning.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Stock Exchange */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/stock-exchange')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <ShowChartIcon sx={{ color: 'primary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Stock Exchange"
                secondary="EVE / NYSE / NASDAQ"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'primary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* D&D Checks */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/dnd-checks')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(76, 175, 80, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <CasinoIcon sx={{ color: 'success.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="D&D Checks"
                secondary="BG3 / D&D 5e"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'success.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* NPC Hiring */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/npc-hiring')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <PersonAddIcon sx={{ color: 'primary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="NPC Hiring"
                secondary="RimWorld / Kenshi"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'primary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Global Events */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/global-events')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <PublicIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Global Events"
                secondary="Timeline 2020-2093"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Player Market */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/player-market')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <AccountBalanceIcon sx={{ color: 'primary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Player Market"
                secondary="EVE / GW2 / Albion"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'primary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Loot Tables */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/loot-tables')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 193, 7, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <CardGiftcardIcon sx={{ color: 'warning.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Loot Tables"
                secondary="Tarkov / Diablo"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'warning.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Hacking Networks */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/hacking-networks')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 165, 0, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <RouterIcon sx={{ color: 'warning.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Hacking Networks"
                secondary="CP2077 / Watch Dogs"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'warning.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Events 2020-2040 */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/events-2020-2040')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 82, 82, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <HistoryIcon sx={{ color: 'error.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Events 2020-2040"
                secondary="Arasaka vs Militech"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'error.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Events 2040-2060 */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/events-2040-2060')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(139, 0, 0, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <Brightness3Icon sx={{ color: '#8B0000' }} />
              </ListItemIcon>
              <ListItemText
                primary="Events 2040-2060"
                secondary="Time of the Red"
                primaryTypographyProps={{ fontSize: '0.875rem', sx: { color: '#8B0000' } }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Events 2060-2077 */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/events-2060-2077')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 215, 0, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <BusinessIcon sx={{ color: '#FFD700' }} />
              </ListItemIcon>
              <ListItemText
                primary="Events 2060-2077"
                secondary="Night City"
                primaryTypographyProps={{ fontSize: '0.875rem', sx: { color: '#FFD700' } }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Trading Guilds */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/trading-guilds')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(76, 175, 80, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <StoreIcon sx={{ color: 'success.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Trading Guilds"
                secondary="EVE Online / WOW"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'success.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Trading Routes */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/trading-routes')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <RouteIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Trading Routes"
                secondary="EVE Online / KENSHI"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Currency Exchange */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/currency-exchange')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <SwapHorizIcon sx={{ color: 'primary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Currency Exchange"
                secondary="Forex / EVE"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'primary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Resources Catalog */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/resources-catalog')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 193, 7, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <CategoryIcon sx={{ color: 'warning.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Resources Catalog"
                secondary="Крафт / Торговля"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'warning.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Mentorship */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/mentorship')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <SchoolIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Наставничество"
                secondary="Наставничество"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Family Relationships */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/family-relationships')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <FamilyRestroomIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Family Relationships"
                secondary="Семейные связи"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Economy Analytics */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/economy-analytics')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <BarChartIcon sx={{ color: 'primary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Economy Analytics"
                secondary="TradingView / Bloomberg"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'primary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Production Chains */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/production-chains')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <HubIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Production Chains"
                secondary="Supply chain / AI"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Contracts */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/contracts')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 121, 255, 0.18)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <GavelIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Contracts"
                secondary="Escrow & disputes"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Anti-Cheat */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/admin/anti-cheat')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 255, 173, 0.18)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <ShieldIcon sx={{ color: 'success.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Anti-Cheat"
                secondary="Reports & bans"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'success.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Moderation */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/admin/moderation')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 127, 255, 0.18)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <BalanceIcon sx={{ color: 'primary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Moderation"
                secondary="Cases & SLA"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'primary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* MVP Content */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/meta/mvp-content')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 128, 0, 0.18)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <AssignmentTurnedInIcon sx={{ color: 'secondary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="MVP Content"
                secondary="Endpoints & data"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'secondary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Logistics */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/logistics')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 193, 7, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <LocalShippingIcon sx={{ color: 'warning.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Logistics"
                secondary="Доставка / Конвой"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'warning.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Investments */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/investments')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(5, 255, 161, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <AccountBalanceIcon sx={{ color: 'success.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Investments"
                secondary="Инвестиции / ROI"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'success.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Travel Events */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/gameplay/travel-events')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.12)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <MenuBookIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Travel Events"
                secondary="Региональные события"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Matchmaking */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/gameplay/matchmaking')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(224, 93, 255, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <SportsEsportsIcon sx={{ color: 'secondary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Matchmaking Ops"
                secondary="Очереди, ready-check"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'secondary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Player Orders Extended */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/player-orders-extended')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <WorkIcon sx={{ color: 'primary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Player Orders"
                secondary="Заказы / Escrow"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'primary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Mentorship Extended */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/mentorship-extended')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <MenuBookIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Mentorship Ext"
                secondary="6 типов / Abilities"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* NPC Hiring Extended */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/npc-hiring-extended')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(5, 255, 161, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <BadgeIcon sx={{ color: 'success.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="NPC Hiring Ext"
                secondary="7 типов / KPI"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'success.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Random Events Extended */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/random-events-extended')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <ShuffleIcon sx={{ color: 'primary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Random Events"
                secondary="73 события / 6 периодов"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'primary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* World Events Framework */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/world-events-framework')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 42, 109, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <LanguageIcon sx={{ color: 'secondary.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="World Events"
                secondary="7 эпох / D&D d100"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'secondary.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Auction House Core */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/auction-house-core')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 193, 7, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <StoreIcon sx={{ color: 'warning.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Auction House"
                secondary="Bid/Buyout / Orders"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'warning.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Auction House Search */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/auction-house-search')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.12)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <ListAltIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Auction Search"
                secondary="Фильтры / Сортировка"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Auction House Orders */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/auction-house-orders')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(33, 150, 243, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <ListAltIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Auction Orders"
                secondary="Buy/Sell / Auto"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Auction House History */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/auction-house-history')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 255, 161, 0.12)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <QueryStatsIcon sx={{ color: 'success.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Auction History"
                secondary="Charts / Trends"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'success.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Player Market Core */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/player-market-core')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 152, 0, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <ShoppingBasketIcon sx={{ color: 'warning.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Player Market"
                secondary="Order book / Orders"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'warning.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Player Market Orders */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/player-market-orders')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 184, 255, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <ReceiptLongIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Market Orders"
                secondary="Create / History"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Player Market Execution */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/player-market-execution')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(80, 227, 194, 0.15)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <ElectricBoltIcon sx={{ color: 'success.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Execution"
                secondary="Trades / Matching"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'success.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Pricing */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/pricing')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.12)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <AttachMoneyIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Pricing"
                secondary="Formulas / Trends"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Classes Progression */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/classes')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <SchoolIcon />
              </ListItemIcon>
              <ListItemText
                primary="Классы"
                secondary="13 классов + подклассы"
                primaryTypographyProps={{ fontSize: '0.875rem' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Progression Detailed */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/game/progression-detailed')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 255, 200, 0.12)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <AnalyticsIcon sx={{ color: 'primary.light' }} />
              </ListItemIcon>
              <ListItemText
                primary="Прогрессия"
                secondary="Атрибуты, синергии, caps"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'primary.light' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Narrative Coherence */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/narrative/coherence')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 127, 255, 0.12)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <TimelineIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Narrative Coherence"
                secondary="Threads & risks"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Voice Chat */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/technical/voice-chat')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 255, 170, 0.14)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <SurroundSoundIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Voice Chat"
                secondary="Каналы и участники"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Realtime Zones */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/technical/realtime-server-zones')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(0, 255, 255, 0.12)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <StorageIcon sx={{ color: 'info.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Realtime Zones"
                secondary="Инстансы и эвакуация"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'info.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>

          {/* Session Lifecycle */}
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate('/technical/session-lifecycle')}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&:hover': {
                  bgcolor: 'rgba(255, 215, 0, 0.14)',
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>
                <SensorsIcon sx={{ color: 'warning.main' }} />
              </ListItemIcon>
              <ListItemText
                primary="Session Lifecycle"
                secondary="Heartbeat, AFK"
                primaryTypographyProps={{ fontSize: '0.875rem', color: 'warning.main' }}
                secondaryTypographyProps={{ fontSize: '0.75rem' }}
              />
            </ListItemButton>
          </ListItem>
        </List>
      </Box>
    </Box>
  )

  // Правая панель - Персонаж и NPC
  const rightPanel = (
    <StatsPanel>
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, height: '100%', minHeight: 0 }}>
        {/* Состояние персонажа из API (POST /game/start response) */}
        {characterState && <CharacterState state={characterState} />}

        {/* Стартовое снаряжение из API (POST /game/start response) */}
        {startingEquipment && startingEquipment.length > 0 && (
          <StartingEquipment equipment={startingEquipment} />
        )}

        <Divider sx={{ my: 1 }} />

        {/* Список NPC - компактный */}
        <Box>
          <Button
            fullWidth
            size="small"
            onClick={() => setShowNPCList(!showNPCList)}
            endIcon={showNPCList ? <ExpandLessIcon /> : <ExpandMoreIcon />}
            sx={{
              justifyContent: 'space-between',
              color: 'primary.main',
              fontSize: '0.875rem',
              textTransform: 'uppercase',
              mb: 1,
            }}
          >
            NPC ({gameState.availableNPCs.length})
          </Button>
          <Collapse in={showNPCList}>
            <List dense>
              {gameState.availableNPCs.map((npc) => (
                <ListItem key={npc.id} disablePadding>
                  <ListItemButton
                    onClick={() => handleNPCSelect(npc)}
                    sx={{
                      borderRadius: 1,
                      mb: 0.5,
                      '&:hover': {
                        bgcolor: 'rgba(0, 247, 255, 0.1)',
                      },
                    }}
                  >
                    <ListItemText
                      primary={npc.name}
                      secondary={npc.type}
                      primaryTypographyProps={{ fontSize: '0.8rem', fontWeight: 'bold' }}
                      secondaryTypographyProps={{ fontSize: '0.7rem' }}
                    />
                    {npc.availableQuests && npc.availableQuests.length > 0 && (
                      <Chip
                        label={npc.availableQuests.length}
                        size="small"
                        color="success"
                        sx={{ height: 18, fontSize: '0.65rem' }}
                      />
                    )}
                  </ListItemButton>
                </ListItem>
              ))}
            </List>
          </Collapse>
        </Box>
      </Box>
    </StatsPanel>
  )

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      <Header />
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        {/* Центральная область - Локация и квест */}
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2, minHeight: 0, overflowY: 'auto' }}>
          {/* Туториал (если включен) - сворачиваемый */}
          {tutorialState.enabled && !tutorialState.completed && tutorialData && (
            <Box>
              <Button
                fullWidth
                size="small"
                onClick={() => setShowTutorial(!showTutorial)}
                endIcon={showTutorial ? <ExpandLessIcon /> : <ExpandMoreIcon />}
                sx={{
                  justifyContent: 'space-between',
                  color: 'warning.main',
                  fontSize: '0.875rem',
                  textTransform: 'uppercase',
                  mb: 1,
                }}
              >
                📚 Туториал
              </Button>
              <Collapse in={showTutorial}>
                <TutorialSteps
                  data={tutorialData}
                  onComplete={handleTutorialComplete}
                  onSkip={handleTutorialSkip}
                />
              </Collapse>
            </Box>
          )}

          {/* Информация о локации */}
          <LocationInfo location={gameState.location} />

          {/* Первый квест - компактно */}
          {gameState.firstQuest && (
            <Box>
              <Button
                fullWidth
                size="small"
                onClick={() => setShowQuestDetails(!showQuestDetails)}
                endIcon={showQuestDetails ? <ExpandLessIcon /> : <ExpandMoreIcon />}
                sx={{
                  justifyContent: 'space-between',
                  color: 'primary.main',
                  fontSize: '0.875rem',
                  textTransform: 'uppercase',
                  mb: 1,
                }}
              >
                Доступный квест
              </Button>
              <Collapse in={showQuestDetails}>
                <QuestCard quest={gameState.firstQuest} onSelect={handleQuestSelect} />
              </Collapse>
            </Box>
          )}
        </Box>
      </GameLayout>

      {/* Диалог результата действия */}
      <ActionResultDialog
        open={actionResultDialog.open}
        onClose={() => setActionResultDialog({ ...actionResultDialog, open: false })}
        title={actionResultDialog.title}
        success={actionResultDialog.success}
        result={actionResultDialog.result}
      />
    </Box>
  )
}

