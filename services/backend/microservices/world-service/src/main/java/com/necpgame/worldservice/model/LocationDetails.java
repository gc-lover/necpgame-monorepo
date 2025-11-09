package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.LocationAction;
import com.necpgame.worldservice.model.LocationDetailsAllOfEvents;
import com.necpgame.worldservice.model.LocationDetailsAllOfPointsOfInterest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LocationDetails
 */


public class LocationDetails {

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

  private String atmosphere;

  @Valid
  private List<@Valid LocationDetailsAllOfPointsOfInterest> pointsOfInterest = new ArrayList<>();

  @Valid
  private List<@Valid LocationAction> availableActions = new ArrayList<>();

  @Valid
  private List<UUID> availableNPCs = new ArrayList<>();

  @Valid
  private List<String> connectedLocations = new ArrayList<>();

  @Valid
  private List<@Valid LocationDetailsAllOfEvents> events = new ArrayList<>();

  public LocationDetails() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LocationDetails(String id, String name, String description, String city, String district, RegionEnum region, DangerLevelEnum dangerLevel, Integer minLevel, TypeEnum type, String atmosphere, List<@Valid LocationAction> availableActions) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.city = city;
    this.district = district;
    this.region = region;
    this.dangerLevel = dangerLevel;
    this.minLevel = minLevel;
    this.type = type;
    this.atmosphere = atmosphere;
    this.availableActions = availableActions;
  }

  public LocationDetails id(String id) {
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

  public LocationDetails name(String name) {
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

  public LocationDetails description(String description) {
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

  public LocationDetails city(String city) {
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

  public LocationDetails district(String district) {
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

  public LocationDetails region(RegionEnum region) {
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

  public LocationDetails dangerLevel(DangerLevelEnum dangerLevel) {
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

  public LocationDetails minLevel(Integer minLevel) {
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

  public LocationDetails type(TypeEnum type) {
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

  public LocationDetails accessible(@Nullable Boolean accessible) {
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

  public LocationDetails atmosphere(String atmosphere) {
    this.atmosphere = atmosphere;
    return this;
  }

  /**
   * Атмосферное описание локации
   * @return atmosphere
   */
  @NotNull 
  @Schema(name = "atmosphere", example = "Небоскребы упираются в облака, неоновые огни...", description = "Атмосферное описание локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("atmosphere")
  public String getAtmosphere() {
    return atmosphere;
  }

  public void setAtmosphere(String atmosphere) {
    this.atmosphere = atmosphere;
  }

  public LocationDetails pointsOfInterest(List<@Valid LocationDetailsAllOfPointsOfInterest> pointsOfInterest) {
    this.pointsOfInterest = pointsOfInterest;
    return this;
  }

  public LocationDetails addPointsOfInterestItem(LocationDetailsAllOfPointsOfInterest pointsOfInterestItem) {
    if (this.pointsOfInterest == null) {
      this.pointsOfInterest = new ArrayList<>();
    }
    this.pointsOfInterest.add(pointsOfInterestItem);
    return this;
  }

  /**
   * Точки интереса в локации
   * @return pointsOfInterest
   */
  @Valid 
  @Schema(name = "pointsOfInterest", example = "[{\"id\":\"arasaka_tower\",\"name\":\"Башня Arasaka\",\"description\":\"Впечатляющий небоскреб корпорации Arasaka\"}]", description = "Точки интереса в локации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pointsOfInterest")
  public List<@Valid LocationDetailsAllOfPointsOfInterest> getPointsOfInterest() {
    return pointsOfInterest;
  }

  public void setPointsOfInterest(List<@Valid LocationDetailsAllOfPointsOfInterest> pointsOfInterest) {
    this.pointsOfInterest = pointsOfInterest;
  }

  public LocationDetails availableActions(List<@Valid LocationAction> availableActions) {
    this.availableActions = availableActions;
    return this;
  }

  public LocationDetails addAvailableActionsItem(LocationAction availableActionsItem) {
    if (this.availableActions == null) {
      this.availableActions = new ArrayList<>();
    }
    this.availableActions.add(availableActionsItem);
    return this;
  }

  /**
   * Доступные действия в локации
   * @return availableActions
   */
  @NotNull @Valid 
  @Schema(name = "availableActions", description = "Доступные действия в локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("availableActions")
  public List<@Valid LocationAction> getAvailableActions() {
    return availableActions;
  }

  public void setAvailableActions(List<@Valid LocationAction> availableActions) {
    this.availableActions = availableActions;
  }

  public LocationDetails availableNPCs(List<UUID> availableNPCs) {
    this.availableNPCs = availableNPCs;
    return this;
  }

  public LocationDetails addAvailableNPCsItem(UUID availableNPCsItem) {
    if (this.availableNPCs == null) {
      this.availableNPCs = new ArrayList<>();
    }
    this.availableNPCs.add(availableNPCsItem);
    return this;
  }

  /**
   * Доступные NPC в локации
   * @return availableNPCs
   */
  @Valid 
  @Schema(name = "availableNPCs", example = "[\"npc_id_1\",\"npc_id_2\"]", description = "Доступные NPC в локации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availableNPCs")
  public List<UUID> getAvailableNPCs() {
    return availableNPCs;
  }

  public void setAvailableNPCs(List<UUID> availableNPCs) {
    this.availableNPCs = availableNPCs;
  }

  public LocationDetails connectedLocations(List<String> connectedLocations) {
    this.connectedLocations = connectedLocations;
    return this;
  }

  public LocationDetails addConnectedLocationsItem(String connectedLocationsItem) {
    if (this.connectedLocations == null) {
      this.connectedLocations = new ArrayList<>();
    }
    this.connectedLocations.add(connectedLocationsItem);
    return this;
  }

  /**
   * Связанные локации (ID)
   * @return connectedLocations
   */
  
  @Schema(name = "connectedLocations", example = "[\"watson_kabuki\",\"westbrook_japantown\"]", description = "Связанные локации (ID)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("connectedLocations")
  public List<String> getConnectedLocations() {
    return connectedLocations;
  }

  public void setConnectedLocations(List<String> connectedLocations) {
    this.connectedLocations = connectedLocations;
  }

  public LocationDetails events(List<@Valid LocationDetailsAllOfEvents> events) {
    this.events = events;
    return this;
  }

  public LocationDetails addEventsItem(LocationDetailsAllOfEvents eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * Текущие события в локации (опционально)
   * @return events
   */
  @Valid 
  @Schema(name = "events", description = "Текущие события в локации (опционально)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("events")
  public List<@Valid LocationDetailsAllOfEvents> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid LocationDetailsAllOfEvents> events) {
    this.events = events;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationDetails locationDetails = (LocationDetails) o;
    return Objects.equals(this.id, locationDetails.id) &&
        Objects.equals(this.name, locationDetails.name) &&
        Objects.equals(this.description, locationDetails.description) &&
        Objects.equals(this.city, locationDetails.city) &&
        Objects.equals(this.district, locationDetails.district) &&
        Objects.equals(this.region, locationDetails.region) &&
        Objects.equals(this.dangerLevel, locationDetails.dangerLevel) &&
        Objects.equals(this.minLevel, locationDetails.minLevel) &&
        Objects.equals(this.type, locationDetails.type) &&
        Objects.equals(this.accessible, locationDetails.accessible) &&
        Objects.equals(this.atmosphere, locationDetails.atmosphere) &&
        Objects.equals(this.pointsOfInterest, locationDetails.pointsOfInterest) &&
        Objects.equals(this.availableActions, locationDetails.availableActions) &&
        Objects.equals(this.availableNPCs, locationDetails.availableNPCs) &&
        Objects.equals(this.connectedLocations, locationDetails.connectedLocations) &&
        Objects.equals(this.events, locationDetails.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, city, district, region, dangerLevel, minLevel, type, accessible, atmosphere, pointsOfInterest, availableActions, availableNPCs, connectedLocations, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationDetails {\n");
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
    sb.append("    atmosphere: ").append(toIndentedString(atmosphere)).append("\n");
    sb.append("    pointsOfInterest: ").append(toIndentedString(pointsOfInterest)).append("\n");
    sb.append("    availableActions: ").append(toIndentedString(availableActions)).append("\n");
    sb.append("    availableNPCs: ").append(toIndentedString(availableNPCs)).append("\n");
    sb.append("    connectedLocations: ").append(toIndentedString(connectedLocations)).append("\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
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

