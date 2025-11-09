import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import ChatBubbleOutlineIcon from '@mui/icons-material/ChatBubbleOutline'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface DialogueChoice {
  id: string
  text: string
  impact: string
}

export interface DialogueSummary {
  npcName: string
  tone: string
  stage: string
  dialogueText: string
  choices: DialogueChoice[]
}

export interface DialogueGeneratorCardProps {
  dialogue: DialogueSummary
}

export const DialogueGeneratorCard: React.FC<DialogueGeneratorCardProps> = ({ dialogue }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <ChatBubbleOutlineIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Dialogue Generator · {dialogue.npcName}
        </Typography>
        <Chip label={dialogue.tone} size="small" color="info" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Stage: {dialogue.stage}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        “{dialogue.dialogueText}”
      </Typography>
      <Stack spacing={0.2}>
        {dialogue.choices.slice(0, 3).map((choice) => (
          <Typography key={choice.id} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {choice.text} [{choice.impact}]
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default DialogueGeneratorCard


