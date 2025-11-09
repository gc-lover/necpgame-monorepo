import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Box, TextField, MenuItem } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import CategoryIcon from '@mui/icons-material/Category'
import GameLayout from '@/features/game/components/GameLayout'
import { ResourceCard } from '../components/ResourceCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import { useGetResources } from '@/api/generated/resources-catalog/resources-catalog/resources-catalog'

const CATEGORIES = ['raw', 'processed', 'components', 'data', 'special']
const RARITIES = ['common', 'uncommon', 'rare', 'epic', 'legendary']

export const ResourcesCatalogPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [selectedCategory, setSelectedCategory] = useState('')
  const [selectedTier, setSelectedTier] = useState('')
  const [selectedRarity, setSelectedRarity] = useState('')

  const { data: resourcesData } = useGetResources({
    category: selectedCategory || undefined,
    tier: selectedTier ? parseInt(selectedTier) : undefined,
    rarity: selectedRarity || undefined,
  })

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="warning">
        Resources Catalog
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Крафт / Торговля
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        label="Категория"
        value={selectedCategory}
        onChange={(e) => setSelectedCategory(e.target.value)}
        size="small"
        fullWidth
        sx={{ '& .MuiInputBase-input': { fontSize: '0.75rem' } }}
      >
        <MenuItem value="">Все</MenuItem>
        {CATEGORIES.map((cat) => (
          <MenuItem key={cat} value={cat} sx={{ fontSize: '0.75rem' }}>
            {cat.toUpperCase()}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        type="number"
        label="Tier"
        value={selectedTier}
        onChange={(e) => setSelectedTier(e.target.value)}
        size="small"
        fullWidth
        inputProps={{ min: 1, max: 5 }}
        sx={{ '& .MuiInputBase-input': { fontSize: '0.75rem' } }}
      />
      <TextField
        select
        label="Редкость"
        value={selectedRarity}
        onChange={(e) => setSelectedRarity(e.target.value)}
        size="small"
        fullWidth
        sx={{ '& .MuiInputBase-input': { fontSize: '0.75rem' } }}
      >
        <MenuItem value="">Все</MenuItem>
        {RARITIES.map((rar) => (
          <MenuItem key={rar} value={rar} sx={{ fontSize: '0.75rem' }}>
            {rar}
          </MenuItem>
        ))}
      </TextField>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Категории
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['Raw Materials (сырьё)', 'Processed (обработанные)', 'Components (компоненты)', 'Data (данные)', 'Special (специальные)'].map((c, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {c}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Параметры
      </Typography>
      <Stack spacing={0.3}>
        {['Tier 1-5', 'Rarity', 'Sources', 'Uses', 'Value', 'Stack size', 'Weight'].map((p, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {p}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <CategoryIcon sx={{ fontSize: '1.5rem', color: 'warning.main' }} />
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Каталог ресурсов
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        Ресурсы для крафта и торговли! Категории: raw/processed/components/data/special. Tier 1-5. Rarity: common→legendary.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        Всего ресурсов: {resourcesData?.resources?.length || 0}
      </Typography>
      <Grid container spacing={1}>
        {resourcesData?.resources?.map((resource, index) => (
          <Grid item xs={12} sm={6} md={4} key={resource.resource_id || index}>
            <ResourceCard resource={resource} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default ResourcesCatalogPage

