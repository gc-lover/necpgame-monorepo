import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SessionDiagnosticsCard } from '../../components/SessionDiagnosticsCard'

describe('SessionDiagnosticsCard', () => {
  it('renders diagnostics', () => {
    render(
      <SessionDiagnosticsCard
        diagnostics={[
          { label: 'Concurrent sessions', value: '1 of 1', status: 'ok' },
        ]}
      />,
    )

    expect(screen.getByText(/Concurrent sessions/i)).toBeInTheDocument()
    expect(screen.getByText(/1 of 1/i)).toBeInTheDocument()
  })
})


