import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button, Stack, CircularProgress, Divider, FormControl, Select, MenuItem, InputLabel } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { ComboCard } from '../components/ComboCard'
import { useGetCombos } from '@/api/generated/combos/combos/combos'

export const CombosPage: React.FC = () => {
  const navigate = useNavigate()
  const [type, setType] = useState<string>('')
  const { data, isLoading } = useGetCombos({ type: type || undefined })

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">Комбо</Typography>
      <Divider />
      <FormControl fullWidth size="small">
        <InputLabel sx={{ fontSize: '0.75rem' }}>Тип</InputLabel>
        <Select value={type} label="Тип" onChange={(e) => setType(e.target.value)} sx={{ fontSize: '0.75rem' }}>
          <MenuItem value=""><em>Все</em></MenuItem>
          <MenuItem value="solo">Solo</MenuItem>
          <MenuItem value="team">Team</MenuItem>
          <MenuItem value="legendary">Legendary</MenuItem>
        </Select>
      </FormControl>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">14+ комбо</Typography>
      <Divider />
      {isLoading && <CircularProgress size={32} />}
      {!isLoading && data?.combos && (
        <Box sx={{ overflowY: 'auto', flex: 1 }}>
          <Stack spacing={1.5}>
            {data.combos.map((combo: any) => <ComboCard key={combo.id} combo={combo} />)}
          </Stack>
        </Box>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel}>{centerContent}</GameLayout>
}

export default CombosPage

