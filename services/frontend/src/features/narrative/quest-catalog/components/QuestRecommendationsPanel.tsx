import { Card, CardContent, Chip, Stack, Typography } from '@mui/material'
import type { GetQuestRecommendations200 } from '@/api/generated/narrative/quest-catalog/models'

interface QuestRecommendationsPanelProps {
  recommendations?: GetQuestRecommendations200
}

export const QuestRecommendationsPanel = ({ recommendations }: QuestRecommendationsPanelProps) => {
  const items = recommendations?.recommendations ?? []

  return (
    <Card variant="outlined">
      <CardContent>
        <Stack spacing={1.5}>
          <Typography variant="h6" fontSize="1rem" fontWeight={600}>
            Рекомендации
          </Typography>
          {items.length === 0 ? (
            <Typography variant="body2" color="text.secondary">
              Рекомендации отсутствуют. Укажите корректный Character ID.
            </Typography>
          ) : (
            <Stack spacing={1}>
              {items.map(item => (
                <Stack key={item.quest?.quest_id} spacing={0.5}>
                  <Stack direction="row" spacing={1} alignItems="center">
                    <Typography variant="subtitle2" fontWeight={600}>
                      {item.quest?.title}
                    </Typography>
                    <Chip label={`${item.match_score?.toFixed(0) ?? 0}%`} size="small" />
                  </Stack>
                  <Typography variant="caption" color="text.secondary">
                    Причины: {item.reasons?.join(', ') || 'не указаны'}
                  </Typography>
                </Stack>
              ))}
            </Stack>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}







