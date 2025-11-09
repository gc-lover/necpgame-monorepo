import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import PaymentsIcon from '@mui/icons-material/Payments'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface DividendScheduleItem {
  fundName: string
  payoutDate: string
  expectedAmount: number
  status: 'PLANNED' | 'PAID'
}

export interface DividendScheduleCardProps {
  schedule: DividendScheduleItem[]
}

export const DividendScheduleCard: React.FC<DividendScheduleCardProps> = ({ schedule }) => (
  <CompactCard color="pink" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <PaymentsIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Dividend Schedule
        </Typography>
      </Box>
      <Stack spacing={0.2}>
        {schedule.map((item) => (
          <Typography key={`${item.fundName}-${item.payoutDate}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            {item.payoutDate}: {item.fundName} · {item.expectedAmount.toLocaleString()}¥ · {item.status === 'PAID' ? 'PAID' : 'PLANNED'}
          </Typography>
        ))}
        {schedule.length === 0 && (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Нет запланированных выплат
          </Typography>
        )}
      </Stack>
    </Stack>
  </CompactCard>
)

export default DividendScheduleCard


