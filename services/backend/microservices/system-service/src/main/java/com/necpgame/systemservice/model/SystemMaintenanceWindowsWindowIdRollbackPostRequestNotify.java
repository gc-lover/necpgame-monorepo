package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify
 */

@JsonTypeName("_system_maintenance_windows__windowId__rollback_post_request_notify")

public class SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify {

  @Valid
  private List<String> channels = new ArrayList<>();

  private @Nullable String message;

  public SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify channels(List<String> channels) {
    this.channels = channels;
    return this;
  }

  public SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify addChannelsItem(String channelsItem) {
    if (this.channels == null) {
      this.channels = new ArrayList<>();
    }
    this.channels.add(channelsItem);
    return this;
  }

  /**
   * Get channels
   * @return channels
   */
  
  @Schema(name = "channels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channels")
  public List<String> getChannels() {
    return channels;
  }

  public void setChannels(List<String> channels) {
    this.channels = channels;
  }

  public SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify systemMaintenanceWindowsWindowIdRollbackPostRequestNotify = (SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify) o;
    return Objects.equals(this.channels, systemMaintenanceWindowsWindowIdRollbackPostRequestNotify.channels) &&
        Objects.equals(this.message, systemMaintenanceWindowsWindowIdRollbackPostRequestNotify.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channels, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemMaintenanceWindowsWindowIdRollbackPostRequestNotify {\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

