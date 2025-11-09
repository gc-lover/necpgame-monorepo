import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import DescriptionIcon from '@mui/icons-material/Description'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ContractTemplateSummary {
  templateId: string
  name: string
  type: 'EXCHANGE' | 'SERVICE' | 'COURIER' | 'AUCTION'
  recommendedCollateral: number
  autoExecution: boolean
  tags: string[]
}

export interface ContractTemplateCardProps {
  template: ContractTemplateSummary
}

export const ContractTemplateCard: React.FC<ContractTemplateCardProps> = ({ template }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <DescriptionIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {template.name}
          </Typography>
        </Box>
        <Chip
          label={template.type}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Template #{template.templateId.slice(0, 6)} · Collateral {template.recommendedCollateral}¥
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Auto-execution: {template.autoExecution ? 'Enabled' : 'Manual review'}
      </Typography>
      <Box display="flex" flexWrap="wrap" gap={0.4}>
        {template.tags.map((tag) => (
          <Chip
            key={tag}
            label={tag}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        ))}
      </Box>
    </Stack>
  </CompactCard>
)

export default ContractTemplateCard


