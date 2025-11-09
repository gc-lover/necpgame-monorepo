import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ProductionJobsCard } from '../ProductionJobsCard'

describe('ProductionJobsCard', () => {
  it('renders job progress', () => {
    render(
      <ProductionJobsCard
        maxConcurrent={3}
        jobs=[
          {
            jobId: 'job-1',
            stage: 'Assembly',
            progressPercent: 65,
            timeLeft: '45m',
            facility: 'Night City Plant',
            status: 'running',
          },
        ]
      />,
    )

    expect(screen.getByText(/Assembly/i)).toBeInTheDocument()
    expect(screen.getByText(/45m/i)).toBeInTheDocument()
  })
})


