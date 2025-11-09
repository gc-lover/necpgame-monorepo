import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MainStoryQuestCard } from '../../components/MainStoryQuestCard'

describe('MainStoryQuestCard', () => {
  it('renders main story quest', () => {
    render(
      <MainStoryQuestCard
        quest={{
          questId: 'main-1',
          name: 'Ghosts of Arasaka',
          period: '2077',
          chapter: 3,
          description: 'Investigate relic site.',
          objectives: ['Meet contact'],
        }}
      />,
    )

    expect(screen.getByText(/Ghosts of Arasaka/i)).toBeInTheDocument()
    expect(screen.getByText(/Chapter 3/i)).toBeInTheDocument()
  })
})


