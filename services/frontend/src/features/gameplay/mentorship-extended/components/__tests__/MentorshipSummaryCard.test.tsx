import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MentorshipSummaryCard } from '../MentorshipSummaryCard'

describe('MentorshipSummaryCard', () => {
  it('renders summary metrics', () => {
    render(
      <MentorshipSummaryCard
        activeMentors={3}
        pendingRequests={1}
        activeLessons={4}
        uniqueAbilitiesUnlocked={7}
        worldImpactScore={1280}
      />,
    )

    expect(screen.getByText(/Active mentors/i)).toBeInTheDocument()
    expect(screen.getByText(/World impact score/i)).toBeInTheDocument()
  })
})


