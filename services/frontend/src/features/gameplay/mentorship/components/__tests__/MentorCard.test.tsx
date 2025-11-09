import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MentorCard } from '../MentorCard'

describe('MentorCard', () => {
  it('renders mentor info', () => {
    const mentor = {
      mentor_id: 'mentor_001',
      name: 'V',
      level: 50,
      specialization: 'Combat',
      mentorship_level: 5,
      active_mentees: 3,
      max_mentees: 5,
      rating: 4.8,
    }
    render(<MentorCard mentor={mentor} />)
    expect(screen.getByText('V')).toBeInTheDocument()
    expect(screen.getByText('LVL 50')).toBeInTheDocument()
    expect(screen.getByText('Combat')).toBeInTheDocument()
  })
})

