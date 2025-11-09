import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { PerkCard } from '../PerkCard'

describe('PerkCard', () => {
  it('renders', () => {
    render(<PerkCard perk={{ name: 'Test Perk' }} />)
    expect(screen.getByText('Test Perk')).toBeInTheDocument()
  })
})

