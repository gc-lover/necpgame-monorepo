/**
 * Список навыков персонажа
 * Данные из OpenAPI: Skill[]
 */
import { Typography, Stack, List, ListItem, ListItemText, LinearProgress, Box } from '@mui/material'
import SchoolIcon from '@mui/icons-material/School'
import type { Skill } from '@/api/generated/character-status/models'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

interface SkillsListDisplayProps {
  skills: Skill[]
}

export function SkillsListDisplay({ skills }: SkillsListDisplayProps) {
  if (skills.length === 0) {
    return (
      <CompactCard color="default" compact sx={{ textAlign: 'center' }}>
        <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.75rem' }}>
          Нет изученных навыков
        </Typography>
      </CompactCard>
    )
  }

  return (
    <CompactCard color="default" compact>
      <Stack direction="row" spacing={1} alignItems="center" sx={{ mb: 2 }}>
        <SchoolIcon color="primary" />
        <Typography variant="h6" sx={{ fontSize: '0.95rem', fontWeight: 'bold', color: 'primary.main' }}>
          Навыки
        </Typography>
      </Stack>

      <List dense>
        {skills.map((skill) => {
          const progress = skill.maxLevel ? (skill.level / skill.maxLevel) * 100 : (skill.level / 10) * 100

          return (
            <ListItem key={skill.id} sx={{ px: 0, pb: 1.5, flexDirection: 'column', alignItems: 'stretch' }}>
              <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 0.5, width: '100%' }}>
                <ListItemText
                  primary={skill.name}
                  primaryTypographyProps={{ fontSize: '0.8rem', fontWeight: 'bold' }}
                />
                <Box
                  sx={{
                    bgcolor: 'rgba(0, 247, 255, 0.1)',
                    px: 1,
                    py: 0.3,
                    borderRadius: 1,
                    minWidth: 35,
                    textAlign: 'center',
                  }}
                >
                  <Typography variant="caption" sx={{ fontSize: '0.75rem', fontWeight: 'bold' }}>
                    {skill.level}
                  </Typography>
                </Box>
              </Stack>
              <LinearProgress
                variant="determinate"
                value={progress}
                sx={{ height: 4, borderRadius: 1 }}
                color="primary"
              />
              <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary', mt: 0.3 }}>
                Опыт: {skill.experience} {skill.maxLevel ? `/ ${skill.maxLevel}` : ''}
              </Typography>
            </ListItem>
          )
        })}
      </List>
    </CompactCard>
  )
}

