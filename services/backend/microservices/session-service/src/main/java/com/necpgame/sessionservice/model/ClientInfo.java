package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ClientInfo
 */


public class ClientInfo {

  private @Nullable String device;

  private @Nullable String build;

  private @Nullable String platform;

  public ClientInfo device(@Nullable String device) {
    this.device = device;
    return this;
  }

  /**
   * Get device
   * @return device
   */
  
  @Schema(name = "device", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("device")
  public @Nullable String getDevice() {
    return device;
  }

  public void setDevice(@Nullable String device) {
    this.device = device;
  }

  public ClientInfo build(@Nullable String build) {
    this.build = build;
    return this;
  }

  /**
   * Get build
   * @return build
   */
  
  @Schema(name = "build", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("build")
  public @Nullable String getBuild() {
    return build;
  }

  public void setBuild(@Nullable String build) {
    this.build = build;
  }

  public ClientInfo platform(@Nullable String platform) {
    this.platform = platform;
    return this;
  }

  /**
   * Get platform
   * @return platform
   */
  
  @Schema(name = "platform", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("platform")
  public @Nullable String getPlatform() {
    return platform;
  }

  public void setPlatform(@Nullable String platform) {
    this.platform = platform;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientInfo clientInfo = (ClientInfo) o;
    return Objects.equals(this.device, clientInfo.device) &&
        Objects.equals(this.build, clientInfo.build) &&
        Objects.equals(this.platform, clientInfo.platform);
  }

  @Override
  public int hashCode() {
    return Objects.hash(device, build, platform);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClientInfo {\n");
    sb.append("    device: ").append(toIndentedString(device)).append("\n");
    sb.append("    build: ").append(toIndentedString(build)).append("\n");
    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
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

