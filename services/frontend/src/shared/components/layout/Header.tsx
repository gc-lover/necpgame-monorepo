import { Box, Typography, Chip } from '@mui/material'
import { useNavigate, useLocation } from 'react-router-dom'

/**
 * Тонкое верхнее меню (Header)
 * 
 * Отображается на всех страницах с GameLayout
 * Содержит:
 * - Название игры (NECPGAME)
 * - Статус системы
 */
export function Header() {
  const navigate = useNavigate()
  const location = useLocation()

  return (
    <Box
      component="header"
      sx={{
        flexShrink: 0,
        zIndex: 1000,
        bgcolor: 'rgba(10, 14, 39, 0.95)',
        background: 'linear-gradient(180deg, rgba(10, 14, 39, 0.98) 0%, rgba(5, 8, 18, 0.95) 100%)',
        borderBottom: '2px solid',
        borderColor: 'rgba(0, 247, 255, 0.2)',
        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.5), inset 0 -2px 4px rgba(0, 247, 255, 0.05)',
        backdropFilter: 'blur(10px)',
      }}
    >
      <Box
        sx={{
          maxWidth: 1920,
          mx: 'auto',
          px: 3,
          py: 1.5,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          gap: 2,
        }}
      >
        {/* Название игры */}
        <Typography
          variant="h6"
          onClick={() => navigate('/characters')}
          sx={{
            color: 'primary.main',
            textShadow: '0 0 8px currentColor',
            fontWeight: 'bold',
            fontSize: '1rem',
            cursor: 'pointer',
            transition: 'opacity 0.2s',
            '&:hover': {
              opacity: 0.8,
            },
          }}
        >
          NECPGAME
        </Typography>

        {/* Статус системы - справа с краю */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.75 }}>
          <Typography
            variant="caption"
            sx={{
              color: 'text.disabled',
              fontSize: '0.65rem',
              opacity: 0.5,
            }}
          >
            Статус системы
          </Typography>
          <Chip
            label="ONLINE"
            size="small"
            sx={{
              bgcolor: 'rgba(5, 255, 161, 0.15)',
              color: 'success.main',
              border: '1px solid',
              borderColor: 'success.main',
              fontSize: '0.55rem',
              height: 18,
              px: 0.5,
              py: 0,
              fontWeight: 'bold',
              '& .MuiChip-label': {
                px: 0.75,
                py: 0,
              },
            }}
          />
        </Box>
      </Box>
    </Box>
  )
}

