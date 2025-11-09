import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EnvironmentSummaryCard } from '../../components/EnvironmentSummaryCard'

describe('EnvironmentSummaryCard', () => {
  it('shows environment summary', () => {
    render(
      <EnvironmentSummaryCard summary={{ name: 'production', services: 42, overrides: 12, driftAlerts: 1 }} />,
    )

    expect(screen.getByText(/production Environment/i)).toBeInTheDocument()
    expect(screen.getByText(/Overrides/i)).toBeInTheDocument()
  })
})


