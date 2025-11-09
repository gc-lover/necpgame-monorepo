import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import SchemaIcon from '@mui/icons-material/Schema'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ModelFieldSummary {
  fieldName: string
  type: string
  required: boolean
}

export interface ModelDefinitionSummary {
  modelName: string
  description: string
  fields: ModelFieldSummary[]
}

export interface ModelsCardProps {
  models: ModelDefinitionSummary[]
}

export const ModelsCard: React.FC<ModelsCardProps> = ({ models }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <SchemaIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          MVP Data Models
        </Typography>
      </Box>
      <Stack spacing={0.3}>
        {models.slice(0, 3).map((model) => (
          <Box key={model.modelName} display="flex" flexDirection="column" gap={0.15}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
              {model.modelName}
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              {model.description}
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Fields: {model.fields.slice(0, 3).map((field) => `${field.fieldName}${field.required ? '*' : ''}`).join(', ')}
              {model.fields.length > 3 ? ' …' : ''}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ModelsCard


