/**
 * Список соседних локаций
 * Данные из OpenAPI: ConnectedLocation[]
 */
import { Paper, Typography, Stack, List, ListItem, ListItemButton, ListItemText, Chip, Box } from '@mui/material'
import ArrowForwardIcon from '@mui/icons-material/ArrowForward'
import LockIcon from '@mui/icons-material/Lock'
import type { ConnectedLocation } from '@/api/generated/locations/models'

interface ConnectedLocationsListProps {
  locations: ConnectedLocation[]
  onTravel: (locationId: string) => void
}

export function ConnectedLocationsList({ locations, onTravel }: ConnectedLocationsListProps) {
  if (locations.length === 0) {
    return (
      <Paper elevation={1} sx={{ p: 2, textAlign: 'center' }}>
        <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.75rem' }}>
          Нет доступных направлений
        </Typography>
      </Paper>
    )
  }

  return (
    <Paper elevation={2} sx={{ p: 1.5 }}>
      <Typography variant="subtitle2" sx={{ fontSize: '0.8rem', mb: 1, color: 'primary.main' }}>
        Куда можно пойти:
      </Typography>
      <List dense sx={{ py: 0 }}>
        {locations.map((loc) => (
          <ListItem key={loc.id} disablePadding sx={{ mb: 0.5 }}>
            <ListItemButton
              onClick={() => onTravel(loc.id)}
              disabled={!loc.accessible}
              sx={{
                borderRadius: 1,
                border: '1px solid',
                borderColor: loc.accessible ? 'divider' : 'error.main',
                '&:hover': loc.accessible ? {
                  borderColor: 'primary.main',
                  bgcolor: 'rgba(0, 247, 255, 0.05)',
                } : undefined,
              }}
            >
              <ListItemText
                primary={
                  <Stack direction="row" spacing={0.5} alignItems="center">
                    <ArrowForwardIcon sx={{ fontSize: '0.8rem' }} />
                    <span style={{ fontSize: '0.8rem' }}>{loc.name}</span>
                  </Stack>
                }
                secondary={
                  <Box sx={{ mt: 0.3 }}>
                    <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary', display: 'block' }}>
                      {loc.distance} • {loc.travelTime} мин
                    </Typography>
                    {loc.fastTravelAvailable && (
                      <Chip
                        label="Быстрое перемещение"
                        size="small"
                        color="primary"
                        sx={{ height: 14, fontSize: '0.55rem', mt: 0.3 }}
                      />
                    )}
                    {loc.requirements && !loc.accessible && (
                      <Chip
                        label="Требования не выполнены"
                        size="small"
                        color="error"
                        icon={<LockIcon sx={{ fontSize: '0.6rem' }} />}
                        sx={{ height: 14, fontSize: '0.55rem', mt: 0.3 }}
                      />
                    )}
                  </Box>
                }
              />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Paper>
  )
}

