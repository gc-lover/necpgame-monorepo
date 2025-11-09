package com.necpgame.backjava.service.player;

import com.necpgame.backjava.entity.CharacterAppearanceEntity;
import com.necpgame.backjava.entity.CharacterEntity;
import com.necpgame.backjava.entity.CityEntity;
import com.necpgame.backjava.entity.PlayerEntity;
import com.necpgame.backjava.exception.BusinessException;
import com.necpgame.backjava.exception.ErrorCode;
import com.necpgame.backjava.mapper.CharacterAppearanceMapperMS;
import com.necpgame.backjava.model.CreatePlayerCharacterRequestAppearance;
import com.necpgame.backjava.model.GameCharacterAppearance;
import com.necpgame.backjava.repository.CityRepository;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerAppearanceService {

    private static final int DEFAULT_APPEARANCE_HEIGHT = 180;
    private static final GameCharacterAppearance.BodyTypeEnum DEFAULT_BODY_TYPE = GameCharacterAppearance.BodyTypeEnum.NORMAL;
    private static final CharacterEntity.Gender DEFAULT_GENDER = CharacterEntity.Gender.other;
    private static final String DEFAULT_ORIGIN_CODE = "origin_default";
    private static final String DEFAULT_HAIR_COLOR = "black";
    private static final String DEFAULT_EYE_COLOR = "brown";
    private static final String DEFAULT_SKIN_COLOR = "light";

    private final CityRepository cityRepository;
    private final CharacterAppearanceMapperMS characterAppearanceMapper;

    public CityEntity resolveDefaultCity(PlayerEntity player) {
        UUID configuredCity = extractUuidSetting(player.getSettings(), "default_city_id");
        if (configuredCity != null) {
            return cityRepository.findById(configuredCity)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Город не найден"));
        }
        return cityRepository.findAll(PageRequest.of(0, 1)).stream()
            .findFirst()
            .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_ERROR, "Стартовый город не настроен"));
    }

    public CharacterAppearanceEntity createAppearance(CreatePlayerCharacterRequestAppearance appearance,
                                                      Map<String, Object> settings) {
        GameCharacterAppearance dto = new GameCharacterAppearance(
            extractIntegerSetting(settings, "default_height", DEFAULT_APPEARANCE_HEIGHT),
            resolveBodyType(appearance, settings),
            firstNonBlank(
                appearance != null ? appearance.getHairColor() : null,
                extractStringSetting(settings, "default_hair_color"),
                DEFAULT_HAIR_COLOR
            ),
            firstNonBlank(
                extractStringSetting(settings, "default_eye_color"),
                DEFAULT_EYE_COLOR
            ),
            firstNonBlank(
                appearance != null ? appearance.getSkinTone() : null,
                extractStringSetting(settings, "default_skin_color"),
                DEFAULT_SKIN_COLOR
            )
        );
        return characterAppearanceMapper.toEntity(dto);
    }

    public CharacterEntity.Gender resolveGender(Map<String, Object> settings) {
        String value = extractStringSetting(settings, "default_gender");
        if (value != null && !value.isBlank()) {
            try {
                return CharacterEntity.Gender.valueOf(value.toLowerCase());
            } catch (IllegalArgumentException ignored) {
                return DEFAULT_GENDER;
            }
        }
        return DEFAULT_GENDER;
    }

    public String resolveOrigin(Map<String, Object> settings, String originId) {
        if (originId != null && !originId.isBlank()) {
            return originId;
        }
        String value = extractStringSetting(settings, "default_origin");
        if (value != null && !value.isBlank()) {
            return value;
        }
        return DEFAULT_ORIGIN_CODE;
    }

    private GameCharacterAppearance.BodyTypeEnum resolveBodyType(CreatePlayerCharacterRequestAppearance appearance,
                                                                 Map<String, Object> settings) {
        String bodyTypeValue = appearance != null ? appearance.getBodyType() : extractStringSetting(settings, "default_body_type");
        if (bodyTypeValue != null && !bodyTypeValue.isBlank()) {
            try {
                return GameCharacterAppearance.BodyTypeEnum.fromValue(bodyTypeValue.toLowerCase());
            } catch (IllegalArgumentException ignored) {
                return DEFAULT_BODY_TYPE;
            }
        }
        return DEFAULT_BODY_TYPE;
    }

    private String extractStringSetting(Map<String, Object> settings, String key) {
        Object value = settings != null ? settings.get(key) : null;
        if (value instanceof String str && !str.isBlank()) {
            return str;
        }
        return null;
    }

    private Integer extractIntegerSetting(Map<String, Object> settings, String key, int defaultValue) {
        Object value = settings != null ? settings.get(key) : null;
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String str) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private UUID extractUuidSetting(Map<String, Object> settings, String key) {
        Object value = settings != null ? settings.get(key) : null;
        if (value instanceof UUID uuid) {
            return uuid;
        }
        if (value instanceof String str && !str.isBlank()) {
            try {
                return UUID.fromString(str);
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }
        return null;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}

