package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * GenerateEventForLocationRequest
 */

@JsonTypeName("generateEventForLocation_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GenerateEventForLocationRequest {

  private UUID characterId;

  private String locationId;

  private @Nullable String locationType;

  /**
   * Gets or Sets timeOfDay
   */
  public enum TimeOfDayEnum {
    MORNING("MORNING"),
    
    DAY("DAY"),
    
    EVENING("EVENING"),
    
    NIGHT("NIGHT");

    private final String value;

    TimeOfDayEnum(String value) {
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
    public static TimeOfDayEnum fromValue(String value) {
      for (TimeOfDayEnum b : TimeOfDayEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TimeOfDayEnum timeOfDay;

  public GenerateEventForLocationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GenerateEventForLocationRequest(UUID characterId, String locationId) {
    this.characterId = characterId;
    this.locationId = locationId;
  }

  public GenerateEventForLocationRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public GenerateEventForLocationRequest locationId(String locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
   */
  @NotNull 
  @Schema(name = "location_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("location_id")
  public String getLocationId() {
    return locationId;
  }

  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }

  public GenerateEventForLocationRequest locationType(@Nullable String locationType) {
    this.locationType = locationType;
    return this;
  }

  /**
   * Get locationType
   * @return locationType
   */
  
  @Schema(name = "location_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location_type")
  public @Nullable String getLocationType() {
    return locationType;
  }

  public void setLocationType(@Nullable String locationType) {
    this.locationType = locationType;
  }

  public GenerateEventForLocationRequest timeOfDay(@Nullable TimeOfDayEnum timeOfDay) {
    this.timeOfDay = timeOfDay;
    return this;
  }

  /**
   * Get timeOfDay
   * @return timeOfDay
   */
  
  @Schema(name = "time_of_day", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_of_day")
  public @Nullable TimeOfDayEnum getTimeOfDay() {
    return timeOfDay;
  }

  public void setTimeOfDay(@Nullable TimeOfDayEnum timeOfDay) {
    this.timeOfDay = timeOfDay;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateEventForLocationRequest generateEventForLocationRequest = (GenerateEventForLocationRequest) o;
    return Objects.equals(this.characterId, generateEventForLocationRequest.characterId) &&
        Objects.equals(this.locationId, generateEventForLocationRequest.locationId) &&
        Objects.equals(this.locationType, generateEventForLocationRequest.locationType) &&
        Objects.equals(this.timeOfDay, generateEventForLocationRequest.timeOfDay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, locationId, locationType, timeOfDay);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateEventForLocationRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    locationType: ").append(toIndentedString(locationType)).append("\n");
    sb.append("    timeOfDay: ").append(toIndentedString(timeOfDay)).append("\n");
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

