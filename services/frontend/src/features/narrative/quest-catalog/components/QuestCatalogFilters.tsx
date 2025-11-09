import { Stack, TextField, Typography } from '@mui/material'
import type { ChangeEvent } from 'react'
import type { GetQuestCatalogParams } from '@/api/generated/narrative/quest-catalog/models'
import {
  GetQuestCatalogType,
  GetQuestCatalogPeriod,
  GetQuestCatalogDifficulty,
} from '@/api/generated/narrative/quest-catalog/models'

interface QuestCatalogFiltersProps {
  filters: GetQuestCatalogParams
  onChange: (filters: GetQuestCatalogParams) => void
}

const typeOptions = Object.values(GetQuestCatalogType)
const periodOptions = Object.values(GetQuestCatalogPeriod)
const difficultyOptions = Object.values(GetQuestCatalogDifficulty)

export const QuestCatalogFilters = ({ filters, onChange }: QuestCatalogFiltersProps) => {
  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value, type } = event.target
    if (type === 'number') {
      onChange({ ...filters, [name]: value ? Number(value) : undefined })
    } else if (type === 'checkbox') {
      onChange({ ...filters, [name]: event.target.checked })
    } else {
      onChange({ ...filters, [name]: value || undefined })
    }
  }

  return (
    <Stack spacing={1}>
      <Typography variant="subtitle2" fontWeight={600}>
        Фильтры каталога
      </Typography>
      <TextField
        label="Тип"
        select
        size="small"
        name="type"
        SelectProps={{ native: true }}
        value={filters.type ?? ''}
        onChange={handleChange}
      >
        <option value="">Все</option>
        {typeOptions.map(type => (
          <option key={type} value={type}>
            {type}
          </option>
        ))}
      </TextField>
      <TextField
        label="Период"
        select
        size="small"
        name="period"
        SelectProps={{ native: true }}
        value={filters.period ?? ''}
        onChange={handleChange}
      >
        <option value="">Все</option>
        {periodOptions.map(period => (
          <option key={period} value={period}>
            {period}
          </option>
        ))}
      </TextField>
      <TextField
        label="Сложность"
        select
        size="small"
        name="difficulty"
        SelectProps={{ native: true }}
        value={filters.difficulty ?? ''}
        onChange={handleChange}
      >
        <option value="">Все</option>
        {difficultyOptions.map(difficulty => (
          <option key={difficulty} value={difficulty}>
            {difficulty}
          </option>
        ))}
      </TextField>
      <TextField
        label="Фракция"
        size="small"
        name="faction"
        value={filters.faction ?? ''}
        onChange={handleChange}
      />
      <TextField
        label="Минимальный уровень"
        size="small"
        type="number"
        name="min_level"
        value={filters.min_level ?? ''}
        onChange={handleChange}
      />
      <TextField
        label="Максимальный уровень"
        size="small"
        type="number"
        name="max_level"
        value={filters.max_level ?? ''}
        onChange={handleChange}
      />
      <TextField
        label="Минимальное время (мин)"
        size="small"
        type="number"
        name="estimated_time_min"
        value={filters.estimated_time_min ?? ''}
        onChange={handleChange}
      />
      <TextField
        label="Максимальное время (мин)"
        size="small"
        type="number"
        name="estimated_time_max"
        value={filters.estimated_time_max ?? ''}
        onChange={handleChange}
      />
      <TextField
        label="Романтика"
        size="small"
        select
        name="has_romance"
        SelectProps={{ native: true }}
        value={
          filters.has_romance === undefined ? '' : filters.has_romance ? 'true' : 'false'
        }
        onChange={event =>
          onChange({
            ...filters,
            has_romance:
              event.target.value === ''
                ? undefined
                : (event.target.value as 'true' | 'false') === 'true',
          })
        }
      >
        <option value="">Не важно</option>
        <option value="true">Да</option>
        <option value="false">Нет</option>
      </TextField>
      <TextField
        label="Боевка"
        size="small"
        select
        name="has_combat"
        SelectProps={{ native: true }}
        value={
          filters.has_combat === undefined ? '' : filters.has_combat ? 'true' : 'false'
        }
        onChange={event =>
          onChange({
            ...filters,
            has_combat:
              event.target.value === ''
                ? undefined
                : (event.target.value as 'true' | 'false') === 'true',
          })
        }
      >
        <option value="">Не важно</option>
        <option value="true">Да</option>
        <option value="false">Нет</option>
      </TextField>
    </Stack>
  )
}







