import type { GameCharacterAppearance } from '../../../api/generated/auth/models/character-creation-reference-models/gameCharacterAppearance'
import { 
  Box, 
  Typography, 
  Grid,
  Slider,
  FormControl,
  FormLabel,
  TextField,
  Select,
  MenuItem,
  InputLabel,
} from '@mui/material'

/**
 * Пропсы компонента AppearanceForm
 */
interface AppearanceFormProps {
  /** Данные внешности */
  appearance: GameCharacterAppearance
  /** Обработчик изменения внешности */
  onAppearanceChange: (appearance: GameCharacterAppearance) => void
}

/**
 * Компонент формы внешности персонажа
 */
export function AppearanceForm({ appearance, onAppearanceChange }: AppearanceFormProps) {
  const handleHeightChange = (value: number) => {
    const height = Math.max(150, Math.min(220, value))
    onAppearanceChange({ ...appearance, height })
  }
  
  const handleBodyTypeChange = (body_type: 'thin' | 'normal' | 'muscular' | 'large') => {
    onAppearanceChange({ ...appearance, body_type })
  }
  
  const handleHairColorChange = (hair_color: string) => {
    onAppearanceChange({ ...appearance, hair_color })
  }
  
  const handleEyeColorChange = (eye_color: string) => {
    onAppearanceChange({ ...appearance, eye_color })
  }
  
  const handleSkinColorChange = (skin_color: string) => {
    onAppearanceChange({ ...appearance, skin_color })
  }
  
  const handleDistinctiveFeaturesChange = (distinctive_features: string | null) => {
    onAppearanceChange({ ...appearance, distinctive_features })
  }
  
  const hairColors = [
    { value: 'black', label: 'Черный', color: '#000000' },
    { value: 'brown', label: 'Коричневый', color: '#8B4513' },
    { value: 'blonde', label: 'Блондин', color: '#FFD700' },
    { value: 'red', label: 'Рыжий', color: '#FF4500' },
    { value: 'white', label: 'Белый', color: '#FFFFFF' },
    { value: 'gray', label: 'Серый', color: '#808080' },
    { value: 'blue', label: 'Синий', color: '#0000FF' },
    { value: 'green', label: 'Зеленый', color: '#00FF00' },
    { value: 'purple', label: 'Фиолетовый', color: '#800080' },
    { value: 'pink', label: 'Розовый', color: '#FFC0CB' },
  ]
  
  const eyeColors = [
    { value: 'brown', label: 'Карие', color: '#8B4513' },
    { value: 'blue', label: 'Голубые', color: '#0000FF' },
    { value: 'green', label: 'Зеленые', color: '#00FF00' },
    { value: 'gray', label: 'Серые', color: '#808080' },
    { value: 'hazel', label: 'Ореховые', color: '#8B7355' },
    { value: 'amber', label: 'Янтарные', color: '#FFBF00' },
    { value: 'red', label: 'Красные', color: '#FF0000' },
    { value: 'cyber', label: 'Кибер', color: '#00FFFF' },
  ]
  
  const skinColors = [
    { value: 'light', label: 'Светлая', color: '#FFDAB9' },
    { value: 'fair', label: 'Бледная', color: '#FAF0E6' },
    { value: 'medium', label: 'Средняя', color: '#D2B48C' },
    { value: 'olive', label: 'Оливковая', color: '#C19A6B' },
    { value: 'tan', label: 'Загорелая', color: '#D2691E' },
    { value: 'brown', label: 'Коричневая', color: '#8B4513' },
    { value: 'dark', label: 'Темная', color: '#654321' },
    { value: 'ebony', label: 'Черная', color: '#3B3131' },
  ]
  
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 0.75, maxWidth: '100%' }}>
      
      {/* Рост */}
      <FormControl fullWidth size="small">
        <FormLabel sx={{ fontSize: '0.65rem', mb: 0.25 }}>Рост</FormLabel>
        <Slider
          value={appearance.height}
          onChange={(_, value) => handleHeightChange(value as number)}
          min={150}
          max={220}
          valueLabelDisplay="auto"
          valueLabelFormat={(value) => `${value} см`}
          size="small"
          sx={{ mt: 0.75 }}
        />
      </FormControl>
      
      {/* Телосложение - выпадающий список */}
      <FormControl fullWidth size="small">
        <InputLabel sx={{ fontSize: '0.65rem' }}>Телосложение</InputLabel>
        <Select
          value={appearance.body_type}
          onChange={(e) => handleBodyTypeChange(e.target.value as 'thin' | 'normal' | 'muscular' | 'large')}
          label="Телосложение"
          size="small"
          sx={{
            fontSize: '0.65rem',
            '& .MuiSelect-select': { fontSize: '0.65rem', py: 0.5 },
          }}
        >
          <MenuItem value="thin" sx={{ fontSize: '0.65rem' }}>Худощавое</MenuItem>
          <MenuItem value="normal" sx={{ fontSize: '0.65rem' }}>Обычное</MenuItem>
          <MenuItem value="muscular" sx={{ fontSize: '0.65rem' }}>Мускулистое</MenuItem>
          <MenuItem value="large" sx={{ fontSize: '0.65rem' }}>Крупное</MenuItem>
        </Select>
      </FormControl>
      
      {/* Цвет волос */}
      <Box>
        <Typography variant="subtitle2" sx={{ mb: 0.25, color: 'primary.main', fontSize: '0.65rem' }}>
          Цвет волос
        </Typography>
        <Grid container spacing={0.3}>
          {hairColors.map((color) => (
            <Grid size={{ xs: 2, sm: 1.5, md: 1.5 }} key={color.value}>
              <Box
                sx={{
                  cursor: 'pointer',
                  width: 28,
                  height: 28,
                  border: '1.5px solid',
                  borderColor: appearance.hair_color === color.value ? 'primary.main' : 'divider',
                  borderRadius: 0.75,
                  bgcolor: color.color,
                  transition: 'all 0.3s ease',
                  boxShadow: appearance.hair_color === color.value ? '0 0 8px rgba(0, 247, 255, 0.6)' : 'none',
                  '&:hover': {
                    borderColor: 'primary.main',
                    transform: 'scale(1.1)',
                    boxShadow: '0 0 6px rgba(0, 247, 255, 0.4)',
                  },
                }}
                onClick={() => handleHairColorChange(color.value)}
                title={color.label}
              />
            </Grid>
          ))}
        </Grid>
      </Box>
      
      {/* Цвет глаз */}
      <Box>
        <Typography variant="subtitle2" sx={{ mb: 0.25, color: 'primary.main', fontSize: '0.65rem' }}>
          Цвет глаз
        </Typography>
        <Grid container spacing={0.3}>
          {eyeColors.map((color) => (
            <Grid size={{ xs: 2, sm: 1.5, md: 1.5 }} key={color.value}>
              <Box
                sx={{
                  cursor: 'pointer',
                  width: 28,
                  height: 28,
                  border: '1.5px solid',
                  borderColor: appearance.eye_color === color.value ? 'primary.main' : 'divider',
                  borderRadius: 0.75,
                  bgcolor: color.color,
                  transition: 'all 0.3s ease',
                  boxShadow: appearance.eye_color === color.value ? '0 0 8px rgba(0, 247, 255, 0.6)' : 'none',
                  '&:hover': {
                    borderColor: 'primary.main',
                    transform: 'scale(1.1)',
                    boxShadow: '0 0 6px rgba(0, 247, 255, 0.4)',
                  },
                }}
                onClick={() => handleEyeColorChange(color.value)}
                title={color.label}
              />
            </Grid>
          ))}
        </Grid>
      </Box>
      
      {/* Цвет кожи */}
      <Box>
        <Typography variant="subtitle2" sx={{ mb: 0.25, color: 'primary.main', fontSize: '0.65rem' }}>
          Цвет кожи
        </Typography>
        <Grid container spacing={0.3}>
          {skinColors.map((color) => (
            <Grid size={{ xs: 2, sm: 1.5, md: 1.5 }} key={color.value}>
              <Box
                sx={{
                  cursor: 'pointer',
                  width: 28,
                  height: 28,
                  border: '1.5px solid',
                  borderColor: appearance.skin_color === color.value ? 'primary.main' : 'divider',
                  borderRadius: 0.75,
                  bgcolor: color.color,
                  transition: 'all 0.3s ease',
                  boxShadow: appearance.skin_color === color.value ? '0 0 8px rgba(0, 247, 255, 0.6)' : 'none',
                  '&:hover': {
                    borderColor: 'primary.main',
                    transform: 'scale(1.1)',
                    boxShadow: '0 0 6px rgba(0, 247, 255, 0.4)',
                  },
                }}
                onClick={() => handleSkinColorChange(color.value)}
                title={color.label}
              />
            </Grid>
          ))}
        </Grid>
      </Box>
      
      {/* Особые приметы */}
      <TextField
        label="Особые приметы (опционально)"
        value={appearance.distinctive_features || ''}
        onChange={(e) => handleDistinctiveFeaturesChange(e.target.value || null)}
        placeholder="Шрамы, татуировки, киберимпланты..."
        multiline
        minRows={2}
        maxRows={3}
        inputProps={{ maxLength: 500 }}
        helperText={`${(appearance.distinctive_features || '').length} / 500`}
        fullWidth
        size="small"
        sx={{ 
          '& .MuiInputLabel-root': { fontSize: '0.6rem' },
          '& .MuiInputBase-root': { fontSize: '0.6rem' },
          '& .MuiInputBase-input': { fontSize: '0.6rem' },
          '& .MuiFormHelperText-root': { fontSize: '0.55rem' },
        }}
      />
    </Box>
  )
}
