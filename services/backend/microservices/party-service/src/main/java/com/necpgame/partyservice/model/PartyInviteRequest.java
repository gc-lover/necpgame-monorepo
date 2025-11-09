package com.necpgame.partyservice.model;

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
 * PartyInviteRequest
 */


public class PartyInviteRequest {

  private String targetId;

  private @Nullable String message;

  private @Nullable Boolean crossPlatform;

  private @Nullable Boolean autoJoin;

  private @Nullable Boolean notify;

  public PartyInviteRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PartyInviteRequest(String targetId) {
    this.targetId = targetId;
  }

  public PartyInviteRequest targetId(String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  @NotNull 
  @Schema(name = "targetId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetId")
  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public PartyInviteRequest message(@Nullable String message) {
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

  public PartyInviteRequest crossPlatform(@Nullable Boolean crossPlatform) {
    this.crossPlatform = crossPlatform;
    return this;
  }

  /**
   * Get crossPlatform
   * @return crossPlatform
   */
  
  @Schema(name = "crossPlatform", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crossPlatform")
  public @Nullable Boolean getCrossPlatform() {
    return crossPlatform;
  }

  public void setCrossPlatform(@Nullable Boolean crossPlatform) {
    this.crossPlatform = crossPlatform;
  }

  public PartyInviteRequest autoJoin(@Nullable Boolean autoJoin) {
    this.autoJoin = autoJoin;
    return this;
  }

  /**
   * Get autoJoin
   * @return autoJoin
   */
  
  @Schema(name = "autoJoin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoJoin")
  public @Nullable Boolean getAutoJoin() {
    return autoJoin;
  }

  public void setAutoJoin(@Nullable Boolean autoJoin) {
    this.autoJoin = autoJoin;
  }

  public PartyInviteRequest notify(@Nullable Boolean notify) {
    this.notify = notify;
    return this;
  }

  /**
   * Get notify
   * @return notify
   */
  
  @Schema(name = "notify", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notify")
  public @Nullable Boolean getNotify() {
    return notify;
  }

  public void setNotify(@Nullable Boolean notify) {
    this.notify = notify;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyInviteRequest partyInviteRequest = (PartyInviteRequest) o;
    return Objects.equals(this.targetId, partyInviteRequest.targetId) &&
        Objects.equals(this.message, partyInviteRequest.message) &&
        Objects.equals(this.crossPlatform, partyInviteRequest.crossPlatform) &&
        Objects.equals(this.autoJoin, partyInviteRequest.autoJoin) &&
        Objects.equals(this.notify, partyInviteRequest.notify);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetId, message, crossPlatform, autoJoin, notify);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyInviteRequest {\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    crossPlatform: ").append(toIndentedString(crossPlatform)).append("\n");
    sb.append("    autoJoin: ").append(toIndentedString(autoJoin)).append("\n");
    sb.append("    notify: ").append(toIndentedString(notify)).append("\n");
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

