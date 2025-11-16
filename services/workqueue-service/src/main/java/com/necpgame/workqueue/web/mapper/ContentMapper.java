package com.necpgame.workqueue.web.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.content.ContentEntityAttributeEntity;
import com.necpgame.workqueue.domain.content.ContentEntityHistoryEntity;
import com.necpgame.workqueue.domain.content.ContentEntityLinkEntity;
import com.necpgame.workqueue.domain.content.ContentEntityLocalizationEntity;
import com.necpgame.workqueue.domain.content.ContentEntityNoteEntity;
import com.necpgame.workqueue.domain.content.ContentEntitySectionEntity;
import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.web.dto.content.ContentAttributeDto;
import com.necpgame.workqueue.web.dto.content.ContentDetailDto;
import com.necpgame.workqueue.web.dto.content.ContentHistoryDto;
import com.necpgame.workqueue.web.dto.content.ContentLinkDto;
import com.necpgame.workqueue.web.dto.content.ContentListResponseDto;
import com.necpgame.workqueue.web.dto.content.ContentLocalizationDto;
import com.necpgame.workqueue.web.dto.content.ContentNoteDto;
import com.necpgame.workqueue.web.dto.content.ContentSectionDto;
import com.necpgame.workqueue.web.dto.content.ContentSummaryDto;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContentMapper {
    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<>() {
    };
    private final ObjectMapper objectMapper;

    public ContentListResponseDto toListResponse(Page<ContentEntryEntity> page) {
        List<ContentSummaryDto> items = page.stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
        return new ContentListResponseDto(items, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
    }

    public ContentDetailDto toDetail(ContentEntryEntity entity) {
        ContentSummaryDto summary = toSummary(entity);
        List<ContentSectionDto> sections = entity.getSections().stream()
                .map(this::toSection)
                .collect(Collectors.toList());
        List<ContentAttributeDto> attributes = entity.getAttributes().stream()
                .map(this::toAttribute)
                .collect(Collectors.toList());
        List<ContentLinkDto> links = entity.getOutgoingLinks().stream()
                .map(this::toLink)
                .collect(Collectors.toList());
        List<ContentLocalizationDto> localizations = entity.getLocalizations().stream()
                .map(this::toLocalization)
                .collect(Collectors.toList());
        List<ContentNoteDto> notes = entity.getNotes().stream()
                .map(this::toNote)
                .collect(Collectors.toList());
        List<ContentHistoryDto> history = entity.getHistory().stream()
                .map(this::toHistory)
                .collect(Collectors.toList());
        return new ContentDetailDto(
                summary,
                entity.getSummary(),
                entity.getSourceDocument(),
                parseStringList(entity.getTags()),
                parseStringList(entity.getTopics()),
                parseMap(entity.getMetadataJson()),
                sections,
                attributes,
                links,
                localizations,
                notes,
                history
        );
    }

    public ContentSummaryDto toSummary(ContentEntryEntity entity) {
        return new ContentSummaryDto(
                entity.getId(),
                entity.getCode(),
                entity.getTitle(),
                toEnum(entity.getEntityType()),
                toEnum(entity.getStatus()),
                toEnum(entity.getCategory()),
                toEnum(entity.getVisibility()),
                toEnum(entity.getRiskLevel()),
                entity.getOwnerRole(),
                entity.getVersion(),
                entity.getLastUpdated(),
                entity.getCreatedAt()
        );
    }

    private ContentSectionDto toSection(ContentEntitySectionEntity section) {
        return new ContentSectionDto(
                section.getId(),
                toEnum(section.getSectionKey()),
                section.getTitle(),
                section.getBody(),
                section.getSortOrder(),
                parseObject(section.getMetadataJson())
        );
    }

    private ContentAttributeDto toAttribute(ContentEntityAttributeEntity attribute) {
        return new ContentAttributeDto(
                attribute.getId(),
                toEnum(attribute.getAttributeKey()),
                toEnum(attribute.getValueType()),
                attribute.getValueString(),
                attribute.getValueInt(),
                attribute.getValueDecimal(),
                attribute.getValueBoolean(),
                parseObject(attribute.getValueJson()),
                attribute.getSource()
        );
    }

    private ContentLinkDto toLink(ContentEntityLinkEntity link) {
        UUID targetId = link.getTarget() != null ? link.getTarget().getId() : null;
        String targetCode = link.getTarget() != null ? link.getTarget().getCode() : null;
        String targetTitle = link.getTarget() != null ? link.getTarget().getTitle() : null;
        return new ContentLinkDto(
                link.getId(),
                toEnum(link.getRelationType()),
                targetId,
                targetCode,
                targetTitle,
                link.getNotes()
        );
    }

    private ContentLocalizationDto toLocalization(ContentEntityLocalizationEntity localization) {
        return new ContentLocalizationDto(
                localization.getId(),
                localization.getLocale(),
                localization.getTitleLocalized(),
                localization.getDescriptionLocalized(),
                localization.getFlavorText(),
                parseObject(localization.getMetadataJson())
        );
    }

    private ContentNoteDto toNote(ContentEntityNoteEntity note) {
        UUID authorId = note.getAuthor() != null ? note.getAuthor().getId() : null;
        String authorName = note.getAuthor() != null ? note.getAuthor().getDisplayName() : null;
        return new ContentNoteDto(
                note.getId(),
                authorId,
                authorName,
                note.getNoteText(),
                note.getCreatedAt()
        );
    }

    private ContentHistoryDto toHistory(ContentEntityHistoryEntity history) {
        return new ContentHistoryDto(
                history.getId(),
                history.getVersion(),
                history.getChangedAt(),
                history.getChangedBy(),
                history.getChangesSummary(),
                history.getDiffBlob()
        );
    }

    public EnumValueDto toEnum(EnumValueEntity value) {
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

    private List<String> parseStringList(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, STRING_LIST_TYPE);
        } catch (JsonProcessingException e) {
            return List.of();
        }
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


