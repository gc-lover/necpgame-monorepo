package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ChannelPermissions;
import com.necpgame.socialservice.model.ChannelScope;
import com.necpgame.socialservice.model.ChannelType;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ChannelTypeInfo
 */


public class ChannelTypeInfo {

  private ChannelType channelType;

  private ChannelScope scope;

  private Integer cooldownSeconds;

  private Integer maxMessageLength;

  private @Nullable Integer maxMembers;

  private @Nullable String description;

  private @Nullable ChannelPermissions defaultPermissions;

  public ChannelTypeInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChannelTypeInfo(ChannelType channelType, ChannelScope scope, Integer cooldownSeconds, Integer maxMessageLength) {
    this.channelType = channelType;
    this.scope = scope;
    this.cooldownSeconds = cooldownSeconds;
    this.maxMessageLength = maxMessageLength;
  }

  public ChannelTypeInfo channelType(ChannelType channelType) {
    this.channelType = channelType;
    return this;
  }

  /**
   * Get channelType
   * @return channelType
   */
  @NotNull @Valid 
  @Schema(name = "channelType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelType")
  public ChannelType getChannelType() {
    return channelType;
  }

  public void setChannelType(ChannelType channelType) {
    this.channelType = channelType;
  }

  public ChannelTypeInfo scope(ChannelScope scope) {
    this.scope = scope;
    return this;
  }

  /**
   * Get scope
   * @return scope
   */
  @NotNull @Valid 
  @Schema(name = "scope", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("scope")
  public ChannelScope getScope() {
    return scope;
  }

  public void setScope(ChannelScope scope) {
    this.scope = scope;
  }

  public ChannelTypeInfo cooldownSeconds(Integer cooldownSeconds) {
    this.cooldownSeconds = cooldownSeconds;
    return this;
  }

  /**
   * Get cooldownSeconds
   * minimum: 0
   * @return cooldownSeconds
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "cooldownSeconds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cooldownSeconds")
  public Integer getCooldownSeconds() {
    return cooldownSeconds;
  }

  public void setCooldownSeconds(Integer cooldownSeconds) {
    this.cooldownSeconds = cooldownSeconds;
  }

  public ChannelTypeInfo maxMessageLength(Integer maxMessageLength) {
    this.maxMessageLength = maxMessageLength;
    return this;
  }

  /**
   * Get maxMessageLength
   * minimum: 1
   * @return maxMessageLength
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "maxMessageLength", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxMessageLength")
  public Integer getMaxMessageLength() {
    return maxMessageLength;
  }

  public void setMaxMessageLength(Integer maxMessageLength) {
    this.maxMessageLength = maxMessageLength;
  }

  public ChannelTypeInfo maxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
    return this;
  }

  /**
   * Get maxMembers
   * @return maxMembers
   */
  
  @Schema(name = "maxMembers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxMembers")
  public @Nullable Integer getMaxMembers() {
    return maxMembers;
  }

  public void setMaxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
  }

  public ChannelTypeInfo description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public ChannelTypeInfo defaultPermissions(@Nullable ChannelPermissions defaultPermissions) {
    this.defaultPermissions = defaultPermissions;
    return this;
  }

  /**
   * Get defaultPermissions
   * @return defaultPermissions
   */
  @Valid 
  @Schema(name = "defaultPermissions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defaultPermissions")
  public @Nullable ChannelPermissions getDefaultPermissions() {
    return defaultPermissions;
  }

  public void setDefaultPermissions(@Nullable ChannelPermissions defaultPermissions) {
    this.defaultPermissions = defaultPermissions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelTypeInfo channelTypeInfo = (ChannelTypeInfo) o;
    return Objects.equals(this.channelType, channelTypeInfo.channelType) &&
        Objects.equals(this.scope, channelTypeInfo.scope) &&
        Objects.equals(this.cooldownSeconds, channelTypeInfo.cooldownSeconds) &&
        Objects.equals(this.maxMessageLength, channelTypeInfo.maxMessageLength) &&
        Objects.equals(this.maxMembers, channelTypeInfo.maxMembers) &&
        Objects.equals(this.description, channelTypeInfo.description) &&
        Objects.equals(this.defaultPermissions, channelTypeInfo.defaultPermissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelType, scope, cooldownSeconds, maxMessageLength, maxMembers, description, defaultPermissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelTypeInfo {\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    cooldownSeconds: ").append(toIndentedString(cooldownSeconds)).append("\n");
    sb.append("    maxMessageLength: ").append(toIndentedString(maxMessageLength)).append("\n");
    sb.append("    maxMembers: ").append(toIndentedString(maxMembers)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    defaultPermissions: ").append(toIndentedString(defaultPermissions)).append("\n");
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

