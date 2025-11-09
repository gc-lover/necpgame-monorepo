import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import DashboardIcon from '@mui/icons-material/Dashboard'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface MainUIDataSummary {
  character: {
    name: string
    level: number
    xp: number
    xpNeeded: number
  }
  stats: Record<string, number>
  quests: string[]
  notifications: string[]
}

export interface MainUIDataCardProps {
  data: MainUIDataSummary
}

export const MainUIDataCard: React.FC<MainUIDataCardProps> = ({ data }) => (
  <CompactCard color="cyan" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <DashboardIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Main UI Data
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {data.character.name} · Lvl {data.character.level} ({data.character.xp}/{data.character.xpNeeded} XP)
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Stats: {Object.entries(data.stats).map(([key, value]) => `${key} ${value}`).join(' · ')}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Quests: {data.quests.join(', ') || '—'}
      </Typography>
      <Stack spacing={0.2}>
        {data.notifications.slice(0, 3).map((note, index) => (
          <Typography key={`${note}-${index}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {note}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default MainUIDataCard


