import { useState } from 'react'
import {
  Alert,
  Button,
  Card,
  CardContent,
  Divider,
  List,
  ListItemButton,
  ListItemText,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import {
  useGetMailInbox,
  useGetMailMailId,
  usePostMailMailIdAttachmentsClaim,
} from '@/api/generated/mail-system/mail/mail'
import type { AttachmentClaimRequest } from '@/api/generated/mail-system/models/mail-components'

export function MailInboxPanel() {
  const [selectedMailId, setSelectedMailId] = useState<string | null>(null)
  const [attachmentIds, setAttachmentIds] = useState('')
  const inboxQuery = useGetMailInbox({ page_size: 20 })
  const { mutate: claimAttachments, isPending: isClaiming } = usePostMailMailIdAttachmentsClaim()

  const mailDetailQuery = useGetMailMailId(selectedMailId ?? '', {
    query: { enabled: Boolean(selectedMailId) },
  })

  const handleSelectMail = (mailId: string) => {
    setSelectedMailId(mailId)
  }

  const handleClaim = () => {
    if (!selectedMailId) {
      return
    }
    const payload: AttachmentClaimRequest = attachmentIds.trim()
      ? { attachmentIds: attachmentIds.split(',').map((value) => value.trim()) }
      : {}
    claimAttachments(
      { mailId: selectedMailId, data: payload },
      {
        onSuccess: () => {
          setAttachmentIds('')
        },
      }
    )
  }

  const inboxItems = inboxQuery.data?.data.items ?? []

  return (
    <Stack spacing={3}>
      <Stack spacing={1}>
        <Typography variant="h6">Входящие письма</Typography>
        <Typography variant="body2" color="text.secondary">
          Используй фильтры в backend: unread, system, attachments. Здесь показаны первые 20 писем.
        </Typography>
      </Stack>

      {inboxQuery.isError && (
        <Alert severity="error">Не удалось загрузить почтовый ящик. Попробуй позже.</Alert>
      )}

      <Card variant="outlined">
        <CardContent>
          <Stack direction={{ xs: 'column', md: 'row' }} spacing={3}>
            <Stack flex={1}>
              <Typography variant="subtitle1" gutterBottom>
                Список писем ({inboxItems.length})
              </Typography>
              <List dense sx={{ maxHeight: 240, overflowY: 'auto' }}>
                {inboxItems.map((item) => (
                  <ListItemButton
                    key={item.mailId}
                    selected={item.mailId === selectedMailId}
                    onClick={() => handleSelectMail(item.mailId)}
                  >
                    <ListItemText
                      primary={item.subject || 'Без темы'}
                      secondary={`${item.senderName ?? item.senderId ?? 'Неизвестный отправитель'} — ${
                        item.status
                      }`}
                    />
                  </ListItemButton>
                ))}
              </List>
            </Stack>

            <Divider orientation="vertical" flexItem sx={{ display: { xs: 'none', md: 'block' } }} />

            <Stack flex={1} spacing={2}>
              <Typography variant="subtitle1">Детали письма</Typography>
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
                        От: {mailDetailQuery.data.data.senderName ?? mailDetailQuery.data.data.senderId}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        Получатель: {mailDetailQuery.data.data.recipientName ?? mailDetailQuery.data.data.recipientId}
                      </Typography>
                      <Typography variant="body2">{mailDetailQuery.data.data.body}</Typography>

                      {mailDetailQuery.data.data.attachments?.length ? (
                        <Alert severity="info">
                          Вложения: {mailDetailQuery.data.data.attachments.map((attachment) => attachment.attachmentId).join(', ')}
                        </Alert>
                      ) : (
                        <Typography variant="body2" color="text.secondary">
                          Вложений нет.
                        </Typography>
                      )}

                      {mailDetailQuery.data.data.attachments?.length ? (
                        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1}>
                          <TextField
                            label="Идентификаторы вложений (через запятую)"
                            value={attachmentIds}
                            onChange={(event) => setAttachmentIds(event.target.value)}
                            size="small"
                            fullWidth
                          />
                          <Button
                            variant="contained"
                            onClick={handleClaim}
                            disabled={isClaiming}
                          >
                            Забрать вложения
                          </Button>
                        </Stack>
                      ) : null}
                    </Stack>
                  )}
                </>
              ) : (
                <Typography variant="body2" color="text.secondary">
                  Выбери письмо, чтобы увидеть детали, вложения и историю.
                </Typography>
              )}
            </Stack>
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )
}


