import {
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Typography,
} from '@mui/material'

interface ReferenceSelectorsProps {
  classes: Array<{
    id?: string | null
    name?: string | null
    subclasses?: Array<{ id?: string | null; name?: string | null }>
  }>
  origins: Array<{
    id?: string | null
    name?: string | null
  }>
  originFactions: Array<{
    id?: string | null
    name?: string | null
  }>
  availableCities: Array<{
    id?: string | null
    name?: string | null
  }>
  classId: string
  subclassId: string
  originId: string
  factionId: string
  cityId: string
  onClassChange: (value: string) => void
  onSubclassChange: (value: string) => void
  onOriginChange: (value: string) => void
  onFactionChange: (value: string) => void
  onCityChange: (value: string) => void
}

export function ReferenceSelectors({
  classes,
  origins,
  originFactions,
  availableCities,
  classId,
  subclassId,
  originId,
  factionId,
  cityId,
  onClassChange,
  onSubclassChange,
  onOriginChange,
  onFactionChange,
  onCityChange,
}: ReferenceSelectorsProps) {
  const currentClass = classes.find((item) => item.id === classId)

  return (
    <>
      <FormControl fullWidth size="small">
        <InputLabel id="class-select">Класс</InputLabel>
        <Select
          labelId="class-select"
          label="Класс"
          value={classId}
          onChange={(event) => onClassChange(event.target.value)}
        >
          {classes.map((item) => (
            <MenuItem key={item.id ?? ''} value={item.id ?? ''}>
              {item.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      {currentClass?.subclasses && currentClass.subclasses.length > 0 && (
        <FormControl fullWidth size="small">
          <InputLabel id="subclass-select">Подкласс</InputLabel>
          <Select
            labelId="subclass-select"
            label="Подкласс"
            value={subclassId}
            onChange={(event) => onSubclassChange(event.target.value)}
          >
            <MenuItem value="">
              <Typography variant="body2" color="text.secondary">
                Без подкласса
              </Typography>
            </MenuItem>
            {currentClass.subclasses.map((item) => (
              <MenuItem key={item.id ?? ''} value={item.id ?? ''}>
                {item.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      )}
      <FormControl fullWidth size="small">
        <InputLabel id="origin-select">Происхождение</InputLabel>
        <Select
          labelId="origin-select"
          label="Происхождение"
          value={originId}
          onChange={(event) => onOriginChange(event.target.value)}
        >
          {origins.map((item) => (
            <MenuItem key={item.id ?? ''} value={item.id ?? ''}>
              {item.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControl fullWidth size="small">
        <InputLabel id="faction-select">Фракция</InputLabel>
        <Select
          labelId="faction-select"
          label="Фракция"
          value={factionId}
          onChange={(event) => onFactionChange(event.target.value)}
          displayEmpty
        >
          <MenuItem value="">
            <Typography variant="body2" color="text.secondary">
              Без фракции
            </Typography>
          </MenuItem>
          {originFactions.map((item) => (
            <MenuItem key={item.id ?? ''} value={item.id ?? ''}>
              {item.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControl fullWidth size="small">
        <InputLabel id="city-select">Стартовый город</InputLabel>
        <Select
          labelId="city-select"
          label="Стартовый город"
          value={cityId}
          onChange={(event) => onCityChange(event.target.value)}
        >
          {availableCities.map((item) => (
            <MenuItem key={item.id ?? ''} value={item.id ?? ''}>
              {item.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </>
  )
}

