import { useEffect, useMemo, useState } from 'react'
import { Alert, Grid, Stack, TextField, Typography } from '@mui/material'
import GroupWorkIcon from '@mui/icons-material/GroupWork'
import { GameLayout } from '@/shared/ui/layout'
import { useListFactionQuests } from '@/api/generated/narrative/faction-quests/faction-quests/faction-quests'
import {
  useGetFactionQuest,
  useGetQuestBranches,
  useGetQuestEndings,
  useGetAvailableFactionQuests,
  useGetFactionQuestProgress,
} from '@/api/generated/narrative/faction-quests/faction-quests/faction-quests'
import { FactionQuestList, type QuestFilters } from '../components/FactionQuestList'
import { QuestDetailsPanel } from '../components/QuestDetailsPanel'
import { QuestAvailabilityPanel } from '../components/QuestAvailabilityPanel'
import { useGameState } from '@/features/game/hooks/useGameState'

export const FactionQuestsPage = () => {
  const { selectedCharacterId } = useGameState()
  const [filters, setFilters] = useState<QuestFilters>({ page: 1, page_size: 20 })
  const [selectedQuestId, setSelectedQuestId] = useState<string | undefined>(undefined)
  const [characterId, setCharacterId] = useState(selectedCharacterId ?? '')

  const questsQuery = useListFactionQuests(filters)
  const questDetailsQuery = useGetFactionQuest(selectedQuestId ?? '', {
    query: { enabled: Boolean(selectedQuestId) },
  })
  const branchesQuery = useGetQuestBranches(selectedQuestId ?? '', {
    query: { enabled: Boolean(selectedQuestId) },
  })
  const endingsQuery = useGetQuestEndings(selectedQuestId ?? '', {
    query: { enabled: Boolean(selectedQuestId) },
  })

  const availabilityQuery = useGetAvailableFactionQuests(characterId, {
    query: { enabled: Boolean(characterId) },
  })
  const progressQuery = useGetFactionQuestProgress(characterId, {
    query: { enabled: Boolean(characterId) },
  })

  const quests = questsQuery.data?.data.data ?? []

  useEffect(() => {
    if (!selectedQuestId && quests.length > 0) {
      setSelectedQuestId(quests[0].quest_id)
    }
  }, [quests, selectedQuestId])

  const activeQuestDetails = questDetailsQuery.data?.data
  const branches = branchesQuery.data?.data.branches ?? []
  const endings = endingsQuery.data?.data.endings ?? []

  const filteredQuests = useMemo(() => {
    if (!filters.search) {
      return quests
    }
    const query = filters.search.toLowerCase()
    return quests.filter(
      quest =>
        quest.title?.toLowerCase().includes(query) ||
        quest.description?.toLowerCase().includes(query) ||
        quest.faction?.toLowerCase().includes(query)
    )
  }, [quests, filters.search])

  return (
    <GameLayout
      leftPanel={
        <Stack spacing={2}>
          <Typography variant="h6" fontSize="1rem" fontWeight={600}>
            Персонаж
          </Typography>
          <TextField
            label="Character ID"
            size="small"
            value={characterId}
            onChange={event => setCharacterId(event.target.value)}
            helperText="Показывает доступность и прогресс"
          />
          <QuestAvailabilityPanel
            availability={availabilityQuery.data?.data}
            progress={progressQuery.data?.data}
          />
        </Stack>
      }
    >
      <Stack spacing={2} sx={{ height: '100%' }}>
        <Stack direction="row" spacing={1} alignItems="center">
          <GroupWorkIcon color="primary" fontSize="small" />
          <Typography variant="h5" fontSize="1.25rem" fontWeight={600}>
            Фракционные квесты
          </Typography>
        </Stack>

        {questsQuery.isError && (
          <Alert severity="error">Не удалось загрузить список квестов. Попробуйте позднее.</Alert>
        )}

        <TextField
          label="Поиск"
          size="small"
          value={filters.search ?? ''}
          onChange={event => setFilters(prev => ({ ...prev, search: event.target.value }))}
        />

        <Grid container spacing={2}>
          <Grid item xs={12} md={5}>
            <FactionQuestList
              quests={filteredQuests}
              selectedQuestId={selectedQuestId}
              filters={filters}
              onFiltersChange={setFilters}
              onSelectQuest={setSelectedQuestId}
            />
          </Grid>
          <Grid item xs={12} md={7}>
            <QuestDetailsPanel
              quest={activeQuestDetails}
              branches={branches}
              endings={endings}
            />
          </Grid>
        </Grid>
      </Stack>
    </GameLayout>
  )
}

export default FactionQuestsPage







