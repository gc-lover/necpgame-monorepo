import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ModerationQueueCard } from '../ModerationQueueCard'

describe('ModerationQueueCard', () => {
  it('renders moderation cases', () => {
    render(
      <ModerationQueueCard
        cases={[
          {
            caseId: 'case-001',
            category: 'CHAT',
            status: 'IN_PROGRESS',
            reportedAt: '3m ago',
            assignee: 'Mod-Kira',
          },
        ]}
      />,
    )

    expect(screen.getByText(/case-001/i)).toBeInTheDocument()
    expect(screen.getByText(/Mod-Kira/i)).toBeInTheDocument()
  })
})


