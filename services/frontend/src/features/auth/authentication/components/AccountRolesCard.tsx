import { Alert, Box, CircularProgress, Stack, Typography } from '@mui/material'
import { useGetRoles } from '@/api/generated/auth/authentication/authorization/authorization'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

type AccountRolesCardProps = {
  enabled: boolean
}

export function AccountRolesCard({ enabled }: AccountRolesCardProps) {
  const rolesQuery = useGetRoles({
    query: {
      enabled,
    },
  })

  return (
    <CompactCard color="blue" glowIntensity="soft" sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      <Stack spacing={2} sx={{ flex: 1 }}>
        <Box>
          <Typography variant="h6" fontSize="0.9rem" textTransform="uppercase">
            Роли аккаунта
          </Typography>
          <Typography variant="body2" color="text.secondary" fontSize="0.75rem">
            Список ролей, предоставленных текущему аккаунту. Требуется активный access token.
          </Typography>
        </Box>

        {rolesQuery.isLoading ? (
          <Box display="flex" justifyContent="center" py={2}>
            <CircularProgress size={26} />
          </Box>
        ) : rolesQuery.error ? (
          <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
            Не удалось загрузить роли. Убедитесь, что вход выполнен.
          </Alert>
        ) : rolesQuery.data?.roles && rolesQuery.data.roles.length > 0 ? (
          <Stack spacing={1}>
            {rolesQuery.data.roles.map((role, index) => (
              <Typography key={index} variant="body2" fontSize="0.75rem">
                • {role}
              </Typography>
            ))}
          </Stack>
        ) : (
          <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
            Роли не назначены или токен не предоставляет доступ к ним.
          </Alert>
        )}
      </Stack>
    </CompactCard>
  )
}



