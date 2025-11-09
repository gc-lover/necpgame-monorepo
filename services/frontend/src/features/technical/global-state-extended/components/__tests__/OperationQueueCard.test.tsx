import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { OperationQueueCard } from '../OperationQueueCard'

describe('OperationQueueCard', () => {
  it('renders queue data', () => {
    render(
      <OperationQueueCard
        queueSize={18}
        throughputPerMinute={40}
        backlogMinutes={12}
        operations={[
          { opId: 'op-1', type: 'CAS_UPDATE', component: 'ECONOMY', retries: 1, etaMs: 240 },
        ]}
      />,
    )

    expect(screen.getByText(/Mutation Queue/i)).toBeInTheDocument()
    expect(screen.getByText(/CAS_UPDATE/i)).toBeInTheDocument()
  })
})


