package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * DistrictQuestsResponseNcpdScannersInner
 */

@JsonTypeName("DistrictQuestsResponse_ncpd_scanners_inner")

public class DistrictQuestsResponseNcpdScannersInner {

  private @Nullable String scannerId;

  private @Nullable String location;

  /**
   * Gets or Sets threatLevel
   */
  public enum ThreatLevelEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    VERY_HIGH("very_high");

    private final String value;

    ThreatLevelEnum(String value) {
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
    public static ThreatLevelEnum fromValue(String value) {
      for (ThreatLevelEnum b : ThreatLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ThreatLevelEnum threatLevel;

  public DistrictQuestsResponseNcpdScannersInner scannerId(@Nullable String scannerId) {
    this.scannerId = scannerId;
    return this;
  }

  /**
   * Get scannerId
   * @return scannerId
   */
  
  @Schema(name = "scanner_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scanner_id")
  public @Nullable String getScannerId() {
    return scannerId;
  }

  public void setScannerId(@Nullable String scannerId) {
    this.scannerId = scannerId;
  }

  public DistrictQuestsResponseNcpdScannersInner location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public DistrictQuestsResponseNcpdScannersInner threatLevel(@Nullable ThreatLevelEnum threatLevel) {
    this.threatLevel = threatLevel;
    return this;
  }

  /**
   * Get threatLevel
   * @return threatLevel
   */
  
  @Schema(name = "threat_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threat_level")
  public @Nullable ThreatLevelEnum getThreatLevel() {
    return threatLevel;
  }

  public void setThreatLevel(@Nullable ThreatLevelEnum threatLevel) {
    this.threatLevel = threatLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DistrictQuestsResponseNcpdScannersInner districtQuestsResponseNcpdScannersInner = (DistrictQuestsResponseNcpdScannersInner) o;
    return Objects.equals(this.scannerId, districtQuestsResponseNcpdScannersInner.scannerId) &&
        Objects.equals(this.location, districtQuestsResponseNcpdScannersInner.location) &&
        Objects.equals(this.threatLevel, districtQuestsResponseNcpdScannersInner.threatLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scannerId, location, threatLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DistrictQuestsResponseNcpdScannersInner {\n");
    sb.append("    scannerId: ").append(toIndentedString(scannerId)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    threatLevel: ").append(toIndentedString(threatLevel)).append("\n");
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

