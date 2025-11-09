import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Stack, Divider, Alert, Grid, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import LocalShippingIcon from '@mui/icons-material/LocalShipping'
import MapIcon from '@mui/icons-material/Map'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ShipmentCard } from '../components/ShipmentCard'
import { RoutesCard } from '../components/RoutesCard'
import { VehicleStatsCard } from '../components/VehicleStatsCard'
import { InsurancePlansCard } from '../components/InsurancePlansCard'
import { RiskMatrixCard } from '../components/RiskMatrixCard'
import { ConvoyStatusCard } from '../components/ConvoyStatusCard'
import { useGameState } from '@/features/game/hooks/useGameState'

const demoShipments = [
  {
    shipment_id: 'ship-FA7331',
    origin: 'Night City',
    destination: 'Watson',
    vehicle_type: 'TRUCK',
    status: 'IN_TRANSIT',
    cargo_weight: 780,
    estimated_delivery: '1h 12m',
    risk_level: 'MEDIUM',
  },
  {
    shipment_id: 'ship-XP9020',
    origin: 'Watson',
    destination: 'Heywood',
    vehicle_type: 'AERODYNE',
    status: 'PENDING',
    cargo_weight: 210,
    estimated_delivery: '0h 28m',
    risk_level: 'LOW',
  },
]

const demoRoutes = [
  {
    routeId: 'route-101',
    origin: 'Night City',
    destination: 'Watson',
    distance: '45km',
    risk: 'MEDIUM' as const,
    recommendedVehicle: 'CAR',
  },
  {
    routeId: 'route-207',
    origin: 'Night City',
    destination: 'Badlands',
    distance: '120km',
    risk: 'HIGH' as const,
    recommendedVehicle: 'TRUCK',
  },
]

const vehicleStats = [
  { type: 'ON_FOOT' as const, speed: '5 km/h', capacity: '50 kg', risk: 'HIGH' as const, cost: '5¥' },
  { type: 'MOTORCYCLE' as const, speed: '90 km/h', capacity: '120 kg', risk: 'MEDIUM' as const, cost: '50¥' },
  { type: 'CAR' as const, speed: '110 km/h', capacity: '350 kg', risk: 'MEDIUM' as const, cost: '90¥' },
  { type: 'TRUCK' as const, speed: '70 km/h', capacity: '4000 kg', risk: 'MEDIUM' as const, cost: '200¥' },
  { type: 'AERODYNE' as const, speed: '240 km/h', capacity: '600 kg', risk: 'LOW' as const, cost: '600¥' },
]

const insurancePlans = [
  { name: 'Basic', coverage: '50%', premium: '120¥', perks: ['Delayed payout'] },
  { name: 'Standard', coverage: '75%', premium: '280¥', perks: ['Priority support'] },
  { name: 'Premium', coverage: '100%', premium: '520¥', perks: ['Instant payout', 'Escort coverage'] },
]

const riskFactors = [
  { name: 'Ambush risk', probability: 'HIGH' as const, mitigation: 'Hire escort squad' },
  { name: 'Weather', probability: 'MEDIUM' as const, mitigation: 'Reroute via highways' },
  { name: 'Mechanical failure', probability: 'LOW' as const, mitigation: 'Pre-trip diagnostics' },
]

const convoyEscorts = [
  { escortId: 'Alpha', members: 5, firepower: 'HIGH' as const, status: 'READY' as const },
  { escortId: 'Bravo', members: 3, firepower: 'MEDIUM' as const, status: 'EN_ROUTE' as const },
]

export const LogisticsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()

  const leftPanel = (
    <Stack spacing={2}>
      <CyberpunkButton
        variant="outlined"
        size="small"
        fullWidth
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/game')}
      >
        Назад
      </CyberpunkButton>
      {selectedCharacterId && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Character: {selectedCharacterId}
        </Typography>
      )}
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="warning.main">
        Logistics Network
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Transportation, insurance, convoy support, live tracking.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Actions
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Создать доставку
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Купить страховку
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Найти маршрут
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <RiskMatrixCard risks={riskFactors} />
      <InsurancePlansCard plans={insurancePlans} />
      <ConvoyStatusCard convoyStrength="82%" escorts={convoyEscorts} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <LocalShippingIcon sx={{ fontSize: '1.4rem', color: 'warning.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Logistics Control Center
        </Typography>
      </Box>
      <Divider />
      <Alert
        severity="info"
        icon={<MapIcon fontSize="small" />}
        sx={{ fontSize: cyberpunkTokens.fonts.sm }}
      >
        Управляйте доставками: транспорт (ON_FOOT → AERODYNE), маршруты, страхование, сопровождение конвоев.
        Риск-менеджмент: ambush, weather, mechanical. Все в одном экране.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <RoutesCard routes={demoRoutes} />
        </Grid>
        <Grid item xs={12} md={6}>
          <VehicleStatsCard vehicles={vehicleStats} />
        </Grid>
        <Grid item xs={12}>
          <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
            Active Shipments
          </Typography>
        </Grid>
        {demoShipments.map((shipment) => (
          <Grid key={shipment.shipment_id} item xs={12} md={6} lg={4}>
            <ShipmentCard shipment={shipment} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default LogisticsPage

