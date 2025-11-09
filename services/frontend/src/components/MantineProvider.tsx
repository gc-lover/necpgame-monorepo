import { MantineProvider as MantineProviderBase, MantineThemeOverride } from '@mantine/core'
import { ReactNode } from 'react'

/**
 * Провайдер Mantine с киберпанк темой
 */
const cyberpunkTheme: MantineThemeOverride = {
  colorScheme: 'dark',
  primaryColor: 'cyan',
  colors: {
    cyan: [
      '#e0f7fa',
      '#b2ebf2',
      '#80deea',
      '#4dd0e1',
      '#26c6da',
      '#00bcd4',
      '#00acc1',
      '#0097a7',
      '#00838f',
      '#006064',
    ],
  },
  defaultRadius: 0,
  fontFamily: 'Orbitron, Rajdhani, sans-serif',
  headings: {
    fontFamily: 'Orbitron, sans-serif',
  },
}

interface MantineProviderProps {
  children: ReactNode
}

export function MantineProvider({ children }: MantineProviderProps) {
  return (
    <MantineProviderBase theme={cyberpunkTheme} withGlobalStyles withNormalizeCSS>
      {children}
    </MantineProviderBase>
  )
}

