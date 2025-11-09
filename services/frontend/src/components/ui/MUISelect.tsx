import { FormControl, InputLabel, Select as MuiSelect, MenuItem, FormHelperText, SelectProps as MuiSelectProps } from '@mui/material'
import { ReactNode } from 'react'

/**
 * Опция для Select
 */
export interface MUISelectOption {
  value: string
  label: string
  disabled?: boolean
}

/**
 * Пропсы компонента Select
 */
export interface MUISelectProps extends Omit<MuiSelectProps, 'error'> {
  label?: string
  options: MUISelectOption[]
  error?: string
  placeholder?: string
  helperText?: string
}

/**
 * Выпадающий список Material UI с киберпанк темой
 */
export function MUISelect({ label, options, error, placeholder, helperText, sx, ...props }: MUISelectProps) {
  const selectId = props.id || `select-${label?.toLowerCase().replace(/\s+/g, '-') || 'default'}`
  const labelId = `${selectId}-label`

  return (
    <FormControl fullWidth error={!!error} sx={sx}>
      {label && <InputLabel id={labelId}>{label}</InputLabel>}
      <MuiSelect
        {...props}
        id={selectId}
        labelId={label ? labelId : undefined}
        label={label}
      >
        {placeholder && (
          <MenuItem value="" disabled>
            {placeholder}
          </MenuItem>
        )}
        {options.map((option) => (
          <MenuItem
            key={option.value}
            value={option.value}
            disabled={option.disabled}
          >
            {option.label}
          </MenuItem>
        ))}
      </MuiSelect>
      {(error || helperText) && (
        <FormHelperText>{error || helperText}</FormHelperText>
      )}
    </FormControl>
  )
}

