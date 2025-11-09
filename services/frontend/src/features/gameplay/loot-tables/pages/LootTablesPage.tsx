import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Tabs, Tab } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import CasinoIcon from '@mui/icons-material/Casino'
import GameLayout from '@/features/game/components/GameLayout'
import { LootTableCard } from '../components/LootTableCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useGetLootTables,
  useGenerateLoot,
} from '@/api/generated/loot-tables/loot-tables/loot-tables'

export const LootTablesPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [sourceTypeFilter, setSourceTypeFilter] = useState<string | undefined>(undefined)
  const [activeTab, setActiveTab] = useState(0)

  const { data: tablesData, isLoading } = useGetLootTables(
    { source_type: sourceTypeFilter as any },
    { query: { enabled: true } }
  )

  const generateLootMutation = useGenerateLoot()

  const handleGenerateLoot = (sourceType: string, sourceId: string) => {
    if (!selectedCharacterId) return
    generateLootMutation.mutate({
      data: {
        source_type: sourceType as any,
        source_id: sourceId,
        character_id: selectedCharacterId,
      },
    })
  }

  const handleTabChange = (_: any, newValue: number) => {
    setActiveTab(newValue)
    const types = [undefined, 'quest', 'enemy', 'container', 'event', 'boss']
    setSourceTypeFilter(types[newValue])
  }

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="warning">
        Loot Tables
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Tarkov / Diablo
      </Typography>
      <Divider />
      <Tabs value={activeTab} onChange={handleTabChange} orientation="vertical" variant="scrollable">
        <Tab label="Все" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab label="Квесты" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab label="Враги" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab label="Контейнеры" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab label="События" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab label="Боссы" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
      </Tabs>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Модификаторы
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['Уровень источника', 'Зона', 'Удача персонажа', 'События мира', 'Сложность', 'Размер группы'].map((m, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {m}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Редкость
      </Typography>
      <Stack spacing={0.5}>
        {['Common', 'Uncommon', 'Rare', 'Epic', 'Legendary', 'Unique'].map((r, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {r}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        Лут-таблицы
      </Typography>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        Система лута для квестов, врагов, контейнеров, боссов, событий. Формулы вероятностей, редкость, модификаторы (уровень, зона, удача).
      </Alert>
      {isLoading ? (
        <Typography variant="body2" fontSize="0.75rem">
          Загрузка таблиц...
        </Typography>
      ) : (
        <>
          <Typography variant="body2" fontSize="0.75rem">
            Таблиц: {tablesData?.loot_tables?.length || 0}
          </Typography>
          <Grid container spacing={1}>
            {tablesData?.loot_tables?.map((table, index) => (
              <Grid item xs={12} sm={6} md={4} key={table.table_id || index}>
                <LootTableCard table={table} />
              </Grid>
            ))}
          </Grid>
        </>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default LootTablesPage

