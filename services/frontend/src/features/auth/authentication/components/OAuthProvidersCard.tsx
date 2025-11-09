import { Stack, Typography, Button } from '@mui/material'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

const providers = [
  { id: 'steam', label: 'Steam' },
  { id: 'google', label: 'Google' },
  { id: 'discord', label: 'Discord' },
]

export function OAuthProvidersCard() {
  const handleProviderClick = (provider: string) => {
    window.open(`/auth/oauth/${provider}/authorize`, '_blank', 'noopener,noreferrer')
  }

  return (
    <CompactCard color="teal" glowIntensity="soft" sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      <Stack spacing={2}>
        <Typography variant="h6" fontSize="0.9rem" textTransform="uppercase">
          OAuth провайдеры
        </Typography>
        <Typography variant="body2" color="text.secondary" fontSize="0.75rem">
          Для тестирования OAuth откройте авторизацию в новом окне. После подтверждения провайдер вернёт
          вас в игру и выдаст токены.
        </Typography>
        <Stack spacing={1}>
          {providers.map((provider) => (
            <Button
              key={provider.id}
              variant="outlined"
              size="small"
              onClick={() => handleProviderClick(provider.id)}
              sx={{ fontSize: '0.75rem' }}
            >
              Авторизация через {provider.label}
            </Button>
          ))}
        </Stack>
      </Stack>
    </CompactCard>
  )
}



