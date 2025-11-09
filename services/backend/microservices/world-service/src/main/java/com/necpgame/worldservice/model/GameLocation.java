package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GameLocation
 */


public class GameLocation {

  private String id;

  private String name;

  private String description;

  private String city;

  private String district;

  /**
   * Регион
   */
  public enum RegionEnum {
    NIGHT_CITY("night_city"),
    
    BADLANDS("badlands"),
    
    OUTSKIRTS("outskirts");

    private final String value;

    RegionEnum(String value) {
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
    public static RegionEnum fromValue(String value) {
      for (RegionEnum b : RegionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RegionEnum region;

  /**
   * Уровень опасности локации
   */
  public enum DangerLevelEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    EXTREME("extreme");

    private final String value;

    DangerLevelEnum(String value) {
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
    public static DangerLevelEnum fromValue(String value) {
      for (DangerLevelEnum b : DangerLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DangerLevelEnum dangerLevel;

  private Integer minLevel;

  /**
   * Тип локации
   */
  public enum TypeEnum {
    CORPORATE("corporate"),
    
    INDUSTRIAL("industrial"),
    
    RESIDENTIAL("residential"),
    
    CRIMINAL("criminal"),
    
    COMMERCIAL("commercial"),
    
    CULTURAL("cultural");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private @Nullable Boolean accessible;

  public GameLocation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameLocation(String id, String name, String description, String city, String district, RegionEnum region, DangerLevelEnum dangerLevel, Integer minLevel, TypeEnum type) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.city = city;
    this.district = district;
    this.region = region;
    this.dangerLevel = dangerLevel;
    this.minLevel = minLevel;
    this.type = type;
  }

  public GameLocation id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор локации
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "downtown_city_center", description = "Уникальный идентификатор локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GameLocation name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название локации
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "City Center", description = "Название локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GameLocation description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Краткое описание локации
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "Сердце Night City, центр корпоративной власти", description = "Краткое описание локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public GameLocation city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Город
   * @return city
   */
  @NotNull 
  @Schema(name = "city", example = "Night City", description = "Город", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public GameLocation district(String district) {
    this.district = district;
    return this;
  }

  /**
   * Район города
   * @return district
   */
  @NotNull 
  @Schema(name = "district", example = "Downtown", description = "Район города", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("district")
  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public GameLocation region(RegionEnum region) {
    this.region = region;
    return this;
  }

  /**
   * Регион
   * @return region
   */
  @NotNull 
  @Schema(name = "region", example = "night_city", description = "Регион", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("region")
  public RegionEnum getRegion() {
    return region;
  }

  public void setRegion(RegionEnum region) {
    this.region = region;
  }

  public GameLocation dangerLevel(DangerLevelEnum dangerLevel) {
    this.dangerLevel = dangerLevel;
    return this;
  }

  /**
   * Уровень опасности локации
   * @return dangerLevel
   */
  @NotNull 
  @Schema(name = "dangerLevel", example = "low", description = "Уровень опасности локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dangerLevel")
  public DangerLevelEnum getDangerLevel() {
    return dangerLevel;
  }

  public void setDangerLevel(DangerLevelEnum dangerLevel) {
    this.dangerLevel = dangerLevel;
  }

  public GameLocation minLevel(Integer minLevel) {
    this.minLevel = minLevel;
    return this;
  }

  /**
   * Минимальный уровень персонажа для доступа
   * minimum: 1
   * @return minLevel
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "minLevel", example = "1", description = "Минимальный уровень персонажа для доступа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("minLevel")
  public Integer getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(Integer minLevel) {
    this.minLevel = minLevel;
  }

  public GameLocation type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Тип локации
   * @return type
   */
  @NotNull 
  @Schema(name = "type", example = "corporate", description = "Тип локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public GameLocation accessible(@Nullable Boolean accessible) {
    this.accessible = accessible;
    return this;
  }

  /**
   * Доступна ли локация для персонажа
   * @return accessible
   */
  
  @Schema(name = "accessible", example = "true", description = "Доступна ли локация для персонажа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accessible")
  public @Nullable Boolean getAccessible() {
    return accessible;
  }

  public void setAccessible(@Nullable Boolean accessible) {
    this.accessible = accessible;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameLocation gameLocation = (GameLocation) o;
    return Objects.equals(this.id, gameLocation.id) &&
        Objects.equals(this.name, gameLocation.name) &&
        Objects.equals(this.description, gameLocation.description) &&
        Objects.equals(this.city, gameLocation.city) &&
        Objects.equals(this.district, gameLocation.district) &&
        Objects.equals(this.region, gameLocation.region) &&
        Objects.equals(this.dangerLevel, gameLocation.dangerLevel) &&
        Objects.equals(this.minLevel, gameLocation.minLevel) &&
        Objects.equals(this.type, gameLocation.type) &&
        Objects.equals(this.accessible, gameLocation.accessible);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, city, district, region, dangerLevel, minLevel, type, accessible);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameLocation {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    district: ").append(toIndentedString(district)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    dangerLevel: ").append(toIndentedString(dangerLevel)).append("\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    accessible: ").append(toIndentedString(accessible)).append("\n");
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

