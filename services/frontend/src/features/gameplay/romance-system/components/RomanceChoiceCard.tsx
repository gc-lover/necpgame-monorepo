import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TipsAndUpdatesIcon from '@mui/icons-material/TipsAndUpdates'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface RomanceChoiceSummary {
  choiceId: string
  text: string
  affectionChange: number
  skillCheck?: string | null
}

export interface RomanceEventInstanceSummary {
  instanceId: string
  eventName: string
  stage: string
  choices: RomanceChoiceSummary[]
}

export interface RomanceChoiceCardProps {
  instance: RomanceEventInstanceSummary
}

export const RomanceChoiceCard: React.FC<RomanceChoiceCardProps> = ({ instance }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TipsAndUpdatesIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {instance.eventName}
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Stage: {instance.stage} · Instance: {instance.instanceId.slice(0, 8)}
      </Typography>
      <Stack spacing={0.3}>
        {instance.choices.map((choice) => (
          <Box key={choice.choiceId} display="flex" flexDirection="column" gap={0.1}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
              {choice.text}
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Affection {choice.affectionChange >= 0 ? '+' : ''}{choice.affectionChange}
              {choice.skillCheck ? ` · Skill: ${choice.skillCheck}` : ''}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default RomanceChoiceCard


