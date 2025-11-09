import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { VoiceQualityCard } from '../../components/VoiceQualityCard'

describe('VoiceQualityCard', () => {
  it('renders quality metrics', () => {
    render(
      <VoiceQualityCard
        profile={{
          bitrateKbps: 96,
          packetLoss: 0.8,
          jitter: 4,
          status: 'good',
        }}
      />,
    )

    expect(screen.getByText(/Voice Quality/i)).toBeInTheDocument()
    expect(screen.getByText(/96 kbps/i)).toBeInTheDocument()
  })
})


