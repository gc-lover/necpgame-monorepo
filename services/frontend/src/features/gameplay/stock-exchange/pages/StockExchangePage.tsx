import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import ShowChartIcon from '@mui/icons-material/ShowChart'
import GameLayout from '@/features/game/components/GameLayout'
import { StockCompanyCard } from '../components/StockCompanyCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useGetStockCompanies,
  useGetStockPortfolio,
  useGetDividends,
  useGetStockNews,
} from '@/api/generated/stock-exchange/companies/companies'

export const StockExchangePage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()

  const { data: companiesData, isLoading } = useGetStockCompanies({}, { query: { enabled: true } })

  const { data: portfolioData } = useGetStockPortfolio({ character_id: selectedCharacterId || '' }, { query: { enabled: !!selectedCharacterId } })

  const { data: dividendsData } = useGetDividends({ character_id: selectedCharacterId || '' }, { query: { enabled: !!selectedCharacterId } })

  const { data: newsData } = useGetStockNews({}, { query: { enabled: true } })

  const handleCompanyClick = (ticker: string) => {
    console.log('Open company details:', ticker)
  }

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">
        Stock Exchange
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        EVE Online / NYSE
      </Typography>
      <Divider />
      {portfolioData && (
        <>
          <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
            Мое портфолио
          </Typography>
          <Box>
            <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
              Стоимость:
            </Typography>
            <Typography variant="body2" fontSize="0.8rem" fontWeight="bold">
              €${portfolioData.total_value?.toLocaleString()}
            </Typography>
          </Box>
          <Box>
            <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
              P/L:
            </Typography>
            <Typography variant="body2" fontSize="0.8rem" fontWeight="bold" color={(portfolioData.profit_loss || 0) >= 0 ? 'success.main' : 'error.main'}>
              {(portfolioData.profit_loss || 0) >= 0 ? '+' : ''}€${portfolioData.profit_loss?.toLocaleString()} ({portfolioData.roi_percent?.toFixed(2)}%)
            </Typography>
          </Box>
          <Divider />
        </>
      )}
      {dividendsData && (
        <>
          <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
            Дивиденды
          </Typography>
          <Typography variant="caption" fontSize="0.7rem">
            Получено: €${dividendsData.total_dividends_received?.toLocaleString()}
          </Typography>
          <Typography variant="caption" fontSize="0.7rem">
            Ожидается: {dividendsData.upcoming_dividends?.length || 0}
          </Typography>
        </>
      )}
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Новости
      </Typography>
      <Divider />
      <Stack spacing={1} sx={{ maxHeight: '400px', overflow: 'auto' }}>
        {newsData?.news?.slice(0, 5).map((news, i) => (
          <Box key={i} p={1} sx={{ bgcolor: 'action.hover', borderRadius: 1 }}>
            <Typography variant="caption" fontSize="0.7rem" fontWeight="bold">
              {news.ticker}
            </Typography>
            <Typography variant="caption" fontSize="0.65rem" display="block">
              {news.headline}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <ShowChartIcon sx={{ fontSize: '1.5rem', color: 'primary.main' }} />
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Биржа акций
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        Корпорации: Arasaka, Militech, Biotechnica, Kang Tao. Цены зависят от событий мира, квестов, корпоративных войн. Дивиденды акционерам!
      </Alert>
      {isLoading ? (
        <Typography variant="body2" fontSize="0.75rem">
          Загрузка корпораций...
        </Typography>
      ) : (
        <>
          <Typography variant="body2" fontSize="0.75rem">
            Торгуемых корпораций: {companiesData?.companies?.length || 0}
          </Typography>
          <Grid container spacing={1}>
            {companiesData?.companies?.map((company, index) => (
              <Grid item xs={12} sm={6} md={4} key={company.ticker || index}>
                <StockCompanyCard company={company} onClick={handleCompanyClick} />
              </Grid>
            ))}
          </Grid>
        </>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default StockExchangePage

