import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ClassCard } from '../ClassCard'

describe('ClassCard', () => {
  it('renders', () => {
    render(<ClassCard gameClass={{ class_id: '1', name: 'Solo', role: 'Combat', source: 'cyberpunk_canon' }} />)
    expect(screen.getByText('Solo')).toBeInTheDocument()
  })
})

