import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RomanceEventCard } from '../RomanceEventCard'

describe('RomanceEventCard', () => {
  it('renders', () => {
    render(<RomanceEventCard event={{ npc_name: 'Panam Palmer' }} />)
    expect(screen.getByText('Panam Palmer')).toBeInTheDocument()
  })
})

