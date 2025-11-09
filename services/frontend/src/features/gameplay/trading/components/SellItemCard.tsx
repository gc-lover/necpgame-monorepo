import { useMemo } from 'react'
import type { InventoryItem } from '@/api/generated/inventory/models'
import { useGetItemPrice } from '@/api/generated/trading/trading/trading'
import { TradeItemCard } from './TradeItemCard'

interface SellItemCardProps {
  vendorId: string
  characterId: string
  item: InventoryItem
  onSell: () => void
  disabled?: boolean
}

export function SellItemCard({ vendorId, characterId, item, onSell, disabled }: SellItemCardProps) {
  const enabled = Boolean(vendorId && characterId)

  const {
    data,
    isLoading,
  } = useGetItemPrice(
    item.id,
    { vendorId, characterId },
    { query: { enabled } }
  )

  const tradeItem = useMemo(
    () => ({
      itemId: item.id,
      name: item.name,
      buyPrice: data?.buyPrice ?? 0,
      sellPrice: data?.sellPrice ?? 0,
      quantity: item.quantity,
      category: item.category,
    }),
    [data?.buyPrice, data?.sellPrice, item]
  )

  return (
    <TradeItemCard
      item={tradeItem}
      mode="sell"
      onTrade={onSell}
      disabled={disabled || !enabled || isLoading}
      loadingPrice={isLoading && !data}
    />
  )
}

