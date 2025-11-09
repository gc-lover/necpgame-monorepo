import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AlertFeedCard } from '../../components/AlertFeedCard'

describe('AlertFeedCard', () => {
  it('renders alert events', () => {
    render(
      <AlertFeedCard
        alerts={[
          { id: 'alert-1', level: 'warning', message: 'Tick duration > 50ms', raisedAt: '18:05' },
        ]}
      />,
    )

    expect(screen.getByText(/Tick duration/i)).toBeInTheDocument()
    expect(screen.getByText(/18:05/i)).toBeInTheDocument()
  })
})


