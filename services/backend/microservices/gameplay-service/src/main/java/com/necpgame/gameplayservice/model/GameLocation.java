package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

  private @Nullable String city;

  private @Nullable String district;

  /**
   * Уровень опасности локации: - low: безопасная зона (Downtown, Westbrook) - medium: средняя опасность (Watson, Santo Domingo) - high: опасная зона (Heywood) 
   */
  public enum DangerLevelEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high");

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

  private @Nullable Integer minLevel;

  /**
   * Тип локации: - corporate: корпоративная зона - industrial: индустриальная зона - residential: жилая зона - criminal: криминальная зона 
   */
  public enum TypeEnum {
    CORPORATE("corporate"),
    
    INDUSTRIAL("industrial"),
    
    RESIDENTIAL("residential"),
    
    CRIMINAL("criminal");

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

  private @Nullable TypeEnum type;

  @Valid
  private List<String> connectedLocations = new ArrayList<>();

  public GameLocation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameLocation(String id, String name, String description, DangerLevelEnum dangerLevel) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.dangerLevel = dangerLevel;
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
  @Schema(name = "id", example = "loc-downtown-001", description = "Уникальный идентификатор локации", requiredMode = Schema.RequiredMode.REQUIRED)
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
  @NotNull @Size(min = 1, max = 200) 
  @Schema(name = "name", example = "Downtown - Корпоративный центр", description = "Название локации", requiredMode = Schema.RequiredMode.REQUIRED)
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
   * Детальное описание локации
   * @return description
   */
  @NotNull @Size(min = 10, max = 2000) 
  @Schema(name = "description", example = "Вы стоите в центре корпоративного района Night City. Вокруг вас возвышаются небоскребы мегакорпораций...", description = "Детальное описание локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public GameLocation city(@Nullable String city) {
    this.city = city;
    return this;
  }

  /**
   * Город
   * @return city
   */
  
  @Schema(name = "city", example = "Night City", description = "Город", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("city")
  public @Nullable String getCity() {
    return city;
  }

  public void setCity(@Nullable String city) {
    this.city = city;
  }

  public GameLocation district(@Nullable String district) {
    this.district = district;
    return this;
  }

  /**
   * Район города
   * @return district
   */
  
  @Schema(name = "district", example = "Downtown", description = "Район города", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("district")
  public @Nullable String getDistrict() {
    return district;
  }

  public void setDistrict(@Nullable String district) {
    this.district = district;
  }

  public GameLocation dangerLevel(DangerLevelEnum dangerLevel) {
    this.dangerLevel = dangerLevel;
    return this;
  }

  /**
   * Уровень опасности локации: - low: безопасная зона (Downtown, Westbrook) - medium: средняя опасность (Watson, Santo Domingo) - high: опасная зона (Heywood) 
   * @return dangerLevel
   */
  @NotNull 
  @Schema(name = "dangerLevel", example = "low", description = "Уровень опасности локации: - low: безопасная зона (Downtown, Westbrook) - medium: средняя опасность (Watson, Santo Domingo) - high: опасная зона (Heywood) ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dangerLevel")
  public DangerLevelEnum getDangerLevel() {
    return dangerLevel;
  }

  public void setDangerLevel(DangerLevelEnum dangerLevel) {
    this.dangerLevel = dangerLevel;
  }

  public GameLocation minLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
    return this;
  }

  /**
   * Минимальный уровень персонажа для посещения
   * minimum: 1
   * maximum: 100
   * @return minLevel
   */
  @Min(value = 1) @Max(value = 100) 
  @Schema(name = "minLevel", example = "1", description = "Минимальный уровень персонажа для посещения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minLevel")
  public @Nullable Integer getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
  }

  public GameLocation type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Тип локации: - corporate: корпоративная зона - industrial: индустриальная зона - residential: жилая зона - criminal: криминальная зона 
   * @return type
   */
  
  @Schema(name = "type", example = "corporate", description = "Тип локации: - corporate: корпоративная зона - industrial: индустриальная зона - residential: жилая зона - criminal: криминальная зона ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public GameLocation connectedLocations(List<String> connectedLocations) {
    this.connectedLocations = connectedLocations;
    return this;
  }

  public GameLocation addConnectedLocationsItem(String connectedLocationsItem) {
    if (this.connectedLocations == null) {
      this.connectedLocations = new ArrayList<>();
    }
    this.connectedLocations.add(connectedLocationsItem);
    return this;
  }

  /**
   * Список ID связанных локаций (для перемещения)
   * @return connectedLocations
   */
  
  @Schema(name = "connectedLocations", example = "[\"loc-watson-001\"]", description = "Список ID связанных локаций (для перемещения)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("connectedLocations")
  public List<String> getConnectedLocations() {
    return connectedLocations;
  }

  public void setConnectedLocations(List<String> connectedLocations) {
    this.connectedLocations = connectedLocations;
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
        Objects.equals(this.dangerLevel, gameLocation.dangerLevel) &&
        Objects.equals(this.minLevel, gameLocation.minLevel) &&
        Objects.equals(this.type, gameLocation.type) &&
        Objects.equals(this.connectedLocations, gameLocation.connectedLocations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, city, district, dangerLevel, minLevel, type, connectedLocations);
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
    sb.append("    dangerLevel: ").append(toIndentedString(dangerLevel)).append("\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    connectedLocations: ").append(toIndentedString(connectedLocations)).append("\n");
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

