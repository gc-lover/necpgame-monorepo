package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * TravelRequest
 */


public class TravelRequest {

  private UUID characterId;

  private String targetLocationId;

  /**
   * Метод перемещения: - walk: пешком (расходует энергию и время) - fast_travel: быстрое перемещение (мгновенное, требует открытой локации) - vehicle: на транспорте (быстрее пешком, расходует топливо) 
   */
  public enum TravelMethodEnum {
    WALK("walk"),
    
    FAST_TRAVEL("fast_travel"),
    
    VEHICLE("vehicle");

    private final String value;

    TravelMethodEnum(String value) {
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
    public static TravelMethodEnum fromValue(String value) {
      for (TravelMethodEnum b : TravelMethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TravelMethodEnum travelMethod = TravelMethodEnum.WALK;

  public TravelRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TravelRequest(UUID characterId, String targetLocationId) {
    this.characterId = characterId;
    this.targetLocationId = targetLocationId;
  }

  public TravelRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * ID персонажа
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", example = "550e8400-e29b-41d4-a716-446655440000", description = "ID персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public TravelRequest targetLocationId(String targetLocationId) {
    this.targetLocationId = targetLocationId;
    return this;
  }

  /**
   * ID целевой локации
   * @return targetLocationId
   */
  @NotNull 
  @Schema(name = "targetLocationId", example = "watson_kabuki", description = "ID целевой локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetLocationId")
  public String getTargetLocationId() {
    return targetLocationId;
  }

  public void setTargetLocationId(String targetLocationId) {
    this.targetLocationId = targetLocationId;
  }

  public TravelRequest travelMethod(TravelMethodEnum travelMethod) {
    this.travelMethod = travelMethod;
    return this;
  }

  /**
   * Метод перемещения: - walk: пешком (расходует энергию и время) - fast_travel: быстрое перемещение (мгновенное, требует открытой локации) - vehicle: на транспорте (быстрее пешком, расходует топливо) 
   * @return travelMethod
   */
  
  @Schema(name = "travelMethod", example = "walk", description = "Метод перемещения: - walk: пешком (расходует энергию и время) - fast_travel: быстрое перемещение (мгновенное, требует открытой локации) - vehicle: на транспорте (быстрее пешком, расходует топливо) ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("travelMethod")
  public TravelMethodEnum getTravelMethod() {
    return travelMethod;
  }

  public void setTravelMethod(TravelMethodEnum travelMethod) {
    this.travelMethod = travelMethod;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TravelRequest travelRequest = (TravelRequest) o;
    return Objects.equals(this.characterId, travelRequest.characterId) &&
        Objects.equals(this.targetLocationId, travelRequest.targetLocationId) &&
        Objects.equals(this.travelMethod, travelRequest.travelMethod);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, targetLocationId, travelMethod);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TravelRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetLocationId: ").append(toIndentedString(targetLocationId)).append("\n");
    sb.append("    travelMethod: ").append(toIndentedString(travelMethod)).append("\n");
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

