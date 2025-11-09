import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ShipmentCard } from '../ShipmentCard'

describe('ShipmentCard', () => {
  it('renders shipment info', () => {
    const shipment = {
      shipment_id: 'ship_123456',
      origin: 'Night City',
      destination: 'Watson',
      vehicle_type: 'TRUCK',
      status: 'IN_TRANSIT',
      cargo_weight: 500,
      risk_level: 'MEDIUM',
    }
    render(<ShipmentCard shipment={shipment} />)
    expect(screen.getByText(/Night City.*Watson/)).toBeInTheDocument()
    expect(screen.getByText('TRUCK')).toBeInTheDocument()
  })
})

