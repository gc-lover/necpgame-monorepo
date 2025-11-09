import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import SignalCellularAltIcon from '@mui/icons-material/SignalCellularAlt'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface CapEntry {
  name: string
  softCap: number
  hardCap: number
  current: number
}

export interface CapsCardProps {
  caps: CapEntry[]
}

export const CapsCard: React.FC<CapsCardProps> = ({ caps }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <SignalCellularAltIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Caps & Limits
        </Typography>
      </Box>
      <Stack spacing={0.3}>
        {caps.slice(0, 4).map((cap) => (
          <Box key={cap.name} display="flex" flexDirection="column" gap={0.1}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
              {cap.name}
            </Typography>
            <ProgressBar
              value={Math.min(100, (cap.current / cap.hardCap) * 100)}
              compact
              color="yellow"
              customText={`Soft ${cap.softCap} · Hard ${cap.hardCap} · Current ${cap.current}`}
            />
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default CapsCard


