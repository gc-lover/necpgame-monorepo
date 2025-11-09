package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.GameCharacterAppearance;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
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
 * CreateCharacterRequest
 */


public class CreateCharacterRequest {

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

  private UUID cityId;

  private GameCharacterAppearance appearance;

  public CreateCharacterRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateCharacterRequest(String name, PropertyClassEnum propertyClass, GenderEnum gender, OriginEnum origin, UUID cityId, GameCharacterAppearance appearance) {
    this.name = name;
    this.propertyClass = propertyClass;
    this.gender = gender;
    this.origin = origin;
    this.cityId = cityId;
    this.appearance = appearance;
  }

  public CreateCharacterRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * РРјСЏ РїРµСЂСЃРѕРЅР°Р¶Р° (3-20 СЃРёРјРІРѕР»РѕРІ, Р±СѓРєРІС‹, С†РёС„СЂС‹, РїСЂРѕР±РµР»С‹, РґРµС„РёСЃС‹)
   * @return name
   */
  @NotNull @Pattern(regexp = "^[a-zA-ZР°-СЏРђ-РЇ0-9\\\\s\\\\-]+$") @Size(min = 3, max = 20) 
  @Schema(name = "name", example = "John Doe", description = "РРјСЏ РїРµСЂСЃРѕРЅР°Р¶Р° (3-20 СЃРёРјРІРѕР»РѕРІ, Р±СѓРєРІС‹, С†РёС„СЂС‹, РїСЂРѕР±РµР»С‹, РґРµС„РёСЃС‹)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CreateCharacterRequest propertyClass(PropertyClassEnum propertyClass) {
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

  public CreateCharacterRequest subclass(String subclass) {
    this.subclass = JsonNullable.of(subclass);
    return this;
  }

  /**
   * РџРѕРґРєР»Р°СЃСЃ РїРµСЂСЃРѕРЅР°Р¶Р° (РґРѕР»Р¶РµРЅ СЃРѕРѕС‚РІРµС‚СЃС‚РІРѕРІР°С‚СЊ РІС‹Р±СЂР°РЅРЅРѕРјСѓ РєР»Р°СЃСЃСѓ)
   * @return subclass
   */
  
  @Schema(name = "subclass", description = "РџРѕРґРєР»Р°СЃСЃ РїРµСЂСЃРѕРЅР°Р¶Р° (РґРѕР»Р¶РµРЅ СЃРѕРѕС‚РІРµС‚СЃС‚РІРѕРІР°С‚СЊ РІС‹Р±СЂР°РЅРЅРѕРјСѓ РєР»Р°СЃСЃСѓ)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subclass")
  public JsonNullable<String> getSubclass() {
    return subclass;
  }

  public void setSubclass(JsonNullable<String> subclass) {
    this.subclass = subclass;
  }

  public CreateCharacterRequest gender(GenderEnum gender) {
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

  public CreateCharacterRequest origin(OriginEnum origin) {
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

  public CreateCharacterRequest factionId(UUID factionId) {
    this.factionId = JsonNullable.of(factionId);
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ С„СЂР°РєС†РёРё (РґРѕР»Р¶РЅР° Р±С‹С‚СЊ РґРѕСЃС‚СѓРїРЅР° РґР»СЏ РІС‹Р±СЂР°РЅРЅРѕРіРѕ РїСЂРѕРёСЃС…РѕР¶РґРµРЅРёСЏ)
   * @return factionId
   */
  @Valid 
  @Schema(name = "faction_id", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ С„СЂР°РєС†РёРё (РґРѕР»Р¶РЅР° Р±С‹С‚СЊ РґРѕСЃС‚СѓРїРЅР° РґР»СЏ РІС‹Р±СЂР°РЅРЅРѕРіРѕ РїСЂРѕРёСЃС…РѕР¶РґРµРЅРёСЏ)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_id")
  public JsonNullable<UUID> getFactionId() {
    return factionId;
  }

  public void setFactionId(JsonNullable<UUID> factionId) {
    this.factionId = factionId;
  }

  public CreateCharacterRequest cityId(UUID cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ СЃС‚Р°СЂС‚РѕРІРѕРіРѕ РіРѕСЂРѕРґР° (РґРѕР»Р¶РµРЅ Р±С‹С‚СЊ РґРѕСЃС‚СѓРїРµРЅ РґР»СЏ РІС‹Р±СЂР°РЅРЅРѕР№ С„СЂР°РєС†РёРё)
   * @return cityId
   */
  @NotNull @Valid 
  @Schema(name = "city_id", example = "550e8400-e29b-41d4-a716-446655440000", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ СЃС‚Р°СЂС‚РѕРІРѕРіРѕ РіРѕСЂРѕРґР° (РґРѕР»Р¶РµРЅ Р±С‹С‚СЊ РґРѕСЃС‚СѓРїРµРЅ РґР»СЏ РІС‹Р±СЂР°РЅРЅРѕР№ С„СЂР°РєС†РёРё)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("city_id")
  public UUID getCityId() {
    return cityId;
  }

  public void setCityId(UUID cityId) {
    this.cityId = cityId;
  }

  public CreateCharacterRequest appearance(GameCharacterAppearance appearance) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateCharacterRequest createCharacterRequest = (CreateCharacterRequest) o;
    return Objects.equals(this.name, createCharacterRequest.name) &&
        Objects.equals(this.propertyClass, createCharacterRequest.propertyClass) &&
        equalsNullable(this.subclass, createCharacterRequest.subclass) &&
        Objects.equals(this.gender, createCharacterRequest.gender) &&
        Objects.equals(this.origin, createCharacterRequest.origin) &&
        equalsNullable(this.factionId, createCharacterRequest.factionId) &&
        Objects.equals(this.cityId, createCharacterRequest.cityId) &&
        Objects.equals(this.appearance, createCharacterRequest.appearance);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, propertyClass, hashCodeNullable(subclass), gender, origin, hashCodeNullable(factionId), cityId, appearance);
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
    sb.append("class CreateCharacterRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    subclass: ").append(toIndentedString(subclass)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    appearance: ").append(toIndentedString(appearance)).append("\n");
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

