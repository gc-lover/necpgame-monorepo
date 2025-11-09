import { useGetFactions } from '../../../api/generated/auth/reference-data/reference-data'
import { Card, CardContent, Box, Typography, CircularProgress, Alert, Grid, Chip } from '@mui/material'
import { CheckCircle } from '@mui/icons-material'
import type { GetFactionsOrigin } from '../../../api/generated/auth/models'

/**
 * Пропсы компонента FactionSelector
 */
interface FactionSelectorProps {
  /** Выбранное происхождение (для фильтрации фракций) */
  origin: string | null
  /** Выбранная фракция */
  selectedFaction: string | null
  /** Обработчик выбора фракции */
  onFactionSelect: (factionId: string | null) => void
}

/**
 * Компонент выбора фракции персонажа
 */
export function FactionSelector({ origin, selectedFaction, onFactionSelect }: FactionSelectorProps) {
  const { data, isLoading, error } = useGetFactions(
    origin ? { origin: origin as GetFactionsOrigin } : undefined
  )
  const selectedFactionData = data?.factions?.find((faction) => faction.id === selectedFaction)
  
  const getFactionTypeIcon = (type: string) => {
    switch (type) {
      case 'corporation': return '🏢'
      case 'gang': return '⚔️'
      case 'organization': return '🤝'
      default: return '🏴'
    }
  }
  
  const getFactionTypeName = (type: string) => {
    switch (type) {
      case 'corporation': return 'Корпорация'
      case 'gang': return 'Банда'
      case 'organization': return 'Организация'
      default: return type
    }
  }
  
  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '200px' }}>
        <CircularProgress />
        <Typography sx={{ ml: 2 }}>Загрузка фракций...</Typography>
      </Box>
    )
  }
  
  if (error) {
    return (
      <Alert severity="error">
        Ошибка загрузки фракций: {error.message}
      </Alert>
    )
  }
  
  const factions = data?.factions || []
  
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Box>
        <Typography variant="h6" sx={{ color: 'primary.main', textShadow: '0 0 10px currentColor', mb: 1 }}>
          Выберите фракцию (опционально)
        </Typography>
        <Typography variant="body2" sx={{ color: 'text.secondary' }}>
          Фракция определяет стартовые отношения с другими группировками и доступные квесты
        </Typography>
      </Box>
      
      {!origin && (
        <Alert severity="warning">
          ⚠ Сначала выберите происхождение
        </Alert>
      )}
      
      {origin && factions.length === 0 && (
        <Alert severity="info">
          Нет доступных фракций для выбранного происхождения
        </Alert>
      )}
      
      {origin && factions.length > 0 && (
        <Grid container spacing={3}>
          <Grid size={{ xs: 12, sm: 6, md: 4 }}>
            <Card
              sx={{
                cursor: 'pointer',
                transition: 'all 0.3s ease',
                border: selectedFaction === null ? '2px solid' : '2px solid',
                borderColor: selectedFaction === null ? 'primary.main' : 'divider',
                boxShadow: selectedFaction === null ? '0 0 15px rgba(0, 247, 255, 0.3)' : 'none',
                '&:hover': {
                  borderColor: 'primary.main',
                  transform: 'translateY(-2px)',
                },
              }}
              onClick={() => onFactionSelect(null)}
            >
              <CardContent>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h3" sx={{ mb: 2 }}>🚶</Typography>
                  <Typography variant="h6" sx={{ color: 'primary.main', fontWeight: 'bold', mb: 1 }}>
                    Без фракции
                  </Typography>
                  <Typography variant="body2" sx={{ color: 'text.secondary', mb: 2 }}>
                    Начать как независимый игрок
                  </Typography>
                  <Chip label="Нейтральный" size="small" sx={{ bgcolor: 'text.secondary', color: 'background.paper' }} />
                </Box>
              </CardContent>
            </Card>
          </Grid>
          
          {factions.map((faction) => (
            <Grid size={{ xs: 12, sm: 6, md: 4 }} key={faction.id}>
              <Card
                sx={{
                  cursor: 'pointer',
                  transition: 'all 0.3s ease',
                  border: selectedFaction === faction.id ? '2px solid' : '2px solid',
                  borderColor: selectedFaction === faction.id ? 'primary.main' : 'divider',
                  boxShadow: selectedFaction === faction.id
                    ? '0 0 20px rgba(0, 247, 255, 0.4), 0 0 40px rgba(0, 247, 255, 0.2)'
                    : 'none',
                  '&:hover': {
                    borderColor: 'primary.main',
                    boxShadow: '0 0 15px rgba(0, 247, 255, 0.3)',
                    transform: 'translateY(-2px)',
                  },
                }}
                onClick={() => onFactionSelect(faction.id)}
              >
                <CardContent>
                  <Box sx={{ display: 'flex', alignItems: 'start', justifyContent: 'space-between', mb: 2 }}>
                    <Typography variant="h3">{getFactionTypeIcon(faction.type)}</Typography>
                    {selectedFaction === faction.id && (
                      <CheckCircle sx={{ color: 'primary.main', fontSize: 28 }} />
                    )}
                  </Box>
                  
                  <Typography variant="h6" sx={{ color: 'primary.main', fontWeight: 'bold', mb: 1 }}>
                    {faction.name}
                  </Typography>
                  
                  <Typography variant="body2" sx={{ color: 'text.secondary', mb: 2 }}>
                    {faction.description}
                  </Typography>
                  
                  <Chip
                    label={getFactionTypeName(faction.type)}
                    size="small"
                    sx={{ bgcolor: 'secondary.main', color: 'white', mb: 1 }}
                  />
                  
                  {faction.available_for_origins && faction.available_for_origins.length > 0 && (
                    <Typography variant="caption" sx={{ color: 'text.secondary', display: 'block', mt: 1 }}>
                      Доступна для: {faction.available_for_origins.join(', ')}
                    </Typography>
                  )}
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
      
      {selectedFactionData && (
        <Box
          sx={{
            mt: 2,
            p: 2,
            bgcolor: 'primary.main',
            color: 'black',
            borderRadius: 1,
            display: 'flex',
            alignItems: 'center',
            gap: 1,
          }}
        >
          <CheckCircle />
          <Typography variant="body1" sx={{ fontWeight: 'bold' }}>
            Выбрана фракция: {selectedFactionData.name}
          </Typography>
        </Box>
      )}
    </Box>
  )
}
