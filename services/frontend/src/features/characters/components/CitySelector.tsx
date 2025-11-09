import { useGetCities } from '../../../api/generated/auth/reference-data/reference-data'
import { Card, CardContent, Box, Typography, CircularProgress, Alert, Grid, Chip } from '@mui/material'
import { CheckCircle } from '@mui/icons-material'

/**
 * Пропсы компонента CitySelector
 */
interface CitySelectorProps {
  /** Выбранная фракция (для фильтрации городов) */
  factionId: string | null
  /** Выбранный город */
  selectedCity: string | null
  /** Обработчик выбора города */
  onCitySelect: (cityId: string) => void
}

/**
 * Компонент выбора стартового города персонажа
 */
export function CitySelector({ factionId, selectedCity, onCitySelect }: CitySelectorProps) {
  const { data, isLoading, error } = useGetCities(
    factionId ? { faction_id: factionId } : undefined
  )
  const selectedCityData = data?.cities?.find((city) => city.id === selectedCity)
  
  const getRegionFlag = (cityRegion: string | null | undefined) => {
    if (!cityRegion) return '🏙️'
    const upperRegion = cityRegion.toUpperCase()
    switch (upperRegion) {
      case 'US':
      case 'NA':
        return '🇺🇸' // США
      case 'JP':
      case 'AS':
        return '🇯🇵' // Япония
      case 'EU':
        return '🇪🇺'
      case 'RU':
        return '🇷🇺'
      default:
        return '🏙️'
    }
  }
  
  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '200px' }}>
        <CircularProgress />
        <Typography sx={{ ml: 2 }}>Загрузка городов...</Typography>
      </Box>
    )
  }
  
  if (error) {
    return (
      <Alert severity="error">
        Ошибка загрузки городов: {error.message}
      </Alert>
    )
  }
  
  const cities = data?.cities || []
  
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Box>
        <Typography variant="h6" sx={{ color: 'primary.main', textShadow: '0 0 10px currentColor', mb: 1 }}>
          Выберите стартовый город
        </Typography>
        <Typography variant="body2" sx={{ color: 'text.secondary' }}>
          Стартовый город определяет регион игры и доступные локации
        </Typography>
      </Box>
      
      {cities.length === 0 && (
        <Alert severity="info">
          Нет доступных городов
        </Alert>
      )}
      
      {cities.length > 0 && (
        <Grid container spacing={3}>
          {cities.map((city) => (
            <Grid size={{ xs: 12, sm: 6, md: 4 }} key={city.id}>
              <Card
                sx={{
                  cursor: 'pointer',
                  transition: 'all 0.3s ease',
                  border: selectedCity === city.id ? '2px solid' : '2px solid',
                  borderColor: selectedCity === city.id ? 'primary.main' : 'divider',
                  boxShadow: selectedCity === city.id
                    ? '0 0 20px rgba(0, 247, 255, 0.4), 0 0 40px rgba(0, 247, 255, 0.2)'
                    : 'none',
                  '&:hover': {
                    borderColor: 'primary.main',
                    boxShadow: '0 0 15px rgba(0, 247, 255, 0.3)',
                    transform: 'translateY(-2px)',
                  },
                }}
                onClick={() => onCitySelect(city.id)}
              >
                <CardContent>
                  <Box sx={{ display: 'flex', alignItems: 'start', justifyContent: 'space-between', mb: 2 }}>
                    <Box>
                      <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 'bold', mb: 1 }}>
                        {city.name}
                      </Typography>
                      <Chip
                        icon={<Box component="span">{getRegionFlag(city.region)}</Box>}
                        label={city.region}
                        size="small"
                        sx={{ bgcolor: 'primary.main', color: 'black' }}
                      />
                    </Box>
                    {selectedCity === city.id && (
                      <CheckCircle sx={{ color: 'primary.main', fontSize: 28 }} />
                    )}
                  </Box>
                  
                  <Typography variant="body2" sx={{ color: 'text.secondary', mb: 2 }}>
                    {city.description}
                  </Typography>
                  
                  {city.available_for_factions && city.available_for_factions.length > 0 && (
                    <Typography variant="caption" sx={{ color: 'text.secondary' }}>
                      Доступных фракций: {city.available_for_factions.length}
                    </Typography>
                  )}
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
      
      {selectedCityData && (
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
            Выбран город: {selectedCityData.name} ({selectedCityData.region})
          </Typography>
        </Box>
      )}
    </Box>
  )
}
