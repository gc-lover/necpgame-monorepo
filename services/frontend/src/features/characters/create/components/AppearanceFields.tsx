import { Box, FormControl, InputLabel, MenuItem, Select, Slider, Stack, TextField, Typography } from '@mui/material'

const bodyTypeOptions = [
  { value: 'thin', label: 'Худощавое' },
  { value: 'normal', label: 'Обычное' },
  { value: 'muscular', label: 'Мускулистое' },
  { value: 'large', label: 'Плотное' },
]

const colorOptions = [
  'black',
  'brown',
  'blonde',
  'red',
  'white',
  'blue',
  'green',
  'hazel',
  'amber',
  'gray',
]

const skinToneOptions = ['light', 'tan', 'dark', 'muted', 'pale']

interface AppearanceFieldsProps {
  height: number
  bodyType: string
  hairColor: string
  eyeColor: string
  skinColor: string
  distinctiveFeatures: string
  onHeightChange: (value: number) => void
  onBodyTypeChange: (value: string) => void
  onHairColorChange: (value: string) => void
  onEyeColorChange: (value: string) => void
  onSkinColorChange: (value: string) => void
  onFeaturesChange: (value: string) => void
}

export function AppearanceFields({
  height,
  bodyType,
  hairColor,
  eyeColor,
  skinColor,
  distinctiveFeatures,
  onHeightChange,
  onBodyTypeChange,
  onHairColorChange,
  onEyeColorChange,
  onSkinColorChange,
  onFeaturesChange,
}: AppearanceFieldsProps) {
  return (
    <>
      <Box>
        <Typography variant="caption" color="text.secondary">
          Рост: {height} см
        </Typography>
        <Slider
          value={height}
          min={150}
          max={220}
          step={1}
          onChange={(_, value) => typeof value === 'number' && onHeightChange(value)}
          size="small"
        />
      </Box>
      <FormControl fullWidth size="small">
        <InputLabel id="body-type-select">Телосложение</InputLabel>
        <Select
          labelId="body-type-select"
          label="Телосложение"
          value={bodyType}
          onChange={(event) => onBodyTypeChange(event.target.value)}
        >
          {bodyTypeOptions.map((item) => (
            <MenuItem key={item.value} value={item.value}>
              {item.label}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1}>
        <FormControl fullWidth size="small">
          <InputLabel id="hair-color-select">Цвет волос</InputLabel>
          <Select
            labelId="hair-color-select"
            label="Цвет волос"
            value={hairColor}
            onChange={(event) => onHairColorChange(event.target.value)}
          >
            {colorOptions.map((color) => (
              <MenuItem key={color} value={color}>
                {color}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <FormControl fullWidth size="small">
          <InputLabel id="eye-color-select">Цвет глаз</InputLabel>
          <Select
            labelId="eye-color-select"
            label="Цвет глаз"
            value={eyeColor}
            onChange={(event) => onEyeColorChange(event.target.value)}
          >
            {colorOptions.map((color) => (
              <MenuItem key={color} value={color}>
                {color}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Stack>
      <FormControl fullWidth size="small">
        <InputLabel id="skin-color-select">Тип кожи</InputLabel>
        <Select
          labelId="skin-color-select"
          label="Тип кожи"
          value={skinColor}
          onChange={(event) => onSkinColorChange(event.target.value)}
        >
          {skinToneOptions.map((tone) => (
            <MenuItem key={tone} value={tone}>
              {tone}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <TextField
        label="Особые приметы"
        value={distinctiveFeatures}
        onChange={(event) => onFeaturesChange(event.target.value)}
        size="small"
        fullWidth
        multiline
        minRows={2}
        inputProps={{ maxLength: 500 }}
        helperText="Опционально, до 500 символов"
      />
    </>
  )
}






