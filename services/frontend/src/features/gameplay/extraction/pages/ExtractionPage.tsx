import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Button, Stack, CircularProgress, Divider, FormControl, Select, MenuItem, InputLabel } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { ExtractionZoneCard } from '../components/ExtractionZoneCard'
import { useGetExtractionZones } from '@/api/generated/extraction/extraction/extraction'

export const ExtractionPage: React.FC = () => {
  const navigate = useNavigate()
  const [riskLevel, setRiskLevel] = useState<string>('')
  const { data, isLoading } = useGetExtractionZones({ risk_level: riskLevel || undefined })

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">Экстракция</Typography>
      <Typography variant="caption" fontSize="0.7rem" color="text.secondary">TARKOV стиль</Typography>
      <Divider />
      <FormControl fullWidth size="small">
        <InputLabel sx={{ fontSize: '0.75rem' }}>Риск</InputLabel>
        <Select value={riskLevel} label="Риск" onChange={(e) => setRiskLevel(e.target.value)} sx={{ fontSize: '0.75rem' }}>
          <MenuItem value=""><em>Все</em></MenuItem>
          <MenuItem value="low">Low</MenuItem>
          <MenuItem value="medium">Medium</MenuItem>
          <MenuItem value="high">High</MenuItem>
          <MenuItem value="extreme">Extreme</MenuItem>
        </Select>
      </FormControl>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Инфо</Typography>
      <Divider />
      <Typography variant="body2" fontSize="0.75rem" color="text.secondary">Экстракт-зоны с лутом. Риск потери при неудачной экстракции.</Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">Зоны экстракции</Typography>
      <Divider />
      {isLoading && <CircularProgress size={32} />}
      {!isLoading && data?.zones && (
        <Box sx={{ overflowY: 'auto', flex: 1 }}>
          <Stack spacing={1.5}>
            {data.zones.map((zone: any) => <ExtractionZoneCard key={zone.id} zone={zone} />)}
          </Stack>
        </Box>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default ExtractionPage

