import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ServiceConfigCard } from '../../components/ServiceConfigCard'

describe('ServiceConfigCard', () => {
  it('renders service configuration info', () => {
    render(
      <ServiceConfigCard
        config={{
          serviceName: 'matchmaking-service',
          environment: 'production',
          version: 'v1',
          configuration: { latencyCapMs: 90 },
        }}
      />,
    )

    expect(screen.getByText(/matchmaking-service/i)).toBeInTheDocument()
    expect(screen.getByText(/Version: v1/i)).toBeInTheDocument()
  })
})


