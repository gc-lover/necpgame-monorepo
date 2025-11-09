import { useMemo, useState } from 'react'
import {
  Alert,
  Box,
  IconButton,
  List,
  ListItem,
  ListItemSecondaryAction,
  ListItemText,
  Stack,
  Typography,
} from '@mui/material'
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline'
import RefreshIcon from '@mui/icons-material/Refresh'
import {
  getListCharactersQueryKey,
  useDeleteCharacter,
  useListCharacters,
} from '@/api/generated/auth/characters/characters'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { CompactCard } from '@/shared/ui/cards/CompactCard'
import { useQueryClient } from '@tanstack/react-query'

interface CharacterRosterCardProps {
  onRefresh?: () => void
}

export function CharacterRosterCard({ onRefresh }: CharacterRosterCardProps) {
  const queryClient = useQueryClient()
  const listQuery = useListCharacters(undefined, { query: { staleTime: 30_000 } })
  const deleteMutation = useDeleteCharacter()
  const [feedback, setFeedback] = useState<{ type: 'success' | 'error'; message: string } | null>(null)

  const characters = useMemo(() => listQuery.data?.characters ?? [], [listQuery.data])

  const handleDelete = (characterId: string, name?: string | null) => {
    setFeedback(null)
    deleteMutation.mutate(
      { characterId },
      {
        onSuccess: () => {
          setFeedback({ type: 'success', message: `Персонаж ${name ?? ''} удалён` })
          queryClient.invalidateQueries({ queryKey: getListCharactersQueryKey() })
          onRefresh?.()
        },
        onError: () => {
          setFeedback({ type: 'error', message: 'Удаление не удалось. Попробуйте снова.' })
        },
      }
    )
  }

  const handleManualRefresh = () => {
    setFeedback(null)
    listQuery.refetch()
    onRefresh?.()
  }

  return (
    <CompactCard
      color="green"
      glowIntensity="normal"
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
      }}
    >
      <Stack spacing={2} sx={{ flex: 1, minHeight: 0 }}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box>
            <Typography variant="h6" fontSize="0.9rem" textTransform="uppercase">
              Персонажи
            </Typography>
            <Typography variant="body2" color="text.secondary" fontSize="0.75rem">
              Управляйте доступными персонажами
            </Typography>
          </Box>
          <IconButton
            aria-label="refresh characters"
            onClick={handleManualRefresh}
            size="small"
            color="primary"
          >
            <RefreshIcon fontSize="small" />
          </IconButton>
        </Box>
        {feedback && (
          <Alert severity={feedback.type} variant="outlined">
            {feedback.message}
          </Alert>
        )}
        {listQuery.isLoading ? (
          <Typography variant="body2" color="text.secondary">
            Загрузка персонажей...
          </Typography>
        ) : listQuery.error ? (
          <Alert severity="error" variant="outlined">
            Не удалось получить список персонажей. Проверьте авторизацию.
          </Alert>
        ) : characters.length === 0 ? (
          <Box sx={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Typography variant="body2" color="text.secondary">
              Персонажей пока нет. Создайте первого героя.
            </Typography>
          </Box>
        ) : (
          <List
            dense
            sx={{
              flex: 1,
              overflowY: 'auto',
              border: '1px solid rgba(255,255,255,0.08)',
              borderRadius: 1,
              px: 1,
            }}
          >
            {characters.map((character) => (
              <ListItem
                key={character.id}
                divider
                sx={{
                  alignItems: 'flex-start',
                  '&:last-of-type': { borderBottom: 'none' },
                }}
              >
                <ListItemText
                  primary={
                    <Typography variant="subtitle2" fontSize="0.8rem" color="primary.main">
                      {character.name}
                    </Typography>
                  }
                  secondary={
                    <Typography variant="caption" color="text.secondary">
                      {character.class} • Уровень {character.level} • {character.city_name}
                    </Typography>
                  }
                />
                <ListItemSecondaryAction>
                  <IconButton
                    edge="end"
                    size="small"
                    color="error"
                    onClick={() => handleDelete(character.id ?? '', character.name)}
                    disabled={deleteMutation.isPending}
                    aria-label="delete character"
                  >
                    <DeleteOutlineIcon fontSize="small" />
                  </IconButton>
                </ListItemSecondaryAction>
              </ListItem>
            ))}
          </List>
        )}
        <CyberpunkButton
          variant="outlined"
          size="small"
          fullWidth
          onClick={handleManualRefresh}
          disabled={listQuery.isFetching}
        >
          {listQuery.isFetching ? 'Обновление...' : 'Обновить'}
        </CyberpunkButton>
      </Stack>
    </CompactCard>
  )
}






