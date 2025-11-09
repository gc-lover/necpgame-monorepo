import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { WorldEventCard } from '../WorldEventCard'

describe('WorldEventCard', () => {
  it('renders event info', () => {
    const event = {
      event_id: 'event_001',
      event_type: 'rad_zone',
      era: '2020-2040',
      description: 'Обнаружена радиационная зона',
      dc_scaling: {
        social: 14,
        tech_hack: 18,
        combat: 16,
      },
    }
    render(<WorldEventCard event={event} />)
    expect(screen.getByText(/RAD ZONE/i)).toBeInTheDocument()
    expect(screen.getByText('2020-2040')).toBeInTheDocument()
  })
})

