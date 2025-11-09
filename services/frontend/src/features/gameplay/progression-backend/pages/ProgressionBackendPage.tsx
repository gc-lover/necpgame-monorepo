import { useState } from 'react'
import { Alert, Box, Grid, Stack, TextField, Typography } from '@mui/material'
import SchoolIcon from '@mui/icons-material/School'
import {
  useGetCharacterExperience,
  useAwardExperience,
} from '@/api/generated/progression/backend/experience/experience'
import {
  useGetCharacterAttributes,
  useSpendAttributePoints,
} from '@/api/generated/progression/backend/attributes/attributes'
import {
  useGetCharacterSkills,
  useAddSkillExperience,
} from '@/api/generated/progression/backend/skills/skills'
import { useGetProgressionMilestones } from '@/api/generated/progression/backend/level-up/level-up'
import { GameLayout } from '@/shared/ui/layout'
import { useGameState } from '@/features/game/hooks/useGameState'
import { ExperienceOverview } from '../components/ExperienceOverview'
import { AttributesPanel } from '../components/AttributesPanel'
import { SkillsList } from '../components/SkillsList'
import { MilestonesList } from '../components/MilestonesList'

export const ProgressionBackendPage = () => {
  const { selectedCharacterId } = useGameState()
  const [characterId, setCharacterId] = useState(selectedCharacterId ?? '')

  const experienceQuery = useGetCharacterExperience(characterId, {
    query: { enabled: Boolean(characterId) },
  })
  const attributesQuery = useGetCharacterAttributes(characterId, {
    query: { enabled: Boolean(characterId) },
  })
  const skillsQuery = useGetCharacterSkills(characterId, {
    query: { enabled: Boolean(characterId) },
  })
  const milestonesQuery = useGetProgressionMilestones(characterId, {
    query: { enabled: Boolean(characterId) },
  })

  const awardMutation = useAwardExperience()
  const spendAttributesMutation = useSpendAttributePoints()
  const addSkillExperienceMutation = useAddSkillExperience()

  const handleAwardExperience = async (amount: number, source: string, multiplier?: number) => {
    if (!characterId) return
    await awardMutation.mutateAsync({
      character_id: characterId,
      data: { amount, source, multiplier },
    })
    experienceQuery.refetch()
  }

  const handleSpendPoints = async (distribution: Record<string, number>) => {
    if (!characterId) return
    await spendAttributesMutation.mutateAsync({
      character_id: characterId,
      data: { distributions: distribution },
    })
    attributesQuery.refetch()
  }

  const handleAddSkillExperience = async (skillId: string, experience: number, source?: string) => {
    if (!characterId) return
    await addSkillExperienceMutation.mutateAsync({
      character_id: characterId,
      skill_id: skillId,
      data: { experience, source },
    })
    skillsQuery.refetch()
  }

  return (
    <GameLayout
      leftPanel={
        <Stack spacing={2}>
          <Typography variant="h6" fontSize="1rem" fontWeight={600}>
            Настройки
          </Typography>
          <TextField
            label="Character ID"
            size="small"
            value={characterId}
            onChange={event => setCharacterId(event.target.value)}
            helperText="Используется для загрузки прогрессии"
          />
          {!characterId && (
            <Alert severity="info" variant="outlined">
              Выберите персонажа, чтобы получить данные.
            </Alert>
          )}
        </Stack>
      }
    >
      <Stack spacing={2} sx={{ height: '100%' }}>
        <Stack direction="row" spacing={1} alignItems="center">
          <SchoolIcon color="primary" fontSize="small" />
          <Typography variant="h5" fontSize="1.25rem" fontWeight={600}>
            Backend прогрессии персонажа
          </Typography>
        </Stack>

        {(experienceQuery.isError || attributesQuery.isError || skillsQuery.isError) && (
          <Alert severity="error">
            Ошибка загрузки части данных. Проверьте идентификатор персонажа и повторите попытку.
          </Alert>
        )}

        <ExperienceOverview
          experience={experienceQuery.data?.data}
          onAwardExperience={handleAwardExperience}
          isMutating={awardMutation.isPending}
          error={awardMutation.error}
        />

        <AttributesPanel
          attributes={attributesQuery.data?.data}
          onSpendPoints={handleSpendPoints}
          isMutating={spendAttributesMutation.isPending}
          error={spendAttributesMutation.error}
        />

        <Grid container spacing={2}>
          <Grid item xs={12} md={7}>
            <SkillsList
              skills={skillsQuery.data?.data}
              onAddExperience={handleAddSkillExperience}
              isMutating={addSkillExperienceMutation.isPending}
              error={addSkillExperienceMutation.error}
            />
          </Grid>
          <Grid item xs={12} md={5}>
            <MilestonesList milestones={milestonesQuery.data?.data.milestones} />
          </Grid>
        </Grid>

        <Box flexGrow={1} />
      </Stack>
    </GameLayout>
  )
}

export default ProgressionBackendPage







