import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CompatibilityCard } from '../../components/CompatibilityCard'

describe('CompatibilityCard', () => {
  it('renders compatibility data', () => {
    render(
      <CompatibilityCard
        summary={{
          score: 88,
          recommendation: 'Proceed with shared mission',
          stage: 'DATING',
          factors: [{ name: 'Chemistry', weight: 0.4, contribution: 0.9 }],
        }}
      />,
    )

    expect(screen.getByText(/Romance Compatibility/i)).toBeInTheDocument()
    expect(screen.getByText(/88%/i)).toBeInTheDocument()
  })
})


