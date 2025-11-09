import { useState, useEffect, useLayoutEffect, useRef } from 'react'
import { useListCharacters, useDeleteCharacter } from '../../../api/generated/auth/characters/characters'
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  CircularProgress,
  Alert,
} from '@mui/material'
import { ConfirmationDialog } from '../../../shared/components/common/ConfirmationDialog'
import { ErrorDialog } from '../../../shared/components/common/ErrorDialog'

/**
 * Пропсы компонента CharacterList
 */
interface CharacterListProps {
  /** ID нового созданного персонажа для анимации */
  newCharacterId?: string
  /** ID выбранного персонажа */
  selectedCharacterId?: string | null
  /** Callback при выборе персонажа */
  onCharacterSelect?: (characterId: string) => void
}

/**
 * Иконки для классов (те же, что при создании)
 */
const getClassIcon = (className: string | null | undefined): string => {
  if (!className) return '⚔️'
  const lowerName = String(className).toLowerCase()
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

/**
 * Иконки для фракций (те же, что при создании)
 */
const getFactionIcon = (factionType: string | null | undefined, factionName?: string | null): string => {
  if (!factionType && !factionName) return '🏴'
  const lowerType = String(factionType || '').toLowerCase()
  const lowerName = String(factionName || '').toLowerCase()
  
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

/**
 * Флаги стран для городов (те же, что при создании)
 */
const getCityFlag = (cityName: string | null | undefined, cityRegion?: string | null | undefined): string => {
  if (!cityName) return '🏙️'
  const lowerName = String(cityName).toLowerCase()
  
  // Проверяем конкретные города по имени
  if (lowerName.includes('night') || lowerName.includes('найт') || lowerName.includes('найтсити')) {
    return '🌃' // Night City
  }
  if (lowerName.includes('tokyo') || lowerName.includes('токио') || lowerName.includes('neo-tokyo')) {
    return '⛩' // Tokyo
  }
  
  // Если не найдено по имени, проверяем регион
  if (cityRegion) {
    const upperRegion = String(cityRegion).toUpperCase()
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

/**
 * Компонент списка персонажей игрока
 * 
 * Отображает список персонажей с краткой информацией:
 * - Имя
 * - Класс
 * - Уровень
 * - Город
 * - Последний вход
 * 
 * Функционал:
 * - Просмотр списка персонажей
 * - Удаление персонажа (с подтверждением)
 * - Переход к созданию нового персонажа
 * - Анимация появления нового персонажа
 */
export function CharacterList({ newCharacterId, selectedCharacterId, onCharacterSelect }: CharacterListProps) {
  const [deletingCharId, setDeletingCharId] = useState<string | null>(null)
  const [animatedCharacterIds, setAnimatedCharacterIds] = useState<Set<string>>(new Set())
  // Отслеживаем персонажей, для которых анимация завершена, чтобы они оставались видимыми
  const [completedAnimationIds, setCompletedAnimationIds] = useState<Set<string>>(new Set())
  // Отслеживаем персонажей, которые удаляются (для анимации удаления)
  const [deletingCharacterIds, setDeletingCharacterIds] = useState<Set<string>>(new Set())
  // Состояние для диалогов
  const [confirmDeleteOpen, setConfirmDeleteOpen] = useState(false)
  const [characterToDelete, setCharacterToDelete] = useState<string | null>(null)
  const [errorDialogOpen, setErrorDialogOpen] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  
  // Ref для таймеров анимации удаления
  const deleteAnimationTimersRef = useRef<Map<string, NodeJS.Timeout>>(new Map())
  
  // Получаем список персонажей
  const { data, isLoading, error, refetch } = useListCharacters()
  
  // Логируем данные для отладки
  useEffect(() => {
    if (data?.characters) {
      console.log('📋 Список персонажей:', data.characters)
      data.characters.forEach((char, index) => {
        console.log(`Персонаж ${index + 1}:`, {
          id: char.id,
          name: char.name,
          class: char.class,
          faction_name: char.faction_name,
          city_name: char.city_name,
          allFields: char,
        })
      })
    }
  }, [data])
  
  // Отслеживаем предыдущий список персонажей для определения действительно новых персонажей
  const prevCharactersRef = useRef<Set<string>>(new Set())
  
  // Отслеживаем появление нового персонажа и добавляем анимацию
  // Используем useLayoutEffect для синхронного обновления до рендера
  useLayoutEffect(() => {
    if (data?.characters) {
      // Создаем Set из текущих ID персонажей
      const currentCharacterIds = new Set(data.characters.map((char) => char.id))
      
      // Если есть newCharacterId, проверяем, что персонаж действительно новый
      if (newCharacterId && currentCharacterIds.has(newCharacterId)) {
        // Проверяем, что персонаж не был в предыдущем списке (действительно новый)
        const wasInPrevList = prevCharactersRef.current.has(newCharacterId)
        
        // Проверяем, что персонаж еще не был анимирован
        if (!wasInPrevList && !animatedCharacterIds.has(newCharacterId)) {
          // Используем небольшой delay для плавности, но устанавливаем начальное состояние сразу при рендере
          // через shouldBeInvisible в JSX
          const timer = setTimeout(() => {
            setAnimatedCharacterIds((prev) => {
              // Двойная проверка на случай, если состояние уже обновилось
              if (prev.has(newCharacterId)) {
                return prev
              }
              return new Set(prev).add(newCharacterId)
            })
          }, 50) // Минимальная задержка для плавности
          
          return () => clearTimeout(timer)
        }
      }
      
      // Обновляем предыдущий список персонажей после обработки
      prevCharactersRef.current = currentCharacterIds
    }
  }, [newCharacterId, data?.characters, animatedCharacterIds]) // Добавили animatedCharacterIds для проверки
  
  // Убираем анимацию через 1.5 секунды после добавления
  // Используем useRef для отслеживания предыдущего состояния animatedCharacterIds
  const prevAnimatedCharacterIdsRef = useRef<Set<string>>(new Set())
  const animationTimersRef = useRef<Map<string, NodeJS.Timeout>>(new Map())
  const completedTimersRef = useRef<Map<string, NodeJS.Timeout>>(new Map())
  
  // Отслеживаем только добавление персонажа в animatedCharacterIds (не все изменения)
  useEffect(() => {
    if (newCharacterId && animatedCharacterIds.has(newCharacterId)) {
      // Проверяем, был ли персонаж добавлен в animatedCharacterIds (не был в предыдущем состоянии)
      const wasAdded = !prevAnimatedCharacterIdsRef.current.has(newCharacterId)
      
      if (wasAdded) {
        // Очищаем старый таймер, если он есть
        const oldTimer = animationTimersRef.current.get(newCharacterId)
        if (oldTimer) {
          clearTimeout(oldTimer)
        }
        
        // Сначала отмечаем персонажа как завершившего анимацию через 0.5s (длительность анимации)
        const completedTimer = setTimeout(() => {
          setCompletedAnimationIds((prev) => new Set(prev).add(newCharacterId))
          // Удаляем таймер из ref после завершения
          completedTimersRef.current.delete(newCharacterId)
        }, 500) // После завершения анимации
        
        // Затем удаляем из animatedCharacterIds через 2 секунды (0.5s анимация + 1.5s задержка)
        const timer = setTimeout(() => {
          setAnimatedCharacterIds((prev) => {
            const newSet = new Set(prev)
            newSet.delete(newCharacterId)
            return newSet
          })
          // Удаляем таймер из ref после завершения
          animationTimersRef.current.delete(newCharacterId)
        }, 2000) // Увеличили время до 2 секунд, чтобы анимация точно завершилась
        
        // Сохраняем оба таймера в разные ref
        completedTimersRef.current.set(newCharacterId, completedTimer)
        animationTimersRef.current.set(newCharacterId, timer)
      }
    }
    
    // Обновляем предыдущее состояние
    prevAnimatedCharacterIdsRef.current = new Set(animatedCharacterIds)
    
    // Очистка таймеров при размонтировании
    return () => {
      // Не очищаем таймеры здесь, так как они нужны для завершения анимации
    }
  }, [newCharacterId, animatedCharacterIds])
  
  // Очищаем refs когда newCharacterId меняется (новый персонаж создан)
  useEffect(() => {
    // Когда newCharacterId меняется, очищаем все таймеры для старых персонажей
    animationTimersRef.current.forEach((timer) => clearTimeout(timer))
    animationTimersRef.current.clear()
    completedTimersRef.current.forEach((timer) => clearTimeout(timer))
    completedTimersRef.current.clear()
    // Очищаем предыдущее состояние
    prevAnimatedCharacterIdsRef.current.clear()
    // НЕ очищаем prevCharactersRef здесь, так как он должен сохранять предыдущий список
    // для корректного определения новых персонажей
  }, [newCharacterId])
  
  // Инициализируем prevCharactersRef при первой загрузке данных
  useEffect(() => {
    if (data?.characters && prevCharactersRef.current.size === 0) {
      // При первой загрузке устанавливаем текущий список как предыдущий
      prevCharactersRef.current = new Set(data.characters.map((char) => char.id))
    }
  }, [data?.characters])

  // Очистка таймеров при размонтировании
  useEffect(() => {
    return () => {
      // Очищаем все таймеры при размонтировании
      deleteAnimationTimersRef.current.forEach((timer) => clearTimeout(timer))
      deleteAnimationTimersRef.current.clear()
    }
  }, [])
  
  // Хук для удаления персонажа
  const { mutate: deleteCharacter } = useDeleteCharacter()
  
  /**
   * Обработчик клика на кнопку удаления персонажа
   */
  const handleDeleteClick = (characterId: string) => {
    setCharacterToDelete(characterId)
    setConfirmDeleteOpen(true)
  }

  /**
   * Обработчик подтверждения удаления персонажа
   */
  const handleConfirmDelete = () => {
    if (!characterToDelete) return

    setConfirmDeleteOpen(false)
    
    // Запускаем анимацию удаления
    setDeletingCharacterIds((prev) => new Set(prev).add(characterToDelete))
    setDeletingCharId(characterToDelete)

    // После завершения анимации удаления (0.5s) - вызываем API для удаления
    const deleteTimer = setTimeout(() => {
      deleteCharacter(
        { characterId: characterToDelete },
        {
          onSuccess: () => {
            console.log('✓ Персонаж успешно удален')
            setDeletingCharId(null)
            setCharacterToDelete(null)
            // Убираем персонажа из состояния удаления
            setDeletingCharacterIds((prev) => {
              const newSet = new Set(prev)
              newSet.delete(characterToDelete)
              return newSet
            })
            // Перезагружаем список персонажей
            refetch()
          },
          onError: (err) => {
            console.error('✗ Ошибка удаления персонажа:', err)
            setDeletingCharId(null)
            setCharacterToDelete(null)
            // Убираем персонажа из состояния удаления при ошибке
            setDeletingCharacterIds((prev) => {
              const newSet = new Set(prev)
              newSet.delete(characterToDelete)
              return newSet
            })
            setErrorMessage('Ошибка удаления персонажа. Попробуйте еще раз.')
            setErrorDialogOpen(true)
          },
        }
      )
      // Удаляем таймер из ref после использования
      deleteAnimationTimersRef.current.delete(characterToDelete)
    }, 500) // Длительность анимации удаления

    // Сохраняем таймер в ref
    deleteAnimationTimersRef.current.set(characterToDelete, deleteTimer)
  }

  /**
   * Обработчик отмены удаления персонажа
   */
  const handleCancelDelete = () => {
    setConfirmDeleteOpen(false)
    setCharacterToDelete(null)
  }
  
  
  // Состояние загрузки
  if (isLoading) {
    return (
      <Card>
        <CardContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', py: 12 }}>
            <CircularProgress sx={{ mb: 4 }} />
            <Typography sx={{ color: 'primary.main', textTransform: 'uppercase', letterSpacing: '0.1em' }}>
              Загрузка персонажей...
            </Typography>
          </Box>
        </CardContent>
      </Card>
    )
  }
  
  // Состояние ошибки
  if (error) {
    return (
      <Alert 
        severity="error"
        action={
          <Button onClick={() => refetch()} color="inherit" size="small">
            Попробовать снова
          </Button>
        }
      >
        <Typography variant="h6" sx={{ mb: 1 }}>
          ⚠ Ошибка загрузки персонажей
        </Typography>
        <Typography variant="body2" sx={{ mb: 2 }}>
          {error.message}
        </Typography>
      </Alert>
    )
  }
  
  const characters = data?.characters || []
  
  // Если нет персонажей, возвращаем null (кнопка будет показана в App)
  if (characters.length === 0) {
    return null
  }
  
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1.5 }}>
      {/* MMORPG Characters List - вертикальный список компактных карточек */}
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1.5 }}>
        {characters.map((character) => {
          // Проверяем, является ли персонаж новым (для установки начального состояния сразу)
          // Персонаж считается новым только если:
          // 1. Его ID совпадает с newCharacterId
          // 2. Он не был в предыдущем списке (проверяется через prevCharactersRef в useLayoutEffect)
          // 3. Он еще не был анимирован
          const isNewChar = newCharacterId === character.id && !prevCharactersRef.current.has(character.id)
          const isAnimated = animatedCharacterIds.has(character.id)
          const isCompleted = completedAnimationIds.has(character.id)
          const isDeleting = deletingCharacterIds.has(character.id)
          // Если это новый персонаж, но анимация еще не запущена, устанавливаем начальное состояние
          const shouldBeInvisible = isNewChar && !isAnimated && !isCompleted
          
          return (
            <Box
              key={character.id}
              sx={{
                // Если это новый персонаж, устанавливаем начальное состояние сразу при рендере
                // После завершения анимации персонаж должен остаться видимым
                ...(shouldBeInvisible ? {
                  // Начальное состояние до запуска анимации
                  opacity: 0,
                  transform: 'translateY(-10px) scale(0.95)',
                } : isCompleted && !isDeleting ? {
                  // Анимация завершена - явно устанавливаем финальное состояние
                  opacity: 1,
                  transform: 'translateY(0) scale(1)',
                } : {}),
                // Если анимация активна, не устанавливаем инлайн-стили - полагаемся на CSS
                willChange: shouldBeInvisible || isAnimated || isDeleting ? 'opacity, transform' : 'auto',
                transformOrigin: 'center',
                // Используем 'both' чтобы CSS применял начальное состояние до анимации и финальное после
                // Для удаления используем fadeOutDown анимацию
                animation: isDeleting 
                  ? 'fadeOutDown 0.5s ease-in forwards' 
                  : isAnimated 
                    ? 'fadeInUp 0.5s ease-out both' 
                    : 'none',
                // После завершения анимации персонаж должен остаться видимым
                // Используем CSS для сохранения финального состояния через 'both' (backwards + forwards)
                '@keyframes fadeInUp': {
                  'from': {
                    opacity: 0,
                    transform: 'translateY(-10px) scale(0.95)',
                  },
                  'to': {
                    opacity: 1,
                    transform: 'translateY(0) scale(1)',
                  },
                },
                '@keyframes fadeOutDown': {
                  'from': {
                    opacity: 1,
                    transform: 'translateY(0) scale(1)',
                    maxHeight: '200px',
                    marginBottom: '12px',
                  },
                  'to': {
                    opacity: 0,
                    transform: 'translateY(20px) scale(0.9)',
                    maxHeight: 0,
                    marginBottom: 0,
                    paddingTop: 0,
                    paddingBottom: 0,
                  },
                },
              }}
            >
              <Card 
                  onClick={() => {
                    if (!isDeleting && onCharacterSelect) {
                      onCharacterSelect(character.id)
                    }
                  }}
                  sx={{ 
                    cursor: isDeleting ? 'default' : 'pointer',
                    position: 'relative',
                    border: '2px solid',
                    borderColor: isDeleting 
                      ? 'error.main' 
                      : selectedCharacterId === character.id
                        ? 'primary.main'
                        : isAnimated 
                          ? 'success.main' 
                          : 'rgba(255, 255, 255, 0.1)',
                    bgcolor: 'rgba(26, 31, 58, 0.8)',
                    background: isDeleting
                      ? 'linear-gradient(135deg, rgba(211, 47, 47, 0.1) 0%, rgba(26, 31, 58, 0.9) 50%, rgba(10, 14, 39, 0.95) 100%)'
                      : selectedCharacterId === character.id
                        ? 'linear-gradient(135deg, rgba(0, 247, 255, 0.15) 0%, rgba(26, 31, 58, 0.9) 50%, rgba(10, 14, 39, 0.95) 100%)'
                        : isAnimated 
                          ? 'linear-gradient(135deg, rgba(5, 255, 161, 0.1) 0%, rgba(26, 31, 58, 0.9) 50%, rgba(10, 14, 39, 0.95) 100%)'
                          : 'linear-gradient(135deg, rgba(26, 31, 58, 0.9) 0%, rgba(10, 14, 39, 0.95) 100%)',
                    minHeight: 90,
                    overflow: 'hidden',
                    boxShadow: isDeleting
                      ? '0 4px 16px rgba(211, 47, 47, 0.3), 0 2px 8px rgba(0, 0, 0, 0.4), inset 0 1px 2px rgba(211, 47, 47, 0.15), inset 0 -1px 2px rgba(0, 0, 0, 0.3)'
                      : selectedCharacterId === character.id
                        ? '0 4px 16px rgba(0, 247, 255, 0.4), 0 2px 8px rgba(0, 0, 0, 0.4), inset 0 1px 2px rgba(0, 247, 255, 0.2), inset 0 -1px 2px rgba(0, 0, 0, 0.3)'
                        : isAnimated
                          ? '0 4px 16px rgba(5, 255, 161, 0.3), 0 2px 8px rgba(0, 0, 0, 0.4), inset 0 1px 2px rgba(5, 255, 161, 0.15), inset 0 -1px 2px rgba(0, 0, 0, 0.3)'
                          : '0 4px 12px rgba(0, 0, 0, 0.5), 0 2px 6px rgba(0, 0, 0, 0.4), inset 0 1px 2px rgba(255, 255, 255, 0.05), inset 0 -1px 2px rgba(0, 0, 0, 0.3)',
                    clipPath: 'polygon(0 0, calc(100% - 8px) 0, 100% 8px, 100% 100%, 8px 100%, 0 calc(100% - 8px))',
                    transition: isDeleting || isAnimated 
                      ? 'border-color 0.3s ease, box-shadow 0.3s ease, background 0.3s ease'
                      : 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                    pointerEvents: isDeleting ? 'none' : 'auto',
                    '&:hover': isDeleting ? {} : {
                      transform: 'translateX(4px)',
                      borderColor: selectedCharacterId === character.id ? 'primary.main' : 'primary.main',
                      boxShadow: selectedCharacterId === character.id 
                        ? '0 8px 24px rgba(0, 247, 255, 0.4), 0 4px 12px rgba(0, 0, 0, 0.6), 0 0 40px rgba(0, 247, 255, 0.3), inset 0 1px 2px rgba(0, 247, 255, 0.2), inset 0 -1px 2px rgba(0, 0, 0, 0.4)'
                        : '0 8px 20px rgba(0, 0, 0, 0.6), 0 4px 10px rgba(0, 0, 0, 0.5), 0 0 30px rgba(0, 247, 255, 0.3), inset 0 1px 2px rgba(0, 247, 255, 0.15), inset 0 -1px 2px rgba(0, 0, 0, 0.4)',
                    },
                  }}
                >
              {/* Градиентный фон для глубины */}
              <Box
                sx={{
                  position: 'absolute',
                  inset: 0,
                  background: 'linear-gradient(135deg, rgba(0, 247, 255, 0.05) 0%, transparent 50%, rgba(0, 0, 0, 0.2) 100%)',
                  opacity: 0.6,
                }}
              />

              <CardContent sx={{ p: 1.5, position: 'relative', zIndex: 1, '&:last-child': { pb: 1.5 } }}>
                <Box sx={{ display: 'flex', gap: 1.5, alignItems: 'center' }}>
                  {/* Character Portrait Placeholder - компактный размер */}
                  <Box
                    sx={{
                      width: 70,
                      height: 70,
                      minWidth: 70,
                      flexShrink: 0,
                      bgcolor: 'rgba(0, 247, 255, 0.1)',
                      border: '2px solid',
                      borderColor: 'primary.main',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      position: 'relative',
                      overflow: 'hidden',
                      borderRadius: '6px',
                      boxShadow: '0 2px 8px rgba(0, 247, 255, 0.3), inset 0 1px 2px rgba(0, 247, 255, 0.2), inset 0 -1px 2px rgba(0, 0, 0, 0.3)',
                      background: 'linear-gradient(135deg, rgba(0, 247, 255, 0.15) 0%, rgba(0, 247, 255, 0.05) 50%, rgba(10, 14, 39, 0.8) 100%)',
                    }}
                  >
                    {/* Character Avatar - используем иконку класса персонажа */}
                    <Typography
                      variant="h4"
                      sx={{
                        color: 'primary.main',
                        textShadow: '0 0 10px currentColor',
                        opacity: 0.9,
                        filter: 'drop-shadow(0 0 5px currentColor)',
                        fontSize: '2.5rem',
                        lineHeight: 1,
                      }}
                    >
                      {getClassIcon(character.class)}
                    </Typography>
                    
                    {/* Level Badge - компактный */}
                    <Box
                      sx={{
                        position: 'absolute',
                        top: 3,
                        right: 3,
                        bgcolor: 'primary.main',
                        color: 'black',
                        px: 0.75,
                        py: 0.25,
                        borderRadius: '4px',
                        fontWeight: 'bold',
                        fontSize: '0.65rem',
                        boxShadow: '0 0 8px rgba(0, 247, 255, 0.6), 0 1px 2px rgba(0, 0, 0, 0.4), inset 0 1px 1px rgba(255, 255, 255, 0.3)',
                        border: '1px solid',
                        borderColor: 'rgba(0, 0, 0, 0.3)',
                        textTransform: 'uppercase',
                        letterSpacing: '0.05em',
                        lineHeight: 1,
                      }}
                    >
                      Lv.{character.level}
                    </Box>
                  </Box>

                  {/* Character Info */}
                  <Box sx={{ flex: 1, minWidth: 0 }}>
                    {/* Character Name */}
                    <Typography 
                      variant="body1" 
                      sx={{ 
                        color: 'primary.main', 
                        textShadow: '0 0 8px currentColor',
                        fontWeight: 'bold',
                        mb: 0.5,
                        textOverflow: 'ellipsis',
                        overflow: 'hidden',
                        whiteSpace: 'nowrap',
                        fontSize: '0.875rem',
                        letterSpacing: '0.02em',
                      }}
                      title={character.name}
                    >
                      {character.name}
                    </Typography>

                    {/* Quick Stats - только иконки фракции и города без текста (класс убран, так как он в аватаре) */}
                    <Box sx={{ display: 'flex', flexDirection: 'row', gap: 1, flexWrap: 'wrap', mb: 0.5 }}>
                      {/* Происхождение - убрано, так как origin не приходит от API в GameCharacterSummary */}
                      {/* Фракция - проверяем, что faction_name не null и не пустая строка */}
                      {character.faction_name && character.faction_name.trim() !== '' && (
                        <Typography 
                          variant="caption" 
                          sx={{ 
                            color: 'primary.main',
                            fontSize: '1rem',
                            filter: 'drop-shadow(0 0 3px currentColor)',
                            lineHeight: 1,
                          }}
                          title={`Фракция: ${typeof character.faction_name === 'object' && character.faction_name !== null
                            ? (character.faction_name as any)?.name || (character.faction_name as any)?.id || String(character.faction_name)
                            : String(character.faction_name)}`}
                        >
                          {getFactionIcon(
                            typeof character.faction_name === 'object' && character.faction_name !== null
                              ? (character.faction_name as any)?.type
                              : null,
                            typeof character.faction_name === 'object' && character.faction_name !== null
                              ? (character.faction_name as any)?.name || (character.faction_name as any)?.id
                              : String(character.faction_name)
                          )}
                        </Typography>
                      )}
                      {/* Город */}
                      {character.city_name && (
                        <Typography 
                          variant="caption" 
                          sx={{ 
                            color: 'primary.main',
                            fontSize: '1rem',
                            filter: 'drop-shadow(0 0 3px currentColor)',
                            lineHeight: 1,
                          }}
                          title="Город"
                        >
                          {getCityFlag(
                            typeof character.city_name === 'object' && character.city_name !== null
                              ? (character.city_name as any)?.name || (character.city_name as any)?.id
                              : String(character.city_name),
                            typeof character.city_name === 'object' && character.city_name !== null
                              ? (character.city_name as any)?.region
                              : undefined
                          )}
                        </Typography>
                      )}
                    </Box>
                  </Box>

                  {/* Actions - только кнопка удаления (маленький крестик) */}
                  <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1, flexShrink: 0, alignItems: 'flex-end' }}>
                    <Button
                      variant="contained"
                      color="error"
                      size="small"
                      onClick={(e) => {
                        e.stopPropagation() // Предотвращаем всплытие события клика на карточку
                        handleDeleteClick(character.id)
                      }}
                      disabled={(isDeleting && deletingCharId === character.id) || isDeleting}
                      title="Удалить персонажа"
                      sx={{
                        fontWeight: 'bold',
                        border: '1px solid',
                        borderColor: 'rgba(255, 255, 255, 0.2)',
                        boxShadow: '0 2px 8px rgba(211, 47, 47, 0.4), 0 1px 4px rgba(0, 0, 0, 0.4), inset 0 1px 1px rgba(255, 255, 255, 0.2), inset 0 -1px 1px rgba(0, 0, 0, 0.3)',
                        fontSize: '0.6rem',
                        py: 0.5,
                        px: 0.75,
                        minWidth: 'auto',
                        width: 28,
                        height: 28,
                        '&:hover': {
                          boxShadow: '0 4px 12px rgba(211, 47, 47, 0.5), 0 2px 6px rgba(0, 0, 0, 0.5), inset 0 1px 1px rgba(255, 255, 255, 0.3), inset 0 -1px 1px rgba(0, 0, 0, 0.4)',
                          transform: 'translateY(-1px)',
                        },
                      }}
                    >
                      {isDeleting && deletingCharId === character.id ? (
                        <CircularProgress size={10} />
                      ) : (
                        '✕'
                      )}
                    </Button>
                  </Box>
                </Box>
              </CardContent>
            </Card>
            </Box>
          )
        })}
      </Box>

      {/* Диалог подтверждения удаления */}
      <ConfirmationDialog
        open={confirmDeleteOpen}
        title="Подтвердите удаление"
        message="Вы уверены, что хотите удалить этого персонажа? Это действие необратимо."
        confirmText="Удалить"
        cancelText="Отмена"
        confirmColor="error"
        onConfirm={handleConfirmDelete}
        onCancel={handleCancelDelete}
      />

      {/* Диалог ошибки */}
      <ErrorDialog
        open={errorDialogOpen}
        title="Ошибка"
        message={errorMessage}
        onClose={() => setErrorDialogOpen(false)}
      />
    </Box>
  )
}

