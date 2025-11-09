import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ReloadControlCard } from '../../components/ReloadControlCard'

describe('ReloadControlCard', () => {
  it('shows reload status', () => {
    render(
      <ReloadControlCard
        status={{
          lastReload: '2025-11-08',
          triggeredBy: 'automation',
          status: 'SUCCESS',
        }}
      />,
    )

    expect(screen.getByText(/Configuration Reload/i)).toBeInTheDocument()
    expect(screen.getByText(/automation/i)).toBeInTheDocument()
  })
})


