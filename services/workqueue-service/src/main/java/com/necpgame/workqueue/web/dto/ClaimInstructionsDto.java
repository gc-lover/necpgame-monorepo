package com.necpgame.workqueue.web.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.necpgame.workqueue.web.dto.agent.AgentBriefDto;

import java.util.List;

public record ClaimInstructionsDto(
        AgentBriefDto brief,
        List<String> knowledgeRefs,
        List<ClaimTemplateDto> templates,
        JsonNode handoffPlan,
        SubmitContractDto submitContract
) {
}

