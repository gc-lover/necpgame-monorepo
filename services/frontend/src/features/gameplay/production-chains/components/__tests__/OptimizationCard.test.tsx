import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { OptimizationCard } from '../OptimizationCard'

describe('OptimizationCard', () => {
  it('renders optimization tips', () => {
    render(
      <OptimizationCard
        goal="Reduce cycle time"
        expectedImprovement="-12%"
        tips=[
          { label: 'Upgrade facility', value: 'T3 Forge' },
        ]
      />,
    )

    expect(screen.getByText(/Reduce cycle time/i)).toBeInTheDocument()
    expect(screen.getByText(/T3 Forge/i)).toBeInTheDocument()
  })
})


