/**
 * Панель с деталями локации
 * Данные из OpenAPI: LocationDetails
 */
import {
  Paper,
  Typography,
  Stack,
  Chip,
  Divider,
  Box,
  List,
  ListItem,
  ListItemText,
} from '@mui/material'
import LocationOnIcon from '@mui/icons-material/LocationOn'
import EventIcon from '@mui/icons-material/Event'
import PlaceIcon from '@mui/icons-material/Place'
import HubIcon from '@mui/icons-material/Hub'
import GroupsIcon from '@mui/icons-material/Groups'
import type { LocationDetails } from '@/api/generated/locations/models'

interface LocationDetailsPanelProps {
  location: LocationDetails
}

export function LocationDetailsPanel({ location }: LocationDetailsPanelProps) {
  return (
    <Paper elevation={2} sx={{ p: 2 }}>
      <Stack spacing={1.5}>
        {/* Заголовок */}
        <Stack direction="row" spacing={1} alignItems="center">
          <LocationOnIcon color="primary" sx={{ fontSize: '1.2rem' }} />
          <Typography variant="h6" sx={{ fontSize: '0.95rem', fontWeight: 'bold' }}>
            {location.name}
          </Typography>
        </Stack>

        <Typography variant="body2" sx={{ fontSize: '0.8rem', color: 'text.secondary' }}>
          {location.atmosphere || location.description}
        </Typography>

        <Divider />

        {/* Основная информация */}
        <Stack spacing={0.5}>
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
            📍 {location.district}, {location.city}
          </Typography>
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
            🗺️ Регион: {location.region}
          </Typography>
          <Stack direction="row" spacing={0.5}>
            <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
              Тип:
            </Typography>
            <Chip label={location.type} size="small" sx={{ height: 16, fontSize: '0.6rem' }} />
          </Stack>
          <Stack direction="row" spacing={0.5}>
            <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
              Опасность:
            </Typography>
            <Chip label={location.dangerLevel} size="small" color="warning" sx={{ height: 16, fontSize: '0.6rem' }} />
          </Stack>
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
            ⭐ Минимальный уровень: {location.minLevel}
          </Typography>
        </Stack>

        {/* Точки интереса */}
        {location.pointsOfInterest && location.pointsOfInterest.length > 0 && (
          <>
            <Divider />
            <Box>
              <Stack direction="row" spacing={0.5} alignItems="center" sx={{ mb: 0.5 }}>
                <PlaceIcon sx={{ fontSize: '0.9rem', color: 'primary.main' }} />
                <Typography variant="caption" sx={{ fontSize: '0.7rem', fontWeight: 'bold', color: 'primary.main' }}>
                  Точки интереса:
                </Typography>
              </Stack>
              <List dense sx={{ py: 0 }}>
                {location.pointsOfInterest.map((poi) => (
                  <ListItem key={poi.id} sx={{ py: 0.2, px: 0 }}>
                    <ListItemText
                      primaryTypographyProps={{ fontSize: '0.7rem', fontWeight: 600 }}
                      secondaryTypographyProps={{ fontSize: '0.65rem' }}
                      primary={poi.name}
                      secondary={poi.description}
                    />
                  </ListItem>
                ))}
              </List>
            </Box>
          </>
        )}

        {/* NPC */}
        {location.availableNPCs && location.availableNPCs.length > 0 && (
          <>
            <Divider />
            <Box>
              <Stack direction="row" spacing={0.5} alignItems="center" sx={{ mb: 0.5 }}>
                <GroupsIcon sx={{ fontSize: '0.9rem', color: 'secondary.main' }} />
                <Typography variant="caption" sx={{ fontSize: '0.7rem', fontWeight: 'bold', color: 'secondary.main' }}>
                  NPC в локации:
                </Typography>
              </Stack>
              <Stack direction="row" flexWrap="wrap" sx={{ gap: 0.5 }}>
                {location.availableNPCs.map((npcId) => (
                  <Chip key={npcId} label={npcId} size="small" sx={{ height: 18, fontSize: '0.6rem' }} />
                ))}
              </Stack>
            </Box>
          </>
        )}

        {/* Связанные локации */}
        {location.connectedLocations && location.connectedLocations.length > 0 && (
          <>
            <Divider />
            <Box>
              <Stack direction="row" spacing={0.5} alignItems="center" sx={{ mb: 0.5 }}>
                <HubIcon sx={{ fontSize: '0.9rem', color: 'info.main' }} />
                <Typography variant="caption" sx={{ fontSize: '0.7rem', fontWeight: 'bold', color: 'info.main' }}>
                  Связанные локации:
                </Typography>
              </Stack>
              <Stack direction="row" flexWrap="wrap" sx={{ gap: 0.5 }}>
                {location.connectedLocations.map((locId) => (
                  <Chip key={locId} label={locId} size="small" variant="outlined" sx={{ height: 18, fontSize: '0.6rem' }} />
                ))}
              </Stack>
            </Box>
          </>
        )}

        {/* События */}
        {location.events && location.events.length > 0 && (
          <>
            <Divider />
            <Box>
              <Stack direction="row" spacing={0.5} alignItems="center" sx={{ mb: 0.5 }}>
                <EventIcon sx={{ fontSize: '0.9rem', color: 'warning.main' }} />
                <Typography variant="caption" sx={{ fontSize: '0.7rem', fontWeight: 'bold', color: 'warning.main' }}>
                  События:
                </Typography>
              </Stack>
              <List dense sx={{ py: 0 }}>
                {location.events.map((event, index) => (
                  <ListItem key={event.id ?? index} sx={{ py: 0.2, px: 0 }}>
                    <ListItemText
                      primaryTypographyProps={{ fontSize: '0.7rem', fontWeight: 600 }}
                      secondaryTypographyProps={{ fontSize: '0.65rem' }}
                      primary={event.name || 'Неизвестное событие'}
                      secondary={event.description}
                    />
                  </ListItem>
                ))}
              </List>
            </Box>
          </>
        )}
      </Stack>
    </Paper>
  )
}

