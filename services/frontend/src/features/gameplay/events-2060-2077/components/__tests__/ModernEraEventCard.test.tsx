import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ModernEraEventCard } from '../ModernEraEventCard'

describe('ModernEraEventCard', () => {
  it('renders event info', () => {
    const event = {
      event_id: 'event_003',
      event_type: 'corporate_audit',
      era: '2060-2077',
      description: 'Arasaka аудит',
      dc_scaling: {
        social: 16,
        tech_hack: 20,
        combat: 16,
      },
    }
    render(<ModernEraEventCard event={event} />)
    expect(screen.getByText(/CORPORATE AUDIT/i)).toBeInTheDocument()
    expect(screen.getByText('2060-2077')).toBeInTheDocument()
  })
})

