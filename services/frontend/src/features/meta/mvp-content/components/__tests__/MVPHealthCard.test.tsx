import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MVPHealthCard } from '../MVPHealthCard'

describe('MVPHealthCard', () => {
  it('renders health status', () => {
    render(
      <MVPHealthCard
        health={{
          status: 'DEGRADED',
          systems: {
            auth: 'HEALTHY',
            player_management: 'DEGRADED',
            quest_engine: 'HEALTHY',
          },
        }}
      />,
    )

    expect(screen.getByText(/DEGRADED/i)).toBeInTheDocument()
    expect(screen.getByText(/player management/i)).toBeInTheDocument()
  })
})


