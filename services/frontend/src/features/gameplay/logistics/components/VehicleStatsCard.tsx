import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import DirectionsCarIcon from '@mui/icons-material/DirectionsCar'
import TwoWheelerIcon from '@mui/icons-material/TwoWheeler'
import LocalShippingIcon from '@mui/icons-material/LocalShipping'
import HikingIcon from '@mui/icons-material/DirectionsWalk'
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface VehicleStat {
  type: 'ON_FOOT' | 'MOTORCYCLE' | 'CAR' | 'TRUCK' | 'AERODYNE'
  speed: string
  capacity: string
  risk: 'LOW' | 'MEDIUM' | 'HIGH'
  cost: string
}

const riskColor: Record<VehicleStat['risk'], string> = {
  LOW: '#05ffa1',
  MEDIUM: '#fef86c',
  HIGH: '#ff2a6d',
}

const vehicleIcon: Record<VehicleStat['type'], JSX.Element> = {
  ON_FOOT: <HikingIcon sx={{ fontSize: '1rem', color: 'text.secondary' }} />,
  MOTORCYCLE: <TwoWheelerIcon sx={{ fontSize: '1rem', color: 'success.main' }} />,
  CAR: <DirectionsCarIcon sx={{ fontSize: '1rem', color: 'info.main' }} />,
  TRUCK: <LocalShippingIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />,
  AERODYNE: <RocketLaunchIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />,
}

export interface VehicleStatsCardProps {
  vehicles: VehicleStat[]
}

export const VehicleStatsCard: React.FC<VehicleStatsCardProps> = ({ vehicles }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
        Vehicle Comparison
      </Typography>
      <Stack spacing={0.3}>
        {vehicles.map((vehicle) => (
          <Box key={vehicle.type} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Box display="flex" alignItems="center" gap={0.5}>
                {vehicleIcon[vehicle.type]}
                <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                  {vehicle.type}
                </Typography>
              </Box>
              <Typography
                variant="caption"
                fontSize={cyberpunkTokens.fonts.xs}
                sx={{ color: riskColor[vehicle.risk] }}
              >
                Risk: {vehicle.risk}
              </Typography>
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Speed: {vehicle.speed} · Capacity: {vehicle.capacity} · Cost: {vehicle.cost}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default VehicleStatsCard

