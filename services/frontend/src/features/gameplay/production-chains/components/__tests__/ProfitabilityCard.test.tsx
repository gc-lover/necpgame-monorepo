import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ProfitabilityCard } from '../ProfitabilityCard'

describe('ProfitabilityCard', () => {
  it('renders profitability data', () => {
    render(
      <ProfitabilityCard
        chainName="Legendary Weapons"
        profitPerCycle={12500}
        roiPercent={32.5}
        cycleTime="4h"
        recommendations={['Upgrade smelter to T3']}
      />,
    )

    expect(screen.getByText(/Legendary Weapons/i)).toBeInTheDocument()
    expect(screen.getByText(/Upgrade smelter/i)).toBeInTheDocument()
  })
})


