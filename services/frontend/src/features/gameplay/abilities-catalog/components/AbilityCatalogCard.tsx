import React from 'react'
import { Card, CardContent, Typography, Chip, Box, Stack } from '@mui/material'

interface AbilityCatalogCardProps {
  ability: any
  onClick?: () => void
}

const categoryColors: Record<string, any> = {
  combat: 'error',
  hacking: 'info',
  tech: 'warning',
  stealth: 'secondary',
  support: 'success',
  mobility: 'primary',
  medic: 'success',
}

export const AbilityCatalogCard: React.FC<AbilityCatalogCardProps> = ({ ability, onClick }) => {
  return (
    <Card
      onClick={onClick}
      sx={{
        cursor: onClick ? 'pointer' : 'default',
        transition: 'all 0.2s',
        border: '1px solid',
        borderColor: 'divider',
        '&:hover': onClick ? { borderColor: 'primary.main', boxShadow: 2 } : {},
      }}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={1}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">
              {ability.name}
            </Typography>
            {ability.category && (
              <Chip
                label={ability.category}
                size="small"
                color={categoryColors[ability.category] || 'default'}
                sx={{ height: 20, fontSize: '0.65rem' }}
              />
            )}
          </Box>
          <Typography variant="body2" color="text.secondary" fontSize="0.7rem" noWrap>
            {ability.description}
          </Typography>
        </Stack>
      </CardContent>
    </Card>
  )
}

