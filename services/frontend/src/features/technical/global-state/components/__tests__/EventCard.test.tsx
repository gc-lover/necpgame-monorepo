import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EventCard } from '../EventCard'

describe('EventCard', () => {
  it('renders', () => {
    render(<EventCard event={{ event_id: '1', event_type: 'test', timestamp: '2023-01-01' }} />)
    expect(screen.getByText('test')).toBeInTheDocument()
  })
})

