import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, it, expect, vi } from 'vitest'
import { WeaponCard } from '../WeaponCard'
import { WeaponSummary } from '@/api/generated/weapons/models'

describe('WeaponCard', () => {
  const mockWeapon: WeaponSummary = {
    id: 'weapon-1',
    name: 'Liberty',
    weapon_class: 'pistol',
    brand: 'Arasaka',
    rarity: 'legendary',
    damage: 150,
    fire_rate: 2.5,
  }

  it('рендерит название оружия', () => {
    render(<WeaponCard weapon={mockWeapon} />)
    expect(screen.getByText('Liberty')).toBeInTheDocument()
  })

  it('отображает редкость', () => {
    render(<WeaponCard weapon={mockWeapon} />)
    expect(screen.getByText('legendary')).toBeInTheDocument()
  })

  it('показывает бренд', () => {
    render(<WeaponCard weapon={mockWeapon} />)
    expect(screen.getByText('ARASAKA')).toBeInTheDocument()
  })

  it('отображает характеристики', () => {
    render(<WeaponCard weapon={mockWeapon} />)
    expect(screen.getByText('150')).toBeInTheDocument()
    expect(screen.getByText('2.5/с')).toBeInTheDocument()
  })

  it('вызывает onClick при клике', async () => {
    const user = userEvent.setup()
    const handleClick = vi.fn()
    render(<WeaponCard weapon={mockWeapon} onClick={handleClick} />)

    const card = screen.getByText('Liberty').closest('.MuiCard-root')
    if (card) {
      await user.click(card)
      expect(handleClick).toHaveBeenCalledTimes(1)
    }
  })

  it('работает без бренда', () => {
    const { brand, ...weaponWithoutBrand } = mockWeapon
    render(<WeaponCard weapon={weaponWithoutBrand} />)
    expect(screen.getByText('Liberty')).toBeInTheDocument()
    expect(screen.queryByText(/ARASAKA/)).not.toBeInTheDocument()
  })

  it('отображает правильный класс оружия', () => {
    render(<WeaponCard weapon={mockWeapon} />)
    expect(screen.getByText('Пистолет')).toBeInTheDocument()
  })
})

