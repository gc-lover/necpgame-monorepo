import React from 'react'
import { Card, CardContent, Typography, Chip, Box, Stack } from '@mui/material'

interface CombatRoleCardProps {
  role: any
  onClick?: () => void
}

export const CombatRoleCard: React.FC<CombatRoleCardProps> = ({ role, onClick }) => {
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
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">
            {role.name}
          </Typography>
          <Typography variant="body2" color="text.secondary" fontSize="0.7rem">
            {role.description}
          </Typography>
        </Stack>
      </CardContent>
    </Card>
  )
}

