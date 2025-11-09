package com.necpgame.backjava.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * РљРѕРЅС„РёРіСѓСЂР°С†РёСЏ Jackson ObjectMapper
 * Р РµРіРёСЃС‚СЂРёСЂСѓРµС‚ JsonNullableModule РґР»СЏ РїРѕРґРґРµСЂР¶РєРё JsonNullable РІ OpenAPI РјРѕРґРµР»СЏС…
 */
@Configuration
public class ObjectMapperConfig {
    
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new JsonNullableModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}

