import {
  Card,
  CardContent,
  Chip,
  List,
  ListItemButton,
  ListItemText,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import type { ChangeEvent } from 'react'
import type { FactionQuest, ListFactionQuestsParams } from '@/api/generated/narrative/faction-quests/models'

export interface QuestFilters extends ListFactionQuestsParams {
  search?: string
}

interface FactionQuestListProps {
  quests: FactionQuest[]
  selectedQuestId?: string
  filters: QuestFilters
  onFiltersChange: (filters: QuestFilters) => void
  onSelectQuest: (questId: string) => void
}

const FACTIONS = [
  'NCPD',
  'MAXTAC',
  'ARASAKA',
  'SIXTH_STREET',
  'VOODOO_BOYS',
  'ALDECALDOS',
  'MILITECH',
  'BIOTECHNICA',
  'VALENTINOS',
  'MAELSTROM',
  'FIXERS',
  'RIPPERS',
  'TRAUMA_TEAM',
  'NETRUNNERS',
  'MEDIA',
  'POLITICS',
]

export const FactionQuestList = ({
  quests,
  selectedQuestId,
  filters,
  onFiltersChange,
  onSelectQuest,
}: FactionQuestListProps) => {
  const handleFilterChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    onFiltersChange({ ...filters, [name]: value || undefined })
  }

  return (
    <Card variant="outlined">
      <CardContent>
        <Stack spacing={2}>
          <Typography variant="h6" fontWeight={600} fontSize="1rem">
            Фракционные квесты
          </Typography>
          <Stack spacing={1}>
            <TextField
              label="Фракция"
              size="small"
              name="faction"
              select
              value={filters.faction ?? ''}
              onChange={handleFilterChange}
              SelectProps={{ native: true }}
            >
              <option value="">Все</option>
              {FACTIONS.map(faction => (
                <option key={faction} value={faction}>
                  {faction}
                </option>
              ))}
            </TextField>
            <TextField
              label="Мин. репутация"
              size="small"
              type="number"
              name="min_reputation"
              value={filters.min_reputation ?? ''}
              onChange={handleFilterChange}
            />
            <TextField
              label="Мин. уровень игрока"
              size="small"
              type="number"
              name="player_level_min"
              value={filters.player_level_min ?? ''}
              onChange={handleFilterChange}
            />
          </Stack>

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
                      {quest.faction && <Chip label={quest.faction} size="small" />}
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
                Квесты не найдены. Измените фильтры.
              </Typography>
            )}
          </List>
        </Stack>
      </CardContent>
    </Card>
  )
}







