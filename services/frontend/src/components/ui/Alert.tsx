import { ReactNode } from 'react'

/**
 * Типы Alert
 */
export type AlertType = 'info' | 'success' | 'warning' | 'error'

/**
 * Пропсы компонента Alert
 */
export interface AlertProps {
  /** Тип алерта */
  type?: AlertType
  /** Заголовок */
  title?: string
  /** Дочерние элементы */
  children: ReactNode
  /** Callback при закрытии */
  onClose?: () => void
}

/**
 * Компонент алерта в киберпанк стиле
 */
export function Alert({ type = 'info', title, children, onClose }: AlertProps) {
  const typeClasses = {
    info: 'alert-info',
    success: 'alert-success',
    warning: 'alert-warning',
    error: 'alert-error',
  }
  
  const icons = {
    info: 'ℹ',
    success: '✓',
    warning: '⚠',
    error: '✕',
  }
  
  return (
    <div className={typeClasses[type]}>
      <div className="flex items-start gap-3">
        <span className="text-2xl flex-shrink-0">{icons[type]}</span>
        <div className="flex-1">
          {title && <p className="font-bold mb-1">{title}</p>}
          <div className="text-sm">{children}</div>
        </div>
        {onClose && (
          <button
            onClick={onClose}
            className="ml-auto text-current opacity-60 hover:opacity-100 transition-opacity flex-shrink-0"
            aria-label="Закрыть"
          >
            ✕
          </button>
        )}
      </div>
    </div>
  )
}

