/**
 * Карточка локации
 * Данные из OpenAPI: GameLocation
 */
import { Card, CardContent, Typography, Chip, Stack, Box } from '@mui/material'
import LocationOnIcon from '@mui/icons-material/LocationOn'
import WarningIcon from '@mui/icons-material/Warning'
import LockIcon from '@mui/icons-material/Lock'
import type { GameLocation } from '@/api/generated/locations/models'

interface LocationCardProps {
  location: GameLocation
  onClick: () => void
  isCurrent?: boolean
}

export function LocationCard({ location, onClick, isCurrent }: LocationCardProps) {
  const getDangerColor = (level: string) => {
    switch (level) {
      case 'low': return 'success'
      case 'medium': return 'warning'
      case 'high': return 'error'
      case 'extreme': return 'error'
      default: return 'default'
    }
  }

  return (
    <Card
      sx={{
        cursor: location.accessible ? 'pointer' : 'default',
        border: '2px solid',
        borderColor: isCurrent ? 'primary.main' : location.accessible ? 'divider' : 'error.main',
        bgcolor: isCurrent ? 'rgba(0, 247, 255, 0.1)' : 'transparent',
        opacity: location.accessible ? 1 : 0.6,
        '&:hover': location.accessible ? {
          borderColor: 'primary.main',
          boxShadow: 2,
          transform: 'translateY(-2px)',
          transition: 'all 0.2s',
        } : undefined,
      }}
      onClick={location.accessible ? onClick : undefined}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Stack direction="row" justifyContent="space-between" alignItems="flex-start">
            <Stack direction="row" spacing={0.5} alignItems="center">
              <LocationOnIcon color={isCurrent ? 'primary' : 'inherit'} sx={{ fontSize: '1rem' }} />
              <Typography variant="subtitle2" sx={{ fontSize: '0.875rem', fontWeight: 'bold' }}>
                {location.name}
              </Typography>
            </Stack>
            {isCurrent && (
              <Chip label="Здесь" size="small" color="primary" sx={{ height: 16, fontSize: '0.6rem' }} />
            )}
          </Stack>

          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
            {location.district}, {location.city}
          </Typography>

          <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary', minHeight: 32 }}>
            {location.description}
          </Typography>

          <Stack direction="row" spacing={0.5} flexWrap="wrap">
            <Chip 
              label={location.type} 
              size="small" 
              color="secondary" 
              sx={{ height: 16, fontSize: '0.6rem' }} 
            />
            <Chip 
              label={location.dangerLevel} 
              size="small" 
              color={getDangerColor(location.dangerLevel) as any}
              icon={<WarningIcon sx={{ fontSize: '0.7rem' }} />}
              sx={{ height: 16, fontSize: '0.6rem' }} 
            />
            <Chip 
              label={`Ур.${location.minLevel}+`} 
              size="small" 
              variant="outlined" 
              sx={{ height: 16, fontSize: '0.6rem' }} 
            />
            {!location.accessible && (
              <Chip 
                label="Закрыто" 
                size="small" 
                color="error" 
                icon={<LockIcon sx={{ fontSize: '0.7rem' }} />}
                sx={{ height: 16, fontSize: '0.6rem' }} 
              />
            )}
          </Stack>
        </Stack>
      </CardContent>
    </Card>
  )
}

