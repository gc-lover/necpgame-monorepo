package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ChannelPermissions;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ChannelSettings
 */


public class ChannelSettings {

  private Integer messageCooldownSeconds;

  private Integer maxMessageLength;

  private @Nullable Integer maxMembers;

  private Boolean isPublic = false;

  private Boolean isModerated = false;

  private Boolean allowMentions = true;

  private @Nullable ChannelPermissions permissions;

  public ChannelSettings() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChannelSettings(Integer messageCooldownSeconds, Integer maxMessageLength) {
    this.messageCooldownSeconds = messageCooldownSeconds;
    this.maxMessageLength = maxMessageLength;
  }

  public ChannelSettings messageCooldownSeconds(Integer messageCooldownSeconds) {
    this.messageCooldownSeconds = messageCooldownSeconds;
    return this;
  }

  /**
   * Get messageCooldownSeconds
   * minimum: 0
   * @return messageCooldownSeconds
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "messageCooldownSeconds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("messageCooldownSeconds")
  public Integer getMessageCooldownSeconds() {
    return messageCooldownSeconds;
  }

  public void setMessageCooldownSeconds(Integer messageCooldownSeconds) {
    this.messageCooldownSeconds = messageCooldownSeconds;
  }

  public ChannelSettings maxMessageLength(Integer maxMessageLength) {
    this.maxMessageLength = maxMessageLength;
    return this;
  }

  /**
   * Get maxMessageLength
   * minimum: 1
   * maximum: 2048
   * @return maxMessageLength
   */
  @NotNull @Min(value = 1) @Max(value = 2048) 
  @Schema(name = "maxMessageLength", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxMessageLength")
  public Integer getMaxMessageLength() {
    return maxMessageLength;
  }

  public void setMaxMessageLength(Integer maxMessageLength) {
    this.maxMessageLength = maxMessageLength;
  }

  public ChannelSettings maxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
    return this;
  }

  /**
   * Get maxMembers
   * minimum: 1
   * @return maxMembers
   */
  @Min(value = 1) 
  @Schema(name = "maxMembers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxMembers")
  public @Nullable Integer getMaxMembers() {
    return maxMembers;
  }

  public void setMaxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
  }

  public ChannelSettings isPublic(Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  /**
   * Get isPublic
   * @return isPublic
   */
  
  @Schema(name = "isPublic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }

  public ChannelSettings isModerated(Boolean isModerated) {
    this.isModerated = isModerated;
    return this;
  }

  /**
   * Get isModerated
   * @return isModerated
   */
  
  @Schema(name = "isModerated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isModerated")
  public Boolean getIsModerated() {
    return isModerated;
  }

  public void setIsModerated(Boolean isModerated) {
    this.isModerated = isModerated;
  }

  public ChannelSettings allowMentions(Boolean allowMentions) {
    this.allowMentions = allowMentions;
    return this;
  }

  /**
   * Get allowMentions
   * @return allowMentions
   */
  
  @Schema(name = "allowMentions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowMentions")
  public Boolean getAllowMentions() {
    return allowMentions;
  }

  public void setAllowMentions(Boolean allowMentions) {
    this.allowMentions = allowMentions;
  }

  public ChannelSettings permissions(@Nullable ChannelPermissions permissions) {
    this.permissions = permissions;
    return this;
  }

  /**
   * Get permissions
   * @return permissions
   */
  @Valid 
  @Schema(name = "permissions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("permissions")
  public @Nullable ChannelPermissions getPermissions() {
    return permissions;
  }

  public void setPermissions(@Nullable ChannelPermissions permissions) {
    this.permissions = permissions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelSettings channelSettings = (ChannelSettings) o;
    return Objects.equals(this.messageCooldownSeconds, channelSettings.messageCooldownSeconds) &&
        Objects.equals(this.maxMessageLength, channelSettings.maxMessageLength) &&
        Objects.equals(this.maxMembers, channelSettings.maxMembers) &&
        Objects.equals(this.isPublic, channelSettings.isPublic) &&
        Objects.equals(this.isModerated, channelSettings.isModerated) &&
        Objects.equals(this.allowMentions, channelSettings.allowMentions) &&
        Objects.equals(this.permissions, channelSettings.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageCooldownSeconds, maxMessageLength, maxMembers, isPublic, isModerated, allowMentions, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelSettings {\n");
    sb.append("    messageCooldownSeconds: ").append(toIndentedString(messageCooldownSeconds)).append("\n");
    sb.append("    maxMessageLength: ").append(toIndentedString(maxMessageLength)).append("\n");
    sb.append("    maxMembers: ").append(toIndentedString(maxMembers)).append("\n");
    sb.append("    isPublic: ").append(toIndentedString(isPublic)).append("\n");
    sb.append("    isModerated: ").append(toIndentedString(isModerated)).append("\n");
    sb.append("    allowMentions: ").append(toIndentedString(allowMentions)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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

