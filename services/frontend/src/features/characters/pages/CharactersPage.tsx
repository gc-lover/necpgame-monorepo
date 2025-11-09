import { useState, useLayoutEffect } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { useListCharacters } from '../../../api/generated/auth/characters/characters'
import { GameLayout, StatsPanel } from '../../../components/layout/GameLayout'
import { CharacterList } from '../components/CharacterList'
import { Header } from '../../../shared/components/layout/Header'
import { SuccessAnimation } from '../../../shared/components/common/SuccessAnimation'
import { useGameState } from '../../game/hooks/useGameState'
import { Box, Typography, Button } from '@mui/material'

/**
 * Страница списка персонажей
 * 
 * Отображает:
 * - Левая панель: список персонажей или кнопка создания
 * - Центральная зона: пустая область (для будущего контента)
 * - Правая панель: статистика и UI KIT кнопка
 */
export function CharactersPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const { data: charactersData } = useListCharacters()
  const hasCharacters = (charactersData?.characters?.length || 0) > 0
  const setSelectedCharacter = useGameState((state) => state.setSelectedCharacter)
  
  // Проверяем, нужно ли показать анимацию успеха
  const locationState = location.state as { showSuccessAnimation?: boolean; successMessage?: string; newCharacterId?: string } | null
  const [showSuccessAnimation, setShowSuccessAnimation] = useState(false)
  const [successMessage, setSuccessMessage] = useState('')
  const [newCharacterId, setNewCharacterId] = useState<string | undefined>()
  const [selectedCharacterId, setSelectedCharacterId] = useState<string | null>(null)
  
  // Показываем анимацию, если она была передана через location state
  // Используем useLayoutEffect для синхронной установки newCharacterId до рендера
  useLayoutEffect(() => {
    if (locationState?.showSuccessAnimation) {
      setShowSuccessAnimation(true)
      setSuccessMessage(locationState.successMessage || 'Успешно!')
      // Критично: устанавливаем newCharacterId синхронно до рендера списка
      setNewCharacterId(locationState.newCharacterId)
      
      // Очищаем state после использования
      navigate(location.pathname, { replace: true, state: {} })
    }
  }, [locationState, navigate, location.pathname])

  // Левая панель - список персонажей или кнопка создания
  const leftPanel = (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, height: '100%', minHeight: 0 }}>
      {/* Заголовок "Персонажи" */}
      <Box sx={{ flexShrink: 0 }}>
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
          Персонажи
        </Typography>
      </Box>

      {/* Список персонажей или кнопка создания по центру */}
      <Box
        sx={{
          flex: 1,
          minHeight: 0,
          overflowY: 'auto',
          overflowX: 'hidden',
          display: 'flex',
          flexDirection: 'column',
          justifyContent: hasCharacters ? 'flex-start' : 'center',
          alignItems: hasCharacters ? 'stretch' : 'center',
        }}
      >
        {hasCharacters ? (
          <CharacterList 
            newCharacterId={newCharacterId} 
            selectedCharacterId={selectedCharacterId}
            onCharacterSelect={setSelectedCharacterId}
          />
        ) : (
          /* Если нет персонажей, показываем кнопку по центру */
          <Button
            variant="contained"
            color="success"
            size="medium"
            onClick={() => navigate('/characters/create')}
            sx={{
              fontWeight: 'bold',
              textTransform: 'uppercase',
              border: '1px solid',
              borderColor: 'rgba(255, 255, 255, 0.2)',
              boxShadow: '0 3px 12px rgba(5, 255, 161, 0.3), 0 1px 6px rgba(0, 0, 0, 0.4), inset 0 1px 2px rgba(255, 255, 255, 0.15), inset 0 -1px 2px rgba(0, 0, 0, 0.3)',
              fontSize: '0.875rem',
              letterSpacing: '0.05em',
              py: 1.5,
              px: 4,
              clipPath: 'polygon(0 0, calc(100% - 8px) 0, 100% 8px, 100% 100%, 8px 100%, 0 calc(100% - 8px))',
              '&:hover': {
                boxShadow: '0 5px 16px rgba(5, 255, 161, 0.4), 0 2px 8px rgba(0, 0, 0, 0.5), inset 0 1px 2px rgba(255, 255, 255, 0.2), inset 0 -1px 2px rgba(0, 0, 0, 0.4)',
                transform: 'translateY(-1px)',
              },
            }}
          >
            Создать персонажа
          </Button>
        )}
      </Box>

      {/* Кнопка создания персонажа - видна внизу только если есть персонажи */}
      {hasCharacters && (
        <Box sx={{ flexShrink: 0, pt: 2, borderTop: '1px solid', borderColor: 'rgba(255, 255, 255, 0.05)' }}>
          <Button
            fullWidth
            variant="contained"
            color="success"
            size="small"
            onClick={() => navigate('/characters/create')}
            sx={{
              fontWeight: 'bold',
              textTransform: 'uppercase',
              border: '1px solid',
              borderColor: 'rgba(255, 255, 255, 0.2)',
              boxShadow: '0 3px 12px rgba(5, 255, 161, 0.3), 0 1px 6px rgba(0, 0, 0, 0.4), inset 0 1px 2px rgba(255, 255, 255, 0.15), inset 0 -1px 2px rgba(0, 0, 0, 0.3)',
              fontSize: '0.75rem',
              letterSpacing: '0.05em',
              py: 1,
              clipPath: 'polygon(0 0, calc(100% - 8px) 0, 100% 8px, 100% 100%, 8px 100%, 0 calc(100% - 8px))',
              '&:hover': {
                boxShadow: '0 5px 16px rgba(5, 255, 161, 0.4), 0 2px 8px rgba(0, 0, 0, 0.5), inset 0 1px 2px rgba(255, 255, 255, 0.2), inset 0 -1px 2px rgba(0, 0, 0, 0.4)',
                transform: 'translateY(-1px)',
              },
            }}
          >
            Создать персонажа
          </Button>
        </Box>
      )}
    </Box>
  )

  // Правая панель - статистика и UI KIT кнопка
  const rightPanel = (
    <StatsPanel>
      {/* UI KIT DEMO - внизу правой панели */}
      <Box sx={{ mt: 'auto', pt: 3, borderTop: '1px solid', borderColor: 'rgba(255, 255, 255, 0.05)' }}>
        <Button
          fullWidth
          variant="text"
          size="small"
          onClick={() => navigate('/ui-kit')}
          sx={{
            color: 'text.disabled',
            opacity: 0.6,
            fontSize: '0.65rem',
            textTransform: 'uppercase',
            letterSpacing: '0.08em',
            py: 0.75,
            '&:hover': {
              opacity: 0.9,
              color: 'primary.main',
              bgcolor: 'rgba(0, 247, 255, 0.1)',
            },
          }}
        >
          🎨 UI KIT
        </Button>
      </Box>
    </StatsPanel>
  )

  return (
    <>
      {/* Анимация успеха */}
      <SuccessAnimation
        show={showSuccessAnimation}
        message={successMessage}
        onComplete={() => {
          setShowSuccessAnimation(false)
        }}
      />

      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
        {/* Верхнее меню на весь экран */}
        <Header />

        {/* GameLayout с панелями */}
        <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
          {/* Main Content - аватар персонажа и кнопка "Играть" если выбран персонаж */}
          <Box 
            sx={{ 
              flex: 1, 
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              gap: 4,
            }}
          >
            {selectedCharacterId && charactersData?.characters?.find((char) => char.id === selectedCharacterId) && (
              <>
                {/* Аватар персонажа */}
                <Box
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    width: 200,
                    height: 200,
                    borderRadius: '50%',
                    bgcolor: 'rgba(0, 247, 255, 0.1)',
                    border: '3px solid',
                    borderColor: 'primary.main',
                    boxShadow: '0 0 40px rgba(0, 247, 255, 0.4), 0 8px 24px rgba(0, 0, 0, 0.6), inset 0 0 20px rgba(0, 247, 255, 0.1)',
                    position: 'relative',
                    overflow: 'hidden',
                    background: 'linear-gradient(135deg, rgba(0, 247, 255, 0.15) 0%, rgba(0, 247, 255, 0.05) 50%, rgba(10, 14, 39, 0.9) 100%)',
                  }}
                >
                  <Typography
                    sx={{
                      fontSize: '8rem',
                      lineHeight: 1,
                      filter: 'drop-shadow(0 0 20px rgba(0, 247, 255, 0.6))',
                    }}
                  >
                    {(() => {
                      const selectedCharacter = charactersData.characters?.find((char) => char.id === selectedCharacterId)
                      if (!selectedCharacter?.class) return '⚔️'
                      const lowerName = String(selectedCharacter.class).toLowerCase()
                      switch (lowerName) {
                        case 'solo': return '🦾'
                        case 'netrunner': return '👨‍💻'
                        case 'fixer': return '🕵️‍♂️'
                        case 'techie': return '🔧'
                        case 'medtech': return '⚕️'
                        case 'media': return '📰'
                        case 'corporate': return '🏢'
                        case 'nomad': return '🛣️'
                        default: return '⚔️'
                      }
                    })()}
                  </Typography>
                </Box>
                
                {/* Кнопка "Играть" */}
                <Button
                  variant="contained"
                  color="success"
                  size="large"
                  onClick={() => {
                    const selectedCharacter = charactersData.characters?.find((char) => char.id === selectedCharacterId)
                    console.log('Играть за', selectedCharacter?.name)
                    // Сохраняем выбранного персонажа в глобальном состоянии
                    setSelectedCharacter(selectedCharacterId)
                    // Переход на приветственный экран игры
                    navigate(`/game/welcome?characterId=${selectedCharacterId}`)
                  }}
                  sx={{
                    fontWeight: 'bold',
                    textTransform: 'uppercase',
                    border: '2px solid',
                    borderColor: 'rgba(255, 255, 255, 0.2)',
                    boxShadow: '0 8px 24px rgba(5, 255, 161, 0.4), 0 4px 12px rgba(0, 0, 0, 0.6), inset 0 2px 4px rgba(255, 255, 255, 0.2), inset 0 -2px 4px rgba(0, 0, 0, 0.4)',
                    fontSize: '1.125rem',
                    letterSpacing: '0.1em',
                    py: 2,
                    px: 6,
                    clipPath: 'polygon(0 0, calc(100% - 12px) 0, 100% 12px, 100% 100%, 12px 100%, 0 calc(100% - 12px))',
                    '&:hover': {
                      boxShadow: '0 12px 32px rgba(5, 255, 161, 0.5), 0 6px 16px rgba(0, 0, 0, 0.7), inset 0 2px 4px rgba(255, 255, 255, 0.3), inset 0 -2px 4px rgba(0, 0, 0, 0.5)',
                      transform: 'translateY(-2px)',
                    },
                  }}
                >
                  ИГРАТЬ
                </Button>
              </>
            )}
          </Box>
        </GameLayout>
      </Box>
    </>
  )
}

