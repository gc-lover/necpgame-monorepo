import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import RouteIcon from '@mui/icons-material/Route'
import GameLayout from '@/features/game/components/GameLayout'
import { TradeRouteCard } from '../components/TradeRouteCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import { useGetTradingRoutes } from '@/api/generated/trading-routes/trading-routes/trading-routes'

export const TradingRoutesPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()

  const { data: routesData } = useGetTradingRoutes(
    { character_id: selectedCharacterId || '' },
    { query: { enabled: !!selectedCharacterId } }
  )

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="info">
        Trading Routes
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        EVE Online / KENSHI
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Торговые узлы T1
      </Typography>
      <Stack spacing={0.3}>
        {['Night City', 'Токио', 'Лондон', 'Шанхай', 'Москва'].map((h, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {h}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Маршруты
      </Typography>
      <Stack spacing={0.3}>
        {['Trans-Pacific', 'Euro-Express', 'American Run', 'Silk Road 2.0', 'African Network'].map((r, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {r}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Товары
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['Кибер-импланты', 'Электроника', 'Braindance', 'Оружие', 'Наркотики'].map((c, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {c}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Риски
      </Typography>
      <Stack spacing={0.5}>
        {['Пираты', 'Таможня', 'Картели', 'Корп-проверки', 'Банды'].map((r, i) => (
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
        <RouteIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Торговые маршруты
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        Глобальная торговля! Маршруты: Trans-Pacific, Euro-Express, American Run, Silk Road 2.0. Прибыль 10-40%. Риски: пираты, картели, таможня.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        Доступных маршрутов: {routesData?.routes?.length || 0}
      </Typography>
      <Grid container spacing={1}>
        {routesData?.routes?.map((route, index) => (
          <Grid item xs={12} sm={6} md={4} key={route.route_id || index}>
            <TradeRouteCard route={route} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default TradingRoutesPage

