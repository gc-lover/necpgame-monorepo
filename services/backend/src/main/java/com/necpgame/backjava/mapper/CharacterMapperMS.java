package com.necpgame.backjava.mapper;

import com.necpgame.backjava.entity.CharacterEntity;
import com.necpgame.backjava.model.GameCharacter;
import com.necpgame.backjava.model.GameCharacterSummary;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * MapStruct РјР°РїРїРµСЂ РґР»СЏ РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ CharacterEntity <-> GameCharacter DTO
 * РђРІС‚РѕРјР°С‚РёС‡РµСЃРєРё РіРµРЅРµСЂРёСЂСѓРµС‚ РєРѕРґ РјР°РїРїРёРЅРіР° СЃ РїРѕРґРґРµСЂР¶РєРѕР№ JsonNullable
 */
@Mapper(
    componentModel = "spring",
    uses = {CharacterAppearanceMapperMS.class, JsonNullableMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CharacterMapperMS {
    
    /**
     * РџСЂРµРѕР±СЂР°Р·РѕРІР°С‚СЊ Entity РІ DTO
     */
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "classCode", target = "propertyClass", qualifiedByName = "stringToClassEnum")
    @Mapping(source = "subclassCode", target = "subclass", qualifiedByName = "stringToJsonNullable")
    @Mapping(source = "gender", target = "gender", qualifiedByName = "genderToEnum")
    @Mapping(source = "originCode", target = "origin", qualifiedByName = "stringToOriginEnum")
    @Mapping(source = "faction.id", target = "factionId", qualifiedByName = "uuidToJsonNullable")
    @Mapping(source = "faction.name", target = "factionName", qualifiedByName = "stringToJsonNullable")
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "lastLogin", target = "lastLogin", qualifiedByName = "dateToJsonNullable")
    GameCharacter toDto(CharacterEntity entity);
    
    /**
     * РџСЂРµРѕР±СЂР°Р·РѕРІР°С‚СЊ Entity РІ РєСЂР°С‚РєРёР№ DTO (РґР»СЏ СЃРїРёСЃРєР° РїРµСЂСЃРѕРЅР°Р¶РµР№)
     */
    @Mapping(source = "classCode", target = "propertyClass")
    @Mapping(source = "faction.name", target = "factionName", qualifiedByName = "stringToJsonNullable")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "lastLogin", target = "lastLogin", qualifiedByName = "dateToJsonNullable")
    GameCharacterSummary toSummaryDto(CharacterEntity entity);
    
    // === Custom mapping methods РґР»СЏ enum РєРѕРЅРІРµСЂСЃРёР№ ===
    
    @Named("stringToClassEnum")
    default GameCharacter.PropertyClassEnum stringToClassEnum(String classCode) {
        return classCode != null ? GameCharacter.PropertyClassEnum.fromValue(classCode) : null;
    }
    
    @Named("genderToEnum")
    default GameCharacter.GenderEnum genderToEnum(CharacterEntity.Gender gender) {
        return gender != null ? GameCharacter.GenderEnum.fromValue(gender.name()) : null;
    }
    
    @Named("stringToOriginEnum")
    default GameCharacter.OriginEnum stringToOriginEnum(String originCode) {
        return originCode != null ? GameCharacter.OriginEnum.fromValue(originCode) : null;
    }
}

