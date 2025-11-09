import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SpatialAudioCard } from '../../components/SpatialAudioCard'

describe('SpatialAudioCard', () => {
  it('renders spatial metrics', () => {
    render(
      <SpatialAudioCard
        metrics={[
          { participantId: 'Nova', angle: 15, distance: 3.2, volume: 80 },
        ]}
      />,
    )

    expect(screen.getByText(/Nova/i)).toBeInTheDocument()
    expect(screen.getByText(/3m/i)).toBeInTheDocument()
  })
})


