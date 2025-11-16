package com.necpgame.workqueue.web.dto.qa;

import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.util.UUID;

public record QaPlanItemDto(
        UUID id,
        int sortOrder,
        String description,
        String expectedResult,
        EnumValueDto testType,
        String automationStatus,
        Object metadata
) {
}


