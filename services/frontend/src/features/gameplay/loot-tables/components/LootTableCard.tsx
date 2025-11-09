import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box } from '@mui/material'
import type { LootTable } from '@/api/generated/loot-tables/models'

interface LootTableCardProps {
  table: LootTable
  onClick?: (tableId: string) => void
}

export const LootTableCard: React.FC<LootTableCardProps> = ({ table, onClick }) => {
  const getSourceColor = (source?: string) => {
    switch (source) {
      case 'boss':
        return 'error'
      case 'enemy':
        return 'warning'
      case 'quest':
        return 'success'
      case 'container':
        return 'info'
      case 'event':
        return 'primary'
      default:
        return 'default'
    }
  }

  const getTierColor = (tier?: string) => {
    if (!tier) return 'default'
    const tierNum = parseInt(tier.replace('t', ''))
    if (tierNum >= 5) return 'error'
    if (tierNum >= 3) return 'warning'
    return 'success'
  }

  return (
    <Card
      sx={{
        border: '1px solid',
        borderColor: 'divider',
        cursor: onClick ? 'pointer' : 'default',
        '&:hover': onClick ? { borderColor: 'primary.main' } : {},
      }}
      onClick={() => onClick && table.table_id && onClick(table.table_id)}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">
              {table.name || 'Loot Table'}
            </Typography>
            <Chip label={table.tier || 't1'} size="small" color={getTierColor(table.tier)} sx={{ height: 18, fontSize: '0.65rem' }} />
          </Box>
          <Box display="flex" gap={0.5}>
            <Chip label={table.source_type || 'source'} size="small" color={getSourceColor(table.source_type)} sx={{ height: 16, fontSize: '0.6rem' }} />
          </Box>
          {table.description && (
            <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
              {table.description}
            </Typography>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

