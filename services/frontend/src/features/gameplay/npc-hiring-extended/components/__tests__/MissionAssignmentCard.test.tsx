import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MissionAssignmentCard } from '../MissionAssignmentCard'

describe('MissionAssignmentCard', () => {
  it('renders mission data', () => {
    render(
      <MissionAssignmentCard
        mission={{
          missionId: 'mission-omega',
          title: 'Neon Convoy Escort',
          location: 'Night City → Badlands',
          dangerLevel: 'HIGH',
          payout: '48 000¥',
          successChance: 72,
          members: [
            { npcName: 'Alpha Ghost', role: 'Hacker', effectiveness: 88 },
            { npcName: 'Morgan Blackhand', role: 'Combat', effectiveness: 92 },
          ],
        }}
      />,
    )

    expect(screen.getByText(/Neon Convoy Escort/i)).toBeInTheDocument()
    expect(screen.getByText(/Night City/i)).toBeInTheDocument()
  })
})


