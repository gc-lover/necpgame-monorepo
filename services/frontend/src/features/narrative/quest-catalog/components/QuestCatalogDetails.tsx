import { Card, CardContent, Chip, Divider, Grid, Stack, Typography } from '@mui/material'
import type { QuestDetails, QuestLootTable, DialogueTree } from '@/api/generated/narrative/quest-catalog/models'

interface QuestCatalogDetailsProps {
  quest?: QuestDetails
  dialogueTree?: DialogueTree
  lootTable?: QuestLootTable
}

export const QuestCatalogDetails = ({
  quest,
  dialogueTree,
  lootTable,
}: QuestCatalogDetailsProps) => (
  <Card variant="outlined">
    <CardContent>
      <Stack spacing={1.5}>
        <Typography variant="h6" fontWeight={600} fontSize="1rem">
          Детали квеста
        </Typography>
        {!quest ? (
          <Typography variant="body2" color="text.secondary">
            Выберите квест для просмотра подробностей.
          </Typography>
        ) : (
          <>
            <Stack direction="row" spacing={1} flexWrap="wrap">
              {quest.type && <Chip label={quest.type} size="small" />}
              {quest.period && <Chip label={quest.period} size="small" color="info" />}
              {quest.difficulty && <Chip label={quest.difficulty} size="small" color="warning" />}
              <Chip label={`Тэги: ${quest.tags?.length ?? 0}`} size="small" />
            </Stack>

            <Typography variant="body2" color="text.secondary">
              {quest.full_description ?? quest.description ?? 'Описание отсутствует.'}
            </Typography>

            {!!quest.objectives?.length && (
              <Stack spacing={0.5}>
                <Typography variant="subtitle2" fontWeight={600}>
                  Цели ({quest.objectives.length})
                </Typography>
                {quest.objectives.map(objective => (
                  <Typography key={objective.objective_id} variant="caption" color="text.secondary">
                    • {objective.description} {objective.optional ? '(опционально)' : ''}
                  </Typography>
                ))}
              </Stack>
            )}

            {!!quest.prerequisites?.length && (
              <Stack spacing={0.5}>
                <Typography variant="subtitle2" fontWeight={600}>
                  Предварительные квесты
                </Typography>
                {quest.prerequisites.map(item => (
                  <Typography key={item} variant="caption" color="text.secondary">
                    • {item}
                  </Typography>
                ))}
              </Stack>
            )}

            <Grid container spacing={1}>
              <Grid item xs={6}>
                <Typography variant="caption" color="text.secondary">
                  Средний рейтинг: {quest.average_rating?.toFixed(1) ?? '—'}
                </Typography>
              </Grid>
              <Grid item xs={6}>
                <Typography variant="caption" color="text.secondary">
                  Завершение: {quest.completion_rate?.toFixed(1) ?? '—'}%
                </Typography>
              </Grid>
            </Grid>

            {dialogueTree && (
              <Stack spacing={0.5}>
                <Typography variant="subtitle2" fontWeight={600}>
                  Диалоговое дерево
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  Узлов: {dialogueTree.total_nodes ?? dialogueTree.nodes?.length ?? 0} • Корневой узел:{' '}
                  {dialogueTree.root_node_id ?? '—'}
                </Typography>
              </Stack>
            )}

            {lootTable && (
              <Stack spacing={0.5}>
                <Typography variant="subtitle2" fontWeight={600}>
                  Награды
                </Typography>
                {!!lootTable.guaranteed_loot?.length && (
                  <Typography variant="caption" color="text.secondary">
                    Гарантированный лут: {lootTable.guaranteed_loot.length} предмет(ов)
                  </Typography>
                )}
                {!!lootTable.random_loot?.length && (
                  <Typography variant="caption" color="text.secondary">
                    Случайный лут: {lootTable.random_loot.length} записей
                  </Typography>
                )}
              </Stack>
            )}

            <Divider />

            {!!quest.rewards_summary && (
              <Typography variant="caption" color="text.secondary">
                XP: {quest.rewards_summary.experience ?? 0} • Эдди:{' '}
                {quest.rewards_summary.eddies ?? 0} • Предметов: {quest.rewards_summary.items_count ?? 0}
              </Typography>
            )}
          </>
        )}
      </Stack>
    </CardContent>
  </Card>
)







