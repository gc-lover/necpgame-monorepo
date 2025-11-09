import {
  Card,
  CardContent,
  Chip,
  List,
  ListItemButton,
  ListItemText,
  Stack,
  Typography,
} from '@mui/material'
import type { QuestCatalogEntry } from '@/api/generated/narrative/quest-catalog/models'

interface QuestCatalogListProps {
  quests: QuestCatalogEntry[]
  selectedQuestId?: string
  onSelectQuest: (questId: string) => void
}

export const QuestCatalogList = ({
  quests,
  selectedQuestId,
  onSelectQuest,
}: QuestCatalogListProps) => (
  <Card variant="outlined">
    <CardContent>
      <Stack spacing={1.5}>
        <Typography variant="h6" fontWeight={600} fontSize="1rem">
          Каталог квестов ({quests.length})
        </Typography>
        <List dense disablePadding>
          {quests.map(quest => (
            <ListItemButton
              key={quest.quest_id}
              selected={quest.quest_id === selectedQuestId}
              onClick={() => quest.quest_id && onSelectQuest(quest.quest_id)}
              divider
            >
              <ListItemText
                primary={
                  <Stack direction="row" spacing={1} alignItems="center">
                    <Typography variant="subtitle2" fontWeight={600}>
                      {quest.title}
                    </Typography>
                    {quest.type && <Chip label={quest.type} size="small" />}
                    {quest.period && <Chip label={quest.period} size="small" color="info" />}
                    {quest.difficulty && <Chip label={quest.difficulty} size="small" color="warning" />}
                  </Stack>
                }
                secondary={
                  <Typography variant="caption" color="text.secondary">
                    {quest.description}
                  </Typography>
                }
              />
            </ListItemButton>
          ))}
          {quests.length === 0 && (
            <Typography variant="body2" color="text.secondary">
              Квесты не найдены.
            </Typography>
          )}
        </List>
      </Stack>
    </CardContent>
  </Card>
)







