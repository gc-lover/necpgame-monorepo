import { Button, ButtonProps, CircularProgress } from '@mui/material'
import { ReactNode } from 'react'

/**
 * Кнопка Material UI с киберпанк темой
 * 
 * Готовый компонент который гарантированно работает
 * 
 * Поддерживает leftIcon и rightIcon которые преобразуются в startIcon и endIcon
 * Поддерживает loading пропс для отображения состояния загрузки
 */
export interface MUIButtonProps extends Omit<ButtonProps, 'startIcon' | 'endIcon'> {
  leftIcon?: ReactNode
  rightIcon?: ReactNode
  loading?: boolean
}

export function MUIButton({ children, leftIcon, rightIcon, loading, disabled, ...props }: MUIButtonProps) {
  // Преобразуем leftIcon и rightIcon в startIcon и endIcon для Material UI
  // Если loading=true, показываем CircularProgress вместо leftIcon
  const muiProps: ButtonProps = {
    ...props,
    disabled: disabled || loading,
    ...(loading 
      ? { startIcon: <CircularProgress size={16} color="inherit" /> }
      : leftIcon && { startIcon: leftIcon }
    ),
    ...(rightIcon && !loading && { endIcon: rightIcon }),
  }

  return (
    <Button {...muiProps}>
      {children}
    </Button>
  )
}

