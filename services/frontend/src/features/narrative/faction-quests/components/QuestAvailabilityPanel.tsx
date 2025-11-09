import { Card, CardContent, Chip, Stack, Typography } from '@mui/material'
import type {
  GetAvailableFactionQuests200,
  GetFactionQuestProgress200,
} from '@/api/generated/narrative/faction-quests/models'

interface QuestAvailabilityPanelProps {
  availability?: GetAvailableFactionQuests200
  progress?: GetFactionQuestProgress200
}

export const QuestAvailabilityPanel = ({
  availability,
  progress,
}: QuestAvailabilityPanelProps) => (
  <Card variant="outlined">
    <CardContent>
      <Stack spacing={1.5}>
        <Typography variant="h6" fontWeight={600} fontSize="1rem">
          Доступность для персонажа
        </Typography>
        {!availability ? (
          <Typography variant="body2" color="text.secondary">
            Укажите Character ID, чтобы увидеть доступные квесты.
          </Typography>
        ) : (
          <>
            <Typography variant="subtitle2" fontWeight={600}>
              Доступные ({availability.available_quests?.length ?? 0})
            </Typography>
            <Stack spacing={0.5}>
              {availability.available_quests?.slice(0, 5).map(item => (
                <Typography key={item.quest_id} variant="caption" color="text.secondary">
                  • {item.title} ({item.faction})
                </Typography>
              ))}
              {!availability.available_quests?.length && (
                <Typography variant="caption" color="text.secondary">
                  Нет доступных квестов.
                </Typography>
              )}
            </Stack>

            <Typography variant="subtitle2" fontWeight={600}>
              Заблокированные ({availability.locked_quests?.length ?? 0})
            </Typography>
            <Stack spacing={0.5}>
              {availability.locked_quests?.slice(0, 5).map(item => (
                <Typography key={item.quest?.quest_id} variant="caption" color="text.secondary">
                  • {item.quest?.title} — требуется репутация{' '}
                  {item.requirements?.min_reputation
                    ? Object.entries(item.requirements.min_reputation)
                        .map(([faction, value]) => `${faction}:${value}`)
                        .join(', ')
                    : 'дополнительные условия'}
                </Typography>
              ))}
              {!availability.locked_quests?.length && (
                <Typography variant="caption" color="text.secondary">
                  Нет заблокированных квестов.
                </Typography>
              )}
            </Stack>
          </>
        )}

        {progress && (
          <>
            <Typography variant="subtitle2" fontWeight={600}>
              Активные ({progress.active_quests?.length ?? 0})
            </Typography>
            <Stack spacing={0.5}>
              {progress.active_quests?.map(item => (
                <Stack direction="row" spacing={1} key={item.quest_id}>
                  <Typography variant="caption" color="text.secondary">
                    {item.quest_id}
                  </Typography>
                  {item.current_branch && <Chip label={item.current_branch} size="small" />}
                </Stack>
              ))}
              {!progress.active_quests?.length && (
                <Typography variant="caption" color="text.secondary">
                  Активных квестов нет.
                </Typography>
              )}
            </Stack>
          </>
        )}
      </Stack>
    </CardContent>
  </Card>
)







