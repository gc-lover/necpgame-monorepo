import { Paper, Stack, Typography, Divider } from '@mui/material'
import type { GameQuest, TutorialStepsResponse } from '@/api/generated/game/models'
import { QuestCard, TutorialSteps } from '../../components'

interface InitialStateSidebarProps {
  quest?: GameQuest
  tutorial?: TutorialStepsResponse
  onCompleteTutorial?: () => void
  onSkipTutorial?: () => void
}

export function InitialStateSidebar({
  quest,
  tutorial,
  onCompleteTutorial,
  onSkipTutorial,
}: InitialStateSidebarProps) {
  return (
    <Stack spacing={2} sx={{ height: '100%', overflowY: 'auto', pr: 1 }}>
      <Paper
        elevation={3}
        sx={{
          p: 2,
          backgroundColor: 'background.paper',
          border: '1px solid',
          borderColor: 'divider',
        }}
      >
        <Typography variant="h6" sx={{ fontSize: '0.9rem', fontWeight: 600, color: 'primary.main', mb: 1 }}>
          Первый квест
        </Typography>
        {quest ? (
          <QuestCard quest={quest} />
        ) : (
          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            Квест не найден для текущего персонажа.
          </Typography>
        )}
      </Paper>

      <Divider />

      {tutorial ? (
        <TutorialSteps data={tutorial} onComplete={onCompleteTutorial} onSkip={onSkipTutorial} />
      ) : (
        <Paper
          elevation={0}
          variant="outlined"
          sx={{
            p: 2,
            borderStyle: 'dashed',
            borderColor: 'divider',
          }}
        >
          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            Туториал недоступен или уже завершен.
          </Typography>
        </Paper>
      )}
    </Stack>
  )
}

