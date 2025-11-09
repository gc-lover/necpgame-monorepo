/**
 * Диалоговое окно с NPC
 * Данные из OpenAPI: NPCDialogue
 */
import { Paper, Typography, Stack, Button, Box, Divider } from '@mui/material'
import ChatIcon from '@mui/icons-material/Chat'
import type { NPCDialogue } from '@/api/generated/npcs/models'

interface DialogueBoxProps {
  dialogue: NPCDialogue
  onSelectOption: (optionId: string) => void
  npcName?: string
}

export function DialogueBox({ dialogue, onSelectOption, npcName }: DialogueBoxProps) {
  return (
    <Paper elevation={3} sx={{ p: 2, maxWidth: 600, mx: 'auto' }}>
      <Stack spacing={2}>
        {/* NPC текст */}
        <Box sx={{ bgcolor: 'rgba(0, 247, 255, 0.05)', p: 1.5, borderRadius: 1, borderLeft: '3px solid', borderColor: 'primary.main' }}>
          <Stack direction="row" spacing={1} alignItems="center" sx={{ mb: 1 }}>
            <ChatIcon color="primary" sx={{ fontSize: '1rem' }} />
            <Typography variant="subtitle2" sx={{ fontSize: '0.8rem', color: 'primary.main', fontWeight: 'bold' }}>
              {npcName || 'NPC'}
            </Typography>
          </Stack>
          <Typography variant="body2" sx={{ fontSize: '0.85rem', whiteSpace: 'pre-wrap' }}>
            {dialogue.text}
          </Typography>
        </Box>

        <Divider />

        {/* Опции ответа */}
        <Stack spacing={1}>
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary', textTransform: 'uppercase' }}>
            Выберите ответ:
          </Typography>
          {dialogue.options.map((option, index) => (
            <Button
              key={option.id}
              variant="outlined"
              size="small"
              onClick={() => onSelectOption(option.id)}
              sx={{
                justifyContent: 'flex-start',
                textAlign: 'left',
                textTransform: 'none',
                fontSize: '0.8rem',
                py: 1,
                '&:hover': {
                  borderColor: 'primary.main',
                  bgcolor: 'rgba(0, 247, 255, 0.1)',
                },
              }}
            >
              <Typography variant="body2" sx={{ fontSize: '0.8rem' }}>
                {index + 1}. {option.text}
              </Typography>
            </Button>
          ))}
        </Stack>

        {dialogue.npcState && (
          <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.disabled', fontStyle: 'italic' }}>
            Состояние: {dialogue.npcState}
          </Typography>
        )}
      </Stack>
    </Paper>
  )
}

