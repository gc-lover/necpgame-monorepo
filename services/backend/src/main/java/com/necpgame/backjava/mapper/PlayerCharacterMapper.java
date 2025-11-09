package com.necpgame.backjava.mapper;

import com.necpgame.backjava.entity.CharacterEntity;
import com.necpgame.backjava.entity.CharacterStatusEntity;
import com.necpgame.backjava.entity.PlayerEntity;
import com.necpgame.backjava.model.PlayerCharacter;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct-мэппер для сводной информации о персонажах игрока.
 */
@Mapper(componentModel = "spring")
public interface PlayerCharacterMapper {

    @Mapping(target = "characterId", source = "character.id")
    @Mapping(target = "playerId", source = "player.id")
    @Mapping(target = "name", source = "character.name")
    @Mapping(target = "classId", source = "character.classCode")
    @Mapping(target = "level", source = "status.level")
    @Mapping(target = "experience", source = "status.experience", qualifiedByName = "toBigDecimal")
    @Mapping(target = "createdAt", source = "character.createdAt")
    @Mapping(target = "lastLogin", source = "character.lastLogin")
    @Mapping(target = "isDeleted", source = "character.deleted")
    PlayerCharacter toSummary(CharacterEntity character, CharacterStatusEntity status, PlayerEntity player);

    @Named("toBigDecimal")
    default BigDecimal toBigDecimal(Integer value) {
        return value == null ? null : BigDecimal.valueOf(value.longValue());
    }
}
