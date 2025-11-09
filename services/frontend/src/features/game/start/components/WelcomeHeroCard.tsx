import { Box, Typography } from '@mui/material'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

interface WelcomeHeroCardProps {
  message: string
  subtitle: string
}

export function WelcomeHeroCard({ message, subtitle }: WelcomeHeroCardProps) {
  return (
    <CompactCard
      color="cyan"
      glowIntensity="strong"
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        gap: 2,
        justifyContent: 'center',
        textAlign: 'center',
      }}
    >
      <Typography
        variant="h4"
        sx={{
          fontWeight: 700,
          textTransform: 'uppercase',
          letterSpacing: '0.12em',
          color: 'primary.main',
        }}
      >
        {message}
      </Typography>

      <Typography
        variant="h6"
        sx={{
          color: 'text.secondary',
          fontStyle: 'italic',
          letterSpacing: '0.08em',
        }}
      >
        {subtitle}
      </Typography>

      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          gap: 1,
          flexWrap: 'wrap',
        }}
      >
        <Typography variant="caption" color="text.secondary">
          Night City • 2020
        </Typography>
        <Typography variant="caption" color="text.secondary">
          Downtown District
        </Typography>
        <Typography variant="caption" color="text.secondary">
          Неон, дождь и новые возможности
        </Typography>
      </Box>
    </CompactCard>
  )
}






