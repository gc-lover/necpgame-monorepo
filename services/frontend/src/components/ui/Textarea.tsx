import { TextareaHTMLAttributes, forwardRef } from 'react'

/**
 * Пропсы компонента Textarea
 */
export interface TextareaProps extends TextareaHTMLAttributes<HTMLTextAreaElement> {
  /** Лейбл */
  label?: string
  /** Текст ошибки */
  error?: string
  /** Показывать счетчик символов */
  showCount?: boolean
}

/**
 * Базовый компонент textarea в киберпанк стиле
 */
export const Textarea = forwardRef<HTMLTextAreaElement, TextareaProps>(
  ({ label, error, showCount = false, className = '', value, maxLength, ...props }, ref) => {
    const currentLength = typeof value === 'string' ? value.length : 0
    
    return (
      <div className="w-full">
        {label && (
          <label className="input-label">
            {label}
            {props.required && <span className="text-cyber-neon-pink ml-1">*</span>}
          </label>
        )}
        
        <textarea
          ref={ref}
          className={`textarea ${error ? 'border-cyber-neon-pink' : ''} ${className}`}
          value={value}
          maxLength={maxLength}
          {...props}
        />
        
        <div className="flex items-center justify-between mt-2">
          <div>
            {error && (
              <p className="text-cyber-neon-pink text-sm flex items-center gap-2">
                <span>⚠</span>
                <span>{error}</span>
              </p>
            )}
          </div>
          
          {showCount && maxLength && (
            <p className="text-white/70 text-sm font-mono-cyber">
              {currentLength} / {maxLength}
            </p>
          )}
        </div>
      </div>
    )
  }
)

Textarea.displayName = 'Textarea'

