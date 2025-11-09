import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CultureLoreCard } from '../../components/CultureLoreCard'

describe('CultureLoreCard', () => {
  it('shows culture theme', () => {
    render(
      <CultureLoreCard
        culture={{
          theme: 'Street Culture',
          influences: ['Synthwave'],
          iconicMedia: ['Afterlife Radio'],
          slangTerms: ['edger'],
        }}
      />,
    )

    expect(screen.getByText(/Street Culture/i)).toBeInTheDocument()
    expect(screen.getByText(/Synthwave/i)).toBeInTheDocument()
  })
})


