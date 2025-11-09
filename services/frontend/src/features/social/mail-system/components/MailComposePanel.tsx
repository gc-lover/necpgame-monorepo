import { useState } from 'react'
import {
  Alert,
  Button,
  Card,
  CardContent,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import { usePostMail } from '@/api/generated/mail-system/mail/mail'
import type { MailSendRequest } from '@/api/generated/mail-system/models/mail-components'

export function MailComposePanel() {
  const [recipients, setRecipients] = useState('')
  const [subject, setSubject] = useState('')
  const [body, setBody] = useState('')
  const [statusMessage, setStatusMessage] = useState<string | null>(null)
  const [isError, setIsError] = useState(false)

  const { mutate: sendMail, isPending } = usePostMail()

  const handleSend = () => {
    const normalizedRecipients = recipients
      .split(',')
      .map((value) => value.trim())
      .filter(Boolean)
    if (!normalizedRecipients.length) {
      setStatusMessage('Добавь хотя бы одного получателя.')
      setIsError(true)
      return
    }

    const payload: MailSendRequest = {
      recipients: normalizedRecipients,
      subject: subject || 'Без темы',
      body,
    }

    sendMail(
      { data: payload },
      {
        onSuccess: (response) => {
          setStatusMessage(`Письмо отправлено: ${response.data.mailId}`)
          setIsError(false)
        },
        onError: (error) => {
          setStatusMessage(error?.message ?? 'Не удалось отправить письмо')
          setIsError(true)
        },
      }
    )
  }

  return (
    <Card variant="outlined">
      <CardContent>
        <Stack spacing={2}>
          <Typography variant="h6">Новая рассылка</Typography>
          <Typography variant="body2" color="text.secondary">
            Используй эту форму для отправки личных и системных писем. UID добавляется через список получателей.
          </Typography>

          <TextField
            label="Получатели (через запятую)"
            value={recipients}
            onChange={(event) => setRecipients(event.target.value)}
            size="small"
            fullWidth
          />
          <TextField
            label="Тема"
            value={subject}
            onChange={(event) => setSubject(event.target.value)}
            size="small"
            fullWidth
          />
          <TextField
            label="Текст письма"
            value={body}
            onChange={(event) => setBody(event.target.value)}
            size="small"
            fullWidth
            multiline
            minRows={4}
          />

          <Button variant="contained" onClick={handleSend} disabled={isPending}>
            Отправить
          </Button>

          {statusMessage && (
            <Alert severity={isError ? 'error' : 'success'}>{statusMessage}</Alert>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}


