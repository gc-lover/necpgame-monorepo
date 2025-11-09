import { Card, CardContent, Chip, Stack, Typography } from '@mui/material'
import type { ProgressionMilestone } from '@/api/generated/progression/backend/models'

interface MilestonesListProps {
  milestones?: ProgressionMilestone[]
}

export const MilestonesList = ({ milestones = [] }: MilestonesListProps) => (
  <Card variant="outlined">
    <CardContent>
      <Stack spacing={2}>
        <Typography variant="h6" fontSize="1rem" fontWeight={600}>
          Прогрессионные вехи
        </Typography>
        {milestones.length === 0 ? (
          <Typography variant="body2" color="text.secondary">
            Вехи не найдены
          </Typography>
        ) : (
          <Stack spacing={1.5}>
            {milestones.map(milestone => (
              <Stack key={milestone.milestone_id ?? milestone.name} spacing={0.75}>
                <Stack direction="row" spacing={1} alignItems="center">
                  <Typography variant="subtitle2" fontWeight={600}>
                    {milestone.name}
                  </Typography>
                  {milestone.completed && <Chip label="Выполнено" size="small" color="success" />}
                </Stack>
                {milestone.description && (
                  <Typography variant="body2" color="text.secondary">
                    {milestone.description}
                  </Typography>
                )}
                {milestone.requirement?.type && (
                  <Typography variant="caption" color="text.secondary">
                    Требование: {milestone.requirement.type} → {milestone.requirement.target_value}
                  </Typography>
                )}
              </Stack>
            ))}
          </Stack>
        )}
      </Stack>
    </CardContent>
  </Card>
)







