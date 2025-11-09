import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SkillsMappingCard } from '../SkillsMappingCard'

describe('SkillsMappingCard', () => {
  it('renders mapping entries', () => {
    render(
      <SkillsMappingCard
        mappingType="TO_ITEMS"
        entries={[
          { source: 'Hacking', targets: ['Deck Mk.III', 'Quickhack Suite'] },
        ]}
      />,
    )

    expect(screen.getByText(/Hacking/i)).toBeInTheDocument()
    expect(screen.getByText(/Deck Mk.III/i)).toBeInTheDocument()
  })
})


