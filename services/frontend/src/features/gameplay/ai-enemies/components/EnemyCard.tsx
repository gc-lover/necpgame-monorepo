import React from 'react'
import { Card, CardContent, Typography, Chip, Box, Stack } from '@mui/material'

interface EnemyCardProps {
  enemy: any
  onClick?: () => void
}

export const EnemyCard: React.FC<EnemyCardProps> = ({ enemy, onClick }) => {
  return (
    <Card
      onClick={onClick}
      sx={{
        cursor: onClick ? 'pointer' : 'default',
        transition: 'all 0.2s',
        border: '1px solid',
        borderColor: 'divider',
        '&:hover': onClick ? { borderColor: 'error.main', boxShadow: 2 } : {},
      }}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">
            {enemy.name}
          </Typography>
          <Typography variant="body2" color="text.secondary" fontSize="0.7rem" noWrap>
            {enemy.description}
          </Typography>
          {enemy.threat_level && (
            <Chip
              label={`Уровень угрозы: ${enemy.threat_level}`}
              size="small"
              color="error"
              sx={{ height: 18, fontSize: '0.65rem', alignSelf: 'flex-start' }}
            />
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

