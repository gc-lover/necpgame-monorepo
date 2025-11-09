import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { VoiceChannelCard } from '../../components/VoiceChannelCard'

describe('VoiceChannelCard', () => {
  it('renders channel information', () => {
    render(
      <VoiceChannelCard
        channel={{
          channelId: 'voice-1',
          channelName: 'Valentinos HQ',
          channelType: 'guild',
          owner: 'Guild Valentinos',
          isActive: true,
          participants: 18,
          maxParticipants: 32,
        }}
      />,
    )

    expect(screen.getByText(/Valentinos HQ/i)).toBeInTheDocument()
    expect(screen.getByText(/Participants/i)).toBeInTheDocument()
  })
})


