import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, CircularProgress, Divider, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { ClassCard } from '../components/ClassCard'
import { useGetClasses } from '@/api/generated/classes-progression/classes/classes'

export const ClassesProgressionPage: React.FC = () => {
  const navigate = useNavigate()
  const { data, isLoading } = useGetClasses({ include_custom_path: true })

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">Классы</Typography>
      <Typography variant="caption" fontSize="0.7rem">13 классов Cyberpunk</Typography>
      <Divider />
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Инфо</Typography>
      <Divider />
      <Typography variant="body2" fontSize="0.75rem" color="text.secondary">
        10 канонных + 3 авторских. Подклассы 2-3 на класс. "Свой путь" доступен.
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">Система классов</Typography>
      <Divider />
      {isLoading && <CircularProgress size={32} />}
      {!isLoading && data?.classes && (
        <Box sx={{ overflowY: 'auto', flex: 1 }}>
          <Stack spacing={1.5}>
            {data.classes.map((cls: any) => <ClassCard key={cls.class_id} gameClass={cls} />)}
          </Stack>
        </Box>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default ClassesProgressionPage

