import React from 'react'
import { Box, Typography, Chip, Stack } from '@mui/material'
import CasinoIcon from '@mui/icons-material/Casino'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import CancelIcon from '@mui/icons-material/Cancel'
import StarIcon from '@mui/icons-material/Star'

interface CheckResultData {
  roll?: number
  attribute_modifier?: number
  skill_bonus?: number
  situation_modifiers?: number
  total?: number
  dc?: number
  success?: boolean
  critical?: boolean
  critical_type?: string
  margin?: number
}

interface DiceRollDisplayProps {
  result: CheckResultData
}

export const DiceRollDisplay: React.FC<DiceRollDisplayProps> = ({ result }) => {
  const isCritical = result.critical
  const isCritSuccess = isCritical && result.critical_type === 'success'
  const isCritFail = isCritical && result.critical_type === 'failure'

  return (
    <Box
      sx={{
        p: 2,
        border: '2px solid',
        borderColor: isCritSuccess ? 'success.main' : isCritFail ? 'error.main' : result.success ? 'info.main' : 'warning.main',
        borderRadius: 2,
        bgcolor: isCritSuccess ? 'rgba(76, 175, 80, 0.1)' : isCritFail ? 'rgba(244, 67, 54, 0.1)' : 'rgba(33, 150, 243, 0.05)',
      }}
    >
      <Stack spacing={1.5}>
        {/* Result */}
        <Box display="flex" alignItems="center" justifyContent="space-between">
          <Box display="flex" alignItems="center" gap={1}>
            <CasinoIcon sx={{ fontSize: '1.5rem', color: isCritical ? 'warning.main' : 'primary.main' }} />
            <Typography variant="h5" fontSize="1.5rem" fontWeight="bold">
              {result.roll}
            </Typography>
            {isCritical && <StarIcon sx={{ fontSize: '1.5rem', color: 'warning.main' }} />}
          </Box>
          {result.success ? (
            <CheckCircleIcon sx={{ fontSize: '2rem', color: 'success.main' }} />
          ) : (
            <CancelIcon sx={{ fontSize: '2rem', color: 'error.main' }} />
          )}
        </Box>

        {/* Breakdown */}
        <Box display="flex" gap={1} flexWrap="wrap">
          <Chip label={`d20: ${result.roll}`} size="small" sx={{ fontSize: '0.7rem' }} />
          {result.attribute_modifier !== undefined && <Chip label={`Attr: ${result.attribute_modifier >= 0 ? '+' : ''}${result.attribute_modifier}`} size="small" color="primary" sx={{ fontSize: '0.7rem' }} />}
          {result.skill_bonus !== undefined && result.skill_bonus > 0 && <Chip label={`Skill: +${result.skill_bonus}`} size="small" color="info" sx={{ fontSize: '0.7rem' }} />}
          {result.situation_modifiers !== undefined && result.situation_modifiers !== 0 && <Chip label={`Situation: ${result.situation_modifiers >= 0 ? '+' : ''}${result.situation_modifiers}`} size="small" color="warning" sx={{ fontSize: '0.7rem' }} />}
        </Box>

        {/* Total vs DC */}
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="body1" fontSize="1rem" fontWeight="bold">
            Итого: {result.total}
          </Typography>
          <Typography variant="body1" fontSize="1rem" color="text.secondary">
            DC: {result.dc}
          </Typography>
        </Box>

        {/* Result Text */}
        <Typography variant="body2" fontSize="0.875rem" fontWeight="bold" color={result.success ? 'success.main' : 'error.main'}>
          {isCritSuccess && '🌟 КРИТИЧЕСКИЙ УСПЕХ!'}
          {isCritFail && '💀 КРИТИЧЕСКИЙ ПРОВАЛ!'}
          {!isCritical && result.success && `OK УСПЕХ (запас: ${result.margin})`}
          {!isCritical && !result.success && `❌ ПРОВАЛ (не хватило: ${Math.abs(result.margin || 0)})`}
        </Typography>
      </Stack>
    </Box>
  )
}

