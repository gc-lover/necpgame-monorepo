/**
 * Компонент списка симптомов киберпсихоза
 * Данные из OpenAPI: Symptom[]
 */
import { Box, Typography, List, ListItem, ListItemText, Chip, Paper } from '@mui/material'
import WarningIcon from '@mui/icons-material/Warning'
import ErrorIcon from '@mui/icons-material/Error'
import InfoIcon from '@mui/icons-material/Info'
import type { Symptom } from '@/api/generated/gameplay/cyberpsychosis/models'

interface SymptomsListProps {
  symptoms: Symptom[]
}

export function SymptomsList({ symptoms }: SymptomsListProps) {
  const getSeverityIcon = (severity: string) => {
    switch (severity) {
      case 'minor':
        return <InfoIcon sx={{ fontSize: '0.875rem' }} color="info" />
      case 'moderate':
        return <WarningIcon sx={{ fontSize: '0.875rem' }} color="warning" />
      case 'severe':
      case 'critical':
        return <ErrorIcon sx={{ fontSize: '0.875rem' }} color="error" />
      default:
        return null
    }
  }

  const getSeverityColor = (severity: string) => {
    switch (severity) {
      case 'minor':
        return 'info'
      case 'moderate':
        return 'warning'
      case 'severe':
      case 'critical':
        return 'error'
      default:
        return 'default'
    }
  }

  if (!symptoms || symptoms.length === 0) {
    return (
      <Paper elevation={1} sx={{ p: 2 }}>
        <Typography variant="caption" sx={{ color: 'success.main', fontSize: '0.75rem' }}>
          ✓ Нет активных симптомов
        </Typography>
      </Paper>
    )
  }

  return (
    <List dense disablePadding>
      {symptoms.map((symptom) => (
        <ListItem
          key={symptom.symptom_id}
          sx={{
            mb: 0.5,
            p: 1,
            borderRadius: 1,
            border: '1px solid',
            borderColor: 'divider',
            bgcolor: 'background.paper',
          }}
        >
          <ListItemText
            primary={
              <Stack direction="row" spacing={0.5} alignItems="center">
                {getSeverityIcon(symptom.severity)}
                <Typography variant="body2" sx={{ fontSize: '0.8rem', fontWeight: 'bold' }}>
                  {symptom.name}
                </Typography>
              </Stack>
            }
            secondary={
              <Box>
                <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
                  {symptom.description}
                </Typography>
                <Stack direction="row" spacing={0.5} sx={{ mt: 0.5 }}>
                  <Chip
                    label={symptom.severity}
                    size="small"
                    color={getSeverityColor(symptom.severity)}
                    sx={{ height: 16, fontSize: '0.6rem' }}
                  />
                  {symptom.duration && (
                    <Chip
                      label={`${symptom.duration}с`}
                      size="small"
                      variant="outlined"
                      sx={{ height: 16, fontSize: '0.6rem' }}
                    />
                  )}
                </Stack>
              </Box>
            }
          />
        </ListItem>
      ))}
    </List>
  )
}

