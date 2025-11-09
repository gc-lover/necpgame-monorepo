import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button, Stack, Alert, CircularProgress, Divider } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { CombatRoleCard } from '../components/CombatRoleCard'
import { useGetCombatRoles } from '@/api/generated/combat-roles/combat-roles/combat-roles'

export const CombatRolesPage: React.FC = () => {
  const navigate = useNavigate()
  const { data, isLoading, error } = useGetCombatRoles()

  const leftPanel = (
    <Stack spacing={2}>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/game')}
        fullWidth
        variant="outlined"
        size="small"
        sx={{ fontSize: '0.75rem' }}
      >
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">
        Боевые роли
      </Typography>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Информация
      </Typography>
      <Divider />
      <Typography variant="body2" fontSize="0.75rem" color="text.secondary">
        Tank, DPS, Support, Healer, Hybrids
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        Доступные роли
      </Typography>
      <Divider />
      {error && <Alert severity="error" sx={{ fontSize: '0.75rem' }}>Ошибка загрузки</Alert>}
      {isLoading && <CircularProgress size={32} />}
      {!isLoading && data?.roles && (
        <Box sx={{ overflowY: 'auto', flex: 1 }}>
          <Stack spacing={1.5}>
            {data.roles.map((role: any) => (
              <CombatRoleCard key={role.id} role={role} />
            ))}
          </Stack>
        </Box>
      )}
    </Stack>
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      {centerContent}
    </GameLayout>
  )
}

export default CombatRolesPage

