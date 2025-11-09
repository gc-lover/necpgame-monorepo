import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MentorshipLessonCard } from '../MentorshipLessonCard'

describe('MentorshipLessonCard', () => {
  it('renders lesson details', () => {
    render(
      <MentorshipLessonCard
        lesson={{
          lessonId: 'lesson-1',
          title: 'Advanced Quickhacks',
          stage: 'ADVANCED',
          difficulty: 'LEGENDARY',
          requirements: ['Cyberdeck Mk.IV', 'INT 80+'],
          reward: '+20% quickhack damage',
          recommendedScore: 85,
        }}
      />,
    )

    expect(screen.getByText(/Advanced Quickhacks/i)).toBeInTheDocument()
    expect(screen.getByText(/Reward/i)).toBeInTheDocument()
  })
})


