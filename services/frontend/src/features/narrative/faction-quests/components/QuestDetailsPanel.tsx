import { Card, CardContent, Chip, Divider, Stack, Typography } from '@mui/material'
import type {
  FactionQuestDetailed,
  QuestBranch,
  QuestEnding,
} from '@/api/generated/narrative/faction-quests/models'

interface QuestDetailsPanelProps {
  quest?: FactionQuestDetailed
  branches?: QuestBranch[]
  endings?: QuestEnding[]
}

export const QuestDetailsPanel = ({ quest, branches = [], endings = [] }: QuestDetailsPanelProps) => (
  <Card variant="outlined">
    <CardContent>
      <Stack spacing={1.5}>
        <Typography variant="h6" fontWeight={600} fontSize="1rem">
          Детали квеста
        </Typography>
        {!quest ? (
          <Typography variant="body2" color="text.secondary">
            Выберите квест, чтобы увидеть подробную информацию.
          </Typography>
        ) : (
          <>
            <Stack direction="row" spacing={1} flexWrap="wrap">
              {quest.faction && <Chip label={quest.faction} size="small" color="primary" />}
              {quest.category && <Chip label={quest.category} size="small" />}
              {quest.difficulty && <Chip label={quest.difficulty} size="small" color="warning" />}
              <Chip label={`Веток: ${quest.branches_count ?? branches.length}`} size="small" />
              <Chip label={`Концовок: ${quest.endings_count ?? endings.length}`} size="small" />
            </Stack>

            <Typography variant="body2">{quest.storyline ?? quest.description}</Typography>

            {quest.requirements && (
              <Stack spacing={0.5}>
                <Typography variant="subtitle2" fontWeight={600}>
                  Требования
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  Минимальный уровень: {quest.requirements.min_level ?? '—'}
                </Typography>
                {!!quest.requirements.required_quests?.length && (
                  <Typography variant="caption" color="text.secondary">
                    Требуемые квесты: {quest.requirements.required_quests.join(', ')}
                  </Typography>
                )}
              </Stack>
            )}

            {!!branches.length && (
              <Stack spacing={0.5}>
                <Typography variant="subtitle2" fontWeight={600}>
                  Ветвления ({branches.length})
                </Typography>
                {branches.slice(0, 4).map(branch => (
                  <Typography key={branch.branch_id} variant="caption" color="text.secondary">
                    • {branch.name} — {branch.description}
                  </Typography>
                ))}
              </Stack>
            )}

            {!!endings.length && (
              <Stack spacing={0.5}>
                <Typography variant="subtitle2" fontWeight={600}>
                  Концовки ({endings.length})
                </Typography>
                {endings.slice(0, 4).map(ending => (
                  <Typography key={ending.ending_id} variant="caption" color="text.secondary">
                    • {ending.name} ({ending.type})
                  </Typography>
                ))}
              </Stack>
            )}

            <Divider />

            {quest.rewards?.experience && (
              <Typography variant="caption" color="text.secondary">
                Награды: {quest.rewards.experience} XP, {quest.rewards.currency?.eddies ?? 0} eddies
              </Typography>
            )}
          </>
        )}
      </Stack>
    </CardContent>
  </Card>
)







