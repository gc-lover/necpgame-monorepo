import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { PlotThreadCard } from '../PlotThreadCard'

describe('PlotThreadCard', () => {
  it('renders plot thread info', () => {
    render(
      <PlotThreadCard
        thread={{
          threadId: 'thread-1',
          title: 'Phantom Rebellion',
          faction: 'Neon Phantoms',
          arcStage: 'CONFLICT',
          coherenceScore: 82,
          openBeats: 4,
          resolvedBeats: 6,
          synopsis: 'Underground cells prepare for corporate takedown.',
        }}
      />,
    )

    expect(screen.getByText(/Phantom Rebellion/i)).toBeInTheDocument()
    expect(screen.getByText(/Underground cells/i)).toBeInTheDocument()
  })
})


