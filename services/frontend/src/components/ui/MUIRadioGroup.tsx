import { FormControl, FormLabel, RadioGroup as MuiRadioGroup, Radio, FormControlLabel, RadioGroupProps } from '@mui/material'
import { ReactNode } from 'react'

/**
 * Опция для RadioGroup
 */
export interface MUIRadioOption {
  value: string
  label: string | ReactNode
  disabled?: boolean
}

/**
 * Пропсы компонента RadioGroup
 */
export interface MUIRadioGroupProps extends Omit<RadioGroupProps, 'children'> {
  label?: string
  options: MUIRadioOption[]
  error?: string
}

/**
 * Группа радиокнопок Material UI с киберпанк темой
 */
export function MUIRadioGroup({ label, options, error, ...props }: MUIRadioGroupProps) {
  return (
    <FormControl error={!!error} fullWidth>
      {label && <FormLabel>{label}</FormLabel>}
      <MuiRadioGroup {...props}>
        {options.map((option) => (
          <FormControlLabel
            key={option.value}
            value={option.value}
            control={<Radio />}
            label={option.label}
            disabled={option.disabled}
          />
        ))}
      </MuiRadioGroup>
    </FormControl>
  )
}

