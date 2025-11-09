package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
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
 * HeartbeatRequest
 */


public class HeartbeatRequest {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime clientTimestamp;

  private @Nullable Integer latencyMs;

  /**
   * Gets or Sets activity
   */
  public enum ActivityEnum {
    ACTIVE("active"),
    
    IDLE("idle"),
    
    MENU("menu");

    private final String value;

    ActivityEnum(String value) {
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
    public static ActivityEnum fromValue(String value) {
      for (ActivityEnum b : ActivityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ActivityEnum activity;

  private @Nullable String gameVersion;

  public HeartbeatRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HeartbeatRequest(OffsetDateTime clientTimestamp) {
    this.clientTimestamp = clientTimestamp;
  }

  public HeartbeatRequest clientTimestamp(OffsetDateTime clientTimestamp) {
    this.clientTimestamp = clientTimestamp;
    return this;
  }

  /**
   * Get clientTimestamp
   * @return clientTimestamp
   */
  @NotNull @Valid 
  @Schema(name = "clientTimestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("clientTimestamp")
  public OffsetDateTime getClientTimestamp() {
    return clientTimestamp;
  }

  public void setClientTimestamp(OffsetDateTime clientTimestamp) {
    this.clientTimestamp = clientTimestamp;
  }

  public HeartbeatRequest latencyMs(@Nullable Integer latencyMs) {
    this.latencyMs = latencyMs;
    return this;
  }

  /**
   * Get latencyMs
   * @return latencyMs
   */
  
  @Schema(name = "latencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latencyMs")
  public @Nullable Integer getLatencyMs() {
    return latencyMs;
  }

  public void setLatencyMs(@Nullable Integer latencyMs) {
    this.latencyMs = latencyMs;
  }

  public HeartbeatRequest activity(@Nullable ActivityEnum activity) {
    this.activity = activity;
    return this;
  }

  /**
   * Get activity
   * @return activity
   */
  
  @Schema(name = "activity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activity")
  public @Nullable ActivityEnum getActivity() {
    return activity;
  }

  public void setActivity(@Nullable ActivityEnum activity) {
    this.activity = activity;
  }

  public HeartbeatRequest gameVersion(@Nullable String gameVersion) {
    this.gameVersion = gameVersion;
    return this;
  }

  /**
   * Get gameVersion
   * @return gameVersion
   */
  
  @Schema(name = "gameVersion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gameVersion")
  public @Nullable String getGameVersion() {
    return gameVersion;
  }

  public void setGameVersion(@Nullable String gameVersion) {
    this.gameVersion = gameVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HeartbeatRequest heartbeatRequest = (HeartbeatRequest) o;
    return Objects.equals(this.clientTimestamp, heartbeatRequest.clientTimestamp) &&
        Objects.equals(this.latencyMs, heartbeatRequest.latencyMs) &&
        Objects.equals(this.activity, heartbeatRequest.activity) &&
        Objects.equals(this.gameVersion, heartbeatRequest.gameVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientTimestamp, latencyMs, activity, gameVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HeartbeatRequest {\n");
    sb.append("    clientTimestamp: ").append(toIndentedString(clientTimestamp)).append("\n");
    sb.append("    latencyMs: ").append(toIndentedString(latencyMs)).append("\n");
    sb.append("    activity: ").append(toIndentedString(activity)).append("\n");
    sb.append("    gameVersion: ").append(toIndentedString(gameVersion)).append("\n");
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

