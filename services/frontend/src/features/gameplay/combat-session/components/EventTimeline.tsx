import { Card, CardContent, Chip, Divider, Stack, Typography } from '@mui/material'
import type { CombatEvent } from '@/api/generated/gameplay/combat-session/models'
import { format } from 'date-fns'

interface EventTimelineProps {
  events?: CombatEvent[]
}

const formatTimestamp = (value?: string) => {
  if (!value) {
    return '—'
  }

  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }

  return format(date, 'HH:mm:ss')
}

export const EventTimeline = ({ events = [] }: EventTimelineProps) => (
  <Card variant="outlined">
    <CardContent>
      <Stack spacing={1.5}>
        <Typography variant="h6" fontSize="1rem" fontWeight={600}>
          Хронология событий
        </Typography>
        <Divider />
        <Stack spacing={1.5} maxHeight={320} sx={{ overflowY: 'auto' }}>
          {events.length === 0 && (
            <Typography variant="body2" color="text.secondary">
              События отсутствуют
            </Typography>
          )}
          {events.map(event => (
            <Stack key={event.event_id ?? `${event.timestamp}-${event.event_type}`} spacing={0.5}>
              <Stack direction="row" spacing={1} alignItems="center">
                <Typography variant="caption" color="text.secondary">
                  {formatTimestamp(event.timestamp)}
                </Typography>
                {event.event_type && <Chip label={event.event_type} size="small" color="info" />}
              </Stack>
              <Typography variant="body2">
                {event.actor_id ? `${event.actor_id}` : 'UNKNOWN'} →{' '}
                {event.target_id ?? 'UNKNOWN'}
              </Typography>
              {event.data && (
                <Typography variant="caption" color="text.secondary">
                  {JSON.stringify(event.data)}
                </Typography>
              )}
              <Divider />
            </Stack>
          ))}
        </Stack>
      </Stack>
    </CardContent>
  </Card>
)


