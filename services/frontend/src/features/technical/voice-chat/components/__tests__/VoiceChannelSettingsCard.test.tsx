import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { VoiceChannelSettingsCard } from '../../components/VoiceChannelSettingsCard'

describe('VoiceChannelSettingsCard', () => {
  it('renders channel settings', () => {
    render(
      <VoiceChannelSettingsCard
        settings={{
          qualityPreset: 'medium',
          autoCloseMinutes: 45,
          allowedRoles: ['captain', 'officer'],
          proximityEnabled: false,
        }}
      />,
    )

    expect(screen.getByText(/Channel Settings/i)).toBeInTheDocument()
    expect(screen.getByText(/captain/i)).toBeInTheDocument()
  })
})


