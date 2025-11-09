package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InstallDaemonRequest
 */

@JsonTypeName("installDaemon_request")

public class InstallDaemonRequest {

  private String characterId;

  private String daemonId;

  private Integer slot;

  public InstallDaemonRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InstallDaemonRequest(String characterId, String daemonId, Integer slot) {
    this.characterId = characterId;
    this.daemonId = daemonId;
    this.slot = slot;
  }

  public InstallDaemonRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public InstallDaemonRequest daemonId(String daemonId) {
    this.daemonId = daemonId;
    return this;
  }

  /**
   * Get daemonId
   * @return daemonId
   */
  @NotNull 
  @Schema(name = "daemon_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("daemon_id")
  public String getDaemonId() {
    return daemonId;
  }

  public void setDaemonId(String daemonId) {
    this.daemonId = daemonId;
  }

  public InstallDaemonRequest slot(Integer slot) {
    this.slot = slot;
    return this;
  }

  /**
   * Get slot
   * minimum: 1
   * maximum: 8
   * @return slot
   */
  @NotNull @Min(value = 1) @Max(value = 8) 
  @Schema(name = "slot", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slot")
  public Integer getSlot() {
    return slot;
  }

  public void setSlot(Integer slot) {
    this.slot = slot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InstallDaemonRequest installDaemonRequest = (InstallDaemonRequest) o;
    return Objects.equals(this.characterId, installDaemonRequest.characterId) &&
        Objects.equals(this.daemonId, installDaemonRequest.daemonId) &&
        Objects.equals(this.slot, installDaemonRequest.slot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, daemonId, slot);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InstallDaemonRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    daemonId: ").append(toIndentedString(daemonId)).append("\n");
    sb.append("    slot: ").append(toIndentedString(slot)).append("\n");
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

