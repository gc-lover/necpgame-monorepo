import { TextField, TextFieldProps } from '@mui/material'
import { forwardRef } from 'react'

/**
 * Поле ввода Material UI с киберпанк темой
 */
export interface MUIInputProps extends Omit<TextFieldProps, 'error'> {
  error?: string
  hint?: string
}

export const MUIInput = forwardRef<HTMLInputElement, MUIInputProps>(
  ({ error, hint, helperText, sx, ...props }, ref) => {
    return (
      <TextField
        ref={ref}
        {...props}
        error={!!error}
        helperText={error || hint || helperText}
        fullWidth
        sx={sx}
      />
    )
  }
)

MUIInput.displayName = 'MUIInput'

