import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RelationshipCard } from '../RelationshipCard'

describe('RelationshipCard', () => {
  it('renders', () => {
    render(<RelationshipCard relationship={{ name: 'Johnny Silverhand', level: 50 }} />)
    expect(screen.getByText('Johnny Silverhand')).toBeInTheDocument()
  })
})

