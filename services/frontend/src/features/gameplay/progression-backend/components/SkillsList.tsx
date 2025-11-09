import { useState } from 'react'
import {
  Alert,
  Button,
  Card,
  CardContent,
  LinearProgress,
  Paper,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import type { CharacterSkills, Skill } from '@/api/generated/progression/backend/models'

interface SkillsListProps {
  skills?: CharacterSkills
  onAddExperience: (skillId: string, experience: number, source?: string) => Promise<void>
  isMutating: boolean
  error?: unknown
}

export const SkillsList = ({ skills, onAddExperience, isMutating, error }: SkillsListProps) => {
  const [amounts, setAmounts] = useState<Record<string, number>>({})

  const handleChange = (skillId: string, value: number) => {
    setAmounts(prev => ({ ...prev, [skillId]: value }))
  }

  const handleSubmit = async (skill: Skill) => {
    const amount = amounts[skill.skill_id ?? ''] ?? 0
    if (!skill.skill_id || amount <= 0) {
      return
    }
    await onAddExperience(skill.skill_id, amount, 'manual_adjustment')
    setAmounts(prev => ({ ...prev, [skill.skill_id ?? '']: 0 }))
  }

  const skillItems = skills?.skills ?? []

  return (
    <Card variant="outlined">
      <CardContent>
        <Stack spacing={2}>
          <Typography variant="h6" fontSize="1rem" fontWeight={600}>
            Навыки
          </Typography>
          {skillItems.length === 0 ? (
            <Typography variant="body2" color="text.secondary">
              Навыки не найдены
            </Typography>
          ) : (
            <Stack spacing={1.5}>
              {skillItems.map(skill => {
                const progress = skill.progress_percentage ?? 0
                const amount = amounts[skill.skill_id ?? ''] ?? ''

                return (
                  <Paper variant="outlined" sx={{ p: 1.5 }} key={skill.skill_id ?? skill.name}>
                    <Stack spacing={1}>
                      <Stack direction="row" justifyContent="space-between" alignItems="center">
                        <Typography variant="subtitle2" fontWeight={600}>
                          {skill.name ?? skill.skill_id}
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                          Атрибут: {skill.attribute_dependency ?? '—'}
                        </Typography>
                      </Stack>
                      <Typography variant="body2" color="text.secondary">
                        Уровень {skill.level ?? 0} • Опыт {skill.experience ?? 0}/
                        {skill.experience_to_next_level ?? 0}
                      </Typography>
                      <LinearProgress
                        variant="determinate"
                        value={Math.max(0, Math.min(100, progress))}
                        color={progress > 80 ? 'success' : progress > 40 ? 'primary' : 'warning'}
                      />
                      <Stack direction="row" spacing={1}>
                        <TextField
                          label="Добавить XP"
                          size="small"
                          type="number"
                          value={amount}
                          onChange={event =>
                            handleChange(
                              skill.skill_id ?? '',
                              Number.parseInt(event.target.value, 10) || 0
                            )
                          }
                        />
                        <Button
                          variant="contained"
                          size="small"
                          onClick={() => handleSubmit(skill)}
                          disabled={isMutating}
                        >
                          Добавить
                        </Button>
                      </Stack>
                    </Stack>
                  </Paper>
                )
              })}
            </Stack>
          )}

          {error && (
            <Alert severity="error">
              Не удалось добавить опыт навыку. Проверьте значение и повторите попытку.
            </Alert>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}







