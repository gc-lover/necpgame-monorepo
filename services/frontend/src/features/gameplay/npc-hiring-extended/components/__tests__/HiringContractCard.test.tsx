import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { HiringContractCard } from '../HiringContractCard'

describe('HiringContractCard', () => {
  it('renders contract details', () => {
    render(
      <HiringContractCard
        contract={{
          contractId: 'contract-1',
          npcName: 'Delta Viper',
          termDays: 30,
          dailyRate: 3800,
          signingBonus: 15000,
          clauses: ['Hazard pay in Badlands', 'Mission exclusivity'],
          risk: 'HIGH',
        }}
      />,
    )

    expect(screen.getByText(/Delta Viper/i)).toBeInTheDocument()
    expect(screen.getByText(/Hazard pay/i)).toBeInTheDocument()
  })
})


