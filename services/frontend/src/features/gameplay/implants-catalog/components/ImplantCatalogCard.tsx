import React from 'react'
import { Card, CardContent, Typography, Chip, Box, Stack } from '@mui/material'

export const ImplantCatalogCard: React.FC<{ implant: any; onClick?: () => void }> = ({ implant, onClick }) => (
  <Card onClick={onClick} sx={{ cursor: onClick ? 'pointer' : 'default', border: '1px solid', borderColor: 'divider', '&:hover': onClick ? { borderColor: 'info.main', boxShadow: 2 } : {} }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">{implant.name}</Typography>
          {implant.rarity && <Chip label={implant.rarity} size="small" color="info" sx={{ height: 18, fontSize: '0.65rem' }} />}
        </Box>
        <Typography variant="caption" color="primary" fontSize="0.7rem">{implant.type}</Typography>
        <Typography variant="body2" color="text.secondary" fontSize="0.7rem" noWrap>{implant.description}</Typography>
        {implant.brand && <Typography variant="caption" fontSize="0.65rem">🏢 {implant.brand}</Typography>}
      </Stack>
    </CardContent>
  </Card>
)

