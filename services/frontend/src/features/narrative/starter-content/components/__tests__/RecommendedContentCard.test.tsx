import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RecommendedContentCard } from '../../components/RecommendedContentCard'

describe('RecommendedContentCard', () => {
  it('renders recommended quests', () => {
    render(
      <RecommendedContentCard
        originQuest={{
          questId: 'q1',
          name: 'Origin Run',
          questType: 'CLASS',
          description: '',
          rewards: [],
        }}
        classQuests={[
          { questId: 'q2', name: 'Class Mission', questType: 'CLASS', description: '', rewards: [] },
        ]}
        tutorialQuests={[
          { questId: 'q3', name: 'Tutorial', questType: 'TUTORIAL', description: '', rewards: [] },
        ]}
      />,
    )

    expect(screen.getByText(/Origin Run/i)).toBeInTheDocument()
    expect(screen.getByText(/Tutorial/i)).toBeInTheDocument()
  })
})


