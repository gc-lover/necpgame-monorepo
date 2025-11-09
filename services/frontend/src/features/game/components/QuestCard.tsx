/**
 * Компонент карточки квеста
 */
import { Card, CardContent, Typography, Chip, Stack, Divider } from '@mui/material'
import AssignmentIcon from '@mui/icons-material/Assignment'
import StarIcon from '@mui/icons-material/Star'
import AttachMoneyIcon from '@mui/icons-material/AttachMoney'
import type { GameQuest } from '@/api/generated/game/models'

interface QuestCardProps {
  quest: GameQuest
  onSelect?: (quest: GameQuest) => void
}

export function QuestCard({ quest, onSelect }: QuestCardProps) {
  const getQuestTypeColor = (type: string) => {
    switch (type) {
      case 'main':
        return 'error'
      case 'side':
        return 'info'
      case 'contract':
        return 'warning'
      default:
        return 'default'
    }
  }

  const getQuestTypeLabel = (type: string) => {
    switch (type) {
      case 'main':
        return 'Основной'
      case 'side':
        return 'Побочный'
      case 'contract':
        return 'Контракт'
      default:
        return type
    }
  }

  return (
    <Card
      elevation={2}
      sx={{
        border: '1px solid',
        borderColor: 'divider',
        cursor: onSelect ? 'pointer' : 'default',
        '&:hover': onSelect
          ? {
              borderColor: 'primary.main',
              boxShadow: 4,
            }
          : undefined,
      }}
      onClick={() => onSelect?.(quest)}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack direction="row" spacing={0.5} alignItems="center" sx={{ mb: 0.5 }}>
          <AssignmentIcon color="primary" sx={{ fontSize: '1rem' }} />
          <Typography variant="subtitle1" sx={{ color: 'primary.main', fontWeight: 'bold', fontSize: '0.9rem' }}>
            {quest.name}
          </Typography>
        </Stack>

        <Stack direction="row" spacing={0.5} sx={{ mb: 1 }}>
          <Chip
            label={getQuestTypeLabel(quest.type)}
            size="small"
            color={getQuestTypeColor(quest.type)}
            sx={{ height: 18, fontSize: '0.65rem' }}
          />
          <Chip 
            label={`Ур. ${quest.level}`} 
            size="small" 
            color="secondary" 
            sx={{ height: 18, fontSize: '0.65rem' }}
          />
        </Stack>

        <Typography variant="caption" sx={{ mb: 1, color: 'text.secondary', fontSize: '0.75rem', display: 'block' }}>
          {quest.description}
        </Typography>

        <Divider sx={{ my: 0.5 }} />

        <Typography variant="caption" sx={{ mb: 0.5, fontWeight: 'bold', fontSize: '0.7rem', display: 'block' }}>
          Награды:
        </Typography>

        <Stack spacing={0.3}>
          {quest.rewards.experience && (
            <Stack direction="row" spacing={0.3} alignItems="center">
              <StarIcon sx={{ fontSize: '0.875rem' }} color="warning" />
              <Typography variant="caption" sx={{ fontSize: '0.7rem' }}>+{quest.rewards.experience} XP</Typography>
            </Stack>
          )}
          {quest.rewards.money && (
            <Stack direction="row" spacing={0.3} alignItems="center">
              <AttachMoneyIcon sx={{ fontSize: '0.875rem' }} color="success" />
              <Typography variant="caption" sx={{ fontSize: '0.7rem' }}>+{quest.rewards.money} ₵</Typography>
            </Stack>
          )}
          {quest.rewards.reputation && (
            <Typography variant="caption" sx={{ color: 'info.main', fontSize: '0.7rem' }}>
              {quest.rewards.reputation.faction} +{quest.rewards.reputation.amount}
            </Typography>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

