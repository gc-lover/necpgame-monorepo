import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RegionalQuestCard } from '../../components/RegionalQuestCard'

describe('RegionalQuestCard', () => {
  it('renders regional quest info', () => {
    render(
      <RegionalQuestCard
        quest={{
          questId: 'regional-1',
          name: 'Drone Uprising',
          region: 'AFRICA',
          minLevel: 18,
          summary: 'Broker truce',
          faction: 'Biotechnica',
          repeatable: true,
        }}
      />,
    )

    expect(screen.getByText(/Drone Uprising/i)).toBeInTheDocument()
    expect(screen.getByText(/Biotechnica/i)).toBeInTheDocument()
  })
})


