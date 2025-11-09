import { HTMLAttributes } from 'react'

/**
 * Пропсы компонента Progress
 */
export interface ProgressProps extends HTMLAttributes<HTMLDivElement> {
  /** Текущее значение (0-100) */
  value: number
  /** Максимальное значение */
  max?: number
  /** Показывать процент */
  showPercent?: boolean
  /** Цвет прогресс-бара */
  color?: 'cyan' | 'pink' | 'green' | 'purple' | 'yellow'
  /** Размер */
  size?: 'sm' | 'md' | 'lg'
}

/**
 * Компонент прогресс-бара в киберпанк стиле
 */
export function Progress({
  value,
  max = 100,
  showPercent = false,
  color = 'cyan',
  size = 'md',
  className = '',
  ...props
}: ProgressProps) {
  const percentage = Math.min(Math.max((value / max) * 100, 0), 100)
  
  const colorClasses = {
    cyan: 'bg-cyber-neon-cyan shadow-neon-cyan',
    pink: 'bg-cyber-neon-pink shadow-neon-pink',
    green: 'bg-cyber-neon-green shadow-neon-green',
    purple: 'bg-cyber-neon-purple shadow-neon-purple',
    yellow: 'bg-cyber-neon-yellow',
  }
  
  const sizeClasses = {
    sm: 'h-2',
    md: 'h-3',
    lg: 'h-4',
  }
  
  return (
    <div className={`w-full ${className}`} {...props}>
      <div className="flex items-center justify-between mb-2">
        {showPercent && (
          <span className="text-white/90 text-sm font-mono-cyber">
            {Math.round(percentage)}%
          </span>
        )}
      </div>
      
      <div className={`w-full bg-cyber-surface border-2 border-cyber-border rounded-none overflow-hidden ${sizeClasses[size]}`}>
        <div
          className={`h-full transition-all duration-300 ${colorClasses[color]}`}
          style={{ width: `${percentage}%` }}
        />
      </div>
    </div>
  )
}

