import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import ConfirmationNumberIcon from '@mui/icons-material/ConfirmationNumber'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { MatchTicket } from '../types'

const statusColor: Record<MatchTicket['status'], string> = {
  SEARCHING: '#05ffa1',
  READY_CHECK: '#fef86c',
  CONFIRMED: '#00f7ff',
}

export interface MatchTicketCardProps {
  ticket: MatchTicket
}

export const MatchTicketCard: React.FC<MatchTicketCardProps> = ({ ticket }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <ConfirmationNumberIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Ticket {ticket.ticketId}
        </Typography>
        <Chip
          label={ticket.status}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${statusColor[ticket.status]}`, color: statusColor[ticket.status], ml: 'auto' }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Mode: {ticket.mode} · Players: {ticket.players}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Latency: {ticket.latencyMs} ms · Created: {ticket.createdAt}
      </Typography>
    </Stack>
  </CompactCard>
)

export default MatchTicketCard


