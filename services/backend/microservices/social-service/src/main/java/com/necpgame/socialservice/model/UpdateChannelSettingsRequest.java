package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ChannelPermissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateChannelSettingsRequest
 */


public class UpdateChannelSettingsRequest {

  private @Nullable Integer messageCooldownSeconds;

  private @Nullable Integer maxMessageLength;

  private @Nullable Integer maxMembers;

  private @Nullable ChannelPermissions permissions;

  @Valid
  private List<UUID> moderators = new ArrayList<>();

  public UpdateChannelSettingsRequest messageCooldownSeconds(@Nullable Integer messageCooldownSeconds) {
    this.messageCooldownSeconds = messageCooldownSeconds;
    return this;
  }

  /**
   * Get messageCooldownSeconds
   * minimum: 0
   * @return messageCooldownSeconds
   */
  @Min(value = 0) 
  @Schema(name = "messageCooldownSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("messageCooldownSeconds")
  public @Nullable Integer getMessageCooldownSeconds() {
    return messageCooldownSeconds;
  }

  public void setMessageCooldownSeconds(@Nullable Integer messageCooldownSeconds) {
    this.messageCooldownSeconds = messageCooldownSeconds;
  }

  public UpdateChannelSettingsRequest maxMessageLength(@Nullable Integer maxMessageLength) {
    this.maxMessageLength = maxMessageLength;
    return this;
  }

  /**
   * Get maxMessageLength
   * minimum: 1
   * maximum: 2048
   * @return maxMessageLength
   */
  @Min(value = 1) @Max(value = 2048) 
  @Schema(name = "maxMessageLength", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxMessageLength")
  public @Nullable Integer getMaxMessageLength() {
    return maxMessageLength;
  }

  public void setMaxMessageLength(@Nullable Integer maxMessageLength) {
    this.maxMessageLength = maxMessageLength;
  }

  public UpdateChannelSettingsRequest maxMembers(@Nullable Integer maxMembers) {
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

  public UpdateChannelSettingsRequest permissions(@Nullable ChannelPermissions permissions) {
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

  public UpdateChannelSettingsRequest moderators(List<UUID> moderators) {
    this.moderators = moderators;
    return this;
  }

  public UpdateChannelSettingsRequest addModeratorsItem(UUID moderatorsItem) {
    if (this.moderators == null) {
      this.moderators = new ArrayList<>();
    }
    this.moderators.add(moderatorsItem);
    return this;
  }

  /**
   * Get moderators
   * @return moderators
   */
  @Valid 
  @Schema(name = "moderators", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("moderators")
  public List<UUID> getModerators() {
    return moderators;
  }

  public void setModerators(List<UUID> moderators) {
    this.moderators = moderators;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateChannelSettingsRequest updateChannelSettingsRequest = (UpdateChannelSettingsRequest) o;
    return Objects.equals(this.messageCooldownSeconds, updateChannelSettingsRequest.messageCooldownSeconds) &&
        Objects.equals(this.maxMessageLength, updateChannelSettingsRequest.maxMessageLength) &&
        Objects.equals(this.maxMembers, updateChannelSettingsRequest.maxMembers) &&
        Objects.equals(this.permissions, updateChannelSettingsRequest.permissions) &&
        Objects.equals(this.moderators, updateChannelSettingsRequest.moderators);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageCooldownSeconds, maxMessageLength, maxMembers, permissions, moderators);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateChannelSettingsRequest {\n");
    sb.append("    messageCooldownSeconds: ").append(toIndentedString(messageCooldownSeconds)).append("\n");
    sb.append("    maxMessageLength: ").append(toIndentedString(maxMessageLength)).append("\n");
    sb.append("    maxMembers: ").append(toIndentedString(maxMembers)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    moderators: ").append(toIndentedString(moderators)).append("\n");
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

