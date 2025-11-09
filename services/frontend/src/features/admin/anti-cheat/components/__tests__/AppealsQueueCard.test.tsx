import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AppealsQueueCard } from '../AppealsQueueCard'

describe('AppealsQueueCard', () => {
  it('lists appeals', () => {
    render(
      <AppealsQueueCard
        appeals={[
          {
            appealId: 'apl-9001',
            banId: 'ban-1111',
            submittedAt: '2077-11-07 18:40',
            status: 'IN_REVIEW',
          },
        ]}
      />,
    )

    expect(screen.getByText(/ban-1111/i)).toBeInTheDocument()
    expect(screen.getByText(/IN_REVIEW/i)).toBeInTheDocument()
  })
})


