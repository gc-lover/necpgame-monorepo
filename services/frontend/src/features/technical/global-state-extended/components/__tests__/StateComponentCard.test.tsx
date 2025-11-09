import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { StateComponentCard } from '../StateComponentCard'

describe('StateComponentCard', () => {
  it('renders component details', () => {
    render(
      <StateComponentCard
        state={{
          component: 'WORLD',
          version: 102,
          health: 94,
          pendingMutations: 3,
          drift: 2,
          lastUpdated: '2077-11-08 00:12',
        }}
      />,
    )

    expect(screen.getByText(/WORLD state/i)).toBeInTheDocument()
    expect(screen.getByText(/Pending mutations/i)).toBeInTheDocument()
  })
})


