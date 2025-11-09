import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MentorshipRelationshipCard } from '../MentorshipRelationshipCard'

describe('MentorshipRelationshipCard', () => {
  it('renders relationship details', () => {
    render(
      <MentorshipRelationshipCard
        relationship={{
          relationshipId: 'rel-1',
          mentorName: 'Rogue Amendiares',
          type: 'SOCIAL',
          stage: 'ADVANCED',
          bondStrength: 72,
          trust: 64,
          lessonsCompleted: 8,
          totalLessons: 12,
          startedAt: '2077-10-01T00:00:00Z',
        }}
      />,
    )

    expect(screen.getByText(/Rogue Amendiares/i)).toBeInTheDocument()
    expect(screen.getByText(/Lessons/i)).toBeInTheDocument()
  })
})


