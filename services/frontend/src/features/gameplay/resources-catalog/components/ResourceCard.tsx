import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box, Divider } from '@mui/material'
import CategoryIcon from '@mui/icons-material/Category'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'

interface ResourceProps {
  resource: {
    resource_id?: string
    name?: string
    category?: string
    tier?: number
    rarity?: string
    base_value?: number
    stack_size?: number
    weight?: number
    sources?: string[]
  }
}

export const ResourceCard: React.FC<ResourceProps> = ({ resource }) => {
  const getRarityColor = (rarity?: string) => {
    switch (rarity) {
      case 'common':
        return 'default'
      case 'uncommon':
        return 'success'
      case 'rare':
        return 'info'
      case 'epic':
        return 'secondary'
      case 'legendary':
        return 'warning'
      default:
        return 'default'
    }
  }

  return (
    <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box display="flex" alignItems="center" gap={0.5}>
              <CategoryIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
              <Typography variant="caption" fontSize="0.75rem" fontWeight="bold">
                {resource.name}
              </Typography>
            </Box>
            <Chip label={`T${resource.tier || 1}`} size="small" color="primary" sx={{ height: 16, fontSize: '0.6rem' }} />
          </Box>
          <Box display="flex" gap={0.3} flexWrap="wrap">
            <Chip label={resource.category?.toUpperCase()} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />
            {resource.rarity && <Chip label={resource.rarity} size="small" color={getRarityColor(resource.rarity)} sx={{ height: 14, fontSize: '0.55rem' }} />}
          </Box>
          <Divider sx={{ my: 0.5 }} />
          <Box display="flex" justifyContent="space-between">
            <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
              Stack: {resource.stack_size || 0}
            </Typography>
            <Box display="flex" alignItems="center" gap={0.3}>
              <TrendingUpIcon sx={{ fontSize: '0.8rem', color: 'success.main' }} />
              <Typography variant="caption" fontSize="0.65rem" color="success.main">
                {resource.base_value} €$
              </Typography>
            </Box>
          </Box>
          {resource.sources && resource.sources.length > 0 && (
            <Typography variant="caption" fontSize="0.6rem" color="text.secondary">
              Источники: {resource.sources.slice(0, 2).join(', ')}
            </Typography>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

