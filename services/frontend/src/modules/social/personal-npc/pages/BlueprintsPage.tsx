import { useMemo } from 'react'
import {
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Divider,
  Grid,
  MenuItem,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { ScenarioCategoryParameter } from '@/api/generated/social/personal-npc-scenarios/models/scenarioCategoryParameter'
import { ScenarioStatusParameter } from '@/api/generated/social/personal-npc-scenarios/models/scenarioStatusParameter'
import { usePersonalNpcQueries } from '@/modules/social/personal-npc/hooks/usePersonalNpcQueries'
import { usePersonalNpcStore } from '@/modules/social/personal-npc/state/usePersonalNpcStore'

const categories = Object.values(ScenarioCategoryParameter)
const statuses = Object.values(ScenarioStatusParameter)
export const BlueprintsPage = () => {
  const { listQuery } = usePersonalNpcQueries()
  const { filters, pagination, setFilter, setPagination, resetFilters, selectBlueprint } =
    usePersonalNpcStore((state) => ({
      filters: state.filters,
      pagination: state.pagination,
      setFilter: state.setFilter,
      setPagination: state.setPagination,
      resetFilters: state.resetFilters,
      selectBlueprint: state.selectBlueprint,
    }))

  const blueprints = listQuery.data?.data ?? []
  const meta = listQuery.data?.meta
  const isLoading = listQuery.isLoading
  const isRefetching = listQuery.isFetching && !isLoading

  const filteredBlueprints = useMemo(() => {
    if (!filters.search) {
      return blueprints
    }
    const searchValue = filters.search.toLowerCase()
    return blueprints.filter(
      (blueprint) =>
        blueprint.name.toLowerCase().includes(searchValue) ||
        blueprint.description?.toLowerCase().includes(searchValue)
    )
  }, [blueprints, filters.search])

  const totalItems = filters.search ? filteredBlueprints.length : meta?.total ?? blueprints.length

  const pageCount = useMemo(() => {
    if (!meta) {
      return 1
    }
    return Math.max(1, Math.ceil(meta.total / meta.pageSize))
  }, [meta])

  return (
    <Stack spacing={3} sx={{ px: 4, py: 3 }}>
      <Stack direction="row" alignItems="center" justifyContent="space-between">
        <Box>
          <Typography variant="h4" color="text.primary">
            Персональные NPC сценарии
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            Управление блупринтами, публикацией и запуском персональных сценариев NPC
          </Typography>
        </Box>
        <Stack direction="row" spacing={2}>
          <Button variant="outlined" onClick={resetFilters} disabled={isLoading}>
            Сбросить фильтры
          </Button>
          <Button variant="contained" color="primary" disabled={isLoading} onClick={() => selectBlueprint(undefined)}>
            Новый блупринт
          </Button>
        </Stack>
      </Stack>

      <Card variant="outlined">
        <CardContent>
          <Grid container spacing={2}>
            <Grid item xs={12} md={3}>
              <TextField
                fullWidth
                size="small"
                label="Владелец"
                value={filters.ownerId ?? ''}
                onChange={(event) => {
                  const value = event.target.value.trim()
                  setFilter('ownerId', value.length > 0 ? value : undefined)
                }}
              />
            </Grid>
            <Grid item xs={12} md={3}>
              <TextField
                select
                fullWidth
                size="small"
                label="Категория"
                value={filters.category ?? 'all'}
                onChange={(event) => {
                  const value = event.target.value
                  setFilter('category', value === 'all' ? undefined : (value as typeof categories[number]))
                }}
              >
                <MenuItem value="all">Все категории</MenuItem>
                {categories.map((category) => (
                  <MenuItem key={category} value={category}>
                    {category}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} md={3}>
              <TextField
                select
                fullWidth
                size="small"
                label="Статус сценария"
                value={filters.scenarioStatus ?? 'all'}
                onChange={(event) => {
                  const value = event.target.value
                  setFilter(
                    'scenarioStatus',
                    value === 'all' ? undefined : (value as typeof statuses[number])
                  )
                }}
              >
                <MenuItem value="all">Все статусы</MenuItem>
                {statuses.map((status) => (
                  <MenuItem key={status} value={status}>
                    {status}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} md={3}>
              <TextField
                select
                fullWidth
                size="small"
                label="Публичность"
                value={
                  typeof filters.isPublic === 'boolean'
                    ? filters.isPublic
                      ? 'true'
                      : 'false'
                    : 'all'
                }
                onChange={(event) => {
                  const value = event.target.value
                  if (value === 'all') {
                    setFilter('isPublic', undefined)
                  } else {
                    setFilter('isPublic', value === 'true')
                  }
                }}
              >
                <MenuItem value="all">Все блупринты</MenuItem>
                <MenuItem value="true">Публичные</MenuItem>
                <MenuItem value="false">Приватные</MenuItem>
              </TextField>
            </Grid>
            <Grid item xs={12} md={3}>
              <TextField
                fullWidth
                size="small"
                type="number"
                inputProps={{ min: 1 }}
                label="Страница"
                value={pagination.page}
                onChange={(event) => {
                  const value = Number(event.target.value)
                  setPagination({ page: Number.isNaN(value) ? 1 : Math.max(1, value) })
                }}
              />
            </Grid>
            <Grid item xs={12} md={3}>
              <TextField
                fullWidth
                size="small"
                type="number"
                inputProps={{ min: 1, max: 100 }}
                label="На странице"
                value={pagination.pageSize}
                onChange={(event) => {
                  const value = Number(event.target.value)
                  const pageSize = Number.isNaN(value) ? 20 : Math.min(Math.max(1, value), 100)
                  setPagination({ pageSize })
                }}
              />
            </Grid>
            <Grid item xs={12} md={3}>
              <TextField
                fullWidth
                size="small"
                label="Поиск по названию"
                value={filters.search ?? ''}
                onChange={(event) => {
                  const value = event.target.value.trim()
                  setFilter('search', value.length > 0 ? value : undefined)
                }}
              />
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Stack direction="row" alignItems="center" justifyContent="space-between">
              <Typography variant="h6" color="text.primary">
                Найдено {totalItems} блупринтов
              </Typography>
              {isRefetching && <CircularProgress size={20} />}
            </Stack>

            <Divider />

            {isLoading ? (
              <Stack alignItems="center" justifyContent="center" sx={{ py: 6 }}>
                <CircularProgress />
              </Stack>
            ) : (
              <Stack spacing={2}>
                {filteredBlueprints.map((blueprint) => (
                  <Card
                    variant="outlined"
                    key={blueprint.id}
                    sx={{
                      borderColor: 'divider',
                      backgroundColor: 'background.default',
                    }}
                  >
                    <CardContent>
                      <Stack spacing={1.5}>
                        <Stack direction="row" justifyContent="space-between" alignItems="flex-start">
                          <Box>
                            <Typography variant="h6" color="text.primary">
                              {blueprint.name}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                              {blueprint.description}
                            </Typography>
                          </Box>
                          <Stack direction="row" spacing={1}>
                            <Button
                              variant="text"
                              onClick={() => selectBlueprint(blueprint.id)}
                              href={`/game/personal-npc-scenarios/${blueprint.id}`}
                            >
                              Открыть
                            </Button>
                            <Button
                              variant="outlined"
                              href={`/game/personal-npc-scenarios/${blueprint.id}/execute`}
                            >
                              Запустить
                            </Button>
                          </Stack>
                        </Stack>
                        <Stack direction="row" spacing={2} flexWrap="wrap">
                          <Typography variant="body2" color="text.secondary">
                            Категория: {blueprint.category}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            Версия: {blueprint.version ?? '1.0.0'}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            Цена: {blueprint.price ?? 0}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            Доступ: {blueprint.isPublic ? 'Публичный' : 'Приватный'}
                          </Typography>
                        </Stack>
                        <Typography variant="caption" color="text.secondary">
                          Автор: {blueprint.authorId} • Обновлен:{' '}
                          {blueprint.updatedAt?.toString() ?? '—'}
                        </Typography>
                      </Stack>
                    </CardContent>
                  </Card>
                ))}

                {filteredBlueprints.length === 0 && (
                  <Stack alignItems="center" justifyContent="center" sx={{ py: 6 }}>
                    <Typography variant="body1" color="text.secondary">
                      Сценарии не найдены. Измените фильтры или создайте новый блупринт.
                    </Typography>
                  </Stack>
                )}
              </Stack>
            )}

            <Divider />

            <Stack direction="row" justifyContent="space-between" alignItems="center">
              <Typography variant="body2" color="text.secondary">
                Страница {pagination.page} из {pageCount}
              </Typography>
              <Stack direction="row" spacing={1}>
                <Button
                  variant="outlined"
                  disabled={pagination.page <= 1 || isLoading}
                  onClick={() => setPagination({ page: Math.max(1, pagination.page - 1) })}
                >
                  Назад
                </Button>
                <Button
                  variant="outlined"
                  disabled={pagination.page >= pageCount || isLoading}
                  onClick={() =>
                    setPagination({ page: Math.min(pageCount, pagination.page + 1) })
                  }
                >
                  Вперёд
                </Button>
              </Stack>
            </Stack>
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )
}

