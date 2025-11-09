/**
 * Страница управления имплантами
 * 
 * SPA архитектура с компактной 3-колоночной сеткой:
 * - Левая панель: Меню типов имплантов
 * - Центр: Сетка слотов
 * - Правая панель: Лимиты и энергия
 */
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Chip,
  CircularProgress,
  FormControl,
  InputLabel,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  MenuItem,
  Select,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import GpsFixedIcon from '@mui/icons-material/GpsFixed'
import PsychologyIcon from '@mui/icons-material/Psychology'
import ShieldIcon from '@mui/icons-material/Shield'
import DirectionsRunIcon from '@mui/icons-material/DirectionsRun'
import SettingsIcon from '@mui/icons-material/Settings'
import {
  useGetImplantSlots,
  useGetImplantLimits,
  useGetEnergyPool,
  useCheckCompatibility,
} from '@/api/generated/gameplay/combat/combat/combat'
import {
  ImplantSlotsList,
  ImplantLimitInfo,
  EnergyPoolDisplay,
} from '../components'
import { useGameState } from '@/features/game/hooks/useGameState'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import type { CompatibilityResult, SlotInfo } from '@/api/generated/gameplay/combat/models'

type ImplantType = 'combat' | 'tactical' | 'defensive' | 'mobility' | 'os'

export function ImplantsPage() {
  const navigate = useNavigate()
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)

  const [selectedType, setSelectedType] = useState<ImplantType>('combat')
  const [_selectedSlot, setSelectedSlot] = useState<SlotInfo | null>(null)
  const [compatibilityImplantId, setCompatibilityImplantId] = useState('')
  const [compatibilitySlot, setCompatibilitySlot] = useState<ImplantType>('combat')
  const [compatibilityResult, setCompatibilityResult] = useState<CompatibilityResult | null>(null)
  const [compatibilityError, setCompatibilityError] = useState<string | null>(null)

  // Загрузка слотов имплантов из OpenAPI
  const {
    data: slotsData,
    isLoading: isLoadingSlots,
    error: slotsError,
  } = useGetImplantSlots(selectedCharacterId || '', undefined, {
    query: { enabled: !!selectedCharacterId },
  })

  // Загрузка лимитов имплантов из OpenAPI
  const {
    data: limitsData,
    isLoading: isLoadingLimits,
  } = useGetImplantLimits(selectedCharacterId || '', {
    query: { enabled: !!selectedCharacterId },
  })

  // Загрузка энергетического пула из OpenAPI
  const {
    data: energyData,
    isLoading: isLoadingEnergy,
  } = useGetEnergyPool(selectedCharacterId || '', {
    query: { enabled: !!selectedCharacterId },
  })

  const compatibilityMutation = useCheckCompatibility({
    mutation: {
      onSuccess: (result) => {
        setCompatibilityResult(result)
        setCompatibilityError(null)
      },
      onError: (error) => {
        setCompatibilityError(error instanceof Error ? error.message : 'Не удалось проверить совместимость')
        setCompatibilityResult(null)
      },
    },
  })

  // Редирект если нет персонажа
  useEffect(() => {
    if (!selectedCharacterId) {
      navigate('/characters')
    }
  }, [selectedCharacterId, navigate])

  const getTypeIcon = (type: ImplantType) => {
    switch (type) {
      case 'combat':
        return <GpsFixedIcon />
      case 'tactical':
        return <PsychologyIcon />
      case 'defensive':
        return <ShieldIcon />
      case 'mobility':
        return <DirectionsRunIcon />
      case 'os':
        return <SettingsIcon />
    }
  }

  const getTypeName = (type: ImplantType) => {
    switch (type) {
      case 'combat':
        return 'Боевые'
      case 'tactical':
        return 'Тактические'
      case 'defensive':
        return 'Защитные'
      case 'mobility':
        return 'Мобильность'
      case 'os':
        return 'ОС'
    }
  }

  const implantTypes: ImplantType[] = ['combat', 'tactical', 'defensive', 'mobility', 'os']

  const handleCompatibilityCheck = () => {
    if (!selectedCharacterId) {
      return
    }

    if (!compatibilityImplantId.trim()) {
      setCompatibilityError('Укажите ID импланта')
      return
    }

    setCompatibilityError(null)

    compatibilityMutation.mutate({
      playerId: selectedCharacterId,
      data: {
        implant_id: compatibilityImplantId.trim(),
        target_slot: compatibilitySlot,
      },
    })
  }

  // Левая панель - Типы имплантов
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
        Типы имплантов
      </Typography>

      <Box sx={{ flex: 1, minHeight: 0, overflowY: 'auto' }}>
        <List dense>
          {implantTypes.map((type) => {
            const typeSlots = slotsData?.slots_by_type[type] || []
            const occupiedCount = typeSlots.filter((s) => s.is_occupied).length

            return (
              <ListItem key={type} disablePadding>
                <ListItemButton
                  selected={selectedType === type}
                  onClick={() => setSelectedType(type)}
                  sx={{
                    borderRadius: 1,
                    mb: 0.5,
                    '&.Mui-selected': {
                      bgcolor: 'rgba(0, 247, 255, 0.15)',
                      borderLeft: '3px solid',
                      borderColor: 'primary.main',
                    },
                    '&:hover': {
                      bgcolor: 'rgba(0, 247, 255, 0.1)',
                    },
                  }}
                >
                  <ListItemIcon sx={{ minWidth: 36 }}>{getTypeIcon(type)}</ListItemIcon>
                  <ListItemText
                    primary={getTypeName(type)}
                    secondary={`${occupiedCount}/${typeSlots.length}`}
                    primaryTypographyProps={{ fontSize: '0.875rem' }}
                    secondaryTypographyProps={{ fontSize: '0.7rem' }}
                  />
                  {occupiedCount > 0 && (
                    <Chip
                      label={occupiedCount}
                      size="small"
                      color="success"
                      sx={{ height: 18, fontSize: '0.65rem' }}
                    />
                  )}
                </ListItemButton>
              </ListItem>
            )
          })}
        </List>
      </Box>
    </Box>
  )

  // Правая панель - Лимиты и энергия
  const rightPanel = (
    <StatsPanel>
      {limitsData && <ImplantLimitInfo limits={limitsData} />}
      {energyData && <EnergyPoolDisplay energy={energyData} />}

      <Card variant="outlined">
        <CardHeader
          title="Совместимость имплантов"
          subheader="Проверка конфликтов и предупреждений"
          titleTypographyProps={{ fontSize: '0.95rem', fontWeight: 600 }}
          subheaderTypographyProps={{ fontSize: '0.75rem' }}
        />
        <CardContent>
          <Stack spacing={1.5}>
            <TextField
              label="ID импланта"
              size="small"
              value={compatibilityImplantId}
              onChange={(event) => setCompatibilityImplantId(event.target.value)}
            />
            <FormControl size="small" fullWidth>
              <InputLabel sx={{ fontSize: '0.75rem' }}>Целевой слот</InputLabel>
              <Select
                value={compatibilitySlot}
                label="Целевой слот"
                onChange={(event) => setCompatibilitySlot(event.target.value as ImplantType)}
                sx={{ fontSize: '0.75rem' }}
              >
                {implantTypes.map((type) => (
                  <MenuItem key={type} value={type} sx={{ fontSize: '0.75rem' }}>
                    {getTypeName(type)}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <Button
              variant="contained"
              size="small"
              onClick={handleCompatibilityCheck}
              disabled={compatibilityMutation.isPending || !selectedCharacterId}
              sx={{ fontSize: '0.75rem' }}
            >
              {compatibilityMutation.isPending ? 'Проверка...' : 'Проверить'}
            </Button>

            {compatibilityError && (
              <Alert severity="error" sx={{ fontSize: '0.8rem' }}>
                {compatibilityError}
              </Alert>
            )}

            {compatibilityResult && (
              <Alert
                severity={compatibilityResult.is_compatible ? 'success' : 'warning'}
                sx={{ fontSize: '0.8rem' }}
              >
                {compatibilityResult.is_compatible
                  ? 'Имплант совместим с текущей конфигурацией.'
                  : 'Обнаружены конфликты совместимости.'}
              </Alert>
            )}

            {compatibilityResult?.conflicts && compatibilityResult.conflicts.length > 0 && (
              <Stack spacing={0.5}>
                <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
                  Конфликты:
                </Typography>
                {compatibilityResult.conflicts.map((conflict, index) => (
                  <Typography key={index} variant="caption" color="error.main" fontSize="0.7rem">
                    • {conflict.reason} ({conflict.severity}) — {conflict.implant_id}
                  </Typography>
                ))}
              </Stack>
            )}

            {compatibilityResult?.warnings && compatibilityResult.warnings.length > 0 && (
              <Stack spacing={0.5}>
                <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
                  Предупреждения:
                </Typography>
                {compatibilityResult.warnings.map((warning, index) => (
                  <Typography key={index} variant="caption" color="warning.main" fontSize="0.7rem">
                    • {warning.message} ({warning.type})
                  </Typography>
                ))}
              </Stack>
            )}
          </Stack>
        </CardContent>
      </Card>
    </StatsPanel>
  )

  if (isLoadingSlots || isLoadingLimits || isLoadingEnergy) {
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

  if (slotsError || !slotsData) {
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
            <Typography variant="h6">Ошибка загрузки слотов имплантов</Typography>
            <Typography variant="body2">
              {(slotsError as unknown as Error)?.message || 'Не удалось загрузить данные'}
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
        {/* Центр - Слоты выбранного типа */}
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2, minHeight: 0, overflowY: 'auto', p: 2 }}>
          <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 'bold', fontSize: '1.1rem', mb: 1 }}>
            Импланты - {getTypeName(selectedType)}
          </Typography>

          {/* Слоты из OpenAPI */}
          <ImplantSlotsList
            slots={slotsData.slots_by_type[selectedType] || []}
            typeName={getTypeName(selectedType)}
            onSlotClick={setSelectedSlot}
          />
        </Box>
      </GameLayout>
    </Box>
  )
}

