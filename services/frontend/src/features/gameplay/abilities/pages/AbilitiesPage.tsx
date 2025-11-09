import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Alert,
  Box,
  Button,
  CircularProgress,
  Divider,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  Typography,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { Ability } from '@/api/generated/abilities/models'
import { useGetAbilities } from '@/api/generated/abilities/abilities/abilities'
import { useGameState } from '@/features/game/hooks/useGameState'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import {
  AbilityCard,
  AbilityCooldownsPanel,
  AbilityDetailsPanel,
  AbilityLoadoutPanel,
  AbilitySynergiesPanel,
  AbilityUseDialog,
} from '../components'

type SourceType = 'equipment' | 'implants' | 'skills' | 'cyberdeck'

export function AbilitiesPage() {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [sourceFilter, setSourceFilter] = useState<SourceType | ''>('')
  const [selectedAbilityId, setSelectedAbilityId] = useState<string | null>(null)
  const [abilityForDialog, setAbilityForDialog] = useState<Ability | null>(null)
  const [isUseDialogOpen, setIsUseDialogOpen] = useState(false)

  const {
    data: abilitiesData,
    isLoading,
    error,
  } = useGetAbilities(
    { character_id: selectedCharacterId || '', source: sourceFilter || undefined },
    { query: { enabled: !!selectedCharacterId } },
  )

  useEffect(() => {
    if (!selectedCharacterId) {
      navigate('/characters')
    }
  }, [selectedCharacterId, navigate])

  const filteredAbilities = useMemo(() => {
    if (!abilitiesData?.abilities) {
      return []
    }

    if (!sourceFilter) {
      return abilitiesData.abilities
    }

    return abilitiesData.abilities.filter(
      (ability) => ability.source?.type === sourceFilter || ability.slot?.toLowerCase() === sourceFilter,
    )
  }, [abilitiesData, sourceFilter])

  const handleAbilityClick = (ability: Ability) => {
    setSelectedAbilityId(ability.id)
  }

  const handleUseAbility = (ability: Ability) => {
    setAbilityForDialog(ability)
    setIsUseDialogOpen(true)
  }

  const closeDialog = () => {
    setIsUseDialogOpen(false)
    setAbilityForDialog(null)
  }

  const leftPanel = (
    <Stack spacing={2} height="100%">
      <Box>
        <Button
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/game')}
          fullWidth
          variant="outlined"
          size="small"
          sx={{ fontSize: '0.75rem', mb: 1 }}
        >
          Назад
        </Button>
        <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">
          Способности
        </Typography>
        <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
          Q/E/R система (VALORANT)
        </Typography>
      </Box>

      <Divider />

      <Box>
        <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" mb={1}>
          Фильтр
        </Typography>
        <FormControl fullWidth size="small">
          <InputLabel sx={{ fontSize: '0.75rem' }}>Источник</InputLabel>
          <Select
            value={sourceFilter}
            label="Источник"
            onChange={(e) => setSourceFilter(e.target.value as SourceType | '')}
            sx={{ fontSize: '0.75rem' }}
          >
            <MenuItem value=""><em>Все</em></MenuItem>
            <MenuItem value="equipment">Экипировка</MenuItem>
            <MenuItem value="implants">Импланты</MenuItem>
            <MenuItem value="skills">Навыки</MenuItem>
            <MenuItem value="cyberdeck">Кибердека</MenuItem>
          </Select>
        </FormControl>
      </Box>

      <Divider />

      <Box mt="auto">
        <Alert severity="info" sx={{ fontSize: '0.7rem', py: 0.5 }}>
          Способности из экипировки, имплантов, навыков и кибердеки
        </Alert>
      </Box>

      <AbilitySynergiesPanel characterId={selectedCharacterId} />
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2} height="100%">
      <AbilityLoadoutPanel characterId={selectedCharacterId} abilities={abilitiesData?.abilities ?? []} />
      <AbilityCooldownsPanel characterId={selectedCharacterId} />
      <StatsPanel>
        <Typography variant="subtitle2" fontSize="0.8rem" fontWeight={600}>
          Советы
        </Typography>
        <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
          Подбирайте способности по слотам, чтобы активировать сет-бонусы и избегать перегрева системы.
        </Typography>
      </StatsPanel>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        Доступные способности
      </Typography>
      <Divider />

      {error && (
        <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
          Ошибка: {(error as Error).message}
        </Alert>
      )}

      {isLoading && (
        <Box display="flex" justifyContent="center" py={4}>
          <CircularProgress size={32} />
        </Box>
      )}

      {!isLoading && abilitiesData?.abilities && (
        <Box sx={{ overflowY: 'auto', flex: 1 }}>
          {abilitiesData.abilities.length === 0 ? (
            <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
              Способности не найдены
            </Alert>
          ) : (
            <Stack spacing={1.5} sx={{ pb: 1 }}>
              {filteredAbilities.map((ability) => (
                <AbilityCard
                  key={ability.id}
                  ability={ability}
                  onClick={() => handleAbilityClick(ability)}
                />
              ))}
            </Stack>
          )}
        </Box>
      )}

      <AbilityDetailsPanel abilityId={selectedAbilityId} onUseAbility={handleUseAbility} />
    </Stack>
  )

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      <Header />
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        {centerContent}
      </GameLayout>

      <AbilityUseDialog
        open={isUseDialogOpen}
        ability={abilityForDialog}
        characterId={selectedCharacterId ?? undefined}
        onClose={closeDialog}
      />
    </Box>
  )
}

