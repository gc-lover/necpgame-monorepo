/**
 * Страница локаций
 *
 * SPA архитектура с компактной 3-колоночной сеткой:
 * - Левая панель: фильтры (регион, опасность)
 * - Центр: список локаций или детали
 * - Правая панель: связанные локации для перемещения
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
  ListItemText,
  Stack,
  Grid,
  Button,
  Chip,
} from '@mui/material'
import AllInclusiveIcon from '@mui/icons-material/AllInclusive'
import PublicIcon from '@mui/icons-material/Public'
import WarningIcon from '@mui/icons-material/Warning'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import {
  useGetLocations,
  useGetCurrentLocation,
  useGetLocationDetails,
  useGetConnectedLocations,
  useTravelToLocation,
} from '@/api/generated/locations/locations/locations'
import {
  ConnectedLocationsList,
  LocationActionsList,
  LocationCard,
  LocationDetailsPanel,
} from '../components'
import { useGameState } from '@/features/game/hooks/useGameState'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import type {
  GameLocation,
  GameLocationDangerLevel,
  GameLocationRegion,
  GetLocationsParams,
} from '@/api/generated/locations/models'

const REGION_OPTIONS: Readonly<GameLocationRegion[]> = ['night_city', 'badlands', 'outskirts']
const DANGER_OPTIONS: Readonly<GameLocationDangerLevel[]> = ['low', 'medium', 'high', 'extreme']

function formatEnumLabel(value: string) {
  return value.replace(/_/g, ' ')
}

export function LocationsPage() {
  const navigate = useNavigate()
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)
  const [regionFilter, setRegionFilter] = useState<GameLocationRegion | undefined>()
  const [dangerFilter, setDangerFilter] = useState<GameLocationDangerLevel | undefined>()
  const [selectedLocationId, setSelectedLocationId] = useState<string | null>(null)
  const hasCharacter = Boolean(selectedCharacterId)
  const characterId = selectedCharacterId ?? ''

  const {
    data: currentLocation,
    isLoading: isLoadingCurrentLocation,
  } = useGetCurrentLocation(
    { characterId },
    { query: { enabled: hasCharacter } }
  )

  useEffect(() => {
    if (!hasCharacter) {
      navigate('/characters')
    }
  }, [hasCharacter, navigate])

  const locationsParams = useMemo<GetLocationsParams>(() => {
    const params: GetLocationsParams = { characterId }
    if (regionFilter) {
      params.region = regionFilter
    }
    if (dangerFilter) {
      params.dangerLevel = dangerFilter
    }
    return params
  }, [characterId, regionFilter, dangerFilter])

  const {
    data: locationsData,
    isLoading: isLoadingLocations,
    error: locationsError,
  } = useGetLocations(
    locationsParams,
    { query: { enabled: hasCharacter } }
  )

  const selectedLocationSummary: GameLocation | undefined = useMemo(() => {
    if (!selectedLocationId || !locationsData?.locations) {
      return undefined
    }
    return locationsData.locations.find((location) => location.id === selectedLocationId)
  }, [locationsData?.locations, selectedLocationId])

  const shouldLoadDetails = hasCharacter && Boolean(selectedLocationId)

  const {
    data: locationDetails,
    isLoading: isLoadingDetails,
  } = useGetLocationDetails(
    selectedLocationId || 'placeholder',
    { characterId },
    { query: { enabled: shouldLoadDetails } }
  )

  const connectedBaseLocationId = selectedLocationId ?? currentLocation?.id ?? ''

  const {
    data: connectedData,
    isLoading: isLoadingConnected,
  } = useGetConnectedLocations(
    connectedBaseLocationId || 'placeholder',
    connectedBaseLocationId && hasCharacter ? { characterId } : undefined,
    {
      query: {
        enabled: hasCharacter && Boolean(connectedBaseLocationId),
      },
    }
  )

  const connectedLocations = connectedData?.connectedLocations ?? []
  const connectedTitle = selectedLocationSummary?.name ?? currentLocation?.name ?? 'Связанные локации'

  // Mutation для перемещения
  const { mutate: travelToLocation, isPending: isTraveling } = useTravelToLocation()

  const handleLocationClick = (location: GameLocation) => {
    setSelectedLocationId(location.id)
  }

  const handleTravel = (locationId: string) => {
    if (!hasCharacter) {
      return
    }

    travelToLocation(
      {
        data: {
          characterId,
          targetLocationId: locationId,
          travelMethod: 'walk',
        },
      },
      {
        onSuccess: () => {
          navigate('/game/play')
        },
        onError: (error) => {
          console.error('Travel error:', error)
        },
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
        Фильтры
      </Typography>

      {/* Регион */}
      <Box>
        <Typography variant="caption" sx={{ fontSize: '0.7rem', display: 'block', mb: 0.5 }}>
          Регион:
        </Typography>
        <List dense>
          <ListItem disablePadding>
            <ListItemButton
              selected={!regionFilter}
              onClick={() => setRegionFilter(undefined)}
              sx={{
                borderRadius: 1,
                mb: 0.3,
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
                    <AllInclusiveIcon sx={{ fontSize: '0.9rem' }} />
                    <span style={{ fontSize: '0.8rem' }}>Все</span>
                  </Stack>
                }
              />
            </ListItemButton>
          </ListItem>
          {REGION_OPTIONS.map((region) => (
            <ListItem key={region} disablePadding>
              <ListItemButton
                selected={regionFilter === region}
                onClick={() => setRegionFilter(region)}
                sx={{
                  borderRadius: 1,
                  mb: 0.3,
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
                      <PublicIcon sx={{ fontSize: '0.9rem' }} />
                      <span style={{ fontSize: '0.8rem' }}>{formatEnumLabel(region)}</span>
                    </Stack>
                  }
                />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </Box>

      {/* Опасность */}
      <Box>
        <Typography variant="caption" sx={{ fontSize: '0.7rem', display: 'block', mb: 0.5 }}>
          Опасность:
        </Typography>
        <List dense>
          <ListItem disablePadding>
            <ListItemButton
              selected={!dangerFilter}
              onClick={() => setDangerFilter(undefined)}
              sx={{
                borderRadius: 1,
                mb: 0.3,
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
                    <AllInclusiveIcon sx={{ fontSize: '0.9rem' }} />
                    <span style={{ fontSize: '0.8rem' }}>Все</span>
                  </Stack>
                }
              />
            </ListItemButton>
          </ListItem>
          {DANGER_OPTIONS.map((danger) => (
            <ListItem key={danger} disablePadding>
              <ListItemButton
                selected={dangerFilter === danger}
                onClick={() => setDangerFilter(danger)}
                sx={{
                  borderRadius: 1,
                  mb: 0.3,
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
                      <WarningIcon sx={{ fontSize: '0.9rem' }} />
                      <span style={{ fontSize: '0.8rem' }}>{formatEnumLabel(danger)}</span>
                    </Stack>
                  }
                />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </Box>
    </Box>
  )

  const rightPanel = (
    <StatsPanel>
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, height: '100%', minHeight: 0, overflowY: 'auto' }}>
        <Typography
          variant="subtitle2"
          sx={{
            color: 'primary.main',
            fontSize: '0.75rem',
            textTransform: 'uppercase',
          }}
        >
          {connectedTitle}
        </Typography>
        {isLoadingConnected || isLoadingCurrentLocation ? (
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
            Загрузка данных о маршрутах...
          </Typography>
        ) : connectedBaseLocationId ? (
          <ConnectedLocationsList locations={connectedLocations} onTravel={handleTravel} />
        ) : (
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
            Выберите локацию, чтобы увидеть возможные маршруты.
          </Typography>
        )}
      </Box>
    </StatsPanel>
  )

  if (isLoadingLocations) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1 }}>
          <CircularProgress size={60} />
        </Box>
      </Box>
    )
  }

  if (locationsError) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1, p: 3 }}>
          <Alert severity="error" sx={{ maxWidth: 600 }}>
            <Typography variant="h6">Ошибка загрузки локаций</Typography>
            <Typography variant="body2">
              {(locationsError as unknown as Error)?.message || 'Не удалось загрузить список локаций'}
            </Typography>
          </Alert>
        </Box>
      </Box>
    )
  }

  const showDetails = Boolean(selectedLocationId && locationDetails)

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      <Header />
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2, minHeight: 0, overflowY: 'auto', p: 2 }}>
          {showDetails ? (
            <>
              <Button
                startIcon={<ArrowBackIcon />}
                onClick={() => setSelectedLocationId(null)}
                size="small"
                sx={{ alignSelf: 'flex-start', fontSize: '0.75rem' }}
              >
                Назад к списку
              </Button>
              {isLoadingDetails || !locationDetails ? (
                <CircularProgress size={40} />
              ) : (
                <>
                  <LocationDetailsPanel location={locationDetails} />
                  <LocationActionsList actions={locationDetails.availableActions} />
                  <Stack spacing={0.5}>
                    <Button
                      variant="contained"
                      onClick={() => handleTravel(locationDetails.id)}
                      disabled={isTraveling || selectedLocationSummary?.accessible === false}
                      sx={{ fontSize: '0.85rem', alignSelf: 'flex-start' }}
                    >
                      {isTraveling ? 'Перемещение...' : 'Отправиться сюда'}
                    </Button>
                    {selectedLocationSummary?.accessible === false && (
                      <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'error.main' }}>
                        Локация пока недоступна для перемещения.
                      </Typography>
                    )}
                  </Stack>
                </>
              )}
            </>
          ) : (
            <>
              <Stack direction="row" justifyContent="space-between" alignItems="center">
                <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 'bold', fontSize: '1.1rem' }}>
                  Карта локаций
                </Typography>
                {currentLocation && (
                  <Chip
                    label={`Текущая: ${currentLocation.name}`}
                    color="primary"
                    size="small"
                    sx={{ fontSize: '0.7rem' }}
                  />
                )}
              </Stack>
              <Grid container spacing={1.5}>
                {locationsData?.locations && locationsData.locations.length > 0 ? (
                  locationsData.locations.map((location) => (
                    <Grid item xs={12} sm={6} md={4} key={location.id}>
                      <LocationCard
                        location={location}
                        onClick={() => handleLocationClick(location)}
                        isCurrent={currentLocation?.id === location.id}
                      />
                    </Grid>
                  ))
                ) : (
                  <Grid item xs={12}>
                    <Typography variant="caption" sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
                      Нет доступных локаций
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

