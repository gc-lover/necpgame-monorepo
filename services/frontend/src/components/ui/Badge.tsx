import { HTMLAttributes, ReactNode } from 'react'

/**
 * Варианты Badge
 */
export type BadgeVariant = 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info'

/**
 * Пропсы компонента Badge
 */
export interface BadgeProps extends HTMLAttributes<HTMLSpanElement> {
  /** Вариант стиля */
  variant?: BadgeVariant
  /** Дочерние элементы */
  children: ReactNode
}

/**
 * Компонент бейджа в киберпанк стиле
 */
export function Badge({ variant = 'primary', children, className = '', ...props }: BadgeProps) {
  const variantClasses = {
    primary: 'badge-primary',
    secondary: 'badge bg-cyber-surface text-white/90 border-cyber-border',
    success: 'badge bg-cyber-neon-green/20 text-cyber-neon-green border-cyber-neon-green',
    danger: 'badge bg-cyber-neon-pink/20 text-cyber-neon-pink border-cyber-neon-pink',
    warning: 'badge bg-cyber-neon-yellow/20 text-cyber-neon-yellow border-cyber-neon-yellow',
    info: 'badge bg-cyber-neon-cyan/20 text-cyber-neon-cyan border-cyber-neon-cyan',
  }
  
  return (
    <span className={`${variantClasses[variant]} ${className}`} {...props}>
      {children}
    </span>
  )
}

