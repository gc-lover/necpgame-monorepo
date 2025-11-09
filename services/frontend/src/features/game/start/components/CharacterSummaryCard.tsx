import { Stack, Typography, Chip, Divider } from '@mui/material'
import { CompactCard } from '@/shared/ui/cards/CompactCard'
import type { WelcomeScreenResponse } from '@/api/generated/game/models'

interface CharacterSummaryCardProps {
  character: WelcomeScreenResponse['character']
  startingLocation: string
}

export function CharacterSummaryCard({ character, startingLocation }: CharacterSummaryCardProps) {
  return (
    <CompactCard
      color="purple"
      compact
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        gap: 2,
      }}
    >
      <Typography variant="h6" sx={{ fontSize: '0.95rem', textTransform: 'uppercase' }}>
        Персонаж
      </Typography>

      <Stack direction="row" spacing={1} alignItems="center">
        <Chip label={`Класс: ${character.class}`} color="primary" size="small" />
        <Chip label={`Уровень: ${character.level}`} color="secondary" size="small" />
      </Stack>

      <Stack spacing={0.75}>
        <Typography variant="body2">
          <Typography component="span" variant="subtitle2" sx={{ mr: 0.5 }}>
            Имя:
          </Typography>
          {character.name}
        </Typography>
      </Stack>

      <Divider sx={{ borderColor: 'rgba(255,255,255,0.08)' }} />

      <Stack spacing={0.75}>
        <Typography variant="subtitle2" sx={{ textTransform: 'uppercase', color: 'text.secondary' }}>
          Стартовая локация
        </Typography>
        <Typography variant="body2">{startingLocation}</Typography>
        <Typography variant="caption" color="text.secondary">
          Именно здесь начнётся ваша первая сессия. Город не ждёт — двигайтесь.
        </Typography>
      </Stack>
    </CompactCard>
  )
}






