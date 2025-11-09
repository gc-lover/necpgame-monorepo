import { ButtonHTMLAttributes, ReactNode } from 'react'

/**
 * Типы кнопок
 */
export type ButtonVariant = 'primary' | 'secondary' | 'success' | 'danger' | 'ghost' | 'link'

/**
 * Размеры кнопок
 */
export type ButtonSize = 'sm' | 'md' | 'lg'

/**
 * Пропсы компонента Button
 */
export interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  /** Вариант стиля кнопки */
  variant?: ButtonVariant
  /** Размер кнопки */
  size?: ButtonSize
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
}

/**
 * Базовый компонент кнопки в киберпанк стиле
 * 
 * @example
 * ```tsx
 * <Button variant="primary" size="md">
 *   Нажми меня
 * </Button>
 * 
 * <Button variant="danger" leftIcon={<TrashIcon />}>
 *   Удалить
 * </Button>
 * ```
 */
export function Button({
  variant = 'primary',
  size = 'md',
  fullWidth = false,
  loading = false,
  leftIcon,
  rightIcon,
  children,
  className = '',
  disabled,
  ...props
}: ButtonProps) {
  // Базовые классы
  const baseClasses = 'btn font-semibold uppercase tracking-wider transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed'
  
  // Классы вариантов
  const variantClasses = {
    primary: 'btn-primary',
    secondary: 'bg-cyber-surface hover:bg-cyber-surface-hover border-cyber-border hover:border-cyber-border-hover',
    success: 'btn-success',
    danger: 'btn-danger',
    ghost: 'btn-ghost',
    link: 'bg-transparent border-transparent text-cyber-neon-cyan hover:text-cyber-neon-pink underline',
  }
  
  // Классы размеров
  const sizeClasses = {
    sm: 'px-4 py-2 text-xs',
    md: 'px-6 py-3 text-sm',
    lg: 'px-8 py-4 text-base',
  }
  
  const classes = [
    baseClasses,
    variantClasses[variant],
    sizeClasses[size],
    fullWidth ? 'w-full' : '',
    loading ? 'relative' : '',
    className,
  ].filter(Boolean).join(' ')
  
  return (
    <button
      className={classes}
      disabled={disabled || loading}
      {...props}
    >
      {loading && (
        <span className="absolute inset-0 flex items-center justify-center">
          <span className="loading-spinner w-4 h-4"></span>
        </span>
      )}
      
      <span className={`flex items-center justify-center gap-2 ${loading ? 'invisible' : ''}`} style={{ color: 'inherit' }}>
        {leftIcon && <span className="flex-shrink-0" style={{ color: 'inherit' }}>{leftIcon}</span>}
        <span style={{ color: 'inherit' }}>{children}</span>
        {rightIcon && <span className="flex-shrink-0" style={{ color: 'inherit' }}>{rightIcon}</span>}
      </span>
    </button>
  )
}

