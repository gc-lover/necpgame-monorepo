package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HeartbeatResponse
 */


public class HeartbeatResponse {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime nextHeartbeatAt;

  /**
   * Gets or Sets warnings
   */
  public enum WarningsEnum {
    LATE_HEARTBEAT("LATE_HEARTBEAT"),
    
    NEAR_TIMEOUT("NEAR_TIMEOUT");

    private final String value;

    WarningsEnum(String value) {
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
    public static WarningsEnum fromValue(String value) {
      for (WarningsEnum b : WarningsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<WarningsEnum> warnings = new ArrayList<>();

  public HeartbeatResponse nextHeartbeatAt(@Nullable OffsetDateTime nextHeartbeatAt) {
    this.nextHeartbeatAt = nextHeartbeatAt;
    return this;
  }

  /**
   * Get nextHeartbeatAt
   * @return nextHeartbeatAt
   */
  @Valid 
  @Schema(name = "nextHeartbeatAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextHeartbeatAt")
  public @Nullable OffsetDateTime getNextHeartbeatAt() {
    return nextHeartbeatAt;
  }

  public void setNextHeartbeatAt(@Nullable OffsetDateTime nextHeartbeatAt) {
    this.nextHeartbeatAt = nextHeartbeatAt;
  }

  public HeartbeatResponse warnings(List<WarningsEnum> warnings) {
    this.warnings = warnings;
    return this;
  }

  public HeartbeatResponse addWarningsItem(WarningsEnum warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Get warnings
   * @return warnings
   */
  
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<WarningsEnum> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<WarningsEnum> warnings) {
    this.warnings = warnings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HeartbeatResponse heartbeatResponse = (HeartbeatResponse) o;
    return Objects.equals(this.nextHeartbeatAt, heartbeatResponse.nextHeartbeatAt) &&
        Objects.equals(this.warnings, heartbeatResponse.warnings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nextHeartbeatAt, warnings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HeartbeatResponse {\n");
    sb.append("    nextHeartbeatAt: ").append(toIndentedString(nextHeartbeatAt)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
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

