import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RomanceRelationshipCard } from '../RomanceRelationshipCard'

describe('RomanceRelationshipCard', () => {
  it('displays relationship details', () => {
    render(
      <RomanceRelationshipCard
        relationship={{
          relationshipId: 'rel-1',
          npcName: 'Panam Palmer',
          stage: 'DATING',
          affectionLevel: 85,
          trustLevel: 72,
          jealousyLevel: 20,
          eventsCompleted: 12,
          startedAt: '2077-09-10T12:00:00Z',
        }}
      />,
    )

    expect(screen.getByText(/Panam Palmer/i)).toBeInTheDocument()
    expect(screen.getByText(/Events: 12/i)).toBeInTheDocument()
  })
})


