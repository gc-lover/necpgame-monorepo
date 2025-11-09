import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TriggerPredictionCard } from '../../components/TriggerPredictionCard'

describe('TriggerPredictionCard', () => {
  it('shows trigger info', () => {
    render(
      <TriggerPredictionCard
        trigger={{
          relationshipId: 'rel-1',
          shouldTrigger: true,
          eventId: 'event-1',
          triggerProbability: 0.75,
          blockingReasons: [],
        }}
      />,
    )

    expect(screen.getByText(/Trigger Prediction/i)).toBeInTheDocument()
    expect(screen.getByText(/event-1/i)).toBeInTheDocument()
  })
})


