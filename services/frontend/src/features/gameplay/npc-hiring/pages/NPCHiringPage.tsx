import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Tabs, Tab } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { HireableNPCCard } from '../components/HireableNPCCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useGetAvailableNPCs,
  useHireNPC,
  useGetActiveContracts,
  useTerminateContract,
} from '@/api/generated/npc-hiring/npc-hiring/npc-hiring'

export const NPCHiringPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [npcTypeFilter, setNpcTypeFilter] = useState<'combat' | 'vendor' | 'specialist' | undefined>(undefined)
  const [activeTab, setActiveTab] = useState(0)

  const { data: availableNPCsData, isLoading } = useGetAvailableNPCs(
    { character_id: selectedCharacterId || '', npc_type: npcTypeFilter },
    { query: { enabled: !!selectedCharacterId } }
  )

  const { data: contractsData } = useGetActiveContracts({ character_id: selectedCharacterId || '' }, { query: { enabled: !!selectedCharacterId } })

  const hireMutation = useHireNPC()
  const terminateMutation = useTerminateContract()

  const handleHire = (npcId: string) => {
    if (!selectedCharacterId) return
    const duration = prompt('Длительность контракта (дни):')
    if (duration) {
      hireMutation.mutate({
        data: {
          character_id: selectedCharacterId,
          npc_id: npcId,
          contract_duration: parseInt(duration),
          contract_type: 'temporary',
        },
      })
    }
  }

  const handleTabChange = (_: any, newValue: number) => {
    setActiveTab(newValue)
    const types: Array<'combat' | 'vendor' | 'specialist' | undefined> = [undefined, 'combat', 'vendor', 'specialist']
    setNpcTypeFilter(types[newValue])
  }

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">
        NPC Hiring
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        RimWorld / Kenshi
      </Typography>
      <Divider />
      <Tabs value={activeTab} onChange={handleTabChange} orientation="vertical" variant="scrollable">
        <Tab label="Все" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab label="Боевые" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab label="Торговцы" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab label="Специалисты" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
      </Tabs>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Контракты
      </Typography>
      <Divider />
      <Typography variant="caption" fontSize="0.7rem">
        Активных: {contractsData?.contracts?.length || 0}
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Типы NPC
      </Typography>
      <Stack spacing={0.5}>
        {['Боевые (bodyguards, mercs)', 'Торговцы (vendors)', 'Специалисты (ripperdocs, fixers, netrunners)'].map((t, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {t}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        Найм NPC
      </Typography>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        Найм: боевые (bodyguards, mercs), торговцы, специалисты. Процесс: поиск → переговоры → контракт → управление. Ограничения: слоты, зарплата, лояльность.
      </Alert>
      {isLoading ? (
        <Typography variant="body2" fontSize="0.75rem">
          Загрузка NPC...
        </Typography>
      ) : (
        <>
          <Typography variant="body2" fontSize="0.75rem">
            Доступно NPC: {availableNPCsData?.available_npcs?.length || 0}
          </Typography>
          <Grid container spacing={1}>
            {availableNPCsData?.available_npcs?.map((npc, index) => (
              <Grid item xs={12} sm={6} md={4} key={npc.npc_id || index}>
                <HireableNPCCard npc={npc} onHire={handleHire} />
              </Grid>
            ))}
          </Grid>
        </>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default NPCHiringPage

