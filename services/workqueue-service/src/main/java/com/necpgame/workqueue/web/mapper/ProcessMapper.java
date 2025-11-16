package com.necpgame.workqueue.web.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.process.ChecklistDefinitionEntity;
import com.necpgame.workqueue.domain.process.ChecklistDefinitionItemEntity;
import com.necpgame.workqueue.domain.process.ProcessTemplateEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;
import com.necpgame.workqueue.web.dto.process.ChecklistDefinitionDto;
import com.necpgame.workqueue.web.dto.process.ChecklistItemDto;
import com.necpgame.workqueue.web.dto.process.ChecklistListResponseDto;
import com.necpgame.workqueue.web.dto.process.ProcessTemplateDto;
import com.necpgame.workqueue.web.dto.process.ProcessTemplateListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProcessMapper {
    private final ObjectMapper objectMapper;

    public ProcessTemplateListResponseDto toTemplateList(List<ProcessTemplateEntity> templates) {
        List<ProcessTemplateDto> dto = templates.stream()
                .map(this::toTemplate)
                .collect(Collectors.toList());
        return new ProcessTemplateListResponseDto(dto);
    }

    public ProcessTemplateDto toTemplate(ProcessTemplateEntity template) {
        return new ProcessTemplateDto(
                template.getId(),
                toEnum(template.getCode()),
                template.getName(),
                template.getDescription(),
                parseMap(template.getSchemaJson()),
                template.getUsageNotes(),
                template.getCreatedAt(),
                template.getUpdatedAt()
        );
    }

    public ChecklistListResponseDto toChecklistList(List<ChecklistDefinitionEntity> checklists) {
        List<ChecklistDefinitionDto> dto = checklists.stream()
                .map(this::toChecklist)
                .collect(Collectors.toList());
        return new ChecklistListResponseDto(dto);
    }

    public ChecklistDefinitionDto toChecklist(ChecklistDefinitionEntity definition) {
        List<ChecklistItemDto> items = definition.getItems().stream()
                .map(this::toChecklistItem)
                .collect(Collectors.toList());
        return new ChecklistDefinitionDto(
                definition.getId(),
                toEnum(definition.getCode()),
                definition.getName(),
                definition.getDescription(),
                Boolean.TRUE.equals(definition.getRequired()),
                definition.getCreatedAt(),
                definition.getUpdatedAt(),
                items
        );
    }

    private ChecklistItemDto toChecklistItem(ChecklistDefinitionItemEntity item) {
        return new ChecklistItemDto(
                item.getId(),
                item.getSortOrder(),
                item.getDescription(),
                item.getExpectedResult(),
                parseObject(item.getMetadataJson())
        );
    }

    private EnumValueDto toEnum(EnumValueEntity value) {
        if (value == null) {
            return null;
        }
        return new EnumValueDto(
                value.getId(),
                value.getCode(),
                value.getDisplayName(),
                value.getDescription()
        );
    }

    private Map<String, Object> parseMap(String json) {
        Object parsed = parseObject(json);
        if (parsed instanceof Map<?, ?> map) {
            Map<String, Object> result = new LinkedHashMap<>();
            map.forEach((key, value) -> {
                if (key instanceof String stringKey) {
                    result.put(stringKey, value);
                }
            });
            return result;
        }
        return Collections.emptyMap();
    }

    private Object parseObject(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, Object.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}


