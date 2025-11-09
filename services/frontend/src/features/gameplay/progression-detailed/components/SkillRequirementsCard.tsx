import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TaskAltIcon from '@mui/icons-material/TaskAlt'
import ErrorIcon from '@mui/icons-material/Error'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface SkillRequirementEntry {
  skill: string
  required: number
  current: number
}

export interface SkillRequirementsCardProps {
  itemName: string
  isEligible: boolean
  requirements: SkillRequirementEntry[]
}

export const SkillRequirementsCard: React.FC<SkillRequirementsCardProps> = ({ itemName, isEligible, requirements }) => (
  <CompactCard color={isEligible ? 'green' : 'pink'} glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        {isEligible ? (
          <TaskAltIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        ) : (
          <ErrorIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        )}
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {itemName}
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={isEligible ? 'success.main' : 'error.main'}>
        {isEligible ? 'Все требования соблюдены' : 'Не хватает навыков'}
      </Typography>
      <Stack spacing={0.2}>
        {requirements.map((requirement) => (
          <Box key={requirement.skill} display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
              {requirement.skill}
            </Typography>
            <Typography
              variant="caption"
              fontSize={cyberpunkTokens.fonts.xs}
              color={requirement.current >= requirement.required ? 'success.main' : 'error.main'}
            >
              {requirement.current}/{requirement.required}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default SkillRequirementsCard


