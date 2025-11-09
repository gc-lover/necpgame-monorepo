import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CombatRoleCard } from '../CombatRoleCard'

describe('CombatRoleCard', () => {
  it('renders role name', () => {
    const role = { id: '1', name: 'Tank', description: 'Test' }
    render(<CombatRoleCard role={role} />)
    expect(screen.getByText('Tank')).toBeInTheDocument()
  })
})

