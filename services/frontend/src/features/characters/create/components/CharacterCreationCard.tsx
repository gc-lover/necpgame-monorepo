import { useEffect, useMemo, useState } from 'react'
import {
  Alert,
  Box,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  SelectChangeEvent,
  Slider,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import {
  useCreateCharacter,
  useGetCharacterClasses,
  useGetCharacterOrigins,
} from '@/api/generated/auth/characters/characters'
import {
  useGetCities,
  useGetFactions,
} from '@/api/generated/auth/reference-data/reference-data'
import { CreateCharacterRequest } from '@/api/generated/auth/models'
import { getListCharactersQueryKey } from '@/api/generated/auth/characters/characters'
import { MUIToggleButtonGroup } from '@/components/ui/MUIToggleButtonGroup'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { CompactCard } from '@/shared/ui/cards/CompactCard'
import { useQueryClient } from '@tanstack/react-query'

const genderOptions = [
  { value: 'male', label: 'Мужской' },
  { value: 'female', label: 'Женский' },
  { value: 'other', label: 'Другое' },
]

const bodyTypeOptions = [
  { value: 'thin', label: 'Худощавое' },
  { value: 'normal', label: 'Обычное' },
  { value: 'muscular', label: 'Мускулистое' },
  { value: 'large', label: 'Плотное' },
]

const colorOptions = [
  'black',
  'brown',
  'blonde',
  'red',
  'white',
  'blue',
  'green',
  'hazel',
  'amber',
  'gray',
]

interface CharacterCreationCardProps {
  onCreated?: (characterId?: string) => void
}

export function CharacterCreationCard({ onCreated }: CharacterCreationCardProps) {
  const queryClient = useQueryClient()
  const classesQuery = useGetCharacterClasses()
  const originsQuery = useGetCharacterOrigins()
  const factionsQuery = useGetFactions({})
  const citiesQuery = useGetCities({})
  const createMutation = useCreateCharacter()

  const classes = classesQuery.data?.classes ?? []
  const origins = originsQuery.data?.origins ?? []
  const factions = factionsQuery.data?.factions ?? []
  const cities = citiesQuery.data?.cities ?? []

  const [form, setForm] = useState({
    name: '',
    classId: '',
    subclassId: '',
    originId: '',
    factionId: '',
    cityId: '',
    gender: 'male',
    appearance: {
      height: 180,
      bodyType: 'normal',
      hairColor: 'black',
      eyeColor: 'brown',
      skinColor: 'light',
      distinctiveFeatures: '',
    },
  })
  const [feedback, setFeedback] = useState<{ type: 'success' | 'error'; message: string } | null>(null)

  useEffect(() => {
    if (classes.length && !form.classId) {
      setForm((prev) => ({
        ...prev,
        classId: classes[0].id ?? '',
        subclassId: classes[0].subclasses?.[0]?.id ?? '',
      }))
    }
  }, [classes, form.classId])

  useEffect(() => {
    if (origins.length && !form.originId) {
      setForm((prev) => ({
        ...prev,
        originId: origins[0].id ?? '',
      }))
    }
  }, [origins, form.originId])

  useEffect(() => {
    if (cities.length && !form.cityId) {
      setForm((prev) => ({
        ...prev,
        cityId: cities[0].id ?? '',
      }))
    }
  }, [cities, form.cityId])

  const originFactions = useMemo(() => {
    if (!form.originId) return factions
    const origin = origins.find((item) => item.id === form.originId)
    if (!origin) return factions
    return factions.filter((faction) => origin.available_factions?.includes(faction.id ?? ''))
  }, [factions, form.originId, origins])

  const availableCities = useMemo(() => {
    if (!form.factionId) return cities
    return cities.filter((city) => city.available_for_factions?.includes(form.factionId))
  }, [cities, form.factionId])

  const currentClass = classes.find((item) => item.id === form.classId)

  const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setForm((prev) => ({ ...prev, name: event.target.value }))
  }

  const handleClassChange = (value: string) => {
    const matchedClass = classes.find((item) => item.id === value)
    setForm((prev) => ({
      ...prev,
      classId: value,
      subclassId: matchedClass?.subclasses?.[0]?.id ?? '',
    }))
  }

  const handleSubclassChange = (value: string) => {
    setForm((prev) => ({
      ...prev,
      subclassId: value,
    }))
  }

  const handleOriginChange = (value: string) => {
    setForm((prev) => ({
      ...prev,
      originId: value,
      factionId: '',
    }))
  }

  const handleFactionChange = (value: string) => {
    const nextCities = value
      ? cities.filter((city) => city.available_for_factions?.includes(value))
      : cities
    setForm((prev) => ({
      ...prev,
      factionId: value,
      cityId:
        nextCities.find((city) => city.id === prev.cityId)?.id ?? nextCities[0]?.id ?? prev.cityId,
    }))
  }

  const handleCityChange = (value: string) => {
    setForm((prev) => ({
      ...prev,
      cityId: value,
    }))
  }

  const updateAppearance = (field: keyof typeof form.appearance, value: string | number) => {
    setForm((prev) => ({
      ...prev,
      appearance: {
        ...prev.appearance,
        [field]: value,
      },
    }))
  }

  const isValid =
    form.name.trim().length >= 3 &&
    form.classId &&
    form.originId &&
    form.cityId &&
    form.gender &&
    form.appearance.hairColor &&
    form.appearance.eyeColor &&
    form.appearance.skinColor

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault()
    setFeedback(null)
    if (!isValid) {
      setFeedback({ type: 'error', message: 'Заполните обязательные поля' })
      return
    }
    const payload: CreateCharacterRequest = {
      name: form.name.trim(),
      class: (currentClass?.name ?? 'Solo') as CreateCharacterRequest['class'],
      subclass: form.subclassId || null,
      gender: form.gender as CreateCharacterRequest['gender'],
      origin: form.originId as CreateCharacterRequest['origin'],
      faction_id: form.factionId || null,
      city_id: form.cityId,
      appearance: {
        height: form.appearance.height,
        body_type: form.appearance.bodyType as CreateCharacterRequest['appearance']['body_type'],
        hair_color: form.appearance.hairColor,
        eye_color: form.appearance.eyeColor,
        skin_color: form.appearance.skinColor,
        distinctive_features: form.appearance.distinctiveFeatures || null,
      },
    }
    createMutation.mutate(
      { data: payload },
      {
        onSuccess: (data) => {
          setFeedback({ type: 'success', message: `Персонаж ${data.character?.name ?? ''} создан` })
          queryClient.invalidateQueries({ queryKey: getListCharactersQueryKey() })
          onCreated?.(data.character?.id ?? undefined)
          setForm((prev) => ({
            ...prev,
            name: '',
          }))
        },
        onError: (error) => {
          const apiMessage = error.response?.data && 'message' in error.response.data
            ? String((error.response.data as Record<string, unknown>).message)
            : null
          setFeedback({
            type: 'error',
            message: apiMessage || 'Создание персонажа не удалось',
          })
        },
      }
    )
  }

  const isLoadingReference =
    classesQuery.isLoading || originsQuery.isLoading || factionsQuery.isLoading || citiesQuery.isLoading

  return (
    <CompactCard
      color="pink"
      glowIntensity="strong"
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
      }}
    >
      <Stack spacing={2} component="form" onSubmit={handleSubmit} sx={{ flex: 1, minHeight: 0 }}>
        <Box>
          <Typography variant="h6" fontSize="0.9rem" textTransform="uppercase">
            Создание персонажа
          </Typography>
          <Typography variant="body2" color="text.secondary" fontSize="0.75rem">
            Выберите класс, происхождение и стартовую точку
          </Typography>
        </Box>
        {feedback && (
          <Alert severity={feedback.type} variant="outlined">
            {feedback.message}
          </Alert>
        )}
        {isLoadingReference ? (
          <Typography variant="body2" color="text.secondary">
            Загрузка справочных данных...
          </Typography>
        ) : (
          <Stack spacing={1.5} sx={{ flex: 1, minHeight: 0 }}>
          <TextField
            label="Имя персонажа"
            value={form.name}
            onChange={handleNameChange}
            size="small"
            fullWidth
            helperText="3-20 символов"
          />
          <ReferenceSelectors
            classes={classes}
            origins={origins}
            originFactions={originFactions}
            availableCities={availableCities}
            classId={form.classId}
            subclassId={form.subclassId}
            originId={form.originId}
            factionId={form.factionId}
            cityId={form.cityId}
            onClassChange={handleClassChange}
            onSubclassChange={handleSubclassChange}
            onOriginChange={handleOriginChange}
            onFactionChange={handleFactionChange}
            onCityChange={handleCityChange}
          />
            <MUIToggleButtonGroup
              value={form.gender}
              onChange={(_, value) => value && setForm((prev) => ({ ...prev, gender: value }))}
              options={genderOptions}
              label="Пол"
              size="small"
            />
          <AppearanceFields
            height={form.appearance.height}
            bodyType={form.appearance.bodyType}
            hairColor={form.appearance.hairColor}
            eyeColor={form.appearance.eyeColor}
            skinColor={form.appearance.skinColor}
            distinctiveFeatures={form.appearance.distinctiveFeatures ?? ''}
            onHeightChange={(value) => updateAppearance('height', value)}
            onBodyTypeChange={(value) => updateAppearance('bodyType', value)}
            onHairColorChange={(value) => updateAppearance('hairColor', value)}
            onEyeColorChange={(value) => updateAppearance('eyeColor', value)}
            onSkinColorChange={(value) => updateAppearance('skinColor', value)}
            onFeaturesChange={(value) => updateAppearance('distinctiveFeatures', value)}
          />
            <CyberpunkButton
              type="submit"
              variant="success"
              size="large"
              disabled={!isValid || createMutation.isPending}
            >
              {createMutation.isPending ? 'Создание...' : 'Создать персонажа'}
            </CyberpunkButton>
          </Stack>
        )}
      </Stack>
    </CompactCard>
  )
}

