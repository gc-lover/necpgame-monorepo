package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
 * SocialInviteEvent
 */


public class SocialInviteEvent {

  private @Nullable String inviteId;

  private @Nullable String from;

  private @Nullable String context;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public SocialInviteEvent inviteId(@Nullable String inviteId) {
    this.inviteId = inviteId;
    return this;
  }

  /**
   * Get inviteId
   * @return inviteId
   */
  
  @Schema(name = "inviteId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inviteId")
  public @Nullable String getInviteId() {
    return inviteId;
  }

  public void setInviteId(@Nullable String inviteId) {
    this.inviteId = inviteId;
  }

  public SocialInviteEvent from(@Nullable String from) {
    this.from = from;
    return this;
  }

  /**
   * Get from
   * @return from
   */
  
  @Schema(name = "from", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("from")
  public @Nullable String getFrom() {
    return from;
  }

  public void setFrom(@Nullable String from) {
    this.from = from;
  }

  public SocialInviteEvent context(@Nullable String context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  
  @Schema(name = "context", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public @Nullable String getContext() {
    return context;
  }

  public void setContext(@Nullable String context) {
    this.context = context;
  }

  public SocialInviteEvent payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public SocialInviteEvent putPayloadItem(String key, Object payloadItem) {
    if (this.payload == null) {
      this.payload = new HashMap<>();
    }
    this.payload.put(key, payloadItem);
    return this;
  }

  /**
   * Get payload
   * @return payload
   */
  
  @Schema(name = "payload", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payload")
  public Map<String, Object> getPayload() {
    return payload;
  }

  public void setPayload(Map<String, Object> payload) {
    this.payload = payload;
  }

  public SocialInviteEvent expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialInviteEvent socialInviteEvent = (SocialInviteEvent) o;
    return Objects.equals(this.inviteId, socialInviteEvent.inviteId) &&
        Objects.equals(this.from, socialInviteEvent.from) &&
        Objects.equals(this.context, socialInviteEvent.context) &&
        Objects.equals(this.payload, socialInviteEvent.payload) &&
        Objects.equals(this.expiresAt, socialInviteEvent.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inviteId, from, context, payload, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialInviteEvent {\n");
    sb.append("    inviteId: ").append(toIndentedString(inviteId)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

