/**
 * Тесты для LocationDetailsPanel
 */
import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { LocationDetailsPanel } from '../LocationDetailsPanel'
import type { LocationDetails } from '@/api/generated/locations/models'

describe('LocationDetailsPanel', () => {
  const location: LocationDetails = {
    id: 'downtown_city_center',
    name: 'City Center',
    description: 'Сердце Night City',
    city: 'Night City',
    district: 'Downtown',
    region: 'night_city',
    dangerLevel: 'medium',
    minLevel: 5,
    type: 'corporate',
    accessible: true,
    atmosphere: 'Неон и небоскрёбы, шум мегаполиса.',
    pointsOfInterest: [
      {
        id: 'arasaka_tower',
        name: 'Башня Arasaka',
        description: 'Главная цитадель корпорации.',
      },
    ],
    availableActions: [],
    availableNPCs: ['npc-sarah'],
    connectedLocations: ['watson_kabuki'],
  }

  it('отображает основные данные локации', () => {
    render(<LocationDetailsPanel location={location} />)

    expect(screen.getByText('City Center')).toBeInTheDocument()
    expect(screen.getByText(/Night City/)).toBeInTheDocument()
    expect(screen.getByText(/Неон и небоскрёбы/)).toBeInTheDocument()
    expect(screen.getByText('Башня Arasaka')).toBeInTheDocument()
    expect(screen.getByText('npc-sarah')).toBeInTheDocument()
    expect(screen.getByText('watson_kabuki')).toBeInTheDocument()
  })
})

