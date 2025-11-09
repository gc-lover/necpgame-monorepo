import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button, Stack, CircularProgress, Divider, FormControl, Select, MenuItem, InputLabel } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { ImplantCatalogCard } from '../components/ImplantCatalogCard'
import { useGetImplantsCatalog } from '@/api/generated/implants-catalog/implants/implants'

export const ImplantsCatalogPage: React.FC = () => {
  const navigate = useNavigate()
  const [type, setType] = useState('')
  const [rarity, setRarity] = useState('')
  const { data, isLoading } = useGetImplantsCatalog({ type: type || undefined, rarity: rarity || undefined })

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">Импланты</Typography>
      <Typography variant="caption" fontSize="0.7rem">Cyberpunk каталог</Typography>
      <Divider />
      <FormControl fullWidth size="small">
        <InputLabel sx={{ fontSize: '0.75rem' }}>Тип</InputLabel>
        <Select value={type} label="Тип" onChange={(e) => setType(e.target.value)} sx={{ fontSize: '0.75rem' }}>
          <MenuItem value=""><em>Все</em></MenuItem>
          <MenuItem value="combat">Combat</MenuItem>
          <MenuItem value="tactical">Tactical</MenuItem>
          <MenuItem value="defensive">Defensive</MenuItem>
          <MenuItem value="mobility">Mobility</MenuItem>
          <MenuItem value="os">OS</MenuItem>
        </Select>
      </FormControl>
      <FormControl fullWidth size="small">
        <InputLabel sx={{ fontSize: '0.75rem' }}>Редкость</InputLabel>
        <Select value={rarity} label="Редкость" onChange={(e) => setRarity(e.target.value)} sx={{ fontSize: '0.75rem' }}>
          <MenuItem value=""><em>Все</em></MenuItem>
          <MenuItem value="common">Common</MenuItem>
          <MenuItem value="uncommon">Uncommon</MenuItem>
          <MenuItem value="rare">Rare</MenuItem>
          <MenuItem value="epic">Epic</MenuItem>
          <MenuItem value="legendary">Legendary</MenuItem>
        </Select>
      </FormControl>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Культовые</Typography>
      <Divider />
      <Typography variant="body2" fontSize="0.75rem" color="text.secondary">
        Sandevistan, Cyberdeck, Berserk, Gorilla Arms, Mantis Blades, Kiroshi Eyes
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">Каталог имплантов</Typography>
      <Divider />
      {isLoading && <CircularProgress size={32} />}
      {!isLoading && data?.implants && (
        <Box sx={{ overflowY: 'auto', flex: 1 }}>
          <Stack spacing={1.5}>
            {data.implants.map((implant: any) => <ImplantCatalogCard key={implant.id} implant={implant} />)}
          </Stack>
        </Box>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default ImplantsCatalogPage

