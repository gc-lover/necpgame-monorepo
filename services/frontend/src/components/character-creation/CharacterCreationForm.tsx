import { useState, useEffect } from 'react'
import { useCreateCharacter, useGetCharacterClasses, useGetCharacterOrigins } from '../../api/generated/auth/characters/characters'
import { useGetFactions } from '../../api/generated/auth/reference-data/reference-data'
import { useGetCities } from '../../api/generated/auth/reference-data/reference-data'
import type { GetFactionsOrigin } from '../../api/generated/auth/models'
import type { CreateCharacterRequest } from '../../api/generated/auth/models/character-creation-models/createCharacterRequest'
import type { CreateCharacterRequestClass } from '../../api/generated/auth/models/character-creation-models/createCharacterRequestClass'
import type { CreateCharacterRequestOrigin } from '../../api/generated/auth/models/character-creation-models/createCharacterRequestOrigin'
import type { CreateCharacterRequestGender } from '../../api/generated/auth/models/character-creation-models/createCharacterRequestGender'
import type { GameCharacterAppearance } from '../../api/generated/auth/models/character-creation-reference-models/gameCharacterAppearance'
import { AppearanceForm } from './AppearanceForm'
import { 
  Box, 
  Typography, 
  Alert, 
  Chip,
  Button,
  CircularProgress,
  Tooltip,
  Grid,
} from '@mui/material'
import { Visibility, VisibilityOff } from '@mui/icons-material'

  /**
   * Пропсы компонента CharacterCreationForm
   */
interface CharacterCreationFormProps {
  /** Имя персонажа (управляется из App) */
  name: string
  /** Пол персонажа (управляется из App) */
  gender: 'male' | 'female' | null
  /** Обработчик изменения имени */
  onNameChange: (name: string) => void
  /** Обработчик изменения пола */
  onGenderChange: (gender: 'male' | 'female' | null) => void
  /** Обработчик успешного создания персонажа */
  onSuccess?: () => void
  /** Обработчик отмены создания */
  onCancel?: () => void
  /** Callback для получения функции submit */
  onSubmitRef?: (submitFn: () => void) => void
  /** Callback для получения функции canCreate */
  onCanCreateRef?: (canCreateFn: () => boolean) => void
  /** Callback для получения состояния загрузки */
  onIsPendingRef?: (isPending: boolean) => void
  /** Callback для установки информации о выбранном элементе */
  onSelectedInfoChange?: (info: { title: string; description: string } | null) => void
}

/**
 * Главный компонент формы создания персонажа
 * 
 * Все выборы видны сразу в виде маленьких значков
 * Внешность в отдельной вкладке
 */
export function CharacterCreationForm({ name, gender, onNameChange, onGenderChange, onSuccess, onCancel, onSubmitRef, onCanCreateRef, onIsPendingRef, onSelectedInfoChange }: CharacterCreationFormProps) {
  // Показывать ли вкладку внешности
  const [showAppearance, setShowAppearance] = useState(false)
  
  // Данные формы
  const [selectedClass, setSelectedClass] = useState<string | null>(null)
  const [selectedSubclass, setSelectedSubclass] = useState<string | null>(null)
  const [selectedOrigin, setSelectedOrigin] = useState<string | null>(null)
  const [selectedFaction, setSelectedFaction] = useState<string | null>(null)
  const [selectedCity, setSelectedCity] = useState<string | null>(null)
  const [appearance, setAppearance] = useState<GameCharacterAppearance>({
    height: 180,
    body_type: 'normal',
    hair_color: 'black',
    eye_color: 'brown',
    skin_color: 'light',
    distinctive_features: null,
  })
  
  // Состояние для отображения информации о выбранном элементе
  const [selectedInfo, setSelectedInfo] = useState<{
    title: string
    description: string
  } | null>(null)
  
  // Хук для создания персонажа
  const { mutate: createCharacter, isPending, error } = useCreateCharacter()
  
  // Загружаем данные - убрали зависимости, теперь можно выбирать в любом порядке
  const { data: classesData, isLoading: classesLoading } = useGetCharacterClasses()
  const { data: originsData, isLoading: originsLoading } = useGetCharacterOrigins()
  
  // Загружаем фракции и города для всех возможных комбинаций
  const { data: factionsData, isLoading: factionsLoading } = useGetFactions(undefined)
  const { data: citiesData, isLoading: citiesLoading } = useGetCities(undefined)
  
  // Установка значений по умолчанию при загрузке данных
  useEffect(() => {
    if (classesData?.classes && !selectedClass) {
      // Находим Solo
      const soloClass = classesData.classes.find(cls => cls.name?.toLowerCase() === 'solo')
      if (soloClass) {
        setSelectedClass(soloClass.id)
      }
    }
  }, [classesData, selectedClass])
  
  useEffect(() => {
    if (originsData?.origins && !selectedOrigin) {
      // Находим Уличный бродяга (street_kid)
      const streetKidOrigin = originsData.origins.find(orig => orig.id?.toLowerCase() === 'street_kid')
      if (streetKidOrigin) {
        setSelectedOrigin(streetKidOrigin.id)
      }
    }
  }, [originsData, selectedOrigin])
  
  useEffect(() => {
    // Устанавливаем Без фракции по умолчанию (selectedFaction = null)
    // Город будет установлен после загрузки городов
    if (citiesData?.cities && !selectedCity) {
      // Находим Найтсити (Night City)
      const nightCity = citiesData.cities.find(city => 
        city.name?.toLowerCase().includes('night') || 
        city.name?.toLowerCase().includes('найт') ||
        city.name?.toLowerCase().includes('найтсити')
      )
      if (nightCity) {
        setSelectedCity(nightCity.id)
      }
    }
  }, [citiesData, selectedCity])

  /**
   * Валидация имени персонажа
   */
  const validateName = (value: string): string | null => {
    if (value.length < 3) return 'Имя должно содержать минимум 3 символа'
    if (value.length > 20) return 'Имя не может превышать 20 символов'
    if (!/^[a-zA-Zа-яА-Я0-9\s\-]+$/.test(value)) {
      return 'Имя может содержать только буквы, цифры, пробелы и дефисы'
    }
    return null
  }
  
  /**
   * Проверка готовности к созданию персонажа
   */
  const canCreate = (): boolean => {
    return !!(
      name.trim().length >= 3 && 
      validateName(name) === null &&
      gender !== null && 
      selectedClass !== null && 
      selectedOrigin !== null && 
      selectedCity !== null
    )
  }
  
  /**
   * Отправка формы создания персонажа
   */
  const handleSubmit = () => {
    // Финальная валидация
    if (!name || !gender || !selectedClass || !selectedOrigin || !selectedCity) {
      alert('Пожалуйста, заполните все обязательные поля')
      return
    }
    
    const nameError = validateName(name)
    if (nameError) {
      alert(nameError)
      return
    }
    
    // Проверяем, что данные загружены
    if (!classesData?.classes || classesData.classes.length === 0) {
      alert('Ошибка: данные классов не загружены. Пожалуйста, обновите страницу.')
      return
    }
    
    if (!originsData?.origins || originsData.origins.length === 0) {
      alert('Ошибка: данные происхождений не загружены. Пожалуйста, обновите страницу.')
      return
    }
    
    // Находим класс и происхождение по ID
    const classData = classesData.classes.find((cls) => cls.id === selectedClass)
    const originData = originsData.origins.find((orig) => orig.id === selectedOrigin)
    
    // Проверяем, что все данные найдены
    if (!classData) {
      alert('Ошибка: класс не найден. Пожалуйста, выберите класс снова.')
      return
    }
    
    if (!originData) {
      alert('Ошибка: происхождение не найдено. Пожалуйста, выберите происхождение снова.')
      return
    }
    
    // Преобразуем ID в enum значения
    const classEnum = classData.name as CreateCharacterRequestClass
    const originEnum = originData.id as CreateCharacterRequestOrigin
    
    // Формируем запрос
    const request: CreateCharacterRequest = {
      name: name.trim(),
      class: classEnum,
      subclass: selectedSubclass || null,
      gender: gender as CreateCharacterRequestGender,
      origin: originEnum,
      ...(selectedFaction ? { faction_id: selectedFaction } : {}),
      city_id: selectedCity!,
      appearance,
    }
    
    console.log('📤 Отправка запроса на создание персонажа:', request)
    
    // Отправляем запрос
    createCharacter(
      { data: request },
      {
        onSuccess: (response) => {
          console.log('✓ Персонаж успешно создан:', response.character)
          alert(`Персонаж "${response.character?.name}" успешно создан!`)
          onSuccess?.()
        },
        onError: (err: any) => {
          console.error('✗ Ошибка создания персонажа:', err)
          console.error('✗ Детали ошибки:', JSON.stringify(err.response?.data, null, 2) || err.message)
          console.error('✗ Полный ответ:', err.response)
          console.error('✗ Статус:', err.response?.status)
          console.error('✗ Данные запроса:', JSON.stringify(request, null, 2))
          
          const errorMessage = err.response?.data?.error?.message 
            || err.response?.data?.message 
            || JSON.stringify(err.response?.data)
            || err.message 
            || 'Неизвестная ошибка'
          
          alert(`Ошибка создания персонажа (${err.response?.status || 'N/A'}): ${errorMessage}`)
        },
      }
    )
  }

  // Экспортируем функции через refs после их определения
  useEffect(() => {
    onSubmitRef?.(handleSubmit)
    onCanCreateRef?.(canCreate)
    onIsPendingRef?.(isPending)
  }, [onSubmitRef, onCanCreateRef, onIsPendingRef, isPending, name, gender, selectedClass, selectedOrigin, selectedCity])

  // Иконки для пола
  const getGenderIcon = (g: 'male' | 'female' | null) => {
    switch (g) {
      case 'male': return '👦' // Мужской
      case 'female': return '👩' // Женский
      default: return '👤'
    }
  }

  // Иконки для классов
  const getClassIcon = (className: string | null | undefined) => {
    if (!className) return '⚔️'
    const lowerName = className.toLowerCase()
    switch (lowerName) {
      case 'solo': return '🦾' // Соло
      case 'netrunner': return '👨‍💻' // Нетраннер
      case 'fixer': return '🕵️‍♂️' // Фиксер
      case 'techie': return '🔧'
      case 'medtech': return '⚕️'
      case 'media': return '📰'
      case 'corporate': return '🏢'
      case 'nomad': return '🛣️'
      default: return '⚔️'
    }
  }

  // Иконки для происхождения
  const getOriginIcon = (originId: string | null | undefined) => {
    if (!originId) return '🌆'
    const lowerId = originId.toLowerCase()
    switch (lowerId) {
      case 'street_kid':
      case 'streetkid':
        return '👕' // Уличный бродяга
      case 'corpo': return '🧥' // Корпорат
      case 'nomad': return '🚖' // Кочевник
      default: return '🌆'
    }
  }

  // Иконки для фракций (киберпанк стиль)
  const getFactionIcon = (factionType: string | null | undefined, factionName?: string | null) => {
    if (!factionType && !factionName) return '🏴'
    const lowerType = factionType?.toLowerCase() || ''
    const lowerName = factionName?.toLowerCase() || ''
    
    // Специфичные иконки для известных фракций
    if (lowerName.includes('arasaka') || lowerName.includes('арасака')) return '🔴' // Arasaka
    if (lowerName.includes('militech') || lowerName.includes('милитех')) return '🔫' // Militech
    if (lowerName.includes('valentino') || lowerName.includes('валентино')) return '💀' // Valentinos
    if (lowerName.includes('aldecaldos') || lowerName.includes('альдекальдос')) return '🌄' // Aldecaldos
    if (lowerName.includes('maelstrom') || lowerName.includes('маэлстром')) return '😈' // Банда киборгов
    if (lowerName.includes('tyger') || lowerName.includes('тайгер')) return '🐅' // Банда
    if (lowerName.includes('6th street') || lowerName.includes('6-я улица')) return '🎯' // Банда
    if (lowerName.includes('voodoo') || lowerName.includes('вуду')) return '🎭' // Банда
    if (lowerName.includes('animals') || lowerName.includes('животные')) return '🦁' // Банда
    if (lowerName.includes('scavengers') || lowerName.includes('мародеры')) return '💀' // Банда
    if (lowerName.includes('ncpd') || lowerName.includes('нкпд')) return '👮' // Полиция
    if (lowerName.includes('trauma') || lowerName.includes('травма')) return '🚑' // Медицинская
    if (lowerName.includes('netwatch') || lowerName.includes('нетвотч')) return '🕵️' // Организация
    if (lowerName.includes('wraiths') || lowerName.includes('рейты')) return '👻' // Банда
    
    // Общие иконки по типу
    switch (lowerType) {
      case 'corporation': return '🏢' // Корпорация
      case 'gang': return '⚔️' // Банда
      case 'organization': return '🤝' // Организация
      case 'nomad': return '🏍️' // Номады
      default: return '🏴' // Флаг по умолчанию
    }
  }

  // Флаги стран для городов
  const getCityFlag = (cityName: string | null | undefined, cityRegion?: string | null | undefined) => {
    if (!cityName) return '🏙️'
    const lowerName = cityName.toLowerCase()
    
    // Проверяем конкретные города по имени
    if (lowerName.includes('night') || lowerName.includes('найт') || lowerName.includes('найтсити')) {
      return '🌃' // Night City
    }
    if (lowerName.includes('tokyo') || lowerName.includes('токио') || lowerName.includes('neo-tokyo')) {
      return '⛩' // Tokyo
    }
    
    // Если не найдено по имени, проверяем регион
    if (cityRegion) {
      const upperRegion = cityRegion.toUpperCase()
      switch (upperRegion) {
        case 'US':
        case 'NA':
          return '🌃' // Night City (по умолчанию для US)
        case 'JP':
        case 'AS':
          return '⛩' // Tokyo (по умолчанию для JP)
        case 'EU':
          return '🇪🇺'
        case 'RU':
          return '🇷🇺'
        default:
          return '🏙️'
      }
    }
    
    return '🏙️'
  }

  // Получаем названия выбранных элементов
  const selectedClassData = classesData?.classes?.find((cls) => cls.id === selectedClass)
  const selectedOriginData = originsData?.origins?.find((orig) => orig.id === selectedOrigin)
  const selectedFactionData = factionsData?.factions?.find((faction) => faction.id === selectedFaction)
  const selectedCityData = citiesData?.cities?.find((city) => city.id === selectedCity)
  
  const genderLabels: Record<string, string> = {
    male: 'Мужской',
    female: 'Женский',
  }

  // Показываем все фракции и города - выбор доступен в любом порядке
  const availableFactions = factionsData?.factions || []
  const availableCities = citiesData?.cities || []

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%', gap: 2 }}>
      {/* Заголовок */}
      <Box sx={{ flexShrink: 0 }}>
        <Typography 
          variant="h6" 
          sx={{ 
            mb: 2, 
            color: 'primary.main',
            textShadow: '0 0 15px currentColor',
            fontWeight: 'bold',
            textTransform: 'uppercase',
            letterSpacing: '0.1em',
            fontSize: '0.875rem',
          }}
        >
          Создание персонажа
        </Typography>

        {/* Быстрые выборы - квадратные иконки */}
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1.5, mb: 2 }}>
          {/* Пол */}
          <Box>
            <Typography variant="subtitle2" sx={{ mb: 0.5, color: 'primary.main', fontSize: '0.7rem' }}>
              Пол
            </Typography>
            <Grid container spacing={0.5}>
              {(['male', 'female'] as const).map((g) => (
                <Grid size={{ xs: 2, sm: 1.5, md: 1.5 }} key={g}>
                  <Box
                    sx={{
                      cursor: 'pointer',
                      width: 32,
                      height: 32,
                      border: '2px solid',
                      borderColor: gender === g ? 'primary.main' : 'rgba(255, 255, 255, 0.2)',
                      borderRadius: 1,
                      bgcolor: gender === g ? 'primary.main' : 'rgba(255, 255, 255, 0.1)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      fontSize: '1rem',
                      transition: 'all 0.3s ease',
                      boxShadow: gender === g ? '0 0 10px rgba(0, 247, 255, 0.6)' : 'none',
                      '&:hover': {
                        borderColor: 'primary.main',
                        transform: 'scale(1.1)',
                        boxShadow: '0 0 8px rgba(0, 247, 255, 0.4)',
                      },
                    }}
                    onClick={() => {
                      onGenderChange(g)
                      const info = {
                        title: genderLabels[g],
                        description: g === 'male' ? 'Мужской персонаж' : 'Женский персонаж',
                      }
                      setSelectedInfo(info)
                      onSelectedInfoChange?.(info)
                    }}
                    title={genderLabels[g]}
                  >
                    {getGenderIcon(g)}
                  </Box>
                </Grid>
              ))}
            </Grid>
          </Box>

          {/* Класс */}
          <Box>
            <Typography variant="subtitle2" sx={{ mb: 0.5, color: 'primary.main', fontSize: '0.7rem' }}>
              Класс
            </Typography>
            {classesLoading ? (
              <CircularProgress size={16} />
            ) : (
              <Grid container spacing={0.5}>
                {classesData?.classes?.map((cls) => (
                  <Grid size={{ xs: 2, sm: 1.5, md: 1.5 }} key={cls.id}>
                    <Box
                      sx={{
                        cursor: 'pointer',
                        width: 32,
                        height: 32,
                        border: '2px solid',
                        borderColor: selectedClass === cls.id ? 'primary.main' : 'rgba(255, 255, 255, 0.2)',
                        borderRadius: 1,
                        bgcolor: selectedClass === cls.id ? 'primary.main' : 'rgba(255, 255, 255, 0.1)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '1rem',
                        transition: 'all 0.3s ease',
                        boxShadow: selectedClass === cls.id ? '0 0 10px rgba(0, 247, 255, 0.6)' : 'none',
                        '&:hover': {
                          borderColor: 'primary.main',
                          transform: 'scale(1.1)',
                          boxShadow: '0 0 8px rgba(0, 247, 255, 0.4)',
                        },
                      }}
                      onClick={() => {
                        setSelectedSubclass(null)
                        setSelectedClass(cls.id)
                        const info = {
                          title: cls.name || 'Неизвестный класс',
                          description: cls.description || 'Описание отсутствует',
                        }
                        setSelectedInfo(info)
                        onSelectedInfoChange?.(info)
                      }}
                      title={cls.name}
                    >
                      {getClassIcon(cls.name)}
                    </Box>
                  </Grid>
                ))}
              </Grid>
            )}
          </Box>

          {/* Происхождение */}
          <Box>
            <Typography variant="subtitle2" sx={{ mb: 0.5, color: 'primary.main', fontSize: '0.7rem' }}>
              Происхождение
            </Typography>
            {originsLoading ? (
              <CircularProgress size={16} />
            ) : (
              <Grid container spacing={0.5}>
                {originsData?.origins?.map((origin) => (
                  <Grid size={{ xs: 2, sm: 1.5, md: 1.5 }} key={origin.id}>
                    <Box
                      sx={{
                        cursor: 'pointer',
                        width: 32,
                        height: 32,
                        border: '2px solid',
                        borderColor: selectedOrigin === origin.id ? 'primary.main' : 'rgba(255, 255, 255, 0.2)',
                        borderRadius: 1,
                        bgcolor: selectedOrigin === origin.id ? 'primary.main' : 'rgba(255, 255, 255, 0.1)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '1rem',
                        transition: 'all 0.3s ease',
                        boxShadow: selectedOrigin === origin.id ? '0 0 10px rgba(0, 247, 255, 0.6)' : 'none',
                        '&:hover': {
                          borderColor: 'primary.main',
                          transform: 'scale(1.1)',
                          boxShadow: '0 0 8px rgba(0, 247, 255, 0.4)',
                        },
                      }}
                      onClick={() => {
                        setSelectedOrigin(origin.id)
                        const info = {
                          title: origin.name || 'Неизвестное происхождение',
                          description: origin.description || 'Описание отсутствует',
                        }
                        setSelectedInfo(info)
                        onSelectedInfoChange?.(info)
                      }}
                      title={origin.name}
                    >
                      {getOriginIcon(origin.id)}
                    </Box>
                  </Grid>
                ))}
              </Grid>
            )}
          </Box>

          {/* Фракция */}
          <Box>
            <Typography variant="subtitle2" sx={{ mb: 0.5, color: 'primary.main', fontSize: '0.7rem' }}>
              Фракция
            </Typography>
            {factionsLoading ? (
              <CircularProgress size={16} />
            ) : (
              <Grid container spacing={0.5}>
                <Grid size={{ xs: 2, sm: 1.5, md: 1.5 }}>
                  <Box
                    sx={{
                      cursor: 'pointer',
                      width: 32,
                      height: 32,
                      border: '2px solid',
                      borderColor: selectedFaction === null ? 'primary.main' : 'rgba(255, 255, 255, 0.2)',
                      borderRadius: 1,
                      bgcolor: selectedFaction === null ? 'primary.main' : 'rgba(255, 255, 255, 0.1)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      fontSize: '1rem',
                      transition: 'all 0.3s ease',
                      boxShadow: selectedFaction === null ? '0 0 10px rgba(0, 247, 255, 0.6)' : 'none',
                      '&:hover': {
                        borderColor: 'primary.main',
                        transform: 'scale(1.1)',
                        boxShadow: '0 0 8px rgba(0, 247, 255, 0.4)',
                      },
                    }}
                    onClick={() => {
                      setSelectedFaction(null)
                      const info = {
                        title: 'Без фракции',
                        description: 'Независимый персонаж, не принадлежащий ни к одной фракции',
                      }
                      setSelectedInfo(info)
                      onSelectedInfoChange?.(info)
                    }}
                    title="Без фракции"
                  >
                    🚫
                  </Box>
                </Grid>
                {availableFactions.map((faction) => (
                  <Grid size={{ xs: 2, sm: 1.5, md: 1.5 }} key={faction.id}>
                    <Box
                      sx={{
                        cursor: 'pointer',
                        width: 32,
                        height: 32,
                        border: '2px solid',
                        borderColor: selectedFaction === faction.id ? 'primary.main' : 'rgba(255, 255, 255, 0.2)',
                        borderRadius: 1,
                        bgcolor: selectedFaction === faction.id ? 'primary.main' : 'rgba(255, 255, 255, 0.1)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '1rem',
                        transition: 'all 0.3s ease',
                        boxShadow: selectedFaction === faction.id ? '0 0 10px rgba(0, 247, 255, 0.6)' : 'none',
                        '&:hover': {
                          borderColor: 'primary.main',
                          transform: 'scale(1.1)',
                          boxShadow: '0 0 8px rgba(0, 247, 255, 0.4)',
                        },
                      }}
                      onClick={() => {
                        setSelectedFaction(faction.id)
                        const info = {
                          title: faction.name || 'Неизвестная фракция',
                          description: faction.description || 'Описание отсутствует',
                        }
                        setSelectedInfo(info)
                        onSelectedInfoChange?.(info)
                      }}
                      title={faction.name}
                    >
                      {getFactionIcon(faction.type, faction.name)}
                    </Box>
                  </Grid>
                ))}
              </Grid>
            )}
          </Box>

          {/* Город */}
          <Box>
            <Typography variant="subtitle2" sx={{ mb: 0.5, color: 'primary.main', fontSize: '0.7rem' }}>
              Город
            </Typography>
            {citiesLoading ? (
              <CircularProgress size={16} />
            ) : (
              <Grid container spacing={0.5}>
                {availableCities.map((city) => (
                  <Grid size={{ xs: 2, sm: 1.5, md: 1.5 }} key={city.id}>
                    <Box
                      sx={{
                        cursor: 'pointer',
                        width: 32,
                        height: 32,
                        border: '2px solid',
                        borderColor: selectedCity === city.id ? 'primary.main' : 'rgba(255, 255, 255, 0.2)',
                        borderRadius: 1,
                        bgcolor: selectedCity === city.id ? 'primary.main' : 'rgba(255, 255, 255, 0.1)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '1rem',
                        transition: 'all 0.3s ease',
                        boxShadow: selectedCity === city.id ? '0 0 10px rgba(0, 247, 255, 0.6)' : 'none',
                        '&:hover': {
                          borderColor: 'primary.main',
                          transform: 'scale(1.1)',
                          boxShadow: '0 0 8px rgba(0, 247, 255, 0.4)',
                        },
                      }}
                      onClick={() => {
                        setSelectedCity(city.id)
                        const info = {
                          title: city.name || 'Неизвестный город',
                          description: city.description || 'Описание отсутствует',
                        }
                        setSelectedInfo(info)
                        onSelectedInfoChange?.(info)
                      }}
                      title={city.name}
                    >
                      {getCityFlag(city.name, city.region)}
                    </Box>
                  </Grid>
                ))}
              </Grid>
            )}
          </Box>
        </Box>

        {/* Кнопка внешности */}
        <Box sx={{ mb: 2 }}>
          <Button
            variant={showAppearance ? 'contained' : 'outlined'}
            color="secondary"
            size="small"
            startIcon={showAppearance ? <VisibilityOff /> : <Visibility />}
            onClick={() => setShowAppearance(!showAppearance)}
            sx={{
              textTransform: 'uppercase',
              fontSize: '0.7rem',
              py: 0.75,
              px: 2,
            }}
          >
            {showAppearance ? 'Скрыть внешность' : 'Настроить внешность'}
          </Button>
        </Box>
      </Box>

      {/* Контент - внешность или пусто */}
      <Box sx={{ flex: 1, minHeight: 0, overflowY: 'auto', overflowX: 'hidden' }}>
        {showAppearance && (
          <Box sx={{ mb: 2 }}>
            <AppearanceForm
              appearance={appearance}
              onAppearanceChange={setAppearance}
            />
          </Box>
        )}
      </Box>
    </Box>
  )
}
