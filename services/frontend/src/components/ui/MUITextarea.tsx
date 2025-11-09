import { TextField, TextFieldProps } from '@mui/material'
import { forwardRef } from 'react'

/**
 * Текстовое поле Material UI с киберпанк темой
 */
export interface MUITextareaProps extends Omit<TextFieldProps, 'multiline' | 'rows'> {
  rows?: number
  minRows?: number
  maxRows?: number
}

export const MUITextarea = forwardRef<HTMLTextAreaElement, MUITextareaProps>(
  ({ error, hint, helperText, rows, minRows, maxRows, sx, ...props }, ref) => {
    // MUI не позволяет использовать rows вместе с minRows/maxRows
    // Если есть minRows или maxRows, используем их, иначе используем rows
    const textFieldProps = minRows || maxRows
      ? { minRows: minRows || 4, maxRows }
      : { rows: rows || 4 }
    
    return (
      <TextField
        ref={ref}
        {...props}
        multiline
        {...textFieldProps}
        error={!!error}
        helperText={error || hint || helperText}
        fullWidth
        sx={sx}
      />
    )
  }
)

MUITextarea.displayName = 'MUITextarea'

