import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ForceLogoutCard } from '../../components/ForceLogoutCard'

describe('ForceLogoutCard', () => {
  it('renders force logout plan', () => {
    render(
      <ForceLogoutCard
        plan={{
          playerId: 'player-1',
          accountId: 'account-1',
          notify: true,
          scheduledFor: '2025-11-08 04:20',
        }}
      />,
    )

    expect(screen.getByText(/Force Logout/i)).toBeInTheDocument()
    expect(screen.getByText(/player-1/i)).toBeInTheDocument()
  })
})


