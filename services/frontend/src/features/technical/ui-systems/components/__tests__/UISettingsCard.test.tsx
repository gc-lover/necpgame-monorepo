import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { UISettingsCard } from '../../components/UISettingsCard'

describe('UISettingsCard', () => {
  it('shows settings summary', () => {
    render(
      <UISettingsCard
        settings={{
          presetName: 'Night Ops',
          brightness: 60,
          saturation: 75,
          accessibility: ['Colorblind'],
          settings: [
            { key: 'hud.scale', label: 'HUD Scale', value: '0.9' },
          ],
        }}
      />,
    )

    expect(screen.getByText(/Night Ops/i)).toBeInTheDocument()
    expect(screen.getByText(/HUD Scale/i)).toBeInTheDocument()
  })
})


