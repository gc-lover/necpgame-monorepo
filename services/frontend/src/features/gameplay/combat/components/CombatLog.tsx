import { Card, CardContent, Typography, Stack } from '@mui/material'

interface CombatLogProps {
  entries?: string[]
}

export function CombatLog({ entries }: CombatLogProps) {
  if (!entries?.length) {
    return (
      <Card variant="outlined">
        <CardContent sx={{ p: 2 }}>
          <Typography variant="caption" sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
            Лог боя пока пуст.
          </Typography>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent sx={{ p: 2, display: 'flex', flexDirection: 'column', height: '100%' }}>
        <Typography variant="subtitle2" sx={{ fontSize: '0.85rem', fontWeight: 600, mb: 1 }}>
          Лог боя
        </Typography>
        <Stack spacing={0.75} sx={{ overflowY: 'auto' }}>
          {entries.map((item, index) => (
            <Typography key={`${index}-${item}`} variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
              {item}
            </Typography>
          ))}
        </Stack>
      </CardContent>
    </Card>
  )
}

