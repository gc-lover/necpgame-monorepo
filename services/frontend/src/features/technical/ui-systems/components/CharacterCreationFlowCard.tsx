import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import RouteIcon from '@mui/icons-material/Route'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface CreationStepSummary {
  id: string
  name: string
  description: string
  mandatory: boolean
}

export interface CharacterCreationFlowSummary {
  totalSteps: number
  estimatedMinutes: number
  steps: CreationStepSummary[]
  tutorialEnabled: boolean
}

export interface CharacterCreationFlowCardProps {
  flow: CharacterCreationFlowSummary
}

export const CharacterCreationFlowCard: React.FC<CharacterCreationFlowCardProps> = ({ flow }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <RouteIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Character Creation Flow
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Steps: {flow.totalSteps} · ETA: {flow.estimatedMinutes}m
      </Typography>
      <ProgressBar value={(flow.steps.length / flow.totalSteps) * 100} compact color="purple" label="Configured" />
      <Stack spacing={0.2}>
        {flow.steps.slice(0, 4).map((step) => (
          <Box key={step.id} display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              {step.name}
            </Typography>
            {step.mandatory && (
              <Chip label="required" size="small" color="info" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
            )}
          </Box>
        ))}
      </Stack>
      {flow.tutorialEnabled && (
        <Chip label="Tutorial available" size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
      )}
    </Stack>
  </CompactCard>
)

export default CharacterCreationFlowCard


