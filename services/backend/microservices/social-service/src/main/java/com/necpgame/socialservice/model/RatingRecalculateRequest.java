package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * RatingRecalculateRequest
 */


public class RatingRecalculateRequest {

  @Valid
  private List<UUID> playerIds = new ArrayList<>();

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    EXECUTOR("executor"),
    
    CLIENT("client"),
    
    BOTH("both");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RoleEnum role;

  /**
   * Gets or Sets reason
   */
  public enum ReasonEnum {
    SEASONAL_RESET("seasonal_reset"),
    
    ANOMALY_DETECTED("anomaly_detected"),
    
    MANUAL_ADJUSTMENT("manual_adjustment"),
    
    DISPUTE_RESOLUTION("dispute_resolution"),
    
    DATA_SYNC("data_sync");

    private final String value;

    ReasonEnum(String value) {
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
    public static ReasonEnum fromValue(String value) {
      for (ReasonEnum b : ReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ReasonEnum reason;

  private @Nullable UUID requestedBy;

  private Boolean notifyPlayers = false;

  public RatingRecalculateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingRecalculateRequest(List<UUID> playerIds, RoleEnum role, ReasonEnum reason) {
    this.playerIds = playerIds;
    this.role = role;
    this.reason = reason;
  }

  public RatingRecalculateRequest playerIds(List<UUID> playerIds) {
    this.playerIds = playerIds;
    return this;
  }

  public RatingRecalculateRequest addPlayerIdsItem(UUID playerIdsItem) {
    if (this.playerIds == null) {
      this.playerIds = new ArrayList<>();
    }
    this.playerIds.add(playerIdsItem);
    return this;
  }

  /**
   * Get playerIds
   * @return playerIds
   */
  @NotNull @Valid @Size(min = 1, max = 500) 
  @Schema(name = "playerIds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerIds")
  public List<UUID> getPlayerIds() {
    return playerIds;
  }

  public void setPlayerIds(List<UUID> playerIds) {
    this.playerIds = playerIds;
  }

  public RatingRecalculateRequest role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public RatingRecalculateRequest reason(ReasonEnum reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public ReasonEnum getReason() {
    return reason;
  }

  public void setReason(ReasonEnum reason) {
    this.reason = reason;
  }

  public RatingRecalculateRequest requestedBy(@Nullable UUID requestedBy) {
    this.requestedBy = requestedBy;
    return this;
  }

  /**
   * Get requestedBy
   * @return requestedBy
   */
  @Valid 
  @Schema(name = "requestedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requestedBy")
  public @Nullable UUID getRequestedBy() {
    return requestedBy;
  }

  public void setRequestedBy(@Nullable UUID requestedBy) {
    this.requestedBy = requestedBy;
  }

  public RatingRecalculateRequest notifyPlayers(Boolean notifyPlayers) {
    this.notifyPlayers = notifyPlayers;
    return this;
  }

  /**
   * Get notifyPlayers
   * @return notifyPlayers
   */
  
  @Schema(name = "notifyPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyPlayers")
  public Boolean getNotifyPlayers() {
    return notifyPlayers;
  }

  public void setNotifyPlayers(Boolean notifyPlayers) {
    this.notifyPlayers = notifyPlayers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingRecalculateRequest ratingRecalculateRequest = (RatingRecalculateRequest) o;
    return Objects.equals(this.playerIds, ratingRecalculateRequest.playerIds) &&
        Objects.equals(this.role, ratingRecalculateRequest.role) &&
        Objects.equals(this.reason, ratingRecalculateRequest.reason) &&
        Objects.equals(this.requestedBy, ratingRecalculateRequest.requestedBy) &&
        Objects.equals(this.notifyPlayers, ratingRecalculateRequest.notifyPlayers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerIds, role, reason, requestedBy, notifyPlayers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingRecalculateRequest {\n");
    sb.append("    playerIds: ").append(toIndentedString(playerIds)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    requestedBy: ").append(toIndentedString(requestedBy)).append("\n");
    sb.append("    notifyPlayers: ").append(toIndentedString(notifyPlayers)).append("\n");
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

