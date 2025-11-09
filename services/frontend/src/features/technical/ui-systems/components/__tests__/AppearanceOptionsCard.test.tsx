import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AppearanceOptionsCard } from '../../components/AppearanceOptionsCard'

describe('AppearanceOptionsCard', () => {
  it('lists appearance categories', () => {
    render(
      <AppearanceOptionsCard
        options={{
          totalCategories: 12,
          dnaLocking: true,
          categories: [
            { category: 'Hair', options: 45, presets: 12 },
          ],
        }}
      />,
    )

    expect(screen.getByText(/Appearance Options/i)).toBeInTheDocument()
    expect(screen.getByText(/Hair/i)).toBeInTheDocument()
  })
})


