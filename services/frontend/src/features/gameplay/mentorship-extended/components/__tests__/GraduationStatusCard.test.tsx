import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { GraduationStatusCard } from '../GraduationStatusCard'

describe('GraduationStatusCard', () => {
  it('renders graduation progress and requirements', () => {
    render(
      <GraduationStatusCard
        stage="IN_PROGRESS"
        progress={64}
        mentorApproval={72}
        reputationImpact="+150 reputation with Afterlife"
        requirements={[
          { label: 'Complete legendary lesson', completed: false },
          { label: 'Bond strength 70+', completed: true },
        ]}
      />,
    )

    expect(screen.getByText(/Graduation Status/i)).toBeInTheDocument()
    expect(screen.getByText(/Complete legendary lesson/i)).toBeInTheDocument()
    expect(screen.getByText(/Bond strength 70\+/i)).toBeInTheDocument()
  })
})

