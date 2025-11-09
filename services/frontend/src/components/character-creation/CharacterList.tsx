import { useState } from 'react'
import { useListCharacters, useDeleteCharacter } from '../../api/generated/auth/characters/characters'
import { 
  Box, 
  Card, 
  CardContent, 
  Typography, 
  Grid,
  Button, 
  CircularProgress, 
  Alert, 
  Chip,
  Divider,
} from '@mui/material'

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
 */
export function CharacterList() {
  const [deletingCharId, setDeletingCharId] = useState<string | null>(null)
  
  // Получаем список персонажей
  const { data, isLoading, error, refetch } = useListCharacters()
  
  // Хук для удаления персонажа
  const { mutate: deleteCharacter, isPending: isDeleting } = useDeleteCharacter()
  
  /**
   * Обработчик удаления персонажа
   */
  const handleDelete = (characterId: string) => {
    if (!confirm('Вы уверены, что хотите удалить этого персонажа? Это действие необратимо.')) {
      return
    }
    
    setDeletingCharId(characterId)
    
    deleteCharacter(
      { characterId },
      {
        onSuccess: () => {
          console.log('✓ Персонаж успешно удален')
          setDeletingCharId(null)
          // Перезагружаем список персонажей
          refetch()
        },
        onError: (err) => {
          console.error('✗ Ошибка удаления персонажа:', err)
          setDeletingCharId(null)
          alert('Ошибка удаления персонажа. Попробуйте еще раз.')
        },
      }
    )
  }
  
  /**
   * Форматирование даты
   */
  const formatDate = (dateString: string | null | undefined) => {
    if (!dateString) return 'Никогда'
    
    try {
      const date = new Date(dateString)
      return date.toLocaleDateString('ru-RU', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      })
    } catch {
      return 'Неизвестно'
    }
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
          {characters.map((character) => (
            <Card 
              key={character.id}
              sx={{ 
                cursor: 'pointer',
                transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                position: 'relative',
                border: '2px solid',
                borderColor: 'rgba(255, 255, 255, 0.1)',
                bgcolor: 'rgba(26, 31, 58, 0.8)',
                background: 'linear-gradient(135deg, rgba(26, 31, 58, 0.9) 0%, rgba(10, 14, 39, 0.95) 100%)',
                minHeight: 90,
                overflow: 'hidden',
                boxShadow: '0 4px 12px rgba(0, 0, 0, 0.5), 0 2px 6px rgba(0, 0, 0, 0.4), inset 0 1px 2px rgba(255, 255, 255, 0.05), inset 0 -1px 2px rgba(0, 0, 0, 0.3)',
                clipPath: 'polygon(0 0, calc(100% - 8px) 0, 100% 8px, 100% 100%, 8px 100%, 0 calc(100% - 8px))',
                '&:hover': {
                  transform: 'translateX(4px)',
                  borderColor: 'primary.main',
                  boxShadow: '0 8px 20px rgba(0, 0, 0, 0.6), 0 4px 10px rgba(0, 0, 0, 0.5), 0 0 30px rgba(0, 247, 255, 0.3), inset 0 1px 2px rgba(0, 247, 255, 0.15), inset 0 -1px 2px rgba(0, 0, 0, 0.4)',
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
                    {/* Character Icon/Portrait */}
                    <Typography
                      variant="h4"
                      sx={{
                        color: 'primary.main',
                        textShadow: '0 0 10px currentColor',
                        opacity: 0.7,
                        filter: 'drop-shadow(0 0 5px currentColor)',
                        fontSize: '1.75rem',
                      }}
                    >
                      ⚔️
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

                    {/* Class Badge - компактный */}
                    <Box sx={{ display: 'flex', gap: 0.75, mb: 0.75, flexWrap: 'wrap' }}>
                      <Chip 
                        label={String(character.class || 'Неизвестно')}
                        size="small"
                        sx={{ 
                          bgcolor: 'secondary.main',
                          color: 'white',
                          fontWeight: 'bold',
                          fontSize: '0.65rem',
                          height: 20,
                          px: 0.75,
                          border: '1px solid',
                          borderColor: 'rgba(255, 255, 255, 0.2)',
                          boxShadow: '0 1px 4px rgba(0, 0, 0, 0.4), inset 0 1px 1px rgba(255, 255, 255, 0.1)',
                          textTransform: 'uppercase',
                          letterSpacing: '0.03em',
                          '& .MuiChip-label': {
                            px: 0.5,
                          },
                        }}
                      />
                    </Box>

                    {/* Quick Stats */}
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 0.5 }}>
                      {character.faction_name && (
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                          <Typography 
                            variant="caption" 
                            sx={{ 
                              color: 'primary.main',
                              fontSize: '0.75rem',
                              filter: 'drop-shadow(0 0 3px currentColor)',
                            }}
                          >
                            ⚡
                          </Typography>
                          <Typography 
                            variant="caption" 
                            sx={{ 
                              color: 'text.secondary',
                              textOverflow: 'ellipsis',
                              overflow: 'hidden',
                              whiteSpace: 'nowrap',
                              flex: 1,
                              fontSize: '0.7rem',
                              fontWeight: 400,
                            }}
                          >
                            {typeof character.faction_name === 'object' && character.faction_name !== null
                              ? (character.faction_name.name || character.faction_name.id || 'Неизвестная фракция')
                              : String(character.faction_name)
                            }
                          </Typography>
                        </Box>
                      )}
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                        <Typography 
                          variant="caption" 
                          sx={{ 
                            color: 'primary.main',
                            fontSize: '0.75rem',
                            filter: 'drop-shadow(0 0 3px currentColor)',
                          }}
                        >
                          🏙️
                        </Typography>
                        <Typography 
                          variant="caption" 
                          sx={{ 
                            color: 'text.secondary',
                            textOverflow: 'ellipsis',
                            overflow: 'hidden',
                            whiteSpace: 'nowrap',
                            flex: 1,
                            fontSize: '0.7rem',
                            fontWeight: 400,
                          }}
                        >
                          {String(character.city_name || 'Неизвестно')}
                        </Typography>
                      </Box>
                    </Box>
                  </Box>

                  {/* Actions - компактные кнопки */}
                  <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1, flexShrink: 0 }}>
                    <Button
                      variant="contained"
                      color="success"
                      size="small"
                      onClick={() => console.log('Играть за', character.name)}
                      sx={{ 
                        fontWeight: 'bold',
                        textTransform: 'uppercase',
                        bgcolor: 'success.main',
                        border: '1px solid',
                        borderColor: 'rgba(255, 255, 255, 0.2)',
                        boxShadow: '0 2px 8px rgba(5, 255, 161, 0.4), 0 1px 4px rgba(0, 0, 0, 0.4), inset 0 1px 1px rgba(255, 255, 255, 0.2), inset 0 -1px 1px rgba(0, 0, 0, 0.3)',
                        fontSize: '0.7rem',
                        letterSpacing: '0.05em',
                        py: 0.75,
                        px: 1.5,
                        minWidth: 70,
                        '&:hover': {
                          bgcolor: 'success.dark',
                          boxShadow: '0 4px 12px rgba(5, 255, 161, 0.5), 0 2px 6px rgba(0, 0, 0, 0.5), inset 0 1px 1px rgba(255, 255, 255, 0.3), inset 0 -1px 1px rgba(0, 0, 0, 0.4)',
                          transform: 'translateY(-1px)',
                        },
                      }}
                    >
                      ИГРАТЬ
                    </Button>
                    <Button
                      variant="contained"
                      color="error"
                      size="small"
                      onClick={() => handleDelete(character.id)}
                      disabled={isDeleting && deletingCharId === character.id}
                      title="Удалить персонажа"
                      sx={{ 
                        fontWeight: 'bold',
                        border: '1px solid',
                        borderColor: 'rgba(255, 255, 255, 0.2)',
                        boxShadow: '0 2px 8px rgba(211, 47, 47, 0.4), 0 1px 4px rgba(0, 0, 0, 0.4), inset 0 1px 1px rgba(255, 255, 255, 0.2), inset 0 -1px 1px rgba(0, 0, 0, 0.3)',
                        fontSize: '0.75rem',
                        py: 0.75,
                        px: 1.5,
                        minWidth: 70,
                        '&:hover': {
                          boxShadow: '0 4px 12px rgba(211, 47, 47, 0.5), 0 2px 6px rgba(0, 0, 0, 0.5), inset 0 1px 1px rgba(255, 255, 255, 0.3), inset 0 -1px 1px rgba(0, 0, 0, 0.4)',
                          transform: 'translateY(-1px)',
                        },
                      }}
                    >
                      {isDeleting && deletingCharId === character.id ? (
                        <CircularProgress size={12} />
                      ) : (
                        '✕'
                      )}
                    </Button>
                  </Box>
                </Box>
              </CardContent>
            </Card>
          ))}
      </Box>
    </Box>
  )
}

