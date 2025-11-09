import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import FamilyRestroomIcon from '@mui/icons-material/FamilyRestroom'
import GameLayout from '@/features/game/components/GameLayout'
import { FamilyMemberCard } from '../components/FamilyMemberCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import { useGetFamilyRelationships } from '@/api/generated/family-relationships/family-relationships/family-relationships'

export const FamilyRelationshipsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()

  const { data: familyData } = useGetFamilyRelationships({ character_id: selectedCharacterId || '' }, { query: { enabled: !!selectedCharacterId } })

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="info">
        Family Relationships
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Семейные связи
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Типы связей
      </Typography>
      <Stack spacing={0.3}>
        {['Родители', 'Дети', 'Родственники'].map((t, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {t}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Механики
      </Typography>
      <Divider />
      <Stack spacing={0.3}>
        <Typography variant="caption" fontSize="0.7rem">
          Система в разработке
        </Typography>
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <FamilyRestroomIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Семейные отношения
        </Typography>
      </Box>
      <Divider />
      <Alert severity="warning" sx={{ fontSize: '0.75rem' }}>
        Система семейных связей (stub). Родители, дети, родственники. В разработке.
      </Alert>
      <Grid container spacing={1}>
        {[].map((member, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <FamilyMemberCard member={member} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default FamilyRelationshipsPage

