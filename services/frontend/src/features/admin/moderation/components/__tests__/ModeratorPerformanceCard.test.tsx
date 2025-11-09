import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ModeratorPerformanceCard } from '../ModeratorPerformanceCard'

describe('ModeratorPerformanceCard', () => {
  it('renders moderator stats', () => {
    render(
      <ModeratorPerformanceCard
        performance={[
          {
            moderatorId: 'Mod-Lexa',
            handledCases: 28,
            slaCompliancePercent: 92,
            averageResolutionTime: '18m',
          },
        ]}
      />,
    )

    expect(screen.getByText(/Mod-Lexa/i)).toBeInTheDocument()
    expect(screen.getByText(/18m/i)).toBeInTheDocument()
  })
})


