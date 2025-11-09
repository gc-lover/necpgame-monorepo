import React from 'react'
import { Card, CardContent, Typography, Stack, Chip } from '@mui/material'

export const RecipeCard: React.FC<{ recipe: any }> = ({ recipe }) => (
  <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">{recipe.name || 'Рецепт'}</Typography>
        <Chip label="Крафт" size="small" color="success" sx={{ height: 18, fontSize: '0.65rem', width: 'fit-content' }} />
        <Typography variant="caption" fontSize="0.7rem" color="text.secondary">Требует детализации API</Typography>
      </Stack>
    </CardContent>
  </Card>
)

