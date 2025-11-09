import { useGetCharacterOrigins } from '../../api/generated/auth/characters/characters'
import { Card, CardContent, Box, Typography, CircularProgress, Alert, Grid, Chip, List, ListItem, ListItemText } from '@mui/material'
import { CheckCircle } from '@mui/icons-material'

/**
 * Пропсы компонента OriginSelector
 */
interface OriginSelectorProps {
  /** Выбранное происхождение */
  selectedOrigin: string | null
  /** Обработчик выбора происхождения */
  onOriginSelect: (originId: string) => void
}

/**
 * Компонент выбора происхождения персонажа
 */
export function OriginSelector({ selectedOrigin, onOriginSelect }: OriginSelectorProps) {
  const { data, isLoading, error } = useGetCharacterOrigins()
  const selectedOriginData = data?.origins?.find((origin) => origin.id === selectedOrigin)
  
  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '200px' }}>
        <CircularProgress />
        <Typography sx={{ ml: 2 }}>Загрузка происхождений...</Typography>
      </Box>
    )
  }
  
  if (error) {
    return (
      <Alert severity="error">
        Ошибка загрузки происхождений: {error.message}
      </Alert>
    )
  }
  
  const origins = data?.origins || []
  
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Box>
        <Typography variant="h6" sx={{ color: 'primary.main', textShadow: '0 0 10px currentColor', mb: 1 }}>
          Выберите происхождение
        </Typography>
        <Typography variant="body2" sx={{ color: 'text.secondary' }}>
          Происхождение влияет на доступные фракции, стартовые навыки и ресурсы
        </Typography>
      </Box>
      
      <Grid container spacing={3}>
        {origins.map((origin) => (
          <Grid size={{ xs: 12, md: 6 }} key={origin.id}>
            <Card
              sx={{
                cursor: 'pointer',
                transition: 'all 0.3s ease',
                border: selectedOrigin === origin.id ? '2px solid' : '2px solid',
                borderColor: selectedOrigin === origin.id ? 'primary.main' : 'divider',
                boxShadow: selectedOrigin === origin.id
                  ? '0 0 20px rgba(0, 247, 255, 0.4), 0 0 40px rgba(0, 247, 255, 0.2)'
                  : 'none',
                '&:hover': {
                  borderColor: 'primary.main',
                  boxShadow: '0 0 15px rgba(0, 247, 255, 0.3)',
                  transform: 'translateY(-2px)',
                },
              }}
              onClick={() => onOriginSelect(origin.id)}
            >
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'start', justifyContent: 'space-between', mb: 2 }}>
                  <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 'bold' }}>
                    {origin.name}
                  </Typography>
                  {selectedOrigin === origin.id && (
                    <CheckCircle sx={{ color: 'primary.main', fontSize: 28 }} />
                  )}
                </Box>
                
                <Typography variant="body2" sx={{ color: 'text.secondary', mb: 2 }}>
                  {origin.description}
                </Typography>
                
                {origin.starting_skills && origin.starting_skills.length > 0 && (
                  <Box sx={{ mb: 2 }}>
                    <Typography variant="subtitle2" sx={{ mb: 1, color: 'primary.main' }}>
                      Стартовые навыки:
                    </Typography>
                    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                      {origin.starting_skills.map((skill, index) => (
                        <Chip key={index} label={skill} size="small" sx={{ bgcolor: 'primary.main', color: 'black' }} />
                      ))}
                    </Box>
                  </Box>
                )}
                
                {origin.starting_resources && (
                  <Box sx={{ mb: 2 }}>
                    <Typography variant="subtitle2" sx={{ mb: 1, color: 'primary.main' }}>
                      Стартовые ресурсы:
                    </Typography>
                    <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                      💰 Валюта: {origin.starting_resources.currency}
                    </Typography>
                    {origin.starting_resources.items && origin.starting_resources.items.length > 0 && (
                      <Box sx={{ mt: 1 }}>
                        <Typography variant="body2" sx={{ color: 'text.secondary', mb: 0.5 }}>
                          📦 Предметы:
                        </Typography>
                        <List dense sx={{ py: 0 }}>
                          {origin.starting_resources.items.map((item, index) => (
                            <ListItem key={index} sx={{ py: 0.5, px: 0 }}>
                              <ListItemText 
                                primary={item} 
                                primaryTypographyProps={{ variant: 'body2', sx: { color: 'text.secondary' } }}
                              />
                            </ListItem>
                          ))}
                        </List>
                      </Box>
                    )}
                  </Box>
                )}
                
                {origin.available_factions && origin.available_factions.length > 0 && (
                  <Chip
                    label={`⚔️ Доступных фракций: ${origin.available_factions.length}`}
                    size="small"
                    sx={{ bgcolor: 'secondary.main', color: 'white' }}
                  />
                )}
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
      
      {selectedOriginData && (
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
            Выбрано происхождение: {selectedOriginData.name}
          </Typography>
        </Box>
      )}
    </Box>
  )
}
