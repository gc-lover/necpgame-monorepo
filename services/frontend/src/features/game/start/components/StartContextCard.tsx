import { List, ListItem, ListItemIcon, ListItemText, Typography } from '@mui/material'
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch'
import MilitaryTechIcon from '@mui/icons-material/MilitaryTech'
import LocalAtmIcon from '@mui/icons-material/LocalAtm'
import ShieldMoonIcon from '@mui/icons-material/ShieldMoon'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

export function StartContextCard() {
  return (
    <CompactCard
      color="blue"
      compact
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        gap: 2,
      }}
    >
      <Typography variant="h6" sx={{ fontSize: '0.95rem', textTransform: 'uppercase' }}>
        Стартовые условия
      </Typography>

      <List dense disablePadding>
        <ListItem disableGutters>
          <ListItemIcon sx={{ minWidth: 36 }}>
            <RocketLaunchIcon fontSize="small" color="primary" />
          </ListItemIcon>
          <ListItemText
            primary="Сессия создаётся автоматически"
            secondary="Ваш персонаж сразу появляется в Downtown."
            primaryTypographyProps={{ variant: 'body2', fontWeight: 600 }}
            secondaryTypographyProps={{ variant: 'caption', color: 'text.secondary' }}
          />
        </ListItem>
        <ListItem disableGutters>
          <ListItemIcon sx={{ minWidth: 36 }}>
            <MilitaryTechIcon fontSize="small" color="secondary" />
          </ListItemIcon>
          <ListItemText
            primary="Стартовое снаряжение"
            secondary="Пистолет Liberty и уличная броня готовы в инвентаре."
            primaryTypographyProps={{ variant: 'body2', fontWeight: 600 }}
            secondaryTypographyProps={{ variant: 'caption', color: 'text.secondary' }}
          />
        </ListItem>
        <ListItem disableGutters>
          <ListItemIcon sx={{ minWidth: 36 }}>
            <LocalAtmIcon fontSize="small" color="success" />
          </ListItemIcon>
          <ListItemText
            primary="Экономический старт"
            secondary="500 eddies и уровень 1 — прокачивайтесь и инвестируйте с умом."
            primaryTypographyProps={{ variant: 'body2', fontWeight: 600 }}
            secondaryTypographyProps={{ variant: 'caption', color: 'text.secondary' }}
          />
        </ListItem>
        <ListItem disableGutters>
          <ListItemIcon sx={{ minWidth: 36 }}>
            <ShieldMoonIcon fontSize="small" color="info" />
          </ListItemIcon>
          <ListItemText
            primary="Стартовые параметры"
            secondary="Здоровье, энергия и человечность — по 100. Следите за имплантами."
            primaryTypographyProps={{ variant: 'body2', fontWeight: 600 }}
            secondaryTypographyProps={{ variant: 'caption', color: 'text.secondary' }}
          />
        </ListItem>
      </List>
    </CompactCard>
  )
}






