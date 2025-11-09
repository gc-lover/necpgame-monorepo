import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SessionCard } from '../../components/SessionCard'

describe('SessionCard', () => {
  it('renders session info', () => {
    render(
      <SessionCard
        session={{
          sessionId: 'sess-1',
          playerId: 'player-1',
          characterId: 'char-1',
          status: 'ACTIVE',
          createdAt: '2025-11-08 04:10',
          lastHeartbeatAt: '2025-11-08 04:12',
          expiresAt: '2025-11-08 04:40',
        }}
      />,
    )

    expect(screen.getByText(/sess-1/i)).toBeInTheDocument()
    expect(screen.getByText(/Player/i)).toBeInTheDocument()
  })
})


