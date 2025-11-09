import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Typography,
  Stack,
  Divider,
  Alert,
  Grid,
  Box,
  TextField,
  MenuItem,
  FormControlLabel,
  Switch,
} from '@mui/material'
import DonutLargeIcon from '@mui/icons-material/DonutLarge'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import AnalyticsIcon from '@mui/icons-material/Analytics'
import PrecisionManufacturingIcon from '@mui/icons-material/PrecisionManufacturing'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { AttributeDefinitionCard } from '../components/AttributeDefinitionCard'
import { AttributesMatrixCard } from '../components/AttributesMatrixCard'
import { AttributeModifiersCard } from '../components/AttributeModifiersCard'
import { SkillsMappingCard } from '../components/SkillsMappingCard'
import { SkillRequirementsCard } from '../components/SkillRequirementsCard'
import { ClassBonusesCard } from '../components/ClassBonusesCard'
import { SynergiesCard } from '../components/SynergiesCard'
import { CapsCard } from '../components/CapsCard'

const attributeFilterOptions = ['ALL', 'STR', 'DEX', 'CON', 'INT', 'WIS', 'CHA', 'TECH', 'COOL', 'LUCK']
const mappingOptions = ['TO_ITEMS', 'TO_IMPLANTS', 'TO_CLASSES'] as const

const demoAttribute = {
  code: 'INT',
  name: 'Intelligence',
  description: 'Решает скорость взлома, кибердек и эффекты алгоритмов.',
  growthType: 'SOFT_CAP' as const,
  softCap: 70,
  hardCap: 100,
  synergySkills: ['Hacking', 'Quickhacks', 'Cyberdeck Optimization'],
}

const demoMatrix = {
  classId: 'netrunner',
  className: 'Netrunner',
  focus: 'INT / TECH',
  attributes: [
    { attribute: 'INT', base: 6, growth: 3 },
    { attribute: 'TECH', base: 5, growth: 2 },
    { attribute: 'COOL', base: 4, growth: 2 },
  ],
}

const demoModifiers = [
  { attribute: 'INT', total: 78, base: 60, equipment: 10, buffs: 8 },
  { attribute: 'TECH', total: 66, base: 52, equipment: 9, buffs: 5 },
]

const demoClassBonuses = {
  className: 'Neon Ronin',
  focus: 'STR / COOL',
  bonuses: [
    { bonus: 'Blade Damage', value: '+15%', description: 'Синергия с высокими рефлексами.' },
    { bonus: 'Adrenal Boost', value: '+20% stamina regen' },
  ],
}

const demoSkillsMapping = {
  mappingType: 'TO_ITEMS' as const,
  entries: [
    { source: 'Hacking', targets: ['Deck Mk.III', 'ICE Breaker'] },
    { source: 'Engineering', targets: ['Smart SMG', 'Drone Control Rig'] },
  ],
}

const demoRequirements = {
  itemName: 'Prototype Smart Sniper',
  isEligible: false,
  requirements: [
    { skill: 'Tech Weapons', required: 14, current: 11 },
    { skill: 'Perception', required: 12, current: 12 },
  ],
}

const demoSynergies = [
  { name: 'Cyberblade Dancer', description: 'Blades + Reflexes → +8% crit chance.', bonus: '+8% crit' },
  { name: 'Neon Analyst', description: 'Intelligence + Tech → -15% quickhack cost.', bonus: '-15% CPU' },
]

const demoCaps = [
  { name: 'INT', softCap: 70, hardCap: 100, current: 68 },
  { name: 'TECH', softCap: 65, hardCap: 95, current: 60 },
]

export const ProgressionDetailedPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [attributeFilter, setAttributeFilter] = useState<string>('ALL')
  const [mappingFilter, setMappingFilter] = useState<typeof mappingOptions[number]>('TO_ITEMS')
  const [includeEquipment, setIncludeEquipment] = useState<boolean>(true)
  const [includeBuffs, setIncludeBuffs] = useState<boolean>(true)

  const filteredCard = attributeFilter === 'ALL' || attributeFilter === demoAttribute.code ? demoAttribute : null

  const leftPanel = (
    <Stack spacing={2}>
      <CyberpunkButton
        variant="outlined"
        size="small"
        fullWidth
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/game')}
      >
        Назад
      </CyberpunkButton>
      {selectedCharacterId && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Character: {selectedCharacterId}
        </Typography>
      )}
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="primary.light">
        Progression Detailed
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Атрибуты, навыки, синергии. 9 характеристик, caps, матрицы по классам.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтр атрибутов
      </Typography>
      <TextField
        select
        size="small"
        label="Attribute"
        value={attributeFilter}
        onChange={(event) => setAttributeFilter(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {attributeFilterOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Mapping Type
      </Typography>
      <TextField
        select
        size="small"
        label="Mapping"
        value={mappingFilter}
        onChange={(event) => setMappingFilter(event.target.value as typeof mappingOptions[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {mappingOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={
          <Switch
            size="small"
            checked={includeEquipment}
            onChange={(event) => setIncludeEquipment(event.target.checked)}
          />
        }
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Equipment Bonuses</Typography>}
      />
      <FormControlLabel
        control={
          <Switch
            size="small"
            checked={includeBuffs}
            onChange={(event) => setIncludeBuffs(event.target.checked)}
          />
        }
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Temporary Buffs</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth startIcon={<AnalyticsIcon />}>
        Рассчитать модификаторы
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth startIcon={<PrecisionManufacturingIcon />}>
        Проверить предмет
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <CompactCard color="green" glowIntensity="weak" compact>
        <Stack spacing={0.4}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Growth Rules
          </Typography>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Soft caps замедляют прирост. Hard caps требуют редкого импланта или событий фракций.
          </Typography>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            INT/TECH влияет на хакерские задачи, STR/CON на ближний бой, COOL на стелс и резисты.
          </Typography>
        </Stack>
      </CompactCard>
      <SynergiesCard synergies={demoSynergies} />
      <CapsCard caps={demoCaps} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <DonutLargeIcon sx={{ fontSize: '1.4rem', color: 'primary.light' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Progression Control Center
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Управляй атрибутами, синергиями и caps. Просматривай стартовые матрицы, проверяй требования предметов и бонусы классов.
      </Alert>

      <Grid container spacing={1}>
        {filteredCard && (
          <Grid item xs={12} md={6}>
            <AttributeDefinitionCard attribute={filteredCard} />
          </Grid>
        )}
        <Grid item xs={12} md={6}>
          <AttributesMatrixCard entry={demoMatrix} />
        </Grid>
        <Grid item xs={12} md={6}>
          <AttributeModifiersCard modifiers={demoModifiers.map((modifier) => ({
            ...modifier,
            equipment: includeEquipment ? modifier.equipment : 0,
            buffs: includeBuffs ? modifier.buffs : 0,
            total: modifier.base + (includeEquipment ? modifier.equipment : 0) + (includeBuffs ? modifier.buffs : 0),
          }))} />
        </Grid>
        <Grid item xs={12} md={6}>
          <ClassBonusesCard {...demoClassBonuses} />
        </Grid>
      </Grid>

      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <SkillsMappingCard mappingType={mappingFilter} entries={demoSkillsMapping.entries} />
        </Grid>
        <Grid item xs={12} md={6}>
          <SkillRequirementsCard {...demoRequirements} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default ProgressionDetailedPage


