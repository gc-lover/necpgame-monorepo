package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.WorldEventEntity;
import com.necpgame.backjava.entity.enums.WorldEventType;
import com.necpgame.backjava.model.GenerateEventRequest;
import com.necpgame.backjava.model.GeneratedEvent;
import com.necpgame.backjava.model.WorldEvent;
import com.necpgame.backjava.repository.WorldEventRepository;
import com.necpgame.backjava.repository.specification.WorldEventSpecifications;
import com.necpgame.backjava.service.EventGenerationService;
import com.necpgame.backjava.service.mapper.WorldEventMapper;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional(readOnly = true)
public class EventGenerationServiceImpl implements EventGenerationService {

    private final WorldEventRepository worldEventRepository;
    private final WorldEventMapper worldEventMapper;

    public EventGenerationServiceImpl(WorldEventRepository worldEventRepository,
                                      WorldEventMapper worldEventMapper) {
        this.worldEventRepository = worldEventRepository;
        this.worldEventMapper = worldEventMapper;
    }

    @Override
    public GeneratedEvent generateWorldEvent(GenerateEventRequest generateEventRequest) {
        validateRequest(generateEventRequest);

        Specification<WorldEventEntity> specification = Specification.where(WorldEventSpecifications.byEra(generateEventRequest.getEra()))
                .and(WorldEventSpecifications.isActive());

        WorldEventType requestedType = extractEventType(generateEventRequest.getEventType());
        if (requestedType != null) {
            specification = specification.and(WorldEventSpecifications.byType(requestedType));
        }

        List<WorldEventEntity> candidates = worldEventRepository.findAll(specification);
        if (candidates.isEmpty()) {
            if (Boolean.TRUE.equals(generateEventRequest.getForceGenerate())) {
                Specification<WorldEventEntity> relaxed = Specification.where(WorldEventSpecifications.byEra(generateEventRequest.getEra()));
                if (requestedType != null) {
                    relaxed = relaxed.and(WorldEventSpecifications.byType(requestedType));
                }
                candidates = worldEventRepository.findAll(relaxed);
            }

            if (candidates.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Невозможно сгенерировать событие по заданным параметрам");
            }
        }

        int roll = ThreadLocalRandom.current().nextInt(1, 101);
        WorldEventEntity selected = candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
        WorldEvent mapped = worldEventMapper.toWorldEvent(selected);

        GeneratedEvent generatedEvent = new GeneratedEvent();
        generatedEvent.setEvent(mapped);
        generatedEvent.setGenerationRoll(roll);
        generatedEvent.setGenerationTable(buildGenerationTable(generateEventRequest.getEra(), requestedType));
        return generatedEvent;
    }

    private void validateRequest(GenerateEventRequest request) {
        if (request.getEra() == null || request.getEra().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Поле era обязательно");
        }
        if (request.getLocation() == null || request.getLocation().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Поле location обязательно");
        }
    }

    private WorldEventType extractEventType(JsonNullable<String> eventType) {
        if (eventType == null || !eventType.isPresent()) {
            return null;
        }
        String value = eventType.get();
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return WorldEventType.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное значение event_type");
        }
    }

    private String buildGenerationTable(String era, WorldEventType type) {
        if (type == null) {
            return "era-" + era + "-d100";
        }
        return "era-" + era + '-' + type.name().toLowerCase() + "-d100";
    }
}

