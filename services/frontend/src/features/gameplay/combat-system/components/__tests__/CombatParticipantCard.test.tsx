import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CombatParticipantCard } from '../CombatParticipantCard'

describe('CombatParticipantCard', () => {
  it('renders', () => {
    render(<CombatParticipantCard participant={{ id: '1', name: 'Test', type: 'player', health: 100, maxHealth: 100, isAlive: true }} />)
    expect(screen.getByText('Test')).toBeInTheDocument()
  })
})

