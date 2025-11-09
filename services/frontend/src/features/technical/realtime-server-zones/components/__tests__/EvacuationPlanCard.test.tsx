import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EvacuationPlanCard } from '../../components/EvacuationPlanCard'

describe('EvacuationPlanCard', () => {
  it('renders evacuation plan information', () => {
    render(
      <EvacuationPlanCard
        plan={{
          targetZoneId: 'night-city.safe',
          batchSize: 25,
          intervalMs: 500,
          notifyPlayers: true,
          timeoutSeconds: 90,
        }}
      />,
    )

    expect(screen.getByText(/night-city.safe/i)).toBeInTheDocument()
    expect(screen.getByText(/Batch size/i)).toBeInTheDocument()
  })
})


