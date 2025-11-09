import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box } from '@mui/material'
import RouterIcon from '@mui/icons-material/Router'
import LockIcon from '@mui/icons-material/Lock'
import LockOpenIcon from '@mui/icons-material/LockOpen'

interface NetworkNodeProps {
  node: {
    node_id?: string
    type?: string
    status?: string
    security_level?: number
  }
  compromised?: boolean
}

export const NetworkNodeCard: React.FC<NetworkNodeProps> = ({ node, compromised = false }) => {
  const getNodeTypeColor = (type?: string) => {
    switch (type) {
      case 'camera':
        return 'info'
      case 'door':
        return 'warning'
      case 'router':
        return 'primary'
      case 'server':
        return 'error'
      default:
        return 'default'
    }
  }

  return (
    <Card
      sx={{
        border: '1px solid',
        borderColor: compromised ? 'success.main' : 'divider',
        bgcolor: compromised ? 'rgba(76, 175, 80, 0.05)' : 'transparent',
      }}
    >
      <CardContent sx={{ p: 1, '&:last-child': { pb: 1 } }}>
        <Stack spacing={0.3}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box display="flex" alignItems="center" gap={0.5}>
              <RouterIcon sx={{ fontSize: '0.9rem', color: compromised ? 'success.main' : 'text.secondary' }} />
              <Typography variant="caption" fontSize="0.7rem" fontWeight="bold">
                {node.node_id}
              </Typography>
            </Box>
            {compromised ? <LockOpenIcon sx={{ fontSize: '0.8rem', color: 'success.main' }} /> : <LockIcon sx={{ fontSize: '0.8rem', color: 'error.main' }} />}
          </Box>
          <Box display="flex" gap={0.3}>
            <Chip label={node.type || 'node'} size="small" color={getNodeTypeColor(node.type)} sx={{ height: 14, fontSize: '0.55rem' }} />
            {node.security_level && <Chip label={`SEC ${node.security_level}`} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />}
          </Box>
        </Stack>
      </CardContent>
    </Card>
  )
}

