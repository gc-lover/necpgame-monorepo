package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.GameCharacterAppearance;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GameCharacter
 */


public class GameCharacter {

  private UUID id;

  private UUID accountId;

  private String name;

  /**
   * РљР»Р°СЃСЃ РїРµСЂСЃРѕРЅР°Р¶Р°
   */
  public enum PropertyClassEnum {
    SOLO("Solo"),
    
    NETRUNNER("Netrunner"),
    
    FIXER("Fixer"),
    
    ROCKERBOY("Rockerboy"),
    
    MEDIA("Media"),
    
    NOMAD("Nomad"),
    
    CORPO("Corpo"),
    
    LAWMAN("Lawman"),
    
    MEDTECH("Medtech"),
    
    TECHIE("Techie"),
    
    POLITICIAN("Politician"),
    
    TRADER("Trader"),
    
    TEACHER("Teacher");

    private final String value;

    PropertyClassEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PropertyClassEnum fromValue(String value) {
      for (PropertyClassEnum b : PropertyClassEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PropertyClassEnum propertyClass;

  private JsonNullable<String> subclass = JsonNullable.<String>undefined();

  /**
   * РџРѕР» РїРµСЂСЃРѕРЅР°Р¶Р°
   */
  public enum GenderEnum {
    MALE("male"),
    
    FEMALE("female"),
    
    OTHER("other");

    private final String value;

    GenderEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static GenderEnum fromValue(String value) {
      for (GenderEnum b : GenderEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private GenderEnum gender;

  /**
   * РџСЂРѕРёСЃС…РѕР¶РґРµРЅРёРµ РїРµСЂСЃРѕРЅР°Р¶Р°
   */
  public enum OriginEnum {
    STREET_KID("street_kid"),
    
    CORPO("corpo"),
    
    NOMAD("nomad");

    private final String value;

    OriginEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OriginEnum fromValue(String value) {
      for (OriginEnum b : OriginEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private OriginEnum origin;

  private JsonNullable<UUID> factionId = JsonNullable.<UUID>undefined();

  private JsonNullable<String> factionName = JsonNullable.<String>undefined();

  private UUID cityId;

  private String cityName;

  private GameCharacterAppearance appearance;

  private Integer level = 1;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> lastLogin = JsonNullable.<OffsetDateTime>undefined();

  public GameCharacter() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameCharacter(UUID id, UUID accountId, String name, PropertyClassEnum propertyClass, GenderEnum gender, OriginEnum origin, UUID cityId, String cityName, GameCharacterAppearance appearance, Integer level, OffsetDateTime createdAt) {
    this.id = id;
    this.accountId = accountId;
    this.name = name;
    this.propertyClass = propertyClass;
    this.gender = gender;
    this.origin = origin;
    this.cityId = cityId;
    this.cityName = cityName;
    this.appearance = appearance;
    this.level = level;
    this.createdAt = createdAt;
  }

  public GameCharacter id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * РЈРЅРёРєР°Р»СЊРЅС‹Р№ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РїРµСЂСЃРѕРЅР°Р¶Р°
   * @return id
   */
  @NotNull @Valid 
  @Schema(name = "id", example = "550e8400-e29b-41d4-a716-446655440000", description = "РЈРЅРёРєР°Р»СЊРЅС‹Р№ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РїРµСЂСЃРѕРЅР°Р¶Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public GameCharacter accountId(UUID accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ Р°РєРєР°СѓРЅС‚Р° РІР»Р°РґРµР»СЊС†Р°
   * @return accountId
   */
  @NotNull @Valid 
  @Schema(name = "account_id", example = "550e8400-e29b-41d4-a716-446655440001", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ Р°РєРєР°СѓРЅС‚Р° РІР»Р°РґРµР»СЊС†Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("account_id")
  public UUID getAccountId() {
    return accountId;
  }

  public void setAccountId(UUID accountId) {
    this.accountId = accountId;
  }

  public GameCharacter name(String name) {
    this.name = name;
    return this;
  }

  /**
   * РРјСЏ РїРµСЂСЃРѕРЅР°Р¶Р°
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "John Doe", description = "РРјСЏ РїРµСЂСЃРѕРЅР°Р¶Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GameCharacter propertyClass(PropertyClassEnum propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

  /**
   * РљР»Р°СЃСЃ РїРµСЂСЃРѕРЅР°Р¶Р°
   * @return propertyClass
   */
  @NotNull 
  @Schema(name = "class", example = "Solo", description = "РљР»Р°СЃСЃ РїРµСЂСЃРѕРЅР°Р¶Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("class")
  public PropertyClassEnum getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(PropertyClassEnum propertyClass) {
    this.propertyClass = propertyClass;
  }

  public GameCharacter subclass(String subclass) {
    this.subclass = JsonNullable.of(subclass);
    return this;
  }

  /**
   * РџРѕРґРєР»Р°СЃСЃ РїРµСЂСЃРѕРЅР°Р¶Р°
   * @return subclass
   */
  
  @Schema(name = "subclass", description = "РџРѕРґРєР»Р°СЃСЃ РїРµСЂСЃРѕРЅР°Р¶Р°", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subclass")
  public JsonNullable<String> getSubclass() {
    return subclass;
  }

  public void setSubclass(JsonNullable<String> subclass) {
    this.subclass = subclass;
  }

  public GameCharacter gender(GenderEnum gender) {
    this.gender = gender;
    return this;
  }

  /**
   * РџРѕР» РїРµСЂСЃРѕРЅР°Р¶Р°
   * @return gender
   */
  @NotNull 
  @Schema(name = "gender", example = "male", description = "РџРѕР» РїРµСЂСЃРѕРЅР°Р¶Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("gender")
  public GenderEnum getGender() {
    return gender;
  }

  public void setGender(GenderEnum gender) {
    this.gender = gender;
  }

  public GameCharacter origin(OriginEnum origin) {
    this.origin = origin;
    return this;
  }

  /**
   * РџСЂРѕРёСЃС…РѕР¶РґРµРЅРёРµ РїРµСЂСЃРѕРЅР°Р¶Р°
   * @return origin
   */
  @NotNull 
  @Schema(name = "origin", example = "street_kid", description = "РџСЂРѕРёСЃС…РѕР¶РґРµРЅРёРµ РїРµСЂСЃРѕРЅР°Р¶Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("origin")
  public OriginEnum getOrigin() {
    return origin;
  }

  public void setOrigin(OriginEnum origin) {
    this.origin = origin;
  }

  public GameCharacter factionId(UUID factionId) {
    this.factionId = JsonNullable.of(factionId);
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ С„СЂР°РєС†РёРё
   * @return factionId
   */
  @Valid 
  @Schema(name = "faction_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ С„СЂР°РєС†РёРё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_id")
  public JsonNullable<UUID> getFactionId() {
    return factionId;
  }

  public void setFactionId(JsonNullable<UUID> factionId) {
    this.factionId = factionId;
  }

  public GameCharacter factionName(String factionName) {
    this.factionName = JsonNullable.of(factionName);
    return this;
  }

  /**
   * РќР°Р·РІР°РЅРёРµ С„СЂР°РєС†РёРё
   * @return factionName
   */
  
  @Schema(name = "faction_name", description = "РќР°Р·РІР°РЅРёРµ С„СЂР°РєС†РёРё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_name")
  public JsonNullable<String> getFactionName() {
    return factionName;
  }

  public void setFactionName(JsonNullable<String> factionName) {
    this.factionName = factionName;
  }

  public GameCharacter cityId(UUID cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ СЃС‚Р°СЂС‚РѕРІРѕРіРѕ РіРѕСЂРѕРґР°
   * @return cityId
   */
  @NotNull @Valid 
  @Schema(name = "city_id", example = "550e8400-e29b-41d4-a716-446655440000", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ СЃС‚Р°СЂС‚РѕРІРѕРіРѕ РіРѕСЂРѕРґР°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("city_id")
  public UUID getCityId() {
    return cityId;
  }

  public void setCityId(UUID cityId) {
    this.cityId = cityId;
  }

  public GameCharacter cityName(String cityName) {
    this.cityName = cityName;
    return this;
  }

  /**
   * РќР°Р·РІР°РЅРёРµ СЃС‚Р°СЂС‚РѕРІРѕРіРѕ РіРѕСЂРѕРґР°
   * @return cityName
   */
  @NotNull 
  @Schema(name = "city_name", example = "Night City", description = "РќР°Р·РІР°РЅРёРµ СЃС‚Р°СЂС‚РѕРІРѕРіРѕ РіРѕСЂРѕРґР°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("city_name")
  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public GameCharacter appearance(GameCharacterAppearance appearance) {
    this.appearance = appearance;
    return this;
  }

  /**
   * Get appearance
   * @return appearance
   */
  @NotNull @Valid 
  @Schema(name = "appearance", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("appearance")
  public GameCharacterAppearance getAppearance() {
    return appearance;
  }

  public void setAppearance(GameCharacterAppearance appearance) {
    this.appearance = appearance;
  }

  public GameCharacter level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * РЈСЂРѕРІРµРЅСЊ РїРµСЂСЃРѕРЅР°Р¶Р°
   * minimum: 1
   * @return level
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "level", example = "1", description = "РЈСЂРѕРІРµРЅСЊ РїРµСЂСЃРѕРЅР°Р¶Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public GameCharacter createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Р”Р°С‚Р° СЃРѕР·РґР°РЅРёСЏ РїРµСЂСЃРѕРЅР°Р¶Р°
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "created_at", example = "2025-01-27T12:00Z", description = "Р”Р°С‚Р° СЃРѕР·РґР°РЅРёСЏ РїРµСЂСЃРѕРЅР°Р¶Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("created_at")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public GameCharacter lastLogin(OffsetDateTime lastLogin) {
    this.lastLogin = JsonNullable.of(lastLogin);
    return this;
  }

  /**
   * Р”Р°С‚Р° РїРѕСЃР»РµРґРЅРµРіРѕ РІС…РѕРґР° РІ РёРіСЂСѓ
   * @return lastLogin
   */
  @Valid 
  @Schema(name = "last_login", example = "2025-01-27T12:00Z", description = "Р”Р°С‚Р° РїРѕСЃР»РµРґРЅРµРіРѕ РІС…РѕРґР° РІ РёРіСЂСѓ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_login")
  public JsonNullable<OffsetDateTime> getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(JsonNullable<OffsetDateTime> lastLogin) {
    this.lastLogin = lastLogin;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameCharacter gameCharacter = (GameCharacter) o;
    return Objects.equals(this.id, gameCharacter.id) &&
        Objects.equals(this.accountId, gameCharacter.accountId) &&
        Objects.equals(this.name, gameCharacter.name) &&
        Objects.equals(this.propertyClass, gameCharacter.propertyClass) &&
        equalsNullable(this.subclass, gameCharacter.subclass) &&
        Objects.equals(this.gender, gameCharacter.gender) &&
        Objects.equals(this.origin, gameCharacter.origin) &&
        equalsNullable(this.factionId, gameCharacter.factionId) &&
        equalsNullable(this.factionName, gameCharacter.factionName) &&
        Objects.equals(this.cityId, gameCharacter.cityId) &&
        Objects.equals(this.cityName, gameCharacter.cityName) &&
        Objects.equals(this.appearance, gameCharacter.appearance) &&
        Objects.equals(this.level, gameCharacter.level) &&
        Objects.equals(this.createdAt, gameCharacter.createdAt) &&
        equalsNullable(this.lastLogin, gameCharacter.lastLogin);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, accountId, name, propertyClass, hashCodeNullable(subclass), gender, origin, hashCodeNullable(factionId), hashCodeNullable(factionName), cityId, cityName, appearance, level, createdAt, hashCodeNullable(lastLogin));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameCharacter {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    subclass: ").append(toIndentedString(subclass)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    factionName: ").append(toIndentedString(factionName)).append("\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    cityName: ").append(toIndentedString(cityName)).append("\n");
    sb.append("    appearance: ").append(toIndentedString(appearance)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    lastLogin: ").append(toIndentedString(lastLogin)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

