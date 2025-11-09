import { Card, CardProps, CardContent, CardHeader, CardActions } from '@mui/material'
import { ReactNode } from 'react'

/**
 * Карточка Material UI с киберпанк темой
 */
export function MUICard({ children, ...props }: CardProps) {
  return (
    <Card {...props}>
      {children}
    </Card>
  )
}

/**
 * Тело карточки
 */
export function MUICardContent({ children, ...props }: { children: ReactNode }) {
  return (
    <CardContent {...props}>
      {children}
    </CardContent>
  )
}

/**
 * Заголовок карточки
 */
export function MUICardHeader({ children, title, action, ...props }: { children?: ReactNode; title?: string; action?: ReactNode }) {
  return (
    <CardHeader title={title} action={action} {...props}>
      {children}
    </CardHeader>
  )
}

/**
 * Подвал карточки
 */
export function MUICardActions({ children, ...props }: { children: ReactNode }) {
  return (
    <CardActions {...props}>
      {children}
    </CardActions>
  )
}

