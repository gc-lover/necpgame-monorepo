import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { OnCallCard } from '../../components/OnCallCard'

describe('OnCallCard', () => {
  it('renders on-call info', () => {
    render(
      <OnCallCard
        info={{
          currentResponder: 'oncall-engineer',
          rotation: 'SRE-primary',
          timeRemaining: '02:15',
          nextUp: 'platform-duty',
        }}
      />,
    )

    expect(screen.getByText(/oncall-engineer/i)).toBeInTheDocument()
    expect(screen.getByText(/platform-duty/i)).toBeInTheDocument()
  })
})


