/**
 * Страница инвентаря
 * 
 * SPA архитектура с компактной 3-колоночной сеткой:
 * - Левая панель: Фильтры (категории, вес)
 * - Центр: Сетка предметов
 * - Правая панель: Слоты экипировки
 */
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Box,
  CircularProgress,
  Alert,
  Typography,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Paper,
  Stack,
  LinearProgress,
} from '@mui/material'
import AllInclusiveIcon from '@mui/icons-material/AllInclusive'
import CategoryIcon from '@mui/icons-material/Category'
import {
  useGetInventory,
  useGetEquipment,
  useEquipItem,
  useUnequipItem,
  useUseItem,
  useDropItem,
} from '@/api/generated/inventory/inventory/inventory'
import { InventoryGrid, EquipmentSlotDisplay } from '../components'
import { useGameState } from '@/features/game/hooks/useGameState'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import type { ItemCategory, InventoryItem } from '@/api/generated/inventory/models'

export function InventoryPage() {
  const navigate = useNavigate()
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)
  const [categoryFilter, setCategoryFilter] = useState<ItemCategory | undefined>(undefined)

  // Загрузка инвентаря из OpenAPI
  const {
    data: inventoryData,
    isLoading: isLoadingInventory,
    error: inventoryError,
  } = useGetInventory(
    { characterId: selectedCharacterId || '', category: categoryFilter },
    { query: { enabled: !!selectedCharacterId } }
  )

  // Загрузка экипировки из OpenAPI
  const {
    data: equipmentData,
    isLoading: isLoadingEquipment,
  } = useGetEquipment(
    { characterId: selectedCharacterId || '' },
    { query: { enabled: !!selectedCharacterId } }
  )

  // Mutations
  const { mutate: equipItem } = useEquipItem()
  const { mutate: unequipItem } = useUnequipItem()
  const { mutate: useItem } = useUseItem()
  const { mutate: dropItem } = useDropItem()

  useEffect(() => {
    if (!selectedCharacterId) {
      navigate('/characters')
    }
  }, [selectedCharacterId, navigate])

  const handleEquip = (item: InventoryItem) => {
    if (!selectedCharacterId) return
    console.log('Equip item:', item)
    equipItem(
      { data: { characterId: selectedCharacterId, itemId: item.id, slotType: 'weapon_primary' } },
      {
        onSuccess: () => console.log('Equipped!'),
        onError: (err) => console.error('Equip error:', err),
      }
    )
  }

  const handleUse = (item: InventoryItem) => {
    if (!selectedCharacterId) return
    console.log('Use item:', item)
    useItem(
      { data: { characterId: selectedCharacterId, itemId: item.id } },
      {
        onSuccess: () => console.log('Used!'),
        onError: (err) => console.error('Use error:', err),
      }
    )
  }

  const handleDrop = (item: InventoryItem) => {
    if (!selectedCharacterId || item.questItem) return
    console.log('Drop item:', item)
    dropItem(
      { characterId: selectedCharacterId, itemId: item.id },
      {
        onSuccess: () => console.log('Dropped!'),
        onError: (err) => console.error('Drop error:', err),
      }
    )
  }

  const handleUnequip = (slotType: string) => {
    if (!selectedCharacterId) return
    console.log('Unequip slot:', slotType)
    unequipItem(
      { data: { characterId: selectedCharacterId, slotType: slotType as any } },
      {
        onSuccess: () => console.log('Unequipped!'),
        onError: (err) => console.error('Unequip error:', err),
      }
    )
  }

  // Левая панель - Категории
  const leftPanel = (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, height: '100%', minHeight: 0 }}>
      <Typography
        variant="h6"
        sx={{
          color: 'primary.main',
          textShadow: '0 0 8px currentColor',
          fontWeight: 'bold',
          fontSize: '0.875rem',
          textTransform: 'uppercase',
          letterSpacing: '0.1em',
        }}
      >
        Категории
      </Typography>

      <List dense>
        <ListItem disablePadding>
          <ListItemButton
            selected={!categoryFilter}
            onClick={() => setCategoryFilter(undefined)}
            sx={{
              borderRadius: 1,
              mb: 0.5,
              '&.Mui-selected': {
                bgcolor: 'rgba(0, 247, 255, 0.15)',
                borderLeft: '3px solid',
                borderColor: 'primary.main',
              },
            }}
          >
            <ListItemText
              primary={
                <Stack direction="row" spacing={0.5} alignItems="center">
                  <AllInclusiveIcon sx={{ fontSize: '1rem' }} />
                  <span style={{ fontSize: '0.875rem' }}>Все</span>
                </Stack>
              }
              secondary={inventoryData?.items?.length || 0}
              secondaryTypographyProps={{ fontSize: '0.7rem' }}
            />
          </ListItemButton>
        </ListItem>

        {inventoryData?.categories && Object.keys(inventoryData.categories).map((cat) => (
          <ListItem key={cat} disablePadding>
            <ListItemButton
              selected={categoryFilter === cat}
              onClick={() => setCategoryFilter(cat as ItemCategory)}
              sx={{
                borderRadius: 1,
                mb: 0.5,
                '&.Mui-selected': {
                  bgcolor: 'rgba(0, 247, 255, 0.15)',
                  borderLeft: '3px solid',
                  borderColor: 'primary.main',
                },
              }}
            >
              <ListItemText
                primary={
                  <Stack direction="row" spacing={0.5} alignItems="center">
                    <CategoryIcon sx={{ fontSize: '1rem' }} />
                    <span style={{ fontSize: '0.875rem' }}>{cat}</span>
                  </Stack>
                }
                secondary={inventoryData.categories[cat] || 0}
                secondaryTypographyProps={{ fontSize: '0.7rem' }}
              />
            </ListItemButton>
          </ListItem>
        ))}
      </List>

      {/* Вес */}
      {inventoryData && (
        <Paper elevation={2} sx={{ p: 1.5, mt: 'auto' }}>
          <Typography variant="caption" sx={{ fontSize: '0.7rem', display: 'block', mb: 0.5 }}>
            Вес: {inventoryData.currentWeight}кг / {inventoryData.maxWeight}кг
          </Typography>
          <LinearProgress
            variant="determinate"
            value={(inventoryData.currentWeight / inventoryData.maxWeight) * 100}
            sx={{ height: 6, borderRadius: 1 }}
            color={inventoryData.currentWeight >= inventoryData.maxWeight ? 'error' : 'primary'}
          />
        </Paper>
      )}
    </Box>
  )

  // Правая панель - Экипировка
  const rightPanel = (
    <StatsPanel>
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1.5, height: '100%', minHeight: 0, overflowY: 'auto' }}>
        <Typography
          variant="subtitle2"
          sx={{
            color: 'primary.main',
            fontSize: '0.75rem',
            textTransform: 'uppercase',
            mb: 0.5,
          }}
        >
          Экипировка
        </Typography>

        {isLoadingEquipment ? (
          <CircularProgress size={30} />
        ) : equipmentData?.slots ? (
          equipmentData.slots.map((slot) => (
            <EquipmentSlotDisplay
              key={slot.slotType}
              slot={slot}
              onUnequip={!slot.isEmpty ? () => handleUnequip(slot.slotType) : undefined}
            />
          ))
        ) : (
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
            Нет экипировки
          </Typography>
        )}

        {/* Суммарные бонусы */}
        {equipmentData?.totalBonuses && Object.keys(equipmentData.totalBonuses).length > 0 && (
          <Paper elevation={2} sx={{ p: 1.5, mt: 'auto', bgcolor: 'rgba(76, 175, 80, 0.1)' }}>
            <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'success.main', display: 'block', mb: 0.5 }}>
              Суммарные бонусы:
            </Typography>
            {Object.entries(equipmentData.totalBonuses).map(([stat, value]) => (
              <Typography key={stat} variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary', display: 'block' }}>
                +{value} {stat}
              </Typography>
            ))}
          </Paper>
        )}
      </Box>
    </StatsPanel>
  )

  if (isLoadingInventory || isLoadingEquipment) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1 }}>
          <CircularProgress size={60} />
        </Box>
      </Box>
    )
  }

  if (inventoryError) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1, p: 3 }}>
          <Alert severity="error" sx={{ maxWidth: 600 }}>
            <Typography variant="h6">Ошибка загрузки инвентаря</Typography>
            <Typography variant="body2">
              {(inventoryError as unknown as Error)?.message || 'Не удалось загрузить инвентарь'}
            </Typography>
          </Alert>
        </Box>
      </Box>
    )
  }

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      <Header />
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2, minHeight: 0, overflowY: 'auto', p: 2 }}>
          <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 'bold', fontSize: '1.1rem' }}>
            Инвентарь
          </Typography>
          <InventoryGrid
            items={inventoryData?.items || []}
            onEquip={handleEquip}
            onUse={handleUse}
            onDrop={handleDrop}
          />
        </Box>
      </GameLayout>
    </Box>
  )
}

