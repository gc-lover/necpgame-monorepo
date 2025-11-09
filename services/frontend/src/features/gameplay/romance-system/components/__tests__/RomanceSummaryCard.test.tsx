import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RomanceSummaryCard } from '../RomanceSummaryCard'

describe('RomanceSummaryCard', () => {
  it('renders summary metrics', () => {
    render(
      <RomanceSummaryCard
        activeRelationships={2}
        maxConcurrent={3}
        jealousyAlerts={1}
        conflicts={0}
        commitmentRate={45}
      />,
    )

    expect(screen.getByText(/Active romances/i)).toBeInTheDocument()
    expect(screen.getByText(/Jealousy alerts: 1/i)).toBeInTheDocument()
  })
})


