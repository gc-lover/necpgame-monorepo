import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import ListAltIcon from '@mui/icons-material/ListAlt'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface MVPEndpointCategory {
  category: string
  count: number
}

export interface EndpointSummary {
  endpoint: string
  method: string
  priority: 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW'
  implemented: boolean
}

const priorityColor: Record<EndpointSummary['priority'], string> = {
  CRITICAL: '#ff2a6d',
  HIGH: '#fef86c',
  MEDIUM: '#00f7ff',
  LOW: '#05ffa1',
}

export interface EndpointsCardProps {
  total: number
  categories: MVPEndpointCategory[]
  endpoints: EndpointSummary[]
}

export const EndpointsCard: React.FC<EndpointsCardProps> = ({ total, categories, endpoints }) => (
  <CompactCard color="cyan" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <ListAltIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            MVP Endpoints
          </Typography>
        </Box>
        <Chip
          label={`${total} endpoints`}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Box display="flex" gap={0.4} flexWrap="wrap">
        {categories.map((category) => (
          <Chip
            key={category.category}
            label={`${category.category}: ${category.count}`}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        ))}
      </Box>
      <Stack spacing={0.3}>
        {endpoints.slice(0, 4).map((entry) => (
          <Box key={entry.endpoint} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                {entry.method} {entry.endpoint}
              </Typography>
              <Typography
                variant="caption"
                fontSize={cyberpunkTokens.fonts.xs}
                sx={{ color: priorityColor[entry.priority] }}
              >
                {entry.priority}
              </Typography>
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              {entry.implemented ? 'Implemented' : 'Planned'}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default EndpointsCard


