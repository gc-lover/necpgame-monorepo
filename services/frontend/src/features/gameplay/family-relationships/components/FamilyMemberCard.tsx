import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box } from '@mui/material'
import FamilyRestroomIcon from '@mui/icons-material/FamilyRestroom'

interface FamilyMemberProps {
  member: {
    character_id?: string
    name?: string
    relationship?: string
    status?: string
  }
}

export const FamilyMemberCard: React.FC<FamilyMemberProps> = ({ member }) => {
  return (
    <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box display="flex" alignItems="center" gap={0.5}>
              <FamilyRestroomIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
              <Typography variant="caption" fontSize="0.75rem" fontWeight="bold">
                {member.name}
              </Typography>
            </Box>
          </Box>
          {member.relationship && <Chip label={member.relationship} size="small" color="info" sx={{ height: 14, fontSize: '0.55rem' }} />}
        </Stack>
      </CardContent>
    </Card>
  )
}

