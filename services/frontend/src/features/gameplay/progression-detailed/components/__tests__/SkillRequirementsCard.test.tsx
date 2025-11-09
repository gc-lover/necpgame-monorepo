import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SkillRequirementsCard } from '../SkillRequirementsCard'

describe('SkillRequirementsCard', () => {
  it('shows requirements list', () => {
    render(
      <SkillRequirementsCard
        itemName="Mantis Blades"
        isEligible={false}
        requirements={[
          { skill: 'Reflexes', required: 12, current: 10 },
          { skill: 'Blades', required: 8, current: 9 },
        ]}
      />,
    )

    expect(screen.getByText(/Mantis Blades/i)).toBeInTheDocument()
    expect(screen.getByText(/Reflexes/i)).toBeInTheDocument()
  })
})


