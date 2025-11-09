/**
 * Компонент отображения шагов туториала с использованием MUI Stepper
 */
import {
  Box,
  Stepper,
  Step,
  StepLabel,
  StepContent,
  Typography,
  Button,
  Paper,
} from '@mui/material'
import { useState } from 'react'
import type { TutorialStepsResponse } from '@/api/generated/game/models'

interface TutorialStepsProps {
  data: TutorialStepsResponse
  onComplete?: () => void
  onSkip?: () => void
}

export function TutorialSteps({ data, onComplete, onSkip }: TutorialStepsProps) {
  const [activeStep, setActiveStep] = useState(data.currentStep || 0)

  const handleNext = () => {
    if (activeStep === data.steps.length - 1) {
      onComplete?.()
    } else {
      setActiveStep((prevActiveStep) => prevActiveStep + 1)
    }
  }

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1)
  }

  return (
    <Paper
      elevation={3}
      sx={{
        p: 2,
        backgroundColor: 'background.paper',
        border: '1px solid',
        borderColor: 'primary.main',
      }}
    >
      <Box sx={{ mb: 1.5, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h6" sx={{ color: 'primary.main', fontWeight: 'bold', fontSize: '0.9rem' }}>
          Туториал
        </Typography>
        {data.canSkip && (
          <Button variant="text" size="small" onClick={onSkip} sx={{ fontSize: '0.75rem' }}>
            Пропустить
          </Button>
        )}
      </Box>

      <Stepper activeStep={activeStep} orientation="vertical">
        {data.steps.map((step, index) => (
          <Step key={step.id}>
            <StepLabel>
              <Typography variant="subtitle2" sx={{ fontSize: '0.875rem' }}>{step.title}</Typography>
            </StepLabel>
            <StepContent>
              <Typography variant="body2" sx={{ mb: 1, fontSize: '0.8rem' }}>
                {step.description}
              </Typography>
              <Typography
                variant="caption"
                sx={{
                  mb: 1.5,
                  color: 'info.main',
                  fontStyle: 'italic',
                  p: 0.75,
                  backgroundColor: 'action.hover',
                  borderRadius: 1,
                  display: 'block',
                  fontSize: '0.7rem',
                }}
              >
                💡 {step.hint}
              </Typography>
              <Box sx={{ mb: 1 }}>
                <Button
                  variant="contained"
                  onClick={handleNext}
                  size="small"
                  sx={{ mr: 1 }}
                >
                  {index === data.steps.length - 1 ? 'Завершить' : 'Далее'}
                </Button>
                <Button
                  disabled={index === 0}
                  onClick={handleBack}
                  size="small"
                >
                  Назад
                </Button>
              </Box>
            </StepContent>
          </Step>
        ))}
      </Stepper>

      {activeStep === data.steps.length && (
        <Paper square elevation={0} sx={{ p: 2 }}>
          <Typography variant="subtitle1" sx={{ color: 'success.main', fontSize: '0.9rem' }}>
            Туториал завершен! 🎉
          </Typography>
          <Typography variant="caption" sx={{ mt: 0.5, display: 'block', fontSize: '0.75rem' }}>
            Теперь вы готовы начать свое приключение в Night City.
          </Typography>
          <Button onClick={onComplete} sx={{ mt: 1.5 }} variant="contained" size="small">
            Начать игру
          </Button>
        </Paper>
      )}
    </Paper>
  )
}

