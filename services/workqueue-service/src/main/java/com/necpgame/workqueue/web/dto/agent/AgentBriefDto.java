package com.necpgame.workqueue.web.dto.agent;

import java.util.List;

public record AgentBriefDto(
        String segment,
        String roleKey,
        String title,
        String mission,
        List<String> responsibilities,
        List<String> submissionChecklist,
        List<String> handoffNotes
) {
}


