import { InputHTMLAttributes, ReactNode, forwardRef } from 'react'

/**
 * Пропсы компонента Input
 */
export interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  /** Лейбл */
  label?: string
  /** Текст ошибки */
  error?: string
  /** Текст подсказки */
  hint?: string
  /** Иконка слева */
  leftIcon?: ReactNode
  /** Иконка справа */
  rightIcon?: ReactNode
}

/**
 * Базовый компонент input в киберпанк стиле
 */
export const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, hint, leftIcon, rightIcon, className = '', ...props }, ref) => {
    return (
      <div className="w-full">
        {label && (
          <label className="input-label">
            {label}
            {props.required && <span className="text-cyber-neon-pink ml-1">*</span>}
          </label>
        )}
        
        <div className="relative">
          {leftIcon && (
            <div className="absolute left-4 top-1/2 -translate-y-1/2 text-white/70">
              {leftIcon}
            </div>
          )}
          
          <input
            ref={ref}
            className={`input ${leftIcon ? 'pl-12' : ''} ${rightIcon ? 'pr-12' : ''} ${error ? 'border-cyber-neon-pink' : ''} ${className}`}
            {...props}
          />
          
          {rightIcon && (
            <div className="absolute right-4 top-1/2 -translate-y-1/2 text-white/70">
              {rightIcon}
            </div>
          )}
        </div>
        
        {error && (
          <p className="text-cyber-neon-pink text-sm mt-2 flex items-center gap-2">
            <span>⚠</span>
            <span>{error}</span>
          </p>
        )}
        
        {hint && !error && (
          <p className="text-white/70 text-sm mt-2">
            {hint}
          </p>
        )}
      </div>
    )
  }
)

Input.displayName = 'Input'

