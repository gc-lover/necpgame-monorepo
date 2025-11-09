import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Box,
  Typography,
  Button,
  Stack,
  Alert,
  CircularProgress,
  Divider,
  FormControl,
  Select,
  MenuItem,
  InputLabel,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { AbilityCatalogCard } from '../components/AbilityCatalogCard'
import { useGetAbilitiesCatalog } from '@/api/generated/abilities-catalog/abilities-catalog/abilities-catalog'

export const AbilitiesCatalogPage: React.FC = () => {
  const navigate = useNavigate()
  const [category, setCategory] = useState<string>('')

  const { data, isLoading, error } = useGetAbilitiesCatalog({ category: category || undefined })

  const leftPanel = (
    <Stack spacing={2}>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/game/abilities')}
        fullWidth
        variant="outlined"
        size="small"
        sx={{ fontSize: '0.75rem' }}
      >
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">
        Каталог способностей
      </Typography>
      <Divider />
      <FormControl fullWidth size="small">
        <InputLabel sx={{ fontSize: '0.75rem' }}>Категория</InputLabel>
        <Select
          value={category}
          label="Категория"
          onChange={(e) => setCategory(e.target.value)}
          sx={{ fontSize: '0.75rem' }}
        >
          <MenuItem value=""><em>Все</em></MenuItem>
          <MenuItem value="combat">Combat</MenuItem>
          <MenuItem value="hacking">Hacking</MenuItem>
          <MenuItem value="tech">Tech</MenuItem>
          <MenuItem value="stealth">Stealth</MenuItem>
          <MenuItem value="support">Support</MenuItem>
          <MenuItem value="mobility">Mobility</MenuItem>
          <MenuItem value="medic">Medic</MenuItem>
        </Select>
      </FormControl>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Справка
      </Typography>
      <Divider />
      <Typography variant="body2" fontSize="0.75rem" color="text.secondary">
        27+ способностей из различных категорий
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        Все способности
      </Typography>
      <Divider />
      {error && <Alert severity="error" sx={{ fontSize: '0.75rem' }}>Ошибка загрузки</Alert>}
      {isLoading && <CircularProgress size={32} />}
      {!isLoading && data?.abilities && (
        <Box sx={{ overflowY: 'auto', flex: 1 }}>
          <Stack spacing={1.5}>
            {data.abilities.map((ability: any) => (
              <AbilityCatalogCard key={ability.id} ability={ability} />
            ))}
          </Stack>
        </Box>
      )}
    </Stack>
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      {centerContent}
    </GameLayout>
  )
}

export default AbilitiesCatalogPage

