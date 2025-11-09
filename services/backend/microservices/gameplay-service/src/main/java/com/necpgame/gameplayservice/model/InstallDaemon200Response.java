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
 * InstallDaemon200Response
 */

@JsonTypeName("installDaemon_200_response")

public class InstallDaemon200Response {

  private @Nullable Boolean success;

  private @Nullable String daemonId;

  private @Nullable Integer slot;

  public InstallDaemon200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public InstallDaemon200Response daemonId(@Nullable String daemonId) {
    this.daemonId = daemonId;
    return this;
  }

  /**
   * Get daemonId
   * @return daemonId
   */
  
  @Schema(name = "daemon_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("daemon_id")
  public @Nullable String getDaemonId() {
    return daemonId;
  }

  public void setDaemonId(@Nullable String daemonId) {
    this.daemonId = daemonId;
  }

  public InstallDaemon200Response slot(@Nullable Integer slot) {
    this.slot = slot;
    return this;
  }

  /**
   * Get slot
   * @return slot
   */
  
  @Schema(name = "slot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slot")
  public @Nullable Integer getSlot() {
    return slot;
  }

  public void setSlot(@Nullable Integer slot) {
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
    InstallDaemon200Response installDaemon200Response = (InstallDaemon200Response) o;
    return Objects.equals(this.success, installDaemon200Response.success) &&
        Objects.equals(this.daemonId, installDaemon200Response.daemonId) &&
        Objects.equals(this.slot, installDaemon200Response.slot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, daemonId, slot);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InstallDaemon200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
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

