/**
 * Компонент одного слота импланта
 * Данные из OpenAPI: SlotInfo
 */
import { Paper, Typography, Box } from '@mui/material'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import RadioButtonUncheckedIcon from '@mui/icons-material/RadioButtonUnchecked'
import BlockIcon from '@mui/icons-material/Block'
import type { SlotInfo } from '@/api/generated/gameplay/combat/models'

interface ImplantSlotItemProps {
  slot: SlotInfo
  onClick?: () => void
}

export function ImplantSlotItem({ slot, onClick }: ImplantSlotItemProps) {
  const getSlotColor = () => {
    if (!slot.can_install) return 'error.main'
    if (slot.is_occupied) return 'success.main'
    return 'action.disabled'
  }

  const getSlotIcon = () => {
    if (!slot.can_install) return <BlockIcon sx={{ fontSize: '1.2rem' }} />
    if (slot.is_occupied) return <CheckCircleIcon sx={{ fontSize: '1.2rem' }} />
    return <RadioButtonUncheckedIcon sx={{ fontSize: '1.2rem' }} />
  }

  return (
    <Paper
      elevation={1}
      sx={{
        p: 1,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: 60,
        cursor: onClick ? 'pointer' : 'default',
        border: '1px solid',
        borderColor: slot.is_occupied ? 'success.main' : 'divider',
        '&:hover': onClick
          ? {
              borderColor: 'primary.main',
              boxShadow: 2,
            }
          : undefined,
      }}
      onClick={onClick}
    >
      <Box sx={{ color: getSlotColor(), mb: 0.5 }}>{getSlotIcon()}</Box>
      <Typography variant="caption" sx={{ fontSize: '0.65rem', textAlign: 'center' }}>
        #{slot.slot_id.slice(-4)}
      </Typography>
      {slot.is_occupied && slot.installed_implant_id && (
        <Typography
          variant="caption"
          sx={{ fontSize: '0.6rem', color: 'success.main', textAlign: 'center', mt: 0.3 }}
        >
          Установлен
        </Typography>
      )}
    </Paper>
  )
}

