import { HTMLAttributes, ReactNode } from 'react'

/**
 * Простая карточка с гарантированно видимым текстом
 */
export interface MantineCardProps extends HTMLAttributes<HTMLDivElement> {
  children: ReactNode
}

export function MantineCard({ children, className = '', ...props }: MantineCardProps) {
  return (
    <div 
      className={`bg-cyber-surface border-2 border-cyber-border p-6 text-white ${className}`}
      style={{ color: '#ffffff' }}
      {...props}
    >
      {children}
    </div>
  )
}

/**
 * Простые компоненты для структуры карточки
 */
export function CardHeader({ children, className = '', ...props }: HTMLAttributes<HTMLDivElement>) {
  return (
    <div className={`border-b-2 border-cyber-border pb-4 mb-4 ${className}`} {...props}>
      {children}
    </div>
  )
}

export function CardTitle({ children, className = '', ...props }: HTMLAttributes<HTMLHeadingElement>) {
  return (
    <h3 className={`text-2xl font-bold text-cyber-neon-cyan uppercase tracking-wider ${className}`} style={{ textShadow: '0 0 10px currentColor' }} {...props}>
      {children}
    </h3>
  )
}

export function CardBody({ children, className = '', ...props }: HTMLAttributes<HTMLDivElement>) {
  return (
    <div className={`space-y-4 ${className}`} {...props}>
      {children}
    </div>
  )
}

export function CardFooter({ children, className = '', ...props }: HTMLAttributes<HTMLDivElement>) {
  return (
    <div className={`border-t-2 border-cyber-border pt-4 mt-4 ${className}`} {...props}>
      {children}
    </div>
  )
}

