/**
 * Компонент отображения информации о локации
 */
import { Typography, Chip, Stack, Paper } from '@mui/material'
import LocationOnIcon from '@mui/icons-material/LocationOn'
import WarningAmberIcon from '@mui/icons-material/WarningAmber'
import type { GameLocation } from '@/api/generated/game/models'

interface LocationInfoProps {
  location: GameLocation
}

export function LocationInfo({ location }: LocationInfoProps) {
  const getDangerColor = (level: string) => {
    switch (level) {
      case 'low':
        return 'success'
      case 'medium':
        return 'warning'
      case 'high':
        return 'error'
      default:
        return 'default'
    }
  }

  const getDangerLabel = (level: string) => {
    switch (level) {
      case 'low':
        return 'Низкая опасность'
      case 'medium':
        return 'Средняя опасность'
      case 'high':
        return 'Высокая опасность'
      default:
        return level
    }
  }

  return (
    <Paper
      elevation={3}
      sx={{
        p: 2,
        backgroundColor: 'background.paper',
        border: '2px solid',
        borderColor: 'primary.main',
      }}
    >
      <Stack direction="row" spacing={1} alignItems="center" sx={{ mb: 1 }}>
        <LocationOnIcon color="primary" sx={{ fontSize: '1.2rem' }} />
        <Typography variant="h6" sx={{ color: 'primary.main', fontWeight: 'bold', fontSize: '1rem' }}>
          {location.name}
        </Typography>
      </Stack>

      <Typography variant="body2" sx={{ mb: 1.5, color: 'text.primary', fontSize: '0.85rem' }}>
        {location.description}
      </Typography>

      <Stack direction="row" spacing={0.5} flexWrap="wrap" sx={{ gap: 0.5 }}>
        {location.city && (
          <Chip label={location.city} size="small" variant="outlined" sx={{ height: 20, fontSize: '0.65rem' }} />
        )}
        {location.district && (
          <Chip label={location.district} size="small" variant="outlined" sx={{ height: 20, fontSize: '0.65rem' }} />
        )}
        {location.type && (
          <Chip label={location.type} size="small" color="primary" sx={{ height: 20, fontSize: '0.65rem' }} />
        )}
        {location.dangerLevel && (
          <Chip
            icon={<WarningAmberIcon sx={{ fontSize: '0.8rem' }} />}
            label={getDangerLabel(location.dangerLevel)}
            size="small"
            color={getDangerColor(location.dangerLevel)}
            sx={{ height: 20, fontSize: '0.65rem' }}
          />
        )}
        {location.minLevel !== undefined && (
          <Chip label={`Ур.${location.minLevel}+`} size="small" color="secondary" sx={{ height: 20, fontSize: '0.65rem' }} />
        )}
        {location.connectedLocations && location.connectedLocations.length > 0 && (
          <Chip 
            label={`${location.connectedLocations.length} выхода`} 
            size="small" 
            variant="outlined"
            sx={{ height: 20, fontSize: '0.65rem' }}
          />
        )}
      </Stack>
    </Paper>
  )
}

