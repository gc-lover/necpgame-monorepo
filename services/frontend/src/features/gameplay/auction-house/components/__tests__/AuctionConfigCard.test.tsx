import { render, screen } from '@testing-library/react'
import { AuctionConfigCard } from '../AuctionConfigCard'

const createConfig = () => ({
  creation: {
    minStartingBid: 100,
    maxDurationHours: 72,
    allowedDurations: [24, 48, 72],
    buyoutMinRatio: 1.5,
    listingLimit: 10,
    autoBidEnabled: true,
  },
  bidding: {
    minIncrementPercent: 5,
    autoExtendThresholdMinutes: 10,
    autoExtendMinutes: 5,
    maxActiveBids: 20,
    currency: '€$',
  },
  buyout: {
    enabled: true,
    minPrice: 500,
    maxPrice: 15000,
    cooldownSeconds: 60,
    requiresConfirmation: true,
  },
  commission: {
    listingFee: 100,
    saleCommissionPercent: 8,
    buyoutCommissionPercent: 10,
    refundPolicy: '24h partial refund',
  },
  scheduler: {
    frequencyMinutes: 5,
    batchSize: 200,
    gracePeriodMinutes: 2,
    lockingStrategy: 'redis-lock',
  },
  comparison: [
    {
      feature: 'Комиссия',
      auctionHouse: '8%',
      playerMarket: '5% + динамическая',
    },
  ],
})

describe('AuctionConfigCard', () => {
  it('отображает ключевые параметры конфигурации', () => {
    render(<AuctionConfigCard config={createConfig()} />)

    expect(screen.getByText('Конфигурация аукционного дома')).toBeInTheDocument()
    expect(screen.getByText('Минимальная стартовая ставка')).toBeInTheDocument()
    expect(screen.getByText('100')).toBeInTheDocument()
    expect(screen.getByText('Автобид')).toBeInTheDocument()
    expect(screen.getAllByText('Включено').length).toBeGreaterThan(0)
    expect(screen.getAllByText('Комиссия')[0]).toBeInTheDocument()
    expect(screen.getByText('8%')).toBeInTheDocument()
  })
})
