import React from 'react'
import { Card, CardContent, Typography, Chip, Box, Stack } from '@mui/material'

export const ClassCard: React.FC<{ gameClass: any; onClick?: () => void }> = ({ gameClass, onClick }) => (
  <Card onClick={onClick} sx={{ cursor: onClick ? 'pointer' : 'default', border: '1px solid', borderColor: 'divider', '&:hover': onClick ? { borderColor: 'primary.main', boxShadow: 2 } : {} }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">{gameClass.name}</Typography>
          {gameClass.source && <Chip label={gameClass.source} size="small" color="primary" sx={{ height: 18, fontSize: '0.65rem' }} />}
        </Box>
        <Typography variant="caption" color="text.secondary" fontSize="0.7rem">{gameClass.role}</Typography>
        <Typography variant="body2" color="text.secondary" fontSize="0.7rem" noWrap>{gameClass.description}</Typography>
      </Stack>
    </CardContent>
  </Card>
)

