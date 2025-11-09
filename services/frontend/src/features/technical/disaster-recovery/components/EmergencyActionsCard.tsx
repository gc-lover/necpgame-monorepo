import React from 'react'
import { Typography, Stack } from '@mui/material'
import WarningAmberIcon from '@mui/icons-material/WarningAmber'
import { CompactCard } from '@/shared/ui/cards'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface EmergencyActionsCardProps {
  onBackup?: () => void
  onRestore?: () => void
  onFailover?: () => void
}

export const EmergencyActionsCard: React.FC<EmergencyActionsCardProps> = ({ onBackup, onRestore, onFailover }) => (
  <CompactCard color="yellow" glowIntensity="strong" compact>
    <Stack spacing={0.5}>
      <Stack direction="row" alignItems="center" spacing={0.6}>
        <WarningAmberIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Emergency Actions
        </Typography>
      </Stack>
      <CyberpunkButton variant="primary" size="small" onClick={onBackup}>
        Запустить emergency backup
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" onClick={onRestore}>
        Восстановить из backup
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" onClick={onFailover}>
        Переключить на резервный датацентр
      </CyberpunkButton>
    </Stack>
  </CompactCard>
)

export default EmergencyActionsCard


