import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { DailyQuestCard } from '../../components/DailyQuestCard'

describe('DailyQuestCard', () => {
  it('renders daily quest info', () => {
    render(
      <DailyQuestCard
        quest={{
          questId: 'daily-1',
          name: 'Cyber Heist',
          region: 'NIGHT_CITY',
          difficulty: 'HARD',
          objective: 'Stop data heist',
          reward: 'Cred +60',
          resetsAt: '03:00 NCST',
        }}
      />,
    )

    expect(screen.getByText(/Cyber Heist/i)).toBeInTheDocument()
    expect(screen.getByText(/Reset/i)).toBeInTheDocument()
  })
})


