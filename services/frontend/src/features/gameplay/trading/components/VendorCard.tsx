/**
 * Карточка торговца
 * Данные из OpenAPI: Vendor
 */
import { Card, CardContent, Typography, Chip, Stack, Avatar } from '@mui/material'
import StoreIcon from '@mui/icons-material/Store'
import type { Vendor } from '@/api/generated/trading/models'

interface VendorCardProps {
  vendor: Vendor
  onClick: () => void
}

export function VendorCard({ vendor, onClick }: VendorCardProps) {
  return (
    <Card
      sx={{
        cursor: 'pointer',
        border: '1px solid',
        borderColor: 'divider',
        '&:hover': {
          borderColor: 'primary.main',
          boxShadow: 2,
          transform: 'translateY(-2px)',
          transition: 'all 0.2s',
        },
      }}
      onClick={onClick}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack direction="row" spacing={1.5} alignItems="center">
          <Avatar sx={{ width: 40, height: 40, bgcolor: 'success.main' }}>
            <StoreIcon />
          </Avatar>
          
          <Stack flex={1}>
            <Typography variant="subtitle2" sx={{ fontSize: '0.875rem', fontWeight: 'bold' }}>
              {vendor.name}
            </Typography>
            
            {vendor.specialization && (
              <Chip 
                label={vendor.specialization} 
                size="small" 
                color="success" 
                sx={{ height: 16, fontSize: '0.6rem', mt: 0.5, alignSelf: 'flex-start' }} 
              />
            )}
          </Stack>
        </Stack>
      </CardContent>
    </Card>
  )
}

