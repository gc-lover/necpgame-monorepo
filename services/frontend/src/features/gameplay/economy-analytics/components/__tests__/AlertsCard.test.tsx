import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AlertsCard } from '../AlertsCard'

describe('AlertsCard', () => {
  it('renders alerts list', () => {
    render(
      <AlertsCard
        alerts=[
          { alertId: 'alert-1', itemName: 'Smart Rifle', alertType: 'price_above', targetPrice: 5200, notificationMethod: 'in_game' },
        ]
      />,
    )

    expect(screen.getByText(/Active Alerts/i)).toBeInTheDocument()
    expect(screen.getByText(/Smart Rifle/i)).toBeInTheDocument()
  })
})

