import { Slider, SliderProps, FormControl, FormLabel, Typography } from '@mui/material'

/**
 * Пропсы компонента Slider
 */
export interface MUISliderProps extends SliderProps {
  label?: string
  valueLabel?: string
  showValue?: boolean
}

/**
 * Слайдер Material UI с киберпанк темой
 */
export function MUISlider({ label, valueLabel, showValue, value, ...props }: MUISliderProps) {
  return (
    <FormControl fullWidth>
      {label && (
        <FormLabel>
          {label}
          {showValue && valueLabel && (
            <Typography component="span" sx={{ ml: 1, color: 'primary.main' }}>
              {valueLabel}
            </Typography>
          )}
        </FormLabel>
      )}
      <Slider
        {...props}
        value={value}
        valueLabelDisplay={showValue ? 'auto' : 'off'}
      />
      {showValue && !valueLabel && typeof value === 'number' && (
        <Typography variant="body2" sx={{ mt: 1, textAlign: 'center', color: 'text.secondary' }}>
          {value}
        </Typography>
      )}
    </FormControl>
  )
}

