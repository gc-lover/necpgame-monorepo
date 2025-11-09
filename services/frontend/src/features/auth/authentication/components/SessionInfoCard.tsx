import { Box, Chip, Stack, TextField, Typography } from '@mui/material'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

type SessionInfoCardProps = {
  accessToken?: string
  refreshToken?: string
  roles?: string[]
}

const truncate = (value?: string, length = 32) => {
  if (!value) return '—'
  if (value.length <= length) return value
  return `${value.slice(0, length)}…`
}

export function SessionInfoCard({ accessToken, refreshToken, roles }: SessionInfoCardProps) {
  return (
    <CompactCard color="cyan" glowIntensity="soft">
      <Stack spacing={1.5}>
        <Typography variant="h6" fontSize="0.85rem" textTransform="uppercase">
          Текущая сессия
        </Typography>
        <Box>
          <Typography variant="caption" color="text.secondary">
            Access token
          </Typography>
          <TextField
            value={truncate(accessToken)}
            size="small"
            InputProps={{ readOnly: true }}
            sx={{ mt: 0.5 }}
          />
        </Box>
        <Box>
          <Typography variant="caption" color="text.secondary">
            Refresh token
          </Typography>
          <TextField
            value={truncate(refreshToken)}
            size="small"
            InputProps={{ readOnly: true }}
            sx={{ mt: 0.5 }}
          />
        </Box>
        <Box>
          <Typography variant="caption" color="text.secondary">
            Роли
          </Typography>
          <Stack direction="row" spacing={0.5} flexWrap="wrap" mt={0.5}>
            {roles && roles.length > 0 ? (
              roles.map((role) => (
                <Chip key={role} label={role} size="small" sx={{ fontSize: '0.65rem', height: 20 }} />
              ))
            ) : (
              <Typography variant="body2" fontSize="0.7rem" color="text.secondary">
                Роли не назначены
              </Typography>
            )}
          </Stack>
        </Box>
      </Stack>
    </CompactCard>
  )
}



