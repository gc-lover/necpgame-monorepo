import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ReadyCheckCard } from '../../components/ReadyCheckCard'

describe('ReadyCheckCard', () => {
  it('renders ready check info', () => {
    render(
      <ReadyCheckCard
        state={{
          matchId: 'MATCH-1',
          expiresInSeconds: 30,
          accepted: 8,
          declined: 1,
          pending: 1,
        }}
      />,
    )

    expect(screen.getByText(/MATCH-1/i)).toBeInTheDocument()
    expect(screen.getByText(/Expires/i)).toBeInTheDocument()
  })
})


