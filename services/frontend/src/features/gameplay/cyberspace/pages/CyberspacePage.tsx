import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Box, Chip } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import LoginIcon from '@mui/icons-material/Login'
import LogoutIcon from '@mui/icons-material/Logout'
import GameLayout from '@/features/game/components/GameLayout'
import { CyberspaceZoneCard } from '../components/CyberspaceZoneCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useEnterCyberspace,
  useExitCyberspace,
  useGetCyberspaceZones,
  useNavigateToCyberspaceZone,
  useGetCyberspaceAvatar,
} from '@/api/generated/cyberspace/cyberspace/cyberspace'

export const CyberspacePage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [isInCyberspace, setIsInCyberspace] = useState(false)
  const [zoneType, setZoneType] = useState<'hub' | 'arena' | 'pve_zone' | 'deep_zone' | 'custom' | undefined>(undefined)

  const enterMutation = useEnterCyberspace()
  const exitMutation = useExitCyberspace()
  const navigateMutation = useNavigateToCyberspaceZone()

  const { data: zonesData, isLoading: zonesLoading } = useGetCyberspaceZones(
    { character_id: selectedCharacterId || '', zone_type: zoneType },
    { query: { enabled: !!selectedCharacterId && isInCyberspace } }
  )

  const { data: avatarData } = useGetCyberspaceAvatar(
    { character_id: selectedCharacterId || '' },
    { query: { enabled: !!selectedCharacterId && isInCyberspace } }
  )

  const handleEnter = () => {
    if (!selectedCharacterId) return
    enterMutation.mutate(
      { data: { character_id: selectedCharacterId } },
      {
        onSuccess: () => {
          setIsInCyberspace(true)
        },
      }
    )
  }

  const handleExit = () => {
    if (!selectedCharacterId) return
    exitMutation.mutate(
      { data: { character_id: selectedCharacterId } },
      {
        onSuccess: () => {
          setIsInCyberspace(false)
        },
      }
    )
  }

  const handleNavigateToZone = (zoneId: string) => {
    if (!selectedCharacterId) return
    navigateMutation.mutate({
      data: { character_id: selectedCharacterId, zone_id: zoneId },
    })
  }

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">
        Киберпространство
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Полноценный режим игры
      </Typography>
      <Divider />
      {!isInCyberspace ? (
        <Button startIcon={<LoginIcon />} onClick={handleEnter} fullWidth variant="contained" size="small" sx={{ fontSize: '0.75rem' }} disabled={enterMutation.isPending}>
          Войти в Cyberspace
        </Button>
      ) : (
        <>
          <Button startIcon={<LogoutIcon />} onClick={handleExit} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }} disabled={exitMutation.isPending}>
            Выйти из Cyberspace
          </Button>
          <Divider />
          <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
            Фильтры
          </Typography>
          <Stack direction="row" spacing={0.5} flexWrap="wrap" gap={0.5}>
            <Chip label="Все" size="small" variant={!zoneType ? 'filled' : 'outlined'} onClick={() => setZoneType(undefined)} sx={{ fontSize: '0.7rem' }} />
            <Chip label="Хабы" size="small" variant={zoneType === 'hub' ? 'filled' : 'outlined'} onClick={() => setZoneType('hub')} sx={{ fontSize: '0.7rem' }} />
            <Chip label="Арены" size="small" variant={zoneType === 'arena' ? 'filled' : 'outlined'} onClick={() => setZoneType('arena')} sx={{ fontSize: '0.7rem' }} />
            <Chip label="PvE" size="small" variant={zoneType === 'pve_zone' ? 'filled' : 'outlined'} onClick={() => setZoneType('pve_zone')} sx={{ fontSize: '0.7rem' }} />
            <Chip label="Deep" size="small" variant={zoneType === 'deep_zone' ? 'filled' : 'outlined'} onClick={() => setZoneType('deep_zone')} sx={{ fontSize: '0.7rem' }} />
          </Stack>
        </>
      )}
    </Stack>
  )

  const rightPanel = isInCyberspace && avatarData ? (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Аватар
      </Typography>
      <Divider />
      <Box>
        <Typography variant="caption" fontSize="0.7rem">
          Уровень доступа: <Chip label={avatarData.access_level || 'basic'} size="small" sx={{ height: 16, fontSize: '0.65rem' }} />
        </Typography>
      </Box>
      <Typography variant="caption" fontSize="0.7rem">
        Cyberdeck: {avatarData.cyberdeck_rating || 0}
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Репутация: {avatarData.reputation || 0}
      </Typography>
    </Stack>
  ) : (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Инфо
      </Typography>
      <Divider />
      <Typography variant="body2" fontSize="0.75rem" color="text.secondary">
        Хабы, Арены, PvE-зоны, Глубокие зоны
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        Cyberspace
      </Typography>
      <Divider />
      {!isInCyberspace ? (
        <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
          Требуется кибердека (имплант) для входа. Доступ: Basic → Medium → Advanced (Netrunner).
        </Alert>
      ) : (
        <>
          <Typography variant="body2" fontSize="0.75rem">
            {zonesData?.zones?.length || 0} доступных зон
          </Typography>
          {zonesLoading ? (
            <Typography variant="body2" fontSize="0.75rem">
              Загрузка зон...
            </Typography>
          ) : (
            <Grid container spacing={1}>
              {zonesData?.zones?.map((zone, index) => (
                <Grid item xs={12} sm={6} md={4} key={zone.zone_id || index}>
                  <CyberspaceZoneCard zone={zone} onNavigate={handleNavigateToZone} />
                </Grid>
              ))}
            </Grid>
          )}
        </>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default CyberspacePage

