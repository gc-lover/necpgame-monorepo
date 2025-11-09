package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.MatchSearchRequestPartyContext;
import com.necpgame.gameplayservice.model.MatchSearchRequestPreferences;
import com.necpgame.gameplayservice.model.RoleRequirement;
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
 * MatchSearchRequest
 */


public class MatchSearchRequest {

  @Valid
  private List<UUID> queueIds = new ArrayList<>();

  /**
   * Gets or Sets mode
   */
  public enum ModeEnum {
    PVP_RANKED("PVP_RANKED"),
    
    PVP_CASUAL("PVP_CASUAL"),
    
    PVE_DUNGEON("PVE_DUNGEON"),
    
    RAID("RAID"),
    
    ARENA_EVENT("ARENA_EVENT");

    private final String value;

    ModeEnum(String value) {
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
    public static ModeEnum fromValue(String value) {
      for (ModeEnum b : ModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ModeEnum mode;

  @Valid
  private List<@Valid RoleRequirement> requiredRoles = new ArrayList<>();

  private @Nullable Integer latencyCapMs;

  private Boolean allowCrossRegion = false;

  private @Nullable MatchSearchRequestPartyContext partyContext;

  private @Nullable MatchSearchRequestPreferences preferences;

  public MatchSearchRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchSearchRequest(List<UUID> queueIds, ModeEnum mode) {
    this.queueIds = queueIds;
    this.mode = mode;
  }

  public MatchSearchRequest queueIds(List<UUID> queueIds) {
    this.queueIds = queueIds;
    return this;
  }

  public MatchSearchRequest addQueueIdsItem(UUID queueIdsItem) {
    if (this.queueIds == null) {
      this.queueIds = new ArrayList<>();
    }
    this.queueIds.add(queueIdsItem);
    return this;
  }

  /**
   * Get queueIds
   * @return queueIds
   */
  @NotNull @Valid @Size(min = 1, max = 30) 
  @Schema(name = "queueIds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("queueIds")
  public List<UUID> getQueueIds() {
    return queueIds;
  }

  public void setQueueIds(List<UUID> queueIds) {
    this.queueIds = queueIds;
  }

  public MatchSearchRequest mode(ModeEnum mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @NotNull 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mode")
  public ModeEnum getMode() {
    return mode;
  }

  public void setMode(ModeEnum mode) {
    this.mode = mode;
  }

  public MatchSearchRequest requiredRoles(List<@Valid RoleRequirement> requiredRoles) {
    this.requiredRoles = requiredRoles;
    return this;
  }

  public MatchSearchRequest addRequiredRolesItem(RoleRequirement requiredRolesItem) {
    if (this.requiredRoles == null) {
      this.requiredRoles = new ArrayList<>();
    }
    this.requiredRoles.add(requiredRolesItem);
    return this;
  }

  /**
   * Get requiredRoles
   * @return requiredRoles
   */
  @Valid 
  @Schema(name = "requiredRoles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredRoles")
  public List<@Valid RoleRequirement> getRequiredRoles() {
    return requiredRoles;
  }

  public void setRequiredRoles(List<@Valid RoleRequirement> requiredRoles) {
    this.requiredRoles = requiredRoles;
  }

  public MatchSearchRequest latencyCapMs(@Nullable Integer latencyCapMs) {
    this.latencyCapMs = latencyCapMs;
    return this;
  }

  /**
   * Get latencyCapMs
   * minimum: 30
   * maximum: 250
   * @return latencyCapMs
   */
  @Min(value = 30) @Max(value = 250) 
  @Schema(name = "latencyCapMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latencyCapMs")
  public @Nullable Integer getLatencyCapMs() {
    return latencyCapMs;
  }

  public void setLatencyCapMs(@Nullable Integer latencyCapMs) {
    this.latencyCapMs = latencyCapMs;
  }

  public MatchSearchRequest allowCrossRegion(Boolean allowCrossRegion) {
    this.allowCrossRegion = allowCrossRegion;
    return this;
  }

  /**
   * Get allowCrossRegion
   * @return allowCrossRegion
   */
  
  @Schema(name = "allowCrossRegion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowCrossRegion")
  public Boolean getAllowCrossRegion() {
    return allowCrossRegion;
  }

  public void setAllowCrossRegion(Boolean allowCrossRegion) {
    this.allowCrossRegion = allowCrossRegion;
  }

  public MatchSearchRequest partyContext(@Nullable MatchSearchRequestPartyContext partyContext) {
    this.partyContext = partyContext;
    return this;
  }

  /**
   * Get partyContext
   * @return partyContext
   */
  @Valid 
  @Schema(name = "partyContext", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyContext")
  public @Nullable MatchSearchRequestPartyContext getPartyContext() {
    return partyContext;
  }

  public void setPartyContext(@Nullable MatchSearchRequestPartyContext partyContext) {
    this.partyContext = partyContext;
  }

  public MatchSearchRequest preferences(@Nullable MatchSearchRequestPreferences preferences) {
    this.preferences = preferences;
    return this;
  }

  /**
   * Get preferences
   * @return preferences
   */
  @Valid 
  @Schema(name = "preferences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferences")
  public @Nullable MatchSearchRequestPreferences getPreferences() {
    return preferences;
  }

  public void setPreferences(@Nullable MatchSearchRequestPreferences preferences) {
    this.preferences = preferences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchSearchRequest matchSearchRequest = (MatchSearchRequest) o;
    return Objects.equals(this.queueIds, matchSearchRequest.queueIds) &&
        Objects.equals(this.mode, matchSearchRequest.mode) &&
        Objects.equals(this.requiredRoles, matchSearchRequest.requiredRoles) &&
        Objects.equals(this.latencyCapMs, matchSearchRequest.latencyCapMs) &&
        Objects.equals(this.allowCrossRegion, matchSearchRequest.allowCrossRegion) &&
        Objects.equals(this.partyContext, matchSearchRequest.partyContext) &&
        Objects.equals(this.preferences, matchSearchRequest.preferences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queueIds, mode, requiredRoles, latencyCapMs, allowCrossRegion, partyContext, preferences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchSearchRequest {\n");
    sb.append("    queueIds: ").append(toIndentedString(queueIds)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    requiredRoles: ").append(toIndentedString(requiredRoles)).append("\n");
    sb.append("    latencyCapMs: ").append(toIndentedString(latencyCapMs)).append("\n");
    sb.append("    allowCrossRegion: ").append(toIndentedString(allowCrossRegion)).append("\n");
    sb.append("    partyContext: ").append(toIndentedString(partyContext)).append("\n");
    sb.append("    preferences: ").append(toIndentedString(preferences)).append("\n");
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

