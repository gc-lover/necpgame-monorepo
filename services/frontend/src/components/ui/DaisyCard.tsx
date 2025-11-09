import { HTMLAttributes, ReactNode } from 'react'

/**
 * Пропсы компонента DaisyCard
 */
export interface DaisyCardProps extends HTMLAttributes<HTMLDivElement> {
  /** Дочерние элементы */
  children: ReactNode
  /** Эффект hover с границей */
  bordered?: boolean
  /** Тень */
  shadow?: boolean
  /** Без padding */
  noPadding?: boolean
}

/**
 * Карточка на основе DaisyUI
 */
export function DaisyCard({ 
  children, 
  bordered = true,
  shadow = true,
  noPadding = false,
  className = '',
  ...props 
}: DaisyCardProps) {
  const classes = [
    'card',
    'bg-base-300',
    'text-base-content',
    bordered && 'card-bordered',
    shadow && 'shadow-lg',
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
 * Тело карточки
 */
export function DaisyCardBody({ children, className = '', ...props }: HTMLAttributes<HTMLDivElement>) {
  return (
    <div className={`card-body ${className}`} {...props}>
      {children}
    </div>
  )
}

/**
 * Заголовок карточки
 */
export function DaisyCardTitle({ children, className = '', ...props }: HTMLAttributes<HTMLHeadingElement>) {
  return (
    <h2 className={`card-title ${className}`} {...props}>
      {children}
    </h2>
  )
}

/**
 * Подвал карточки
 */
export function DaisyCardActions({ children, className = '', ...props }: HTMLAttributes<HTMLDivElement>) {
  return (
    <div className={`card-actions ${className}`} {...props}>
      {children}
    </div>
  )
}

