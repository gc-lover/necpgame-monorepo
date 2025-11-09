package com.necpgame.backjava.mapper;

import com.necpgame.backjava.entity.CharacterAppearanceEntity;
import com.necpgame.backjava.model.GameCharacterAppearance;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * MapStruct РјР°РїРїРµСЂ РґР»СЏ РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ CharacterAppearanceEntity <-> GameCharacterAppearance DTO
 * РђРІС‚РѕРјР°С‚РёС‡РµСЃРєРё РіРµРЅРµСЂРёСЂСѓРµС‚ РєРѕРґ РјР°РїРїРёРЅРіР° СЃ РїРѕРґРґРµСЂР¶РєРѕР№ JsonNullable
 */
@Mapper(
    componentModel = "spring",
    uses = {JsonNullableMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CharacterAppearanceMapperMS {
    
    /**
     * РџСЂРµРѕР±СЂР°Р·РѕРІР°С‚СЊ Entity РІ DTO
     */
    @Mapping(source = "bodyType", target = "bodyType", qualifiedByName = "bodyTypeToEnum")
    @Mapping(source = "distinctiveFeatures", target = "distinctiveFeatures", qualifiedByName = "stringToJsonNullable")
    GameCharacterAppearance toDto(CharacterAppearanceEntity entity);
    
    /**
     * РџСЂРµРѕР±СЂР°Р·РѕРІР°С‚СЊ DTO РІ Entity
     */
    @Mapping(source = "bodyType", target = "bodyType", qualifiedByName = "enumToBodyType")
    @Mapping(source = "distinctiveFeatures", target = "distinctiveFeatures", qualifiedByName = "jsonNullableToString")
    CharacterAppearanceEntity toEntity(GameCharacterAppearance dto);
    
    // === Custom mapping methods РґР»СЏ enum РєРѕРЅРІРµСЂСЃРёР№ ===
    
    @Named("bodyTypeToEnum")
    default GameCharacterAppearance.BodyTypeEnum bodyTypeToEnum(CharacterAppearanceEntity.BodyType bodyType) {
        return bodyType != null ? GameCharacterAppearance.BodyTypeEnum.fromValue(bodyType.name()) : null;
    }
    
    @Named("enumToBodyType")
    default CharacterAppearanceEntity.BodyType enumToBodyType(GameCharacterAppearance.BodyTypeEnum bodyType) {
        // РСЃРїРѕР»СЊР·СѓРµРј getValue() РІРјРµСЃС‚Рѕ name() С‡С‚РѕР±С‹ РїРѕР»СѓС‡РёС‚СЊ lowercase Р·РЅР°С‡РµРЅРёРµ ("muscular" РІРјРµСЃС‚Рѕ "MUSCULAR")
        return bodyType != null ? CharacterAppearanceEntity.BodyType.valueOf(bodyType.getValue()) : null;
    }
}

