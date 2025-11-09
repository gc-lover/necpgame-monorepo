import { Stack, Typography, Paper } from '@mui/material'
import type { GameLocation, GameNPC } from '@/api/generated/game/models'
import { LocationInfo, NPCList } from '../../components'

interface InitialStateContentProps {
  location?: GameLocation
  npcs: GameNPC[]
}

export function InitialStateContent({ location, npcs }: InitialStateContentProps) {
  if (!location) {
    return (
      <Paper
        elevation={0}
        variant="outlined"
        sx={{
          p: 3,
          borderStyle: 'dashed',
          borderColor: 'divider',
          textAlign: 'center',
        }}
      >
        <Typography variant="body2" sx={{ color: 'text.secondary' }}>
          Данные о локации пока недоступны. Попробуйте обновить страницу.
        </Typography>
      </Paper>
    )
  }

  return (
    <Stack spacing={3}>
      <LocationInfo location={location} />
      <NPCList npcs={npcs} />
    </Stack>
  )
}

