import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { NPCDecisionCard } from '../../components/NPCDecisionCard'

describe('NPCDecisionCard', () => {
  it('renders decision summary', () => {
    render(
      <NPCDecisionCard
        decision={{
          context: 'Negotiation',
          primaryAction: 'Offer discount',
          confidence: 0.8,
          options: [{ action: 'Offer discount', probability: 0.4, rationale: 'Increase loyalty' }],
        }}
      />,
    )

    expect(screen.getByText(/NPC Decision Engine/i)).toBeInTheDocument()
    expect(screen.getByText(/Offer discount/i)).toBeInTheDocument()
  })
})


