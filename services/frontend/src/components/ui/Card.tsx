import { HTMLAttributes, ReactNode } from 'react'

/**
 * Пропсы компонента Card
 */
export interface CardProps extends HTMLAttributes<HTMLDivElement> {
  /** Дочерние элементы */
  children: ReactNode
  /** Эффект hover */
  hoverable?: boolean
  /** Без padding */
  noPadding?: boolean
}

/**
 * Базовый компонент карточки в киберпанк стиле
 */
export function Card({ 
  children, 
  hoverable = false, 
  noPadding = false,
  className = '',
  ...props 
}: CardProps) {
  const classes = [
    'card',
    hoverable ? 'hover-glow cursor-pointer' : '',
    noPadding ? 'p-0' : '',
    className,
  ].filter(Boolean).join(' ')
  
  return (
    <div className={classes} {...props}>
      {children}
    </div>
  )
}

/**
 * Заголовок карточки
 */
export function CardHeader({ children, className = '', ...props }: HTMLAttributes<HTMLDivElement>) {
  return (
    <div className={`card-header ${className}`} {...props}>
      {children}
    </div>
  )
}

/**
 * Заглавие карточки
 */
export function CardTitle({ children, className = '', ...props }: HTMLAttributes<HTMLHeadingElement>) {
  return (
    <h3 className={`card-title ${className}`} {...props}>
      {children}
    </h3>
  )
}

/**
 * Тело карточки
 */
export function CardBody({ children, className = '', ...props }: HTMLAttributes<HTMLDivElement>) {
  return (
    <div className={`space-y-4 ${className}`} {...props}>
      {children}
    </div>
  )
}

/**
 * Подвал карточки
 */
export function CardFooter({ children, className = '', ...props }: HTMLAttributes<HTMLDivElement>) {
  return (
    <div className={`border-t-2 border-cyber-border pt-4 mt-4 ${className}`} {...props}>
      {children}
    </div>
  )
}

