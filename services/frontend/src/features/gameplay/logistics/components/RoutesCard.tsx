import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import AltRouteIcon from '@mui/icons-material/AltRoute'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface RouteInfo {
  routeId: string
  origin: string
  destination: string
  distance: string
  risk: 'LOW' | 'MEDIUM' | 'HIGH'
  recommendedVehicle: string
}

const riskColor: Record<RouteInfo['risk'], string> = {
  LOW: '#05ffa1',
  MEDIUM: '#fef86c',
  HIGH: '#ff2a6d',
}

export interface RoutesCardProps {
  routes: RouteInfo[]
}

export const RoutesCard: React.FC<RoutesCardProps> = ({ routes }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <AltRouteIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Route Options
          </Typography>
        </Box>
        <Chip
          label={routes.length}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Stack spacing={0.3}>
        {routes.map((route) => (
          <Box key={route.routeId} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                {route.origin} → {route.destination}
              </Typography>
              <Typography
                variant="caption"
                fontSize={cyberpunkTokens.fonts.xs}
                sx={{ color: riskColor[route.risk] }}
              >
                {route.risk}
              </Typography>
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Distance: {route.distance} · Vehicle: {route.recommendedVehicle}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default RoutesCard


