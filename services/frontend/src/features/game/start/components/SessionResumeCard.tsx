import { Button, Stack, Typography } from '@mui/material'
import HistoryToggleOffIcon from '@mui/icons-material/HistoryToggleOff'
import type { MouseEventHandler } from 'react'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

interface SessionResumeCardProps {
  hasSession: boolean
  onResume?: MouseEventHandler<HTMLButtonElement>
}

export function SessionResumeCard({ hasSession, onResume }: SessionResumeCardProps) {
  return (
    <CompactCard
      color="magenta"
      compact
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        gap: 2,
        justifyContent: 'space-between',
      }}
    >
      <Stack direction="row" spacing={1} alignItems="center">
        <HistoryToggleOffIcon fontSize="small" color="primary" />
        <Typography variant="h6" sx={{ fontSize: '0.95rem', textTransform: 'uppercase' }}>
          Продолжить сессию
        </Typography>
      </Stack>

      <Typography variant="body2" color="text.secondary">
        {hasSession
          ? 'Мы нашли вашу предыдущую сессию. Можно продолжить без повторного запуска.'
          : 'Пока нет активных сессий. Как только начнёте игру, здесь появится быстрый вход.'}
      </Typography>

      <Button
        variant="outlined"
        color="primary"
        onClick={onResume}
        disabled={!hasSession}
        sx={{ alignSelf: 'flex-start' }}
      >
        Вернуться в игру
      </Button>
    </CompactCard>
  )
}






