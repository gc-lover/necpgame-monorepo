import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { WeeklyQuestCard } from '../../components/WeeklyQuestCard'

describe('WeeklyQuestCard', () => {
  it('renders weekly quest info', () => {
    render(
      <WeeklyQuestCard
        quest={{
          questId: 'weekly-1',
          name: 'Gulf Raid',
          region: 'MIDDLE_EAST',
          recommendedPower: 500,
          description: 'Disable drone hub',
          reward: 'Legendary loot',
        }}
      />,
    )

    expect(screen.getByText(/Gulf Raid/i)).toBeInTheDocument()
    expect(screen.getByText(/Legendary loot/i)).toBeInTheDocument()
  })
})


