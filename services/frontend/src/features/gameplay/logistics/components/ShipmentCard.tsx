import React from 'react'
import { Typography, Stack, Chip, Box, Divider } from '@mui/material'
import LocalShippingIcon from '@mui/icons-material/LocalShipping'
import DirectionsCarIcon from '@mui/icons-material/DirectionsCar'
import TwoWheelerIcon from '@mui/icons-material/TwoWheeler'
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

interface ShipmentProps {
  shipment: {
    shipment_id?: string
    origin?: string
    destination?: string
    vehicle_type?: string
    status?: string
    cargo_weight?: number
    estimated_delivery?: string
    risk_level?: string
  }
}

export const ShipmentCard: React.FC<ShipmentProps> = ({ shipment }) => {
  const getVehicleIcon = (type?: string) => {
    switch (type) {
      case 'AERODYNE':
        return <RocketLaunchIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
      case 'TRUCK':
        return <LocalShippingIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
      case 'CAR':
        return <DirectionsCarIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
      case 'MOTORCYCLE':
        return <TwoWheelerIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
      default:
        return <LocalShippingIcon sx={{ fontSize: '1rem', color: 'text.secondary' }} />
    }
  }

  const getRiskColor = (risk?: string) => {
    switch (risk) {
      case 'LOW':
        return 'success'
      case 'MEDIUM':
        return 'warning'
      case 'HIGH':
        return 'error'
      default:
        return 'default'
    }
  }

  return (
    <CompactCard color="cyan" glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.6}>
            {getVehicleIcon(shipment.vehicle_type)}
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {shipment.shipment_id?.slice(0, 8) ?? 'SHIPMENT'}
            </Typography>
          </Box>
          <Chip
            label={shipment.status ?? 'UNKNOWN'}
            size="small"
            color="info"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {shipment.origin ?? '???'} → {shipment.destination ?? '???'}
        </Typography>
        <Divider sx={{ my: 0.4 }} />
        <Box display="flex" gap={0.4} flexWrap="wrap">
          {shipment.vehicle_type && (
            <Chip
              label={shipment.vehicle_type}
              size="small"
              sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
            />
          )}
          {shipment.risk_level && (
            <Chip
              label={shipment.risk_level.toUpperCase()}
              size="small"
              color={getRiskColor(shipment.risk_level)}
              sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
            />
          )}
          {shipment.cargo_weight && (
            <Chip
              label={`${shipment.cargo_weight}kg`}
              size="small"
              sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
            />
          )}
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          ETA: {shipment.estimated_delivery ?? '—'}
        </Typography>
      </Stack>
    </CompactCard>
  )
}

