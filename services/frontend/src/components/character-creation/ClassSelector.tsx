import { useGetCharacterClasses } from '../../api/generated/auth/characters/characters'
import { Card, CardContent, Box, Typography, CircularProgress, Alert, Grid, Chip } from '@mui/material'
import { CheckCircle } from '@mui/icons-material'

/**
 * Пропсы компонента ClassSelector
 */
interface ClassSelectorProps {
  /** Выбранный класс */
  selectedClass: string | null
  /** Выбранный подкласс */
  selectedSubclass: string | null
  /** Обработчик выбора класса */
  onClassSelect: (classId: string) => void
  /** Обработчик выбора подкласса */
  onSubclassSelect: (subclassId: string | null) => void
}

/**
 * Компонент выбора класса и подкласса персонажа
 */
export function ClassSelector({
  selectedClass,
  selectedSubclass,
  onClassSelect,
  onSubclassSelect,
}: ClassSelectorProps) {
  const { data, isLoading, error } = useGetCharacterClasses()
  const selectedClassData = data?.classes?.find((cls) => cls.id === selectedClass)
  
  const handleClassSelect = (classId: string) => {
    onSubclassSelect(null)
    onClassSelect(classId)
  }
  
  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '200px' }}>
        <CircularProgress />
        <Typography sx={{ ml: 2 }}>Загрузка классов...</Typography>
      </Box>
    )
  }
  
  if (error) {
    return (
      <Alert severity="error">
        Ошибка загрузки классов: {error.message}
      </Alert>
    )
  }
  
  const classes = data?.classes || []
  
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Typography variant="h6" sx={{ color: 'primary.main', textShadow: '0 0 10px currentColor' }}>
        Выберите класс
      </Typography>
      
      <Grid container spacing={3}>
        {classes.map((cls) => (
          <Grid size={{ xs: 12, md: 6 }} key={cls.id}>
            <Card
              sx={{
                cursor: 'pointer',
                transition: 'all 0.3s ease',
                border: selectedClass === cls.id ? '2px solid' : '2px solid',
                borderColor: selectedClass === cls.id ? 'primary.main' : 'divider',
                boxShadow: selectedClass === cls.id
                  ? '0 0 20px rgba(0, 247, 255, 0.4), 0 0 40px rgba(0, 247, 255, 0.2)'
                  : 'none',
                '&:hover': {
                  borderColor: 'primary.main',
                  boxShadow: '0 0 15px rgba(0, 247, 255, 0.3)',
                  transform: 'translateY(-2px)',
                },
              }}
              onClick={() => handleClassSelect(cls.id)}
            >
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'start', justifyContent: 'space-between', mb: 2 }}>
                  <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 'bold' }}>
                    {cls.name}
                  </Typography>
                  {selectedClass === cls.id && (
                    <CheckCircle sx={{ color: 'primary.main', fontSize: 28 }} />
                  )}
                </Box>
                
                <Typography variant="body2" sx={{ color: 'text.secondary', mb: 2 }}>
                  {cls.description}
                </Typography>
                
                {cls.subclasses && cls.subclasses.length > 0 && (
                  <Chip
                    label={`${cls.subclasses.length} подкласс(ов)`}
                    size="small"
                    sx={{ bgcolor: 'primary.main', color: 'black' }}
                  />
                )}
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
      
      {selectedClassData && selectedClassData.subclasses && selectedClassData.subclasses.length > 0 && (
        <Box sx={{ mt: 4 }}>
          <Typography variant="h6" sx={{ mb: 2, color: 'primary.main' }}>
            Выберите подкласс (опционально)
          </Typography>
          
          <Grid container spacing={2}>
            <Grid size={{ xs: 12, sm: 6, md: 4 }}>
              <Card
                sx={{
                  cursor: 'pointer',
                  border: selectedSubclass === null ? '2px solid' : '2px solid',
                  borderColor: selectedSubclass === null ? 'primary.main' : 'divider',
                  boxShadow: selectedSubclass === null ? '0 0 15px rgba(0, 247, 255, 0.3)' : 'none',
                  '&:hover': {
                    borderColor: 'primary.main',
                    transform: 'translateY(-2px)',
                  },
                }}
                onClick={() => onSubclassSelect(null)}
              >
                <CardContent>
                  <Typography variant="h6">Без подкласса</Typography>
                  <Typography variant="body2" sx={{ color: 'text.secondary', mt: 1 }}>
                    Играть без специализации
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
            
            {selectedClassData.subclasses.map((subclass) => (
              <Grid size={{ xs: 12, sm: 6, md: 4 }} key={subclass.id}>
                <Card
                  sx={{
                    cursor: 'pointer',
                    border: selectedSubclass === subclass.id ? '2px solid' : '2px solid',
                    borderColor: selectedSubclass === subclass.id ? 'primary.main' : 'divider',
                    boxShadow: selectedSubclass === subclass.id ? '0 0 15px rgba(0, 247, 255, 0.3)' : 'none',
                    '&:hover': {
                      borderColor: 'primary.main',
                      transform: 'translateY(-2px)',
                    },
                  }}
                  onClick={() => onSubclassSelect(subclass.id)}
                >
                  <CardContent>
                    <Typography variant="h6">{subclass.name}</Typography>
                    <Typography variant="body2" sx={{ color: 'text.secondary', mt: 1 }}>
                      {subclass.description}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Box>
      )}
      
      {selectedClass && (
        <Box
          sx={{
            mt: 3,
            p: 2,
            bgcolor: 'primary.main',
            color: 'black',
            borderRadius: 1,
            display: 'flex',
            alignItems: 'center',
            gap: 1,
          }}
        >
          <CheckCircle />
          <Typography variant="body1" sx={{ fontWeight: 'bold' }}>
            Выбран класс: {selectedClassData?.name || selectedClass}
            {selectedSubclass && (
              <> | Подкласс: {selectedClassData?.subclasses?.find((s) => s.id === selectedSubclass)?.name || selectedSubclass}</>
            )}
          </Typography>
        </Box>
      )}
    </Box>
  )
}
