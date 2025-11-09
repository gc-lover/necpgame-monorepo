import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import RouterIcon from '@mui/icons-material/Router'
import GameLayout from '@/features/game/components/GameLayout'
import { NetworkNodeCard } from '../components/NetworkNodeCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useGetNetworkInfo,
  useHackNetwork,
  useGetNetworkSecurity,
} from '@/api/generated/hacking-networks/hacking-networks/hacking-networks'

export const HackingNetworksPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [selectedNetworkId] = useState('net_building_lobby')

  const { data: networkData } = useGetNetworkInfo(
    { network_id: selectedNetworkId, character_id: selectedCharacterId || '' },
    { query: { enabled: !!selectedCharacterId && !!selectedNetworkId } }
  )

  const { data: securityData } = useGetNetworkSecurity(
    { network_id: selectedNetworkId },
    { query: { enabled: !!selectedNetworkId } }
  )

  const hackNetworkMutation = useHackNetwork({ network_id: selectedNetworkId })

  const handleHackNetwork = (method: 'brute_force' | 'stealth' | 'daemon') => {
    if (!selectedCharacterId) return
    hackNetworkMutation.mutate({
      data: {
        character_id: selectedCharacterId,
        method,
      },
    })
  }

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="warning">
        Hacking Networks
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        CP2077 / Watch Dogs
      </Typography>
      <Divider />
      {networkData && (
        <>
          <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
            {networkData.name}
          </Typography>
          <Box display="flex" gap={0.5} flexWrap="wrap">
            <Chip label={networkData.type || 'local'} size="small" sx={{ fontSize: '0.65rem' }} />
            <Chip label={networkData.complexity || 'simple'} size="small" color="warning" sx={{ fontSize: '0.65rem' }} />
            <Chip label={`SEC ${networkData.security_level || 0}`} size="small" color="error" sx={{ fontSize: '0.65rem' }} />
          </Box>
        </>
      )}
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Методы взлома
      </Typography>
      <Stack spacing={0.5}>
        <Button onClick={() => handleHackNetwork('brute_force')} size="small" fullWidth variant="outlined" sx={{ fontSize: '0.7rem' }}>
          Brute Force
        </Button>
        <Button onClick={() => handleHackNetwork('stealth')} size="small" fullWidth variant="outlined" sx={{ fontSize: '0.7rem' }}>
          Stealth
        </Button>
        <Button onClick={() => handleHackNetwork('daemon')} size="small" fullWidth variant="outlined" sx={{ fontSize: '0.7rem' }}>
          Daemon
        </Button>
      </Stack>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Защита
      </Typography>
      <Divider />
      {securityData && (
        <>
          <Typography variant="caption" fontSize="0.7rem">
            Уровень: {securityData.level || 0}
          </Typography>
          <Typography variant="caption" fontSize="0.7rem">
            ICE активен: {securityData.ice_active ? 'Да' : 'Нет'}
          </Typography>
        </>
      )}
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Типы сетей
      </Typography>
      <Stack spacing={0.5}>
        {['Локальные', 'Корпоративные', 'Городские', 'Персональные'].map((t, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {t}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <RouterIcon sx={{ fontSize: '1.5rem', color: 'warning.main' }} />
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Взлом сетей
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        Структура сетей: простые/сложные/изолированные. Защита: ICE, алерты, trace. Методы: brute force, stealth, daemon.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        Узлов в сети: {networkData?.nodes?.length || 0}
      </Typography>
      <Grid container spacing={1}>
        {networkData?.nodes?.map((node, index) => (
          <Grid item xs={6} sm={4} md={3} key={node.node_id || index}>
            <NetworkNodeCard node={node} compromised={false} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default HackingNetworksPage

