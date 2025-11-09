package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * MatchmakingStatus
 */


public class MatchmakingStatus {

  private String requestId;

  /**
   * Gets or Sets state
   */
  public enum StateEnum {
    QUEUED("queued"),
    
    MATCHED("matched"),
    
    CREATED("created"),
    
    EXPIRED("expired");

    private final String value;

    StateEnum(String value) {
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
    public static StateEnum fromValue(String value) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StateEnum state;

  private @Nullable String suggestedLobbyId;

  private @Nullable Integer estimatedWaitSeconds;

  private @Nullable String assignedRole;

  public MatchmakingStatus() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchmakingStatus(String requestId, StateEnum state) {
    this.requestId = requestId;
    this.state = state;
  }

  public MatchmakingStatus requestId(String requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Get requestId
   * @return requestId
   */
  @NotNull 
  @Schema(name = "requestId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requestId")
  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public MatchmakingStatus state(StateEnum state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  @NotNull 
  @Schema(name = "state", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("state")
  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public MatchmakingStatus suggestedLobbyId(@Nullable String suggestedLobbyId) {
    this.suggestedLobbyId = suggestedLobbyId;
    return this;
  }

  /**
   * Get suggestedLobbyId
   * @return suggestedLobbyId
   */
  
  @Schema(name = "suggestedLobbyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("suggestedLobbyId")
  public @Nullable String getSuggestedLobbyId() {
    return suggestedLobbyId;
  }

  public void setSuggestedLobbyId(@Nullable String suggestedLobbyId) {
    this.suggestedLobbyId = suggestedLobbyId;
  }

  public MatchmakingStatus estimatedWaitSeconds(@Nullable Integer estimatedWaitSeconds) {
    this.estimatedWaitSeconds = estimatedWaitSeconds;
    return this;
  }

  /**
   * Get estimatedWaitSeconds
   * @return estimatedWaitSeconds
   */
  
  @Schema(name = "estimatedWaitSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimatedWaitSeconds")
  public @Nullable Integer getEstimatedWaitSeconds() {
    return estimatedWaitSeconds;
  }

  public void setEstimatedWaitSeconds(@Nullable Integer estimatedWaitSeconds) {
    this.estimatedWaitSeconds = estimatedWaitSeconds;
  }

  public MatchmakingStatus assignedRole(@Nullable String assignedRole) {
    this.assignedRole = assignedRole;
    return this;
  }

  /**
   * Get assignedRole
   * @return assignedRole
   */
  
  @Schema(name = "assignedRole", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assignedRole")
  public @Nullable String getAssignedRole() {
    return assignedRole;
  }

  public void setAssignedRole(@Nullable String assignedRole) {
    this.assignedRole = assignedRole;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchmakingStatus matchmakingStatus = (MatchmakingStatus) o;
    return Objects.equals(this.requestId, matchmakingStatus.requestId) &&
        Objects.equals(this.state, matchmakingStatus.state) &&
        Objects.equals(this.suggestedLobbyId, matchmakingStatus.suggestedLobbyId) &&
        Objects.equals(this.estimatedWaitSeconds, matchmakingStatus.estimatedWaitSeconds) &&
        Objects.equals(this.assignedRole, matchmakingStatus.assignedRole);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestId, state, suggestedLobbyId, estimatedWaitSeconds, assignedRole);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchmakingStatus {\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    suggestedLobbyId: ").append(toIndentedString(suggestedLobbyId)).append("\n");
    sb.append("    estimatedWaitSeconds: ").append(toIndentedString(estimatedWaitSeconds)).append("\n");
    sb.append("    assignedRole: ").append(toIndentedString(assignedRole)).append("\n");
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

