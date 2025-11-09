import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { NetworkNodeCard } from '../NetworkNodeCard'

describe('NetworkNodeCard', () => {
  it('renders node info', () => {
    const node = {
      node_id: 'camera_01',
      type: 'camera',
      status: 'active',
      security_level: 2,
    }
    render(<NetworkNodeCard node={node} />)
    expect(screen.getByText('camera_01')).toBeInTheDocument()
    expect(screen.getByText('camera')).toBeInTheDocument()
  })
})

