import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EnemyCard } from '../EnemyCard'

describe('EnemyCard', () => {
  it('renders enemy name', () => {
    const enemy = { id: '1', name: 'Scav', description: 'Test', threat_level: 'low' }
    render(<EnemyCard enemy={enemy} />)
    expect(screen.getByText('Scav')).toBeInTheDocument()
  })
})

