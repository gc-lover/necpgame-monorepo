package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ChannelScope;
import com.necpgame.socialservice.model.ChannelSettings;
import com.necpgame.socialservice.model.ChannelType;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * ChannelDefinition
 */


public class ChannelDefinition {

  private String channelId;

  private ChannelType channelType;

  private String channelName;

  private ChannelScope scope;

  private @Nullable UUID ownerId;

  private ChannelSettings settings;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  private @Nullable Boolean isActive;

  public ChannelDefinition() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChannelDefinition(String channelId, ChannelType channelType, String channelName, ChannelScope scope, ChannelSettings settings) {
    this.channelId = channelId;
    this.channelType = channelType;
    this.channelName = channelName;
    this.scope = scope;
    this.settings = settings;
  }

  public ChannelDefinition channelId(String channelId) {
    this.channelId = channelId;
    return this;
  }

  /**
   * Get channelId
   * @return channelId
   */
  @NotNull 
  @Schema(name = "channelId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelId")
  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public ChannelDefinition channelType(ChannelType channelType) {
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

  public ChannelDefinition channelName(String channelName) {
    this.channelName = channelName;
    return this;
  }

  /**
   * Get channelName
   * @return channelName
   */
  @NotNull 
  @Schema(name = "channelName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelName")
  public String getChannelName() {
    return channelName;
  }

  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }

  public ChannelDefinition scope(ChannelScope scope) {
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

  public ChannelDefinition ownerId(@Nullable UUID ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * Get ownerId
   * @return ownerId
   */
  @Valid 
  @Schema(name = "ownerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ownerId")
  public @Nullable UUID getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(@Nullable UUID ownerId) {
    this.ownerId = ownerId;
  }

  public ChannelDefinition settings(ChannelSettings settings) {
    this.settings = settings;
    return this;
  }

  /**
   * Get settings
   * @return settings
   */
  @NotNull @Valid 
  @Schema(name = "settings", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("settings")
  public ChannelSettings getSettings() {
    return settings;
  }

  public void setSettings(ChannelSettings settings) {
    this.settings = settings;
  }

  public ChannelDefinition createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ChannelDefinition expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public ChannelDefinition isActive(@Nullable Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  /**
   * Get isActive
   * @return isActive
   */
  
  @Schema(name = "isActive", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isActive")
  public @Nullable Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(@Nullable Boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelDefinition channelDefinition = (ChannelDefinition) o;
    return Objects.equals(this.channelId, channelDefinition.channelId) &&
        Objects.equals(this.channelType, channelDefinition.channelType) &&
        Objects.equals(this.channelName, channelDefinition.channelName) &&
        Objects.equals(this.scope, channelDefinition.scope) &&
        Objects.equals(this.ownerId, channelDefinition.ownerId) &&
        Objects.equals(this.settings, channelDefinition.settings) &&
        Objects.equals(this.createdAt, channelDefinition.createdAt) &&
        Objects.equals(this.expiresAt, channelDefinition.expiresAt) &&
        Objects.equals(this.isActive, channelDefinition.isActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelId, channelType, channelName, scope, ownerId, settings, createdAt, expiresAt, isActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelDefinition {\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    channelName: ").append(toIndentedString(channelName)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
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

