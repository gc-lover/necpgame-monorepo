package com.necpgame.workqueue.web.dto.content;

import java.util.List;
import java.util.Map;

public record ContentDetailDto(
        ContentSummaryDto summary,
        String overview,
        String sourceDocument,
        List<String> tags,
        List<String> topics,
        Map<String, Object> metadata,
        List<ContentSectionDto> sections,
        List<ContentAttributeDto> attributes,
        List<ContentLinkDto> links,
        List<ContentLocalizationDto> localizations,
        List<ContentNoteDto> notes,
        List<ContentHistoryDto> history
) {
}


