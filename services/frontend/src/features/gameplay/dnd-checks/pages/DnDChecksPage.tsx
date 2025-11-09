import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, TextField, MenuItem, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import CasinoIcon from '@mui/icons-material/Casino'
import GameLayout from '@/features/game/components/GameLayout'
import { DiceRollDisplay } from '../components/DiceRollDisplay'
import { useGameState } from '@/features/game/hooks/useGameState'
import { useRollCheck, useCalculateModifier, useGetDCTable } from '@/api/generated/dnd-checks/dn-d-checks/dn-d-checks'

export const DnDChecksPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [attribute, setAttribute] = useState('body')
  const [skill, setSkill] = useState('athletics')
  const [dc, setDc] = useState(15)
  const [advantage, setAdvantage] = useState(false)

  const { data: dcTableData } = useGetDCTable({}, { query: { enabled: true } })

  const rollMutation = useRollCheck()

  const handleRoll = () => {
    if (!selectedCharacterId) return
    rollMutation.mutate({
      data: {
        character_id: selectedCharacterId,
        dice_type: 'd20',
        check_type: 'skill',
        attribute,
        skill,
        dc,
        advantage,
      },
    })
  }

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="success">
        D&D Checks
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Baldur's Gate 3
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Настройки
      </Typography>
      <TextField select label="Атрибут" value={attribute} onChange={(e) => setAttribute(e.target.value)} size="small" fullWidth>
        {['body', 'reflex', 'tech', 'intelligence', 'cool'].map((a) => (
          <MenuItem key={a} value={a}>
            {a.toUpperCase()}
          </MenuItem>
        ))}
      </TextField>
      <TextField select label="Навык" value={skill} onChange={(e) => setSkill(e.target.value)} size="small" fullWidth>
        {['athletics', 'persuasion', 'stealth', 'hacking', 'perception'].map((s) => (
          <MenuItem key={s} value={s}>
            {s}
          </MenuItem>
        ))}
      </TextField>
      <TextField label="DC" type="number" value={dc} onChange={(e) => setDc(parseInt(e.target.value))} size="small" fullWidth />
      <TextField select label="Advantage/Disadvantage" value={advantage ? 'advantage' : 'none'} onChange={(e) => setAdvantage(e.target.value === 'advantage')} size="small" fullWidth>
        <MenuItem value="none">Обычный</MenuItem>
        <MenuItem value="advantage">Advantage (2d20 лучший)</MenuItem>
        <MenuItem value="disadvantage">Disadvantage (2d20 худший)</MenuItem>
      </TextField>
      <Button startIcon={<CasinoIcon />} onClick={handleRoll} fullWidth variant="contained" size="small" disabled={rollMutation.isPending}>
        Бросить d20
      </Button>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        DC Table
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {dcTableData?.difficulties?.map((d: any, i: number) => (
          <Box key={i} display="flex" justifyContent="space-between">
            <Typography variant="caption" fontSize="0.7rem">
              {d.name}
            </Typography>
            <Typography variant="caption" fontSize="0.7rem" fontWeight="bold">
              DC {d.value}
            </Typography>
          </Box>
        ))}
      </Stack>
      <Divider />
      <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
        Formula: d20 + Attr Mod + Skill + Situation ≥ DC
      </Typography>
      <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
        Attr Mod = floor((ATTR - 10) / 2)
      </Typography>
      <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
        Skill Bonus: Novice (+1) → Legendary (+5)
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        Система D&D проверок
      </Typography>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        Baldur's Gate 3 стиль: d20 + модификаторы. Критические: 1 (автофейл), 20 (автоуспех). Advantage/Disadvantage: 2d20.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        <strong>Формула:</strong> d20 + Атрибутный Мод + Навыковый Бонус + Ситуационные ≥ DC
      </Typography>
      {rollMutation.data && (
        <Box mt={2}>
          <DiceRollDisplay result={rollMutation.data} />
        </Box>
      )}
      {!rollMutation.data && (
        <Typography variant="body2" fontSize="0.75rem" color="text.secondary">
          Выберите параметры слева и бросьте d20
        </Typography>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default DnDChecksPage

