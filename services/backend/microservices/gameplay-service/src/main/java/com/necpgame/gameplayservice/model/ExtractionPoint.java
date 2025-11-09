package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CreateExtractionPointRequestLocation;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ExtractionPoint
 */


public class ExtractionPoint {

  private @Nullable String pointId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    FIXED("fixed"),
    
    DYNAMIC("dynamic"),
    
    PLAYER_CREATED("player_created");

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

  private @Nullable CreateExtractionPointRequestLocation location;

  private @Nullable BigDecimal activationTime;

  private @Nullable Boolean pvpRisk;

  public ExtractionPoint pointId(@Nullable String pointId) {
    this.pointId = pointId;
    return this;
  }

  /**
   * Get pointId
   * @return pointId
   */
  
  @Schema(name = "point_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("point_id")
  public @Nullable String getPointId() {
    return pointId;
  }

  public void setPointId(@Nullable String pointId) {
    this.pointId = pointId;
  }

  public ExtractionPoint type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public ExtractionPoint location(@Nullable CreateExtractionPointRequestLocation location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  @Valid 
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable CreateExtractionPointRequestLocation getLocation() {
    return location;
  }

  public void setLocation(@Nullable CreateExtractionPointRequestLocation location) {
    this.location = location;
  }

  public ExtractionPoint activationTime(@Nullable BigDecimal activationTime) {
    this.activationTime = activationTime;
    return this;
  }

  /**
   * Время активации (секунды)
   * @return activationTime
   */
  @Valid 
  @Schema(name = "activation_time", description = "Время активации (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activation_time")
  public @Nullable BigDecimal getActivationTime() {
    return activationTime;
  }

  public void setActivationTime(@Nullable BigDecimal activationTime) {
    this.activationTime = activationTime;
  }

  public ExtractionPoint pvpRisk(@Nullable Boolean pvpRisk) {
    this.pvpRisk = pvpRisk;
    return this;
  }

  /**
   * Есть ли риск PvP
   * @return pvpRisk
   */
  
  @Schema(name = "pvp_risk", description = "Есть ли риск PvP", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pvp_risk")
  public @Nullable Boolean getPvpRisk() {
    return pvpRisk;
  }

  public void setPvpRisk(@Nullable Boolean pvpRisk) {
    this.pvpRisk = pvpRisk;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExtractionPoint extractionPoint = (ExtractionPoint) o;
    return Objects.equals(this.pointId, extractionPoint.pointId) &&
        Objects.equals(this.type, extractionPoint.type) &&
        Objects.equals(this.location, extractionPoint.location) &&
        Objects.equals(this.activationTime, extractionPoint.activationTime) &&
        Objects.equals(this.pvpRisk, extractionPoint.pvpRisk);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pointId, type, location, activationTime, pvpRisk);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExtractionPoint {\n");
    sb.append("    pointId: ").append(toIndentedString(pointId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    activationTime: ").append(toIndentedString(activationTime)).append("\n");
    sb.append("    pvpRisk: ").append(toIndentedString(pvpRisk)).append("\n");
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

