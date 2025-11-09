import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { FamilyMemberCard } from '../FamilyMemberCard'

describe('FamilyMemberCard', () => {
  it('renders member info', () => {
    const member = {
      character_id: 'char_001',
      name: 'John Doe',
      relationship: 'Father',
    }
    render(<FamilyMemberCard member={member} />)
    expect(screen.getByText('John Doe')).toBeInTheDocument()
  })
})

