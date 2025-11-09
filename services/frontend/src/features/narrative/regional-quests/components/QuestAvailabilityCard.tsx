import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import AccessTimeIcon from '@mui/icons-material/AccessTime'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { QuestAvailability } from '../types'

export interface QuestAvailabilityCardProps {
  availability: QuestAvailability
}

export const QuestAvailabilityCard: React.FC<QuestAvailabilityCardProps> = ({ availability }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <AccessTimeIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Quest Slots
        </Typography>
      </Box>
      <ProgressBar
        value={(availability.dailySlotsUsed / availability.dailySlotsAvailable) * 100}
        compact
        color="green"
        label="Daily"
        customText={`${availability.dailySlotsUsed}/${availability.dailySlotsAvailable}`}
      />
      <ProgressBar
        value={(availability.weeklySlotsUsed / availability.weeklySlotsAvailable) * 100}
        compact
        color="yellow"
        label="Weekly"
        customText={`${availability.weeklySlotsUsed}/${availability.weeklySlotsAvailable}`}
      />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Reset: {availability.resetsAt}
      </Typography>
    </Stack>
  </CompactCard>
)

export default QuestAvailabilityCard


