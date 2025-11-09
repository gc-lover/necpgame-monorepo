import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MainUIDataCard } from '../MainUIDataCard'

describe('MainUIDataCard', () => {
  it('renders UI data', () => {
    render(
      <MainUIDataCard
        data={{
          character: { name: 'V', level: 7, xp: 1400, xpNeeded: 2000 },
          stats: { STR: 6, INT: 8 },
          quests: ['Heist'],
          notifications: ['New message from Judy'],
        }}
      />,
    )

    expect(screen.getByText(/V · Lvl 7/i)).toBeInTheDocument()
    expect(screen.getByText(/Heist/i)).toBeInTheDocument()
  })
})

