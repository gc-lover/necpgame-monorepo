import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SkillCard } from '../SkillCard'

describe('SkillCard', () => {
  it('renders', () => {
    render(<SkillCard skill={{ name: 'Gunplay', level: 5, progress: 50 }} />)
    expect(screen.getByText('Gunplay')).toBeInTheDocument()
  })
})

