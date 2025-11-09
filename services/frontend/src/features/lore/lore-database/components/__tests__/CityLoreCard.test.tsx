import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CityLoreCard } from '../../components/CityLoreCard'

describe('CityLoreCard', () => {
  it('renders city lore details', () => {
    render(
      <CityLoreCard
        city={{
          name: 'Night City',
          region: 'North America',
          population: '8.1M',
          controllingFaction: 'Arasaka Council',
          dangerLevel: 'HIGH',
          timeline: [{ year: 2077, event: 'Relic incident' }],
        }}
      />,
    )

    expect(screen.getByText(/Night City/i)).toBeInTheDocument()
    expect(screen.getByText(/8.1M/i)).toBeInTheDocument()
  })
})


