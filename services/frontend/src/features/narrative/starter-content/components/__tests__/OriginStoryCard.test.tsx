import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { OriginStoryCard } from '../../components/OriginStoryCard'

describe('OriginStoryCard', () => {
  it('renders origin story data', () => {
    render(
      <OriginStoryCard
        origin={{
          originId: 'origin-1',
          name: 'Street Kid',
          description: 'Survive the Kabuki alleys.',
          recommendedClass: 'FIXER',
          startingLocation: 'Kabuki',
        }}
      />,
    )

    expect(screen.getByText(/Street Kid/i)).toBeInTheDocument()
    expect(screen.getByText(/Kabuki/i)).toBeInTheDocument()
  })
})


