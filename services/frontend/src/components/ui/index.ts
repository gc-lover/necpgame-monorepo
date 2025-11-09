/**
 * Библиотека UI компонентов в киберпанк стиле
 * 
 * Все базовые компоненты для построения интерфейса NECPGAME
 * 
 * ВНИМАНИЕ: Используйте Mantine компоненты для надежной работы!
 */

// Material UI компоненты (рекомендуется)
export { MUIButton as Button } from './MUIButton'
export type { MUIButtonProps as ButtonProps } from './MUIButton'
export { 
  MUICard as Card,
  MUICardContent as CardBody,
  MUICardHeader as CardHeader,
  MUICardActions as CardFooter,
} from './MUICard'

// Mantine компоненты (альтернатива)
export { MantineButton } from './MantineButton'
export { 
  MantineCard,
  CardHeader as MantineCardHeader,
  CardTitle as MantineCardTitle,
  CardBody as MantineCardBody,
  CardFooter as MantineCardFooter,
} from './MantineCard'

// Старые компоненты (deprecated - могут иметь проблемы с цветами)
// export { Button } from './Button'
// export type { ButtonProps, ButtonVariant, ButtonSize } from './Button'
// export { Card, CardHeader, CardTitle, CardBody, CardFooter } from './Card'
// export type { CardProps } from './Card'

// Material UI компоненты форм (рекомендуется)
export { MUIInput as Input } from './MUIInput'
export type { MUIInputProps as InputProps } from './MUIInput'

export { MUISelect as Select } from './MUISelect'
export type { MUISelectProps as SelectProps, MUISelectOption as SelectOption } from './MUISelect'

export { MUIRadioGroup as RadioGroup } from './MUIRadioGroup'
export type { MUIRadioGroupProps as RadioGroupProps, MUIRadioOption as RadioOption } from './MUIRadioGroup'

export { MUIToggleButtonGroup as ToggleButtonGroup } from './MUIToggleButtonGroup'
export type { MUIToggleButtonGroupProps as ToggleButtonGroupProps, MUIToggleButtonOption as ToggleButtonOption } from './MUIToggleButtonGroup'

export { MUISlider as Slider } from './MUISlider'
export type { MUISliderProps as SliderProps } from './MUISlider'

export { MUITextarea as Textarea } from './MUITextarea'
export type { MUITextareaProps as TextareaProps } from './MUITextarea'

// Старые компоненты (deprecated)
// export { Input } from './Input'
// export type { InputProps } from './Input'
// export { Select } from './Select'
// export type { SelectProps, SelectOption } from './Select'
// export { Textarea } from './Textarea'
// export type { TextareaProps } from './Textarea'

// Навигация и организация
export { Modal, ModalFooter } from './Modal'
export type { ModalProps } from './Modal'

export { Tabs } from './Tabs'
export type { TabsProps, Tab } from './Tabs'

// Обратная связь
export { Badge } from './Badge'
export type { BadgeProps, BadgeVariant } from './Badge'

export { Alert } from './Alert'
export type { AlertProps, AlertType } from './Alert'

export { Progress } from './Progress'
export type { ProgressProps } from './Progress'

