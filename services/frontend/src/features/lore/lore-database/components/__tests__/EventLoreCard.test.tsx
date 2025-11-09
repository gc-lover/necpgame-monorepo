import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EventLoreCard } from '../../components/EventLoreCard'

describe('EventLoreCard', () => {
  it('renders event overview', () => {
    render(
      <EventLoreCard
        event={{
          name: 'Fifth Corporate War',
          years: '2069-2077',
          participants: ['Arasaka'],
          outcome: 'Stalemate',
          phases: [{ phase: 'Net War', description: 'AI conflict' }],
        }}
      />,
    )

    expect(screen.getByText(/Fifth Corporate War/i)).toBeInTheDocument()
    expect(screen.getByText(/Stalemate/i)).toBeInTheDocument()
  })
})


