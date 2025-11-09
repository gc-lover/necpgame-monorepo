import { ToggleButtonGroup, ToggleButton, ToggleButtonGroupProps, FormControl, FormLabel } from '@mui/material'
import { ReactNode } from 'react'

/**
 * Опция для ToggleButtonGroup
 */
export interface MUIToggleButtonOption {
  value: string
  label: string | ReactNode
  disabled?: boolean
}

/**
 * Пропсы компонента ToggleButtonGroup
 */
export interface MUIToggleButtonGroupProps extends Omit<ToggleButtonGroupProps, 'children'> {
  label?: string
  required?: boolean
  options: MUIToggleButtonOption[]
}

/**
 * Группа переключаемых кнопок Material UI с киберпанк темой
 */
export function MUIToggleButtonGroup({ label, required, options, ...props }: MUIToggleButtonGroupProps) {
  const toggleGroup = (
    <ToggleButtonGroup {...props} exclusive fullWidth>
      {options.map((option) => (
        <ToggleButton
          key={option.value}
          value={option.value}
          disabled={option.disabled}
        >
          {option.label}
        </ToggleButton>
      ))}
    </ToggleButtonGroup>
  )

  if (label) {
    return (
      <FormControl fullWidth>
        <FormLabel required={required}>{label}</FormLabel>
        {toggleGroup}
      </FormControl>
    )
  }

  return toggleGroup
}

