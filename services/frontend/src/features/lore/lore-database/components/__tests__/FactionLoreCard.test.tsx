import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { FactionLoreCard } from '../../components/FactionLoreCard'

describe('FactionLoreCard', () => {
  it('renders faction summary', () => {
    render(
      <FactionLoreCard
        faction={{
          name: 'Tyger Claws',
          type: 'GANG',
          region: 'Japantown',
          influence: 0.7,
          foundedYear: 2070,
          keywords: ['katana'],
        }}
      />,
    )

    expect(screen.getByText(/Tyger Claws/i)).toBeInTheDocument()
    expect(screen.getByText(/Japantown/i)).toBeInTheDocument()
  })
})


