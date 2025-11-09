import { useMemo, useState } from 'react'
import {
  Alert,
  Card,
  CardContent,
  Chip,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import {
  useListAchievements,
  useGetAchievement,
} from '@/api/generated/progression/achievement-system/achievements/achievements'
import {
  ListAchievementsCategory,
  ListAchievementsRarity,
} from '@/api/generated/progression/achievement-system/models'

const categories = Object.values(ListAchievementsCategory)
const rarities = Object.values(ListAchievementsRarity)

export function AchievementCatalogPanel() {
  const [category, setCategory] = useState<string>('')
  const [rarity, setRarity] = useState<string>('')
  const [selectedId, setSelectedId] = useState<string | null>(null)
  const [limit, setLimit] = useState(20)

  const achievementsQuery = useListAchievements(
    {
      category: category || undefined,
      rarity: rarity || undefined,
      limit,
    },
    {}
  )

  const detailQuery = useGetAchievement(selectedId ?? '', {
    query: { enabled: Boolean(selectedId) },
  })

  const achievements = useMemo(
    () => achievementsQuery.data?.data.items ?? [],
    [achievementsQuery.data]
  )

  return (
    <Stack spacing={3}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
        <FormControl size="small" sx={{ minWidth: 160 }}>
          <InputLabel id="achievement-category">Категория</InputLabel>
          <Select
            labelId="achievement-category"
            label="Категория"
            value={category}
            onChange={(event) => setCategory(event.target.value)}
          >
            <MenuItem value="">Все</MenuItem>
            {categories.map((value) => (
              <MenuItem key={value} value={value}>
                {value}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <FormControl size="small" sx={{ minWidth: 160 }}>
          <InputLabel id="achievement-rarity">Редкость</InputLabel>
          <Select
            labelId="achievement-rarity"
            label="Редкость"
            value={rarity}
            onChange={(event) => setRarity(event.target.value)}
          >
            <MenuItem value="">Все</MenuItem>
            {rarities.map((value) => (
              <MenuItem key={value} value={value}>
                {value}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <TextField
          label="limit"
          type="number"
          size="small"
          value={limit}
          onChange={(event) => setLimit(Number(event.target.value))}
        />
        <Chip
          label={`Всего: ${achievementsQuery.data?.data.total ?? 0}`}
          color="primary"
          variant="outlined"
        />
      </Stack>

      {achievementsQuery.isError ? (
        <Alert severity="error">Не удалось загрузить список достижений.</Alert>
      ) : (
        <Stack spacing={2}>
          {achievements.map((achievement) => (
            <Card
              key={achievement.id}
              variant={achievement.id === selectedId ? 'elevation' : 'outlined'}
              sx={{ cursor: 'pointer' }}
              onClick={() => setSelectedId(achievement.id)}
            >
              <CardContent>
                <Stack spacing={1}>
                  <Stack direction="row" spacing={1} alignItems="center">
                    <Typography variant="h6">{achievement.name}</Typography>
                    <Chip label={achievement.category} color="secondary" variant="outlined" />
                    <Chip
                      label={achievement.rarity}
                      color={achievement.rarity === 'LEGENDARY' ? 'warning' : 'default'}
                      variant="outlined"
                    />
                  </Stack>
                  <Typography variant="body2" color="text.secondary">
                    {achievement.description}
                  </Typography>
                </Stack>
              </CardContent>
            </Card>
          ))}

          {!achievements.length && (
            <Typography variant="body2" color="text.secondary">
              Нет достижений для выбранных фильтров.
            </Typography>
          )}
        </Stack>
      )}

      {selectedId && detailQuery.data && (
        <Card variant="outlined">
          <CardContent>
            <Stack spacing={1}>
              <Typography variant="h6">Детали: {detailQuery.data.data.name}</Typography>
              <Typography variant="body2" color="text.secondary">
                Требования: {detailQuery.data.data.requirements?.map((req) => req.description).join(', ') || '—'}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Награды: {detailQuery.data.data.rewards?.items?.length ? 'предметы' : '—'}
              </Typography>
              {detailQuery.data.data.rewards?.titles?.length ? (
                <Typography variant="body2">
                  Титулы: {detailQuery.data.data.rewards.titles.join(', ')}
                </Typography>
              ) : null}
            </Stack>
          </CardContent>
        </Card>
      )}
    </Stack>
  )
}


