package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RewardClaimRequest
 */


public class RewardClaimRequest {

  private Integer level;

  /**
   * Gets or Sets track
   */
  public enum TrackEnum {
    FREE("FREE"),
    
    PREMIUM("PREMIUM");

    private final String value;

    TrackEnum(String value) {
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
    public static TrackEnum fromValue(String value) {
      for (TrackEnum b : TrackEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TrackEnum track;

  private Boolean autoClaimExtras = false;

  @Valid
  private Map<String, Object> clientContext = new HashMap<>();

  public RewardClaimRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RewardClaimRequest(Integer level, TrackEnum track) {
    this.level = level;
    this.track = track;
  }

  public RewardClaimRequest level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  @NotNull 
  @Schema(name = "level", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public RewardClaimRequest track(TrackEnum track) {
    this.track = track;
    return this;
  }

  /**
   * Get track
   * @return track
   */
  @NotNull 
  @Schema(name = "track", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("track")
  public TrackEnum getTrack() {
    return track;
  }

  public void setTrack(TrackEnum track) {
    this.track = track;
  }

  public RewardClaimRequest autoClaimExtras(Boolean autoClaimExtras) {
    this.autoClaimExtras = autoClaimExtras;
    return this;
  }

  /**
   * Get autoClaimExtras
   * @return autoClaimExtras
   */
  
  @Schema(name = "autoClaimExtras", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoClaimExtras")
  public Boolean getAutoClaimExtras() {
    return autoClaimExtras;
  }

  public void setAutoClaimExtras(Boolean autoClaimExtras) {
    this.autoClaimExtras = autoClaimExtras;
  }

  public RewardClaimRequest clientContext(Map<String, Object> clientContext) {
    this.clientContext = clientContext;
    return this;
  }

  public RewardClaimRequest putClientContextItem(String key, Object clientContextItem) {
    if (this.clientContext == null) {
      this.clientContext = new HashMap<>();
    }
    this.clientContext.put(key, clientContextItem);
    return this;
  }

  /**
   * Get clientContext
   * @return clientContext
   */
  
  @Schema(name = "clientContext", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clientContext")
  public Map<String, Object> getClientContext() {
    return clientContext;
  }

  public void setClientContext(Map<String, Object> clientContext) {
    this.clientContext = clientContext;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardClaimRequest rewardClaimRequest = (RewardClaimRequest) o;
    return Objects.equals(this.level, rewardClaimRequest.level) &&
        Objects.equals(this.track, rewardClaimRequest.track) &&
        Objects.equals(this.autoClaimExtras, rewardClaimRequest.autoClaimExtras) &&
        Objects.equals(this.clientContext, rewardClaimRequest.clientContext);
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, track, autoClaimExtras, clientContext);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardClaimRequest {\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    track: ").append(toIndentedString(track)).append("\n");
    sb.append("    autoClaimExtras: ").append(toIndentedString(autoClaimExtras)).append("\n");
    sb.append("    clientContext: ").append(toIndentedString(clientContext)).append("\n");
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

