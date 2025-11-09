import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EndpointsCard } from '../EndpointsCard'

describe('EndpointsCard', () => {
  it('renders endpoints summary', () => {
    render(
      <EndpointsCard
        total={42}
        categories={[{ category: 'Auth', count: 6 }]}
        endpoints={[
          { endpoint: '/auth/login', method: 'POST', priority: 'CRITICAL', implemented: true },
        ]}
      />,
    )

    expect(screen.getByText(/42 endpoints/i)).toBeInTheDocument()
    expect(screen.getByText(/POST/i)).toBeInTheDocument()
  })
})


