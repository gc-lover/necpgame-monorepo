import React from 'react'
import { Card, CardContent, Typography, Chip, Box, Stack } from '@mui/material'

export const ComboCard: React.FC<{ combo: any }> = ({ combo }) => (
  <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">
            {combo.name}
          </Typography>
          {combo.type && <Chip label={combo.type} size="small" sx={{ height: 18, fontSize: '0.65rem' }} />}
        </Box>
        <Typography variant="body2" color="text.secondary" fontSize="0.7rem">
          {combo.description}
        </Typography>
      </Stack>
    </CardContent>
  </Card>
)

