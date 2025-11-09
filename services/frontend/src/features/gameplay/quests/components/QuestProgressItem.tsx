/**
 * Компонент отображения прогресса квеста
 * Данные из OpenAPI: QuestProgress
 */
import { Paper, Typography, LinearProgress, Stack, Chip, Divider } from '@mui/material'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import type { QuestProgress } from '@/api/generated/quests/models'

interface QuestProgressItemProps {
  quest: QuestProgress
  onClick?: () => void
}

export function QuestProgressItem({ quest, onClick }: QuestProgressItemProps) {
  const progress = quest.progress || 0

  return (
    <Paper
      elevation={2}
      sx={{
        p: 1.5,
        cursor: onClick ? 'pointer' : 'default',
        '&:hover': onClick ? { borderColor: 'primary.main', boxShadow: 2 } : undefined,
      }}
      onClick={onClick}
    >
      <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 0.5 }}>
        <Typography variant="subtitle2" sx={{ fontSize: '0.85rem', fontWeight: 'bold' }}>
          {quest.name}
        </Typography>
        {quest.status === 'completed' && <CheckCircleIcon color="success" sx={{ fontSize: '1rem' }} />}
      </Stack>

      <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary', display: 'block', mb: 1 }}>
        {quest.description}
      </Typography>

      <Stack spacing={0.5}>
        <Stack direction="row" justifyContent="space-between">
          <Typography variant="caption" sx={{ fontSize: '0.7rem' }}>
            Прогресс:
          </Typography>
          <Typography variant="caption" sx={{ fontSize: '0.7rem', fontWeight: 'bold' }}>
            {progress}%
          </Typography>
        </Stack>
        <LinearProgress
          variant="determinate"
          value={progress}
          sx={{ height: 4, borderRadius: 1 }}
          color={progress === 100 ? 'success' : 'primary'}
        />
      </Stack>

      {quest.objectives && quest.objectives.length > 0 && (
        <>
          <Divider sx={{ my: 1 }} />
          <Stack spacing={0.3}>
            {quest.objectives.map((obj) => (
              <Stack key={obj.id} direction="row" justifyContent="space-between" alignItems="center">
                <Typography variant="caption" sx={{ fontSize: '0.65rem' }}>
                  {obj.description}
                </Typography>
                <Chip
                  label={obj.completed ? '✓' : `${obj.currentProgress}/${obj.targetProgress}`}
                  size="small"
                  color={obj.completed ? 'success' : 'default'}
                  sx={{ height: 14, fontSize: '0.6rem' }}
                />
              </Stack>
            ))}
          </Stack>
        </>
      )}
    </Paper>
  )
}

