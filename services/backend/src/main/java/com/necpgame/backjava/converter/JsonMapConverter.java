package com.necpgame.backjava.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Конвертер Map<String, Object> ↔ JSONB для хранения произвольных настроек игрока.
 */
@Converter(autoApply = false)
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        Map<String, Object> value = attribute == null ? Collections.emptyMap() : attribute;
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось сериализовать настройки игрока в JSON", ex);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return new HashMap<>();
        }
        try {
            return OBJECT_MAPPER.readValue(dbData, Map.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось десериализовать JSON настроек игрока", ex);
        }
    }
}

