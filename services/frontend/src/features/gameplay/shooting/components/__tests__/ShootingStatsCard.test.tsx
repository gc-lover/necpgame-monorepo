import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ShootingStatsCard } from '../ShootingStatsCard'

describe('ShootingStatsCard', () => {
  it('renders', () => {
    render(<ShootingStatsCard stats={{ damage: 50 }} />)
    expect(screen.getByText('Статы оружия')).toBeInTheDocument()
  })
})

