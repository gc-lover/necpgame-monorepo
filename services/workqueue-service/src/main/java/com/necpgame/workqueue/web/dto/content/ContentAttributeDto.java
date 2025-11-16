package com.necpgame.workqueue.web.dto.content;

import java.math.BigDecimal;
import java.util.UUID;

public record ContentAttributeDto(
        UUID id,
        EnumValueDto key,
        EnumValueDto valueType,
        String valueString,
        Long valueInt,
        BigDecimal valueDecimal,
        Boolean valueBoolean,
        Object valueJson,
        String source
) {
}


