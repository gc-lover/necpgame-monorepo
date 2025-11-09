import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TechnologyLoreCard } from '../../components/TechnologyLoreCard'

describe('TechnologyLoreCard', () => {
  it('renders technology data', () => {
    render(
      <TechnologyLoreCard
        technology={{
          name: 'Blackwall',
          eraIntroduced: 2063,
          riskLevel: 80,
          description: 'AI firewall',
          keySystems: ['Netwatch nodes'],
        }}
      />,
    )

    expect(screen.getByText(/Blackwall/i)).toBeInTheDocument()
    expect(screen.getByText(/AI firewall/i)).toBeInTheDocument()
  })
})


