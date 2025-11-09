import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Box, TextField, MenuItem } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import StoreIcon from '@mui/icons-material/Store'
import GameLayout from '@/features/game/components/GameLayout'
import { TradingGuildCard } from '../components/TradingGuildCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import { useListTradingGuilds } from '@/api/generated/trading-guilds/trading-guilds/trading-guilds'

const GUILD_TYPES = ['MERCHANT', 'CRAFTSMAN', 'TRANSPORT', 'FINANCIAL', 'MIXED']

export const TradingGuildsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [selectedType, setSelectedType] = useState('')
  const [minLevel, setMinLevel] = useState('')

  const { data: guildsData } = useListTradingGuilds({
    type: selectedType || undefined,
    min_level: minLevel ? parseInt(minLevel) : undefined,
  })

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="success">
        Trading Guilds
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        EVE Online / WOW
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        label="Тип гильдии"
        value={selectedType}
        onChange={(e) => setSelectedType(e.target.value)}
        size="small"
        fullWidth
        sx={{ '& .MuiInputBase-input': { fontSize: '0.75rem' } }}
      >
        <MenuItem value="">Все типы</MenuItem>
        {GUILD_TYPES.map((type) => (
          <MenuItem key={type} value={type} sx={{ fontSize: '0.75rem' }}>
            {type}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        type="number"
        label="Мин. уровень"
        value={minLevel}
        onChange={(e) => setMinLevel(e.target.value)}
        size="small"
        fullWidth
        inputProps={{ min: 1, max: 5 }}
        sx={{ '& .MuiInputBase-input': { fontSize: '0.75rem' } }}
      />
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Типы гильдий
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['MERCHANT (товары)', 'CRAFTSMAN (крафт)', 'TRANSPORT (логистика)', 'FINANCIAL (финансы)', 'MIXED (универсальные)'].map((t, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {t}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Роли в гильдии
      </Typography>
      <Stack spacing={0.5}>
        {['Guild Master', 'Treasurer', 'Merchant', 'Trader'].map((r, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {r}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <StoreIcon sx={{ fontSize: '1.5rem', color: 'success.main' }} />
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Торговые гильдии
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        EVE Online механики! Гильдии: типы, роли, казна, репутация. Уровни 1-5. Эксклюзивные маршруты. Межгильдийная торговля.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        Всего гильдий: {guildsData?.data?.length || 0}
      </Typography>
      <Grid container spacing={1}>
        {guildsData?.data?.map((guild, index) => (
          <Grid item xs={12} sm={6} md={4} key={guild.guild_id || index}>
            <TradingGuildCard guild={guild} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default TradingGuildsPage

