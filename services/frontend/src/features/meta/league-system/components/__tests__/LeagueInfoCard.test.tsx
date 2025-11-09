import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { LeagueInfoCard } from '../LeagueInfoCard'

describe('LeagueInfoCard', () => {
  it('renders', () => {
    render(<LeagueInfoCard league={{ season: 1, phase: 'Active' }} />)
    expect(screen.getByText(/Лига/)).toBeInTheDocument()
  })
})

