import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TravelEventGenerationCard } from '../../TravelEventGenerationCard'

describe('TravelEventGenerationCard', () => {
  it('shows generation data', () => {
    render(
      <TravelEventGenerationCard
        transportMode="VEHICLE"
        origin="Kabuki"
        destination="Badlands"
        timeOfDay="Night"
        lastEncounter="Wraith chase"
        eventGenerated={true}
        event={{
          eventId: 'evt-1',
          name: 'Ambush',
          period: '2077',
          locationTypes: ['BADLANDS'],
          triggerChance: 0.3,
          description: 'Scav ambush',
          choices: [],
          outcomes: [],
        }}
        autoGenerate={true}
      />,
    )

    expect(screen.getByText(/Kabuki/i)).toBeInTheDocument()
    expect(screen.getByText(/Ambush/i)).toBeInTheDocument()
  })
})



