import { useMemo, useState } from 'react'
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  MenuItem,
  Stack,
  Typography,
} from '@mui/material'

type HackMethod = 'breach' | 'quickhack' | 'daemon'

interface UseFormValues {
  objectId: string
  locationId: string
}

interface HackFormValues {
  targetId: string
  method: HackMethod
}

interface BaseProps {
  open: boolean
  onClose: () => void
  isLoading?: boolean
}

interface UsePromptProps extends BaseProps {
  mode: 'use'
  defaultLocationId?: string
  onSubmit: (values: UseFormValues) => void
}

interface HackPromptProps extends BaseProps {
  mode: 'hack'
  onSubmit: (values: HackFormValues) => void
}

type ActionPromptDialogProps = UsePromptProps | HackPromptProps

export function ActionPromptDialog(props: ActionPromptDialogProps) {
  const { open, onClose, isLoading } = props
  const [objectId, setObjectId] = useState('')
  const [locationId, setLocationId] = useState(props.mode === 'use' ? props.defaultLocationId ?? '' : '')
  const [targetId, setTargetId] = useState('')
  const [method, setMethod] = useState<HackMethod>('breach')

  const canSubmit = useMemo(() => {
    if (props.mode === 'use') {
      return objectId.trim().length > 0
    }
    return targetId.trim().length > 0
  }, [props.mode, objectId, targetId])

  const resetState = () => {
    setObjectId('')
    setLocationId(props.mode === 'use' ? props.defaultLocationId ?? '' : '')
    setTargetId('')
    setMethod('breach')
  }

  const handleClose = () => {
    resetState()
    onClose()
  }

  const handleSubmit = () => {
    if (!canSubmit) return
    if (props.mode === 'use') {
      props.onSubmit({
        objectId: objectId.trim(),
        locationId: locationId.trim(),
      })
    } else {
      props.onSubmit({
        targetId: targetId.trim(),
        method,
      })
    }
    if (!isLoading) {
      resetState()
    }
  }

  const dialogTitle = props.mode === 'use' ? 'Использовать объект' : 'Взлом системы'

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
      <DialogTitle sx={{ fontSize: '1rem' }}>{dialogTitle}</DialogTitle>
      <DialogContent>
        <Stack spacing={2} sx={{ mt: 1 }}>
          {props.mode === 'use' ? (
            <>
              <TextField
                label="ID объекта"
                value={objectId}
                onChange={(event) => setObjectId(event.target.value)}
                size="small"
                autoFocus
              />
              <TextField
                label="ID локации"
                value={locationId}
                onChange={(event) => setLocationId(event.target.value)}
                size="small"
              />
              <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
                Укажите идентификатор объекта, который нужно активировать. Локацию можно оставить пустой.
              </Typography>
            </>
          ) : (
            <>
              <TextField
                label="ID цели"
                value={targetId}
                onChange={(event) => setTargetId(event.target.value)}
                size="small"
                autoFocus
              />
              <TextField
                label="Метод"
                select
                value={method}
                onChange={(event) => setMethod(event.target.value as HackMethod)}
                size="small"
              >
                <MenuItem value="breach">Breach</MenuItem>
                <MenuItem value="quickhack">Quickhack</MenuItem>
                <MenuItem value="daemon">Daemon</MenuItem>
              </TextField>
              <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
                Взлом доступен для систем в текущей локации. Выберите подходящий метод.
              </Typography>
            </>
          )}
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} size="small" sx={{ fontSize: '0.75rem' }}>
          Отмена
        </Button>
        <Button
          onClick={handleSubmit}
          variant="contained"
          size="small"
          sx={{ fontSize: '0.75rem' }}
          disabled={!canSubmit || Boolean(isLoading)}
        >
          Подтвердить
        </Button>
      </DialogActions>
    </Dialog>
  )
}





