import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ReportSummaryCard } from '../ReportSummaryCard'

describe('ReportSummaryCard', () => {
  it('renders reports list', () => {
    render(
      <ReportSummaryCard
        reports={[
          {
            reportId: 'rep-1234',
            cheatType: 'AIMBOT',
            status: 'PENDING',
            severity: 'HIGH',
            lastSeen: '5m ago',
          },
        ]}
      />,
    )

    expect(screen.getByText(/AIMBOT/i)).toBeInTheDocument()
    expect(screen.getByText(/5m ago/i)).toBeInTheDocument()
  })
})


