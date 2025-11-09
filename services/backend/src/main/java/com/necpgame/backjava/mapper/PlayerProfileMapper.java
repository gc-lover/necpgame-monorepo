package com.necpgame.backjava.mapper;

import com.necpgame.backjava.entity.PlayerEntity;
import com.necpgame.backjava.model.PlayerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct-мэппер профиля игрока.
 */
@Mapper(componentModel = "spring")
public interface PlayerProfileMapper {

    @Mapping(target = "playerId", source = "id")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "premiumCurrency", source = "premiumCurrency", qualifiedByName = "toInteger")
    @Mapping(target = "settings", source = "settings")
    @Mapping(target = "createdAt", source = "createdAt")
    PlayerProfile toProfile(PlayerEntity player);

    @Named("toInteger")
    default Integer toInteger(Long value) {
        return value == null ? null : Math.toIntExact(value);
    }
}

