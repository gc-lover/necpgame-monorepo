import { useState } from 'react'
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
  Chip,
  Tabs,
  Tab,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import RefreshIcon from '@mui/icons-material/Refresh'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import { WeaponCard } from '../components/WeaponCard'
import { WeaponDetailsDialog } from '../components/WeaponDetailsDialog'
import { MasteryDisplay } from '../components/MasteryDisplay'
import { useGameState } from '@/features/game/hooks/useGameState'
import { useGetWeaponMastery } from '@/api/generated/weapons/mastery/mastery'
import {
  useGetWeaponsCatalog,
  useGetWeapon,
  useGetWeaponMods,
  useGetMetaWeapons,
} from '@/api/generated/weapons/weapons/weapons'
import type {
  WeaponSummary,
  WeaponDetails,
  WeaponMod,
  GetWeaponsCatalogWeaponClass,
  GetWeaponsCatalogRarity,
} from '@/api/generated/weapons/models'

const CLASS_OPTIONS: { value: '' | GetWeaponsCatalogWeaponClass; label: string }[] = [
  { value: '', label: 'Все классы' },
  { value: 'pistol', label: 'Пистолеты' },
  { value: 'assault_rifle', label: 'Штурмовые винтовки' },
  { value: 'shotgun', label: 'Дробовики' },
  { value: 'sniper_rifle', label: 'Снайперские винтовки' },
  { value: 'smg', label: 'Пистолеты-пулемёты' },
  { value: 'lmg', label: 'Лёгкие пулемёты' },
  { value: 'melee', label: 'Ближний бой' },
  { value: 'cyberware', label: 'Кибероружие' },
]

const RARITY_OPTIONS: { value: '' | GetWeaponsCatalogRarity; label: string }[] = [
  { value: '', label: 'Любая редкость' },
  { value: 'common', label: 'Common' },
  { value: 'uncommon', label: 'Uncommon' },
  { value: 'rare', label: 'Rare' },
  { value: 'epic', label: 'Epic' },
  { value: 'legendary', label: 'Legendary' },
  { value: 'iconic', label: 'Iconic' },
]

const BRAND_OPTIONS = [
  { value: '', label: 'Все бренды' },
  { value: 'arasaka', label: 'Arasaka' },
  { value: 'militech', label: 'Militech' },
  { value: 'kang_tao', label: 'Kang Tao' },
  { value: 'budget_arms', label: 'Budget Arms' },
  { value: 'constitutional_arms', label: 'Constitutional Arms' },
]

const META_CONTENT_TYPES = [
  { value: 'pve', label: 'PvE' },
  { value: 'pvp', label: 'PvP' },
  { value: 'extraction', label: 'Extraction' },
  { value: 'raid', label: 'Raid' },
]

export function WeaponsPage() {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [weaponClass, setWeaponClass] = useState<'' | GetWeaponsCatalogWeaponClass>('')
  const [rarity, setRarity] = useState<'' | GetWeaponsCatalogRarity>('')
  const [brand, setBrand] = useState('')
  const [selectedWeaponId, setSelectedWeaponId] = useState<string | null>(null)
  const [detailsOpen, setDetailsOpen] = useState(false)
  const [contentType, setContentType] = useState<'pve' | 'pvp' | 'extraction' | 'raid'>('pve')

  const catalogQuery = useGetWeaponsCatalog({
    weapon_class: weaponClass || undefined,
    rarity: rarity || undefined,
    brand: brand || undefined,
  })

  const weaponDetailsQuery = useGetWeapon(selectedWeaponId ?? '', {
    query: { enabled: Boolean(selectedWeaponId) },
  })

  const masteryQuery = useGetWeaponMastery(selectedCharacterId || '', {
    query: { enabled: Boolean(selectedCharacterId) },
  })

  const modsQuery = useGetWeaponMods(
    { weapon_class: weaponClass || undefined },
    { query: { enabled: Boolean(weaponClass) } }
  )

  const metaQuery = useGetMetaWeapons(contentType, { query: { enabled: true } })

  const weapons = catalogQuery.data?.weapons ?? []
  const mods = modsQuery.data?.mods ?? []
  const recommendations = metaQuery.data?.recommendations ?? []

  const handleWeaponClick = (weapon: WeaponSummary) => {
    setSelectedWeaponId(weapon.id)
    setDetailsOpen(true)
  }

  const handleCloseDetails = () => {
    setDetailsOpen(false)
    setSelectedWeaponId(null)
  }

  const leftPanel = (
    <Stack spacing={2} sx={{ height: '100%' }}>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/game')}
        variant="outlined"
        size="small"
        sx={{ fontSize: '0.75rem' }}
      >
        Назад к игре
      </Button>

      <Divider />

      <Typography variant="subtitle2" fontSize="0.85rem" fontWeight="bold">
        Фильтры каталога
      </Typography>

      <FormControl fullWidth size="small">
        <InputLabel sx={{ fontSize: '0.75rem' }}>Класс оружия</InputLabel>
        <Select
          value={weaponClass}
          label="Класс оружия"
          onChange={(event) => setWeaponClass(event.target.value as '' | GetWeaponsCatalogWeaponClass)}
          sx={{ fontSize: '0.75rem' }}
        >
          {CLASS_OPTIONS.map((option) => (
            <MenuItem key={option.value} value={option.value} sx={{ fontSize: '0.75rem' }}>
              {option.label}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <FormControl fullWidth size="small">
        <InputLabel sx={{ fontSize: '0.75rem' }}>Редкость</InputLabel>
        <Select
          value={rarity}
          label="Редкость"
          onChange={(event) => setRarity(event.target.value as '' | GetWeaponsCatalogRarity)}
          sx={{ fontSize: '0.75rem' }}
        >
          {RARITY_OPTIONS.map((option) => (
            <MenuItem key={option.value} value={option.value} sx={{ fontSize: '0.75rem' }}>
              {option.label}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <FormControl fullWidth size="small">
        <InputLabel sx={{ fontSize: '0.75rem' }}>Бренд</InputLabel>
        <Select
          value={brand}
          label="Бренд"
          onChange={(event) => setBrand(event.target.value)}
          sx={{ fontSize: '0.75rem' }}
        >
          {BRAND_OPTIONS.map((option) => (
            <MenuItem key={option.value} value={option.value} sx={{ fontSize: '0.75rem' }}>
              {option.label}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      {(weaponClass || rarity || brand) && (
        <Button variant="text" size="small" onClick={() => {
          setWeaponClass('')
          setRarity('')
          setBrand('')
        }} sx={{ fontSize: '0.7rem' }}>
          Сбросить фильтры
        </Button>
      )}

      <Divider />

      <Alert severity="info" sx={{ fontSize: '0.7rem' }}>
        Нажмите на оружие, чтобы открыть полное описание и характеристики.
      </Alert>
    </Stack>
  )

  const rightPanel = (
    <StatsPanel>
      <Stack spacing={2} sx={{ height: '100%', overflowY: 'auto' }}>
        <Typography variant="subtitle2" fontSize="0.85rem" fontWeight="bold">
          Weapon Mastery
        </Typography>

        {masteryQuery.isFetching && <CircularProgress size={20} />}
        {!masteryQuery.isFetching && masteryQuery.data && (
          <MasteryDisplay mastery={masteryQuery.data} />
        )}
        {!masteryQuery.isFetching && !masteryQuery.data && (
          <Alert severity="info" sx={{ fontSize: '0.7rem' }}>
            Авторизуйтесь в боевой сессии, чтобы отслеживать прогресс владения оружием.
          </Alert>
        )}

        <Divider />

        <Stack spacing={1}>
          <Typography variant="subtitle2" fontSize="0.8rem" fontWeight="bold">
            META подборки
          </Typography>
          <Tabs
            value={contentType}
            onChange={(_, value) => setContentType(value)}
            variant="scrollable"
            scrollButtons="auto"
            sx={{ minHeight: 36 }}
          >
            {META_CONTENT_TYPES.map((option) => (
              <Tab
                key={option.value}
                value={option.value}
                label={option.label}
                sx={{ fontSize: '0.7rem', minHeight: 36 }}
              />
            ))}
          </Tabs>
          {metaQuery.isFetching && <CircularProgress size={20} />}
          {!metaQuery.isFetching && recommendations.length > 0 && (
            <Stack spacing={0.5}>
              {recommendations.map((item) => (
                <Chip
                  key={`${item.weapon_id}-${item.rank}`}
                  label={`${item.weapon_id} • ${item.rank}`}
                  size="small"
                  color={item.rank === 'S' ? 'primary' : 'default'}
                  sx={{ fontSize: '0.7rem', width: 'fit-content' }}
                />
              ))}
            </Stack>
          )}
        </Stack>

        {mods.length > 0 && (
          <>
            <Divider />
            <Stack spacing={1}>
              <Typography variant="subtitle2" fontSize="0.8rem" fontWeight="bold">
                Доступные моды
              </Typography>
              <Stack spacing={0.5}>
                {mods.map((mod: WeaponMod) => (
                  <Chip
                    key={mod.id}
                    label={`${mod.name} • ${mod.type}`}
                    size="small"
                    sx={{ fontSize: '0.7rem', width: '100%' }}
                  />
                ))}
              </Stack>
            </Stack>
          </>
        )}
      </Stack>
    </StatsPanel>
  )

  const centerContent = (
    <Stack spacing={2} sx={{ height: '100%', overflowY: 'auto' }}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Typography variant="h5" fontSize="1.15rem" fontWeight="bold">
          Каталог оружия
        </Typography>
        {weapons.length > 0 && (
          <Chip
            label={`Найдено: ${weapons.length}`}
            size="small"
            color="primary"
            sx={{ fontSize: '0.7rem' }}
          />
        )}
      </Box>

      {catalogQuery.isFetching && (
        <Box display="flex" justifyContent="center" py={4}>
          <CircularProgress size={32} />
        </Box>
      )}

      {!catalogQuery.isFetching && catalogQuery.error && (
        <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
          {(catalogQuery.error as Error).message}
        </Alert>
      )}

      {!catalogQuery.isFetching && !catalogQuery.error && (
        <>
          <Stack spacing={1.5}>
            {weapons.map((weapon) => (
              <WeaponCard key={weapon.id} weapon={weapon} onClick={() => handleWeaponClick(weapon)} />
            ))}
          </Stack>
          {weapons.length === 0 && (
            <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
              Каталог пуст. Попробуйте изменить фильтры.
            </Alert>
          )}
        </>
      )}

      <Button
        variant="outlined"
        size="small"
        onClick={() => catalogQuery.refetch()}
        startIcon={<RefreshIcon />}
        sx={{ alignSelf: 'flex-start', fontSize: '0.75rem' }}
      >
        Обновить каталог
      </Button>
    </Stack>
  )

  const weaponDetails: WeaponDetails | null = weaponDetailsQuery.data ?? null

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh', overflow: 'hidden' }}>
      <Header />
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        {centerContent}
      </GameLayout>
      <WeaponDetailsDialog open={detailsOpen} weapon={weaponDetails} onClose={handleCloseDetails} />
    </Box>
  )
}

export default WeaponsPage





