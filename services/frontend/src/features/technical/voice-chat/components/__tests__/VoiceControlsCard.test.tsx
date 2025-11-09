import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { VoiceControlsCard } from '../../components/VoiceControlsCard'

describe('VoiceControlsCard', () => {
  it('renders control settings', () => {
    render(
      <VoiceControlsCard
        controls={{
          inputDevice: 'Mic',
          outputDevice: 'Headset',
          noiseSuppression: true,
          echoCancellation: true,
          spatialAudio: false,
        }}
      />,
    )

    expect(screen.getByText(/Input: Mic/i)).toBeInTheDocument()
    expect(screen.getByText(/Spatial/i)).toBeInTheDocument()
  })
})


