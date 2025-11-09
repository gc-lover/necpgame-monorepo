import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CapsCard } from '../CapsCard'

describe('CapsCard', () => {
  it('displays cap values', () => {
    render(
      <CapsCard
        caps={[
          { name: 'INT', softCap: 70, hardCap: 100, current: 65 },
        ]}
      />,
    )

    expect(screen.getByText(/INT/i)).toBeInTheDocument()
    expect(screen.getByText(/Soft 70/i)).toBeInTheDocument()
  })
})


