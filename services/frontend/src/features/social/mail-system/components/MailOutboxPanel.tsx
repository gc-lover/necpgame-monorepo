import { useState } from 'react'
import {
  Alert,
  Button,
  Card,
  CardContent,
  List,
  ListItemButton,
  ListItemText,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import {
  useGetMailOutbox,
  useGetMailMailId,
  useDeleteMailMailId,
  usePostMailMailIdFlag,
} from '@/api/generated/mail-system/mail/mail'
import type { MailFlagRequest } from '@/api/generated/mail-system/models/mail-components'

export function MailOutboxPanel() {
  const [selectedMailId, setSelectedMailId] = useState<string | null>(null)
  const [flagCategory, setFlagCategory] = useState('')
  const outboxQuery = useGetMailOutbox({ page_size: 20 })
  const { mutate: deleteMail, isPending: isDeleting } = useDeleteMailMailId()
  const { mutate: flagMail, isPending: isFlagging } = usePostMailMailIdFlag()

  const mailDetailQuery = useGetMailMailId(selectedMailId ?? '', {
    query: { enabled: Boolean(selectedMailId) },
  })

  const handleSelectMail = (mailId: string) => {
    setSelectedMailId(mailId)
  }

  const handleDelete = () => {
    if (!selectedMailId) {
      return
    }
    deleteMail(
      { mailId: selectedMailId },
      {
        onSuccess: () => {
          setSelectedMailId(null)
        },
      }
    )
  }

  const handleFlag = () => {
    if (!selectedMailId) {
      return
    }
    const payload: MailFlagRequest = {
      category: flagCategory || 'abuse',
      comment: 'Отметка из UI агента',
    }
    flagMail({ mailId: selectedMailId, data: payload })
  }

  const outboxItems = outboxQuery.data?.data.items ?? []

  return (
    <Stack spacing={3}>
      <Stack spacing={1}>
        <Typography variant="h6">Отправленные письма</Typography>
        <Typography variant="body2" color="text.secondary">
          Контролируй доставку COD и системных писем. Здесь можно отозвать отправку и создать жалобу.
        </Typography>
      </Stack>

      {outboxQuery.isError && (
        <Alert severity="error">Не удалось загрузить исходящие письма.</Alert>
      )}

      <Card variant="outlined">
        <CardContent>
          <Stack direction={{ xs: 'column', md: 'row' }} spacing={3}>
            <Stack flex={1}>
              <Typography variant="subtitle1" gutterBottom>
                Всего писем: {outboxItems.length}
              </Typography>
              <List dense sx={{ maxHeight: 240, overflowY: 'auto' }}>
                {outboxItems.map((item) => (
                  <ListItemButton
                    key={item.mailId}
                    selected={item.mailId === selectedMailId}
                    onClick={() => handleSelectMail(item.mailId)}
                  >
                    <ListItemText
                      primary={item.subject || 'Без темы'}
                      secondary={`${item.recipientName ?? item.recipientId ?? 'Неизвестный получатель'} — ${
                        item.status
                      }`}
                    />
                  </ListItemButton>
                ))}
              </List>
            </Stack>

            <Stack flex={1} spacing={2}>
              <Typography variant="subtitle1">Детали отправленного письма</Typography>
              {selectedMailId ? (
                <>
                  {mailDetailQuery.isLoading && <Typography>Загрузка...</Typography>}
                  {mailDetailQuery.isError && (
                    <Alert severity="error">Не удалось получить детали письма.</Alert>
                  )}
                  {mailDetailQuery.data && (
                    <Stack spacing={1}>
                      <Typography variant="body1" fontWeight={700}>
                        {mailDetailQuery.data.data.subject || 'Без темы'}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        Адресаты: {mailDetailQuery.data.data.recipientName ?? mailDetailQuery.data.data.recipientId}
                      </Typography>
                      <Typography variant="body2">{mailDetailQuery.data.data.body}</Typography>
                      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1}>
                        <Button
                          variant="contained"
                          color="warning"
                          onClick={handleDelete}
                          disabled={isDeleting}
                        >
                          Отозвать письмо
                        </Button>
                        <TextField
                          label="Категория жалобы"
                          size="small"
                          value={flagCategory}
                          onChange={(event) => setFlagCategory(event.target.value)}
                        />
                        <Button
                          variant="outlined"
                          onClick={handleFlag}
                          disabled={isFlagging}
                        >
                          Пожаловаться
                        </Button>
                      </Stack>
                    </Stack>
                  )}
                </>
              ) : (
                <Typography variant="body2" color="text.secondary">
                  Выбери письмо, чтобы увидеть содержание и выполнить дополнительные действия.
                </Typography>
              )}
            </Stack>
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )
}


