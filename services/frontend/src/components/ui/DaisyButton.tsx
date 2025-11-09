import { ButtonHTMLAttributes, ReactNode } from 'react'

/**
 * Типы кнопок DaisyUI
 */
export type DaisyButtonVariant = 'primary' | 'secondary' | 'accent' | 'success' | 'warning' | 'error' | 'ghost' | 'link'

/**
 * Размеры кнопок
 */
export type DaisyButtonSize = 'xs' | 'sm' | 'md' | 'lg'

/**
 * Пропсы компонента DaisyButton
 */
export interface DaisyButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  /** Вариант стиля кнопки */
  variant?: DaisyButtonVariant
  /** Размер кнопки */
  size?: DaisyButtonSize
  /** Кнопка во всю ширину */
  fullWidth?: boolean
  /** Состояние загрузки */
  loading?: boolean
  /** Иконка слева */
  leftIcon?: ReactNode
  /** Иконка справа */
  rightIcon?: ReactNode
  /** Дочерние элементы */
  children: ReactNode
  /** Outline стиль */
  outline?: boolean
}

/**
 * Кнопка на основе DaisyUI
 * 
 * Использует готовые компоненты DaisyUI для гарантированной видимости текста
 */
export function DaisyButton({
  variant = 'primary',
  size = 'md',
  fullWidth = false,
  loading = false,
  leftIcon,
  rightIcon,
  children,
  className = '',
  disabled,
  outline = false,
  ...props
}: DaisyButtonProps) {
  const variantClass = outline ? `btn-outline btn-${variant}` : `btn-${variant}`
  const sizeClass = size !== 'md' ? `btn-${size}` : ''
  const fullWidthClass = fullWidth ? 'btn-block' : ''
  const loadingClass = loading ? 'loading' : ''
  
  const classes = [
    'btn',
    variantClass,
    sizeClass,
    fullWidthClass,
    loadingClass,
    className,
  ].filter(Boolean).join(' ')
  
  return (
    <button
      className={classes}
      disabled={disabled || loading}
      {...props}
    >
      {leftIcon && <span className="mr-2">{leftIcon}</span>}
      {children}
      {rightIcon && <span className="ml-2">{rightIcon}</span>}
    </button>
  )
}

