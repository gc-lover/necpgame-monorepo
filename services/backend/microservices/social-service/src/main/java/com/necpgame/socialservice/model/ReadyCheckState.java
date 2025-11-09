package com.necpgame.socialservice.model;

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
 * ReadyCheckState
 */


public class ReadyCheckState {

  private String readyCheckId;

  /**
   * Gets or Sets state
   */
  public enum StateEnum {
    RUNNING("running"),
    
    COMPLETED("completed"),
    
    FAILED("failed");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime expiresAt;

  @Valid
  private List<String> readyPlayers = new ArrayList<>();

  @Valid
  private List<String> pendingPlayers = new ArrayList<>();

  @Valid
  private List<String> failedPlayers = new ArrayList<>();

  public ReadyCheckState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReadyCheckState(String readyCheckId, StateEnum state, OffsetDateTime expiresAt) {
    this.readyCheckId = readyCheckId;
    this.state = state;
    this.expiresAt = expiresAt;
  }

  public ReadyCheckState readyCheckId(String readyCheckId) {
    this.readyCheckId = readyCheckId;
    return this;
  }

  /**
   * Get readyCheckId
   * @return readyCheckId
   */
  @NotNull 
  @Schema(name = "readyCheckId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("readyCheckId")
  public String getReadyCheckId() {
    return readyCheckId;
  }

  public void setReadyCheckId(String readyCheckId) {
    this.readyCheckId = readyCheckId;
  }

  public ReadyCheckState state(StateEnum state) {
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

  public ReadyCheckState expiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @NotNull @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expiresAt")
  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public ReadyCheckState readyPlayers(List<String> readyPlayers) {
    this.readyPlayers = readyPlayers;
    return this;
  }

  public ReadyCheckState addReadyPlayersItem(String readyPlayersItem) {
    if (this.readyPlayers == null) {
      this.readyPlayers = new ArrayList<>();
    }
    this.readyPlayers.add(readyPlayersItem);
    return this;
  }

  /**
   * Get readyPlayers
   * @return readyPlayers
   */
  
  @Schema(name = "readyPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("readyPlayers")
  public List<String> getReadyPlayers() {
    return readyPlayers;
  }

  public void setReadyPlayers(List<String> readyPlayers) {
    this.readyPlayers = readyPlayers;
  }

  public ReadyCheckState pendingPlayers(List<String> pendingPlayers) {
    this.pendingPlayers = pendingPlayers;
    return this;
  }

  public ReadyCheckState addPendingPlayersItem(String pendingPlayersItem) {
    if (this.pendingPlayers == null) {
      this.pendingPlayers = new ArrayList<>();
    }
    this.pendingPlayers.add(pendingPlayersItem);
    return this;
  }

  /**
   * Get pendingPlayers
   * @return pendingPlayers
   */
  
  @Schema(name = "pendingPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pendingPlayers")
  public List<String> getPendingPlayers() {
    return pendingPlayers;
  }

  public void setPendingPlayers(List<String> pendingPlayers) {
    this.pendingPlayers = pendingPlayers;
  }

  public ReadyCheckState failedPlayers(List<String> failedPlayers) {
    this.failedPlayers = failedPlayers;
    return this;
  }

  public ReadyCheckState addFailedPlayersItem(String failedPlayersItem) {
    if (this.failedPlayers == null) {
      this.failedPlayers = new ArrayList<>();
    }
    this.failedPlayers.add(failedPlayersItem);
    return this;
  }

  /**
   * Get failedPlayers
   * @return failedPlayers
   */
  
  @Schema(name = "failedPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("failedPlayers")
  public List<String> getFailedPlayers() {
    return failedPlayers;
  }

  public void setFailedPlayers(List<String> failedPlayers) {
    this.failedPlayers = failedPlayers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadyCheckState readyCheckState = (ReadyCheckState) o;
    return Objects.equals(this.readyCheckId, readyCheckState.readyCheckId) &&
        Objects.equals(this.state, readyCheckState.state) &&
        Objects.equals(this.expiresAt, readyCheckState.expiresAt) &&
        Objects.equals(this.readyPlayers, readyCheckState.readyPlayers) &&
        Objects.equals(this.pendingPlayers, readyCheckState.pendingPlayers) &&
        Objects.equals(this.failedPlayers, readyCheckState.failedPlayers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(readyCheckId, state, expiresAt, readyPlayers, pendingPlayers, failedPlayers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadyCheckState {\n");
    sb.append("    readyCheckId: ").append(toIndentedString(readyCheckId)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    readyPlayers: ").append(toIndentedString(readyPlayers)).append("\n");
    sb.append("    pendingPlayers: ").append(toIndentedString(pendingPlayers)).append("\n");
    sb.append("    failedPlayers: ").append(toIndentedString(failedPlayers)).append("\n");
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

