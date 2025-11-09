import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RcaCard } from '../../components/RcaCard'

describe('RcaCard', () => {
  it('renders RCA details', () => {
    render(
      <RcaCard
        rca={{
          rootCause: 'Cache misconfiguration',
          contributingFactors: ['No warmup'],
          correctiveActions: [{ description: 'Add validation', owner: 'sre-team', dueDate: '2025-11-20', status: 'pending' }],
          lessonsLearned: ['Require canary'],
        }}
      />,
    )

    expect(screen.getByText(/Cache misconfiguration/i)).toBeInTheDocument()
    expect(screen.getByText(/Add validation/i)).toBeInTheDocument()
  })
})


