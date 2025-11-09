import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Tabs, Tab, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { DaemonCard } from '../components/DaemonCard'
import { HeatMeter } from '../components/HeatMeter'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useGetHeatLevel,
  useGetAvailableDaemons,
  useHackEnemy,
  useHackDevice,
  useHackInfrastructure,
  useInstallDaemon,
} from '@/api/generated/hacking/hacking/hacking'

export const HackingPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [activeTab, setActiveTab] = useState(0)

  const { data: heatData } = useGetHeatLevel({ character_id: selectedCharacterId || '' }, { query: { enabled: !!selectedCharacterId, refetchInterval: 2000 } })

  const { data: daemonsData } = useGetAvailableDaemons({ character_id: selectedCharacterId || '' }, { query: { enabled: !!selectedCharacterId } })

  const hackEnemyMutation = useHackEnemy()
  const hackDeviceMutation = useHackDevice()
  const hackInfrastructureMutation = useHackInfrastructure()
  const installDaemonMutation = useInstallDaemon()

  const handleUseDaemon = (daemonId: string) => {
    if (!selectedCharacterId) return
    // В реальной игре здесь будет выбор цели
    hackEnemyMutation.mutate({
      data: {
        character_id: selectedCharacterId,
        target_id: 'target_placeholder',
        hack_type: 'overheat',
        daemon_id: daemonId,
      },
    })
  }

  const isOverheating = heatData && heatData.current_heat >= heatData.overheat_threshold

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="warning">
        Hacking
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Cyberpunk 2077 / Watch Dogs
      </Typography>
      <Divider />
      {heatData && (
        <HeatMeter
          currentHeat={heatData.current_heat || 0}
          maxHeat={heatData.max_heat || 100}
          coolingRate={heatData.cooling_rate || 1}
          overheatThreshold={heatData.overheat_threshold || 80}
        />
      )}
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Типы взлома
      </Typography>
      <Tabs value={activeTab} onChange={(_, v) => setActiveTab(v)} variant="scrollable" scrollButtons="auto">
        <Tab label="Враги" sx={{ fontSize: '0.7rem', minHeight: 36 }} />
        <Tab label="Устройства" sx={{ fontSize: '0.7rem', minHeight: 36 }} />
        <Tab label="Инфраструктура" sx={{ fontSize: '0.7rem', minHeight: 36 }} />
      </Tabs>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Статистика
      </Typography>
      <Divider />
      <Typography variant="caption" fontSize="0.7rem">
        Демонов: {daemonsData?.daemons?.length || 0}
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        RAM: {/* Из кибердека */}
      </Typography>
      <Divider />
      <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
        Netrunner: все типы + продвинутые кибердеки
      </Typography>
      <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
        Другие классы: базовые устройства
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        Quickhacks (Демоны)
      </Typography>
      <Divider />
      {isOverheating && (
        <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
          ⚠️ ПЕРЕГРЕВ! Кибердек отключен. Дождитесь охлаждения.
        </Alert>
      )}
      {activeTab === 0 && (
        <>
          <Typography variant="body2" fontSize="0.75rem">
            Взлом врагов: отключение имплантов, контроль, урон, перегрев
          </Typography>
          <Grid container spacing={1}>
            {daemonsData?.daemons
              ?.filter((d) => d.type === 'enemy' || d.type === 'combat')
              .map((daemon, index) => (
                <Grid item xs={12} sm={6} md={4} key={daemon.daemon_id || index}>
                  <DaemonCard daemon={daemon} onUse={handleUseDaemon} disabled={isOverheating} />
                </Grid>
              ))}
          </Grid>
        </>
      )}
      {activeTab === 1 && (
        <>
          <Typography variant="body2" fontSize="0.75rem">
            Взлом устройств: камеры, двери, роботы, транспорт
          </Typography>
          <Grid container spacing={1}>
            {daemonsData?.daemons
              ?.filter((d) => d.type === 'device')
              .map((daemon, index) => (
                <Grid item xs={12} sm={6} md={4} key={daemon.daemon_id || index}>
                  <DaemonCard daemon={daemon} onUse={handleUseDaemon} disabled={isOverheating} />
                </Grid>
              ))}
          </Grid>
        </>
      )}
      {activeTab === 2 && (
        <>
          <Typography variant="body2" fontSize="0.75rem">
            Взлом инфраструктуры: городские системы, сети, корпоративные системы (требует Netrunner)
          </Typography>
          <Grid container spacing={1}>
            {daemonsData?.daemons
              ?.filter((d) => d.type === 'infrastructure')
              .map((daemon, index) => (
                <Grid item xs={12} sm={6} md={4} key={daemon.daemon_id || index}>
                  <DaemonCard daemon={daemon} onUse={handleUseDaemon} disabled={isOverheating} />
                </Grid>
              ))}
          </Grid>
        </>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default HackingPage

