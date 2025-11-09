import { SelectHTMLAttributes, forwardRef } from 'react'

/**
 * Опция для Select
 */
export interface SelectOption {
  value: string
  label: string
  disabled?: boolean
}

/**
 * Пропсы компонента Select
 */
export interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
  /** Лейбл */
  label?: string
  /** Опции */
  options: SelectOption[]
  /** Текст ошибки */
  error?: string
  /** Placeholder опция */
  placeholder?: string
}

/**
 * Базовый компонент select в киберпанк стиле
 */
export const Select = forwardRef<HTMLSelectElement, SelectProps>(
  ({ label, options, error, placeholder, className = '', ...props }, ref) => {
    return (
      <div className="w-full">
        {label && (
          <label className="input-label">
            {label}
            {props.required && <span className="text-cyber-neon-pink ml-1">*</span>}
          </label>
        )}
        
        <div className="relative">
          <select
            ref={ref}
            className={`input appearance-none cursor-pointer ${error ? 'border-cyber-neon-pink' : ''} ${className}`}
            {...props}
          >
            {placeholder && (
              <option value="" disabled>
                {placeholder}
              </option>
            )}
            {options.map((option) => (
              <option
                key={option.value}
                value={option.value}
                disabled={option.disabled}
              >
                {option.label}
              </option>
            ))}
          </select>
          
          {/* Custom arrow */}
          <div className="absolute right-4 top-1/2 -translate-y-1/2 pointer-events-none text-cyber-neon-cyan">
            ▾
          </div>
        </div>
        
        {error && (
          <p className="text-cyber-neon-pink text-sm mt-2 flex items-center gap-2">
            <span>⚠</span>
            <span>{error}</span>
          </p>
        )}
      </div>
    )
  }
)

Select.displayName = 'Select'

