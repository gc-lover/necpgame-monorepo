import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AttributesMatrixCard } from '../AttributesMatrixCard'

describe('AttributesMatrixCard', () => {
  it('shows class matrix info', () => {
    render(
      <AttributesMatrixCard
        entry={{
          classId: 'netrunner',
          className: 'Netrunner',
          focus: 'Tech / INT',
          attributes: [
            { attribute: 'INT', base: 6, growth: 3 },
            { attribute: 'TECH', base: 5, growth: 2 },
          ],
        }}
      />,
    )

    expect(screen.getByText(/Netrunner/i)).toBeInTheDocument()
    expect(screen.getByText(/INT/i)).toBeInTheDocument()
  })
})


