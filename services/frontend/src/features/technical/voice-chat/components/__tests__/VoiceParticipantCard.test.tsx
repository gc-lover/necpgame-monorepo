import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { VoiceParticipantCard } from '../../components/VoiceParticipantCard'

describe('VoiceParticipantCard', () => {
  it('renders participant details', () => {
    render(
      <VoiceParticipantCard
        participant={{
          playerId: 'player-1',
          displayName: 'NovaRunner',
          role: 'leader',
          muted: false,
          deafened: false,
          speaking: true,
          latencyMs: 32,
        }}
      />,
    )

    expect(screen.getByText(/NovaRunner/i)).toBeInTheDocument()
    expect(screen.getByText(/Latency/i)).toBeInTheDocument()
  })
})


