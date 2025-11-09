import { ButtonHTMLAttributes, ReactNode } from 'react'

/**
 * Простая кнопка с гарантированно видимым текстом
 * Используем простые стили вместо Mantine чтобы избежать конфликтов
 */
export interface MantineButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: ReactNode
  color?: 'cyan' | 'pink' | 'green' | 'purple' | 'yellow' | 'red'
  variant?: 'filled' | 'outline' | 'subtle' | 'light'
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl'
}

const colorClasses = {
  cyan: 'bg-cyber-neon-cyan/40 border-cyber-neon-cyan text-white hover:bg-cyber-neon-cyan/60',
  pink: 'bg-cyber-neon-pink/40 border-cyber-neon-pink text-white hover:bg-cyber-neon-pink/60',
  green: 'bg-cyber-neon-green/40 border-cyber-neon-green text-white hover:bg-cyber-neon-green/60',
  purple: 'bg-cyber-neon-purple/40 border-cyber-neon-purple text-white hover:bg-cyber-neon-purple/60',
  yellow: 'bg-cyber-neon-yellow/40 border-cyber-neon-yellow text-white hover:bg-cyber-neon-yellow/60',
  red: 'bg-cyber-neon-pink/40 border-cyber-neon-pink text-white hover:bg-cyber-neon-pink/60',
}

const sizeClasses = {
  xs: 'px-2 py-1 text-xs',
  sm: 'px-4 py-2 text-sm',
  md: 'px-6 py-3 text-base',
  lg: 'px-8 py-4 text-lg',
  xl: 'px-10 py-5 text-xl',
}

export function MantineButton({ 
  children, 
  color = 'cyan',
  variant = 'filled',
  size = 'md',
  className = '',
  ...props 
}: MantineButtonProps) {
  const baseClasses = 'font-bold border-2 transition-colors disabled:opacity-50 disabled:cursor-not-allowed'
  const variantClasses = variant === 'outline' 
    ? 'bg-transparent border-2'
    : colorClasses[color] || colorClasses.cyan
  
  const classes = [
    baseClasses,
    variantClasses,
    sizeClasses[size],
    className,
  ].filter(Boolean).join(' ')
  
  return (
    <button className={classes} style={{ color: '#ffffff' }} {...props}>
      {children}
    </button>
  )
}

