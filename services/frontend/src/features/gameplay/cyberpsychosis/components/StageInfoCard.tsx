/**
 * Компонент информации о стадии киберпсихоза
 * Данные из OpenAPI: StageInfo
 */
import { Paper, Typography, Stack, Chip, Divider } from '@mui/material'
import type { StageInfo } from '@/api/generated/gameplay/cyberpsychosis/models'

interface StageInfoCardProps {
  stage: StageInfo
}

export function StageInfoCard({ stage }: StageInfoCardProps) {
  const getStageColor = (stageName: string) => {
    switch (stageName) {
      case 'stable':
        return 'success'
      case 'anxious':
        return 'info'
      case 'dissociative':
        return 'warning'
      case 'cyberpsycho':
        return 'error'
      default:
        return 'default'
    }
  }

  return (
    <Paper
      elevation={3}
      sx={{
        p: 2,
        backgroundColor: 'background.paper',
        border: '2px solid',
        borderColor: `${getStageColor(stage.name)}.main`,
      }}
    >
      <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 1 }}>
        <Typography variant="h6" sx={{ fontSize: '0.9rem', fontWeight: 'bold' }}>
          {stage.description}
        </Typography>
        <Chip
          label={stage.name}
          size="small"
          color={getStageColor(stage.name)}
          sx={{ height: 20, fontSize: '0.65rem', fontWeight: 'bold' }}
        />
      </Stack>

      {/* Диапазон человечности */}
      <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary', mb: 1, display: 'block' }}>
        Диапазон: {stage.humanity_range.min} - {stage.humanity_range.max}
      </Typography>

      {stage.symptoms && stage.symptoms.length > 0 && (
        <>
          <Divider sx={{ my: 1 }} />
          <Typography variant="caption" sx={{ fontSize: '0.7rem', fontWeight: 'bold', display: 'block', mb: 0.5 }}>
            Симптомы ({stage.symptoms.length}):
          </Typography>
          <Stack direction="row" spacing={0.5} flexWrap="wrap" sx={{ gap: 0.5 }}>
            {stage.symptoms.slice(0, 3).map((symptom, index) => (
              <Chip
                key={index}
                label={symptom}
                size="small"
                variant="outlined"
                sx={{ height: 18, fontSize: '0.6rem' }}
              />
            ))}
            {stage.symptoms.length > 3 && (
              <Chip
                label={`+${stage.symptoms.length - 3}`}
                size="small"
                variant="outlined"
                sx={{ height: 18, fontSize: '0.6rem' }}
              />
            )}
          </Stack>
        </>
      )}

      {stage.effects && stage.effects.length > 0 && (
        <>
          <Divider sx={{ my: 1 }} />
          <Typography variant="caption" sx={{ fontSize: '0.7rem', fontWeight: 'bold', display: 'block', mb: 0.5 }}>
            Эффекты:
          </Typography>
          {stage.effects.slice(0, 2).map((effect, index) => (
            <Typography
              key={index}
              variant="caption"
              sx={{ fontSize: '0.65rem', color: 'text.secondary', display: 'block' }}
            >
              • {effect}
            </Typography>
          ))}
        </>
      )}
    </Paper>
  )
}

