package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterState;
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
 * SyncResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SyncResponse {

  private @Nullable Integer serverVersion;

  private @Nullable Boolean needsUpdate;

  @Valid
  private Map<String, Object> stateDelta = new HashMap<>();

  private @Nullable CharacterState fullState;

  public SyncResponse serverVersion(@Nullable Integer serverVersion) {
    this.serverVersion = serverVersion;
    return this;
  }

  /**
   * Get serverVersion
   * @return serverVersion
   */
  
  @Schema(name = "server_version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("server_version")
  public @Nullable Integer getServerVersion() {
    return serverVersion;
  }

  public void setServerVersion(@Nullable Integer serverVersion) {
    this.serverVersion = serverVersion;
  }

  public SyncResponse needsUpdate(@Nullable Boolean needsUpdate) {
    this.needsUpdate = needsUpdate;
    return this;
  }

  /**
   * Get needsUpdate
   * @return needsUpdate
   */
  
  @Schema(name = "needs_update", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("needs_update")
  public @Nullable Boolean getNeedsUpdate() {
    return needsUpdate;
  }

  public void setNeedsUpdate(@Nullable Boolean needsUpdate) {
    this.needsUpdate = needsUpdate;
  }

  public SyncResponse stateDelta(Map<String, Object> stateDelta) {
    this.stateDelta = stateDelta;
    return this;
  }

  public SyncResponse putStateDeltaItem(String key, Object stateDeltaItem) {
    if (this.stateDelta == null) {
      this.stateDelta = new HashMap<>();
    }
    this.stateDelta.put(key, stateDeltaItem);
    return this;
  }

  /**
   * Изменения которые нужно применить
   * @return stateDelta
   */
  
  @Schema(name = "state_delta", description = "Изменения которые нужно применить", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state_delta")
  public Map<String, Object> getStateDelta() {
    return stateDelta;
  }

  public void setStateDelta(Map<String, Object> stateDelta) {
    this.stateDelta = stateDelta;
  }

  public SyncResponse fullState(@Nullable CharacterState fullState) {
    this.fullState = fullState;
    return this;
  }

  /**
   * Get fullState
   * @return fullState
   */
  @Valid 
  @Schema(name = "full_state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("full_state")
  public @Nullable CharacterState getFullState() {
    return fullState;
  }

  public void setFullState(@Nullable CharacterState fullState) {
    this.fullState = fullState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SyncResponse syncResponse = (SyncResponse) o;
    return Objects.equals(this.serverVersion, syncResponse.serverVersion) &&
        Objects.equals(this.needsUpdate, syncResponse.needsUpdate) &&
        Objects.equals(this.stateDelta, syncResponse.stateDelta) &&
        Objects.equals(this.fullState, syncResponse.fullState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serverVersion, needsUpdate, stateDelta, fullState);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SyncResponse {\n");
    sb.append("    serverVersion: ").append(toIndentedString(serverVersion)).append("\n");
    sb.append("    needsUpdate: ").append(toIndentedString(needsUpdate)).append("\n");
    sb.append("    stateDelta: ").append(toIndentedString(stateDelta)).append("\n");
    sb.append("    fullState: ").append(toIndentedString(fullState)).append("\n");
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

