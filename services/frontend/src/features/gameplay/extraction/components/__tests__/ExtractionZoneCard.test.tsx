import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ExtractionZoneCard } from '../ExtractionZoneCard'

describe('ExtractionZoneCard', () => {
  it('renders', () => {
    render(<ExtractionZoneCard zone={{ id: '1', name: 'Zone 1', risk_level: 'high' }} />)
    expect(screen.getByText('Zone 1')).toBeInTheDocument()
  })
})

