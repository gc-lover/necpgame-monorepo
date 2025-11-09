/**
 * Компактный элемент списка квестов
 * Данные из OpenAPI: Quest
 */
import { ListItem, ListItemButton, ListItemText, Chip, Stack } from '@mui/material'
import AssignmentIcon from '@mui/icons-material/Assignment'
import type { Quest } from '@/api/generated/quests/models'

interface QuestListItemProps {
  quest: Quest
  onClick: () => void
}

export function QuestListItem({ quest, onClick }: QuestListItemProps) {
  const getTypeColor = (type: string) => {
    switch (type) {
      case 'main': return 'error'
      case 'side': return 'info'
      case 'contract': return 'warning'
      case 'daily': return 'success'
      default: return 'default'
    }
  }

  return (
    <ListItem disablePadding sx={{ mb: 0.5 }}>
      <ListItemButton
        onClick={onClick}
        sx={{
          borderRadius: 1,
          border: '1px solid',
          borderColor: 'divider',
          '&:hover': {
            borderColor: 'primary.main',
            bgcolor: 'rgba(0, 247, 255, 0.05)',
          },
        }}
      >
        <ListItemText
          primary={
            <Stack direction="row" spacing={1} alignItems="center">
              <AssignmentIcon sx={{ fontSize: '1rem' }} color="primary" />
              <span style={{ fontSize: '0.875rem', fontWeight: 'bold' }}>{quest.name}</span>
            </Stack>
          }
          secondary={
            <Stack direction="row" spacing={0.5} sx={{ mt: 0.5 }}>
              <Chip label={quest.type} size="small" color={getTypeColor(quest.type)} sx={{ height: 16, fontSize: '0.6rem' }} />
              <Chip label={`Ур.${quest.level}`} size="small" color="secondary" sx={{ height: 16, fontSize: '0.6rem' }} />
              {quest.timeLimit && <Chip label={`${quest.timeLimit}m`} size="small" variant="outlined" sx={{ height: 16, fontSize: '0.6rem' }} />}
            </Stack>
          }
        />
      </ListItemButton>
    </ListItem>
  )
}

