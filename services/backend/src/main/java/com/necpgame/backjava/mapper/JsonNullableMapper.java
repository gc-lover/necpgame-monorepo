package com.necpgame.backjava.mapper;

import org.mapstruct.Named;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

/**
 * РћР±С‰РёР№ РјР°РїРїРµСЂ РґР»СЏ РєРѕРЅРІРµСЂС‚Р°С†РёРё РІ/РёР· JsonNullable
 * РСЃРїРѕР»СЊР·СѓРµС‚СЃСЏ РІСЃРµРјРё MapStruct РјР°РїРїРµСЂР°РјРё РґР»СЏ РёР·Р±РµР¶Р°РЅРёСЏ РґСѓР±Р»РёСЂРѕРІР°РЅРёСЏ
 */
@Component
public class JsonNullableMapper {
    
    @Named("stringToJsonNullable")
    public JsonNullable<String> stringToJsonNullable(String value) {
        return value != null ? JsonNullable.of(value) : JsonNullable.undefined();
    }
    
    @Named("jsonNullableToString")
    public String jsonNullableToString(JsonNullable<String> jsonNullable) {
        return jsonNullable != null && jsonNullable.isPresent() ? jsonNullable.get() : null;
    }
    
    @Named("uuidToJsonNullable")
    public JsonNullable<java.util.UUID> uuidToJsonNullable(java.util.UUID value) {
        return value != null ? JsonNullable.of(value) : JsonNullable.undefined();
    }
    
    @Named("dateToJsonNullable")
    public JsonNullable<java.time.OffsetDateTime> dateToJsonNullable(java.time.OffsetDateTime value) {
        return value != null ? JsonNullable.of(value) : JsonNullable.undefined();
    }
}

