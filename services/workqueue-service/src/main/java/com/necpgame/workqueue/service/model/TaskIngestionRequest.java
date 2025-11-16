package com.necpgame.workqueue.service.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record TaskIngestionRequest(
        String sourceId,
        String segment,
        String initialStatus,
        int priority,
        String title,
        String summary,
        List<String> knowledgeRefs,
        Templates templates,
        Map<String, Object> payload,
        HandoffPlan handoffPlan
) {
    public TaskIngestionRequest {
        knowledgeRefs = knowledgeRefs == null ? List.of() : List.copyOf(knowledgeRefs);
        templates = templates == null ? Templates.empty() : templates;
        payload = payload == null ? Map.of() : Collections.unmodifiableMap(payload);
        handoffPlan = handoffPlan == null ? HandoffPlan.empty() : handoffPlan;
    }

    public record Templates(
            List<String> primary,
            List<String> checklists,
            List<TemplateReference> references
    ) {
        public Templates {
            primary = primary == null ? List.of() : List.copyOf(primary);
            checklists = checklists == null ? List.of() : List.copyOf(checklists);
            references = references == null ? List.of() : List.copyOf(references);
        }

        public static Templates empty() {
            return new Templates(List.of(), List.of(), List.of());
        }
    }

    public record TemplateReference(
            String code,
            String version,
            String path
    ) {
    }

    public record HandoffPlan(
            String nextSegment,
            List<HandoffCondition> conditions,
            String notes
    ) {
        public HandoffPlan {
            conditions = conditions == null ? List.of() : List.copyOf(conditions);
        }

        public static HandoffPlan empty() {
            return new HandoffPlan(null, List.of(), null);
        }
    }

    public record HandoffCondition(
            String status,
            String targetSegment
    ) {
    }
}

