import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { GlobalEventCard } from '../GlobalEventCard'

describe('GlobalEventCard', () => {
  it('renders event info', () => {
    const event = {
      event_id: 'event_001',
      name: 'The Collapse',
      type: 'economic' as const,
      era: '2020-2030',
      year_start: 2022,
      is_active: false,
      short_description: 'Global economic collapse',
    }
    render(<GlobalEventCard event={event} />)
    expect(screen.getByText('The Collapse')).toBeInTheDocument()
    expect(screen.getByText('2020-2030')).toBeInTheDocument()
  })
})

