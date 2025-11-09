import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RedEraEventCard } from '../RedEraEventCard'

describe('RedEraEventCard', () => {
  it('renders event info', () => {
    const event = {
      event_id: 'event_002',
      event_type: 'braindance_leak',
      era: '2040-2060',
      description: 'Утечка брейндэнс контента',
      dc_scaling: {
        social: 15,
        tech_hack: 18,
        combat: 14,
      },
    }
    render(<RedEraEventCard event={event} />)
    expect(screen.getByText(/BRAINDANCE LEAK/i)).toBeInTheDocument()
    expect(screen.getByText('2040-2060')).toBeInTheDocument()
  })
})

