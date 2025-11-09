import { useEffect, useMemo, useState } from 'react'
import { Alert, Grid, Stack, TextField, Typography } from '@mui/material'
import LibraryBooksIcon from '@mui/icons-material/LibraryBooks'
import { GameLayout } from '@/shared/ui/layout'
import { useGameState } from '@/features/game/hooks/useGameState'
import { QuestCatalogFilters } from '../components/QuestCatalogFilters'
import { QuestCatalogList } from '../components/QuestCatalogList'
import { QuestCatalogDetails } from '../components/QuestCatalogDetails'
import { QuestRecommendationsPanel } from '../components/QuestRecommendationsPanel'
import {
  useGetQuestCatalog,
  useGetQuestDetails,
  useGetQuestDialogueTree,
  useGetQuestLootTable,
  useGetQuestRecommendations,
  useGetQuestChains,
} from '@/api/generated/narrative/quest-catalog/quest-catalog/quest-catalog'
import { useSearchQuests } from '@/api/generated/narrative/quest-catalog/quest-search/quest-search'
import type { GetQuestCatalogParams } from '@/api/generated/narrative/quest-catalog/models'

export const QuestCatalogPage = () => {
  const { selectedCharacterId } = useGameState()
  const [filters, setFilters] = useState<GetQuestCatalogParams>({ page: 1, page_size: 25 })
  const [selectedQuestId, setSelectedQuestId] = useState<string | undefined>(undefined)
  const [characterId, setCharacterId] = useState(selectedCharacterId ?? '')
  const [searchQuery, setSearchQuery] = useState('')

  const catalogQuery = useGetQuestCatalog(filters)
  const searchQueryEnabled = searchQuery.trim().length > 2
  const searchResultsQuery = useSearchQuests(
    searchQueryEnabled ? { q: searchQuery, page_size: 25 } : undefined,
    {
      query: { enabled: searchQueryEnabled },
    }
  )

  const questDetailsQuery = useGetQuestDetails(selectedQuestId ?? '', {
    query: { enabled: Boolean(selectedQuestId) },
  })
  const dialogueTreeQuery = useGetQuestDialogueTree(selectedQuestId ?? '', {
    query: { enabled: Boolean(selectedQuestId) },
  })
  const lootTableQuery = useGetQuestLootTable(selectedQuestId ?? '', {
    query: { enabled: Boolean(selectedQuestId) },
  })
  const recommendationsQuery = useGetQuestRecommendations(characterId, {
    query: { enabled: Boolean(characterId) },
  })
  const chainsQuery = useGetQuestChains({
    query: { enabled: false },
  })

  const catalogEntries = catalogQuery.data?.data.data ?? []
  const searchEntries = searchResultsQuery.data?.data.data ?? []
  const quests = searchQueryEnabled ? searchEntries.map(item => item.quest) : catalogEntries

  useEffect(() => {
    if (!selectedQuestId && quests.length > 0) {
      setSelectedQuestId(quests[0].quest_id)
    }
  }, [quests, selectedQuestId])

  const chainsCount = chainsQuery.data?.data.chains?.length ?? 0

  const sortedQuests = useMemo(
    () =>
      quests.slice().sort((a, b) => {
        if ((b.match_score ?? 0) !== (a.match_score ?? 0)) {
          return (b.match_score ?? 0) - (a.match_score ?? 0)
        }
        return (a.title ?? '').localeCompare(b.title ?? '')
      }),
    [quests]
  )

  return (
    <GameLayout
      leftPanel={
        <Stack spacing={2}>
          <Typography variant="h6" fontSize="1rem" fontWeight={600}>
            Персонаж и рекомендации
          </Typography>
          <TextField
            label="Character ID"
            size="small"
            value={characterId}
            onChange={event => setCharacterId(event.target.value)}
          />
          <QuestRecommendationsPanel recommendations={recommendationsQuery.data?.data} />
        </Stack>
      }
    >
      <Stack spacing={2} sx={{ height: '100%' }}>
        <Stack direction="row" spacing={1} alignItems="center">
          <LibraryBooksIcon color="primary" fontSize="small" />
          <Typography variant="h5" fontSize="1.25rem" fontWeight={600}>
            Каталог квестов
          </Typography>
        </Stack>

        {(catalogQuery.isError || searchResultsQuery.isError) && (
          <Alert severity="error">Не удалось загрузить каталог квестов.</Alert>
        )}

        <TextField
          label="Поиск (минимум 3 символа)"
          size="small"
          value={searchQuery}
          onChange={event => setSearchQuery(event.target.value)}
          helperText="Поиск по названиям, описаниям, NPC"
        />

        <QuestCatalogFilters filters={filters} onChange={setFilters} />

        <Typography variant="caption" color="text.secondary">
          Коллекция: {catalogEntries.length} • Цепочек: {chainsCount}
        </Typography>

        <Grid container spacing={2}>
          <Grid item xs={12} md={5}>
            <QuestCatalogList
              quests={sortedQuests}
              selectedQuestId={selectedQuestId}
              onSelectQuest={setSelectedQuestId}
            />
          </Grid>
          <Grid item xs={12} md={7}>
            <QuestCatalogDetails
              quest={questDetailsQuery.data?.data}
              dialogueTree={dialogueTreeQuery.data?.data}
              lootTable={lootTableQuery.data?.data}
            />
          </Grid>
        </Grid>
      </Stack>
    </GameLayout>
  )
}

export default QuestCatalogPage







