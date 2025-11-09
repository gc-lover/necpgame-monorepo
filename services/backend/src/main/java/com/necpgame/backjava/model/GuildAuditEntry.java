package com.necpgame.backjava.model;

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
 * GuildAuditEntry
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildAuditEntry {

  private @Nullable String entryId;

  private @Nullable String actorId;

  private @Nullable String action;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public GuildAuditEntry entryId(@Nullable String entryId) {
    this.entryId = entryId;
    return this;
  }

  /**
   * Get entryId
   * @return entryId
   */
  
  @Schema(name = "entryId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entryId")
  public @Nullable String getEntryId() {
    return entryId;
  }

  public void setEntryId(@Nullable String entryId) {
    this.entryId = entryId;
  }

  public GuildAuditEntry actorId(@Nullable String actorId) {
    this.actorId = actorId;
    return this;
  }

  /**
   * Get actorId
   * @return actorId
   */
  
  @Schema(name = "actorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actorId")
  public @Nullable String getActorId() {
    return actorId;
  }

  public void setActorId(@Nullable String actorId) {
    this.actorId = actorId;
  }

  public GuildAuditEntry action(@Nullable String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable String getAction() {
    return action;
  }

  public void setAction(@Nullable String action) {
    this.action = action;
  }

  public GuildAuditEntry payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public GuildAuditEntry putPayloadItem(String key, Object payloadItem) {
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

  public GuildAuditEntry createdAt(@Nullable OffsetDateTime createdAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildAuditEntry guildAuditEntry = (GuildAuditEntry) o;
    return Objects.equals(this.entryId, guildAuditEntry.entryId) &&
        Objects.equals(this.actorId, guildAuditEntry.actorId) &&
        Objects.equals(this.action, guildAuditEntry.action) &&
        Objects.equals(this.payload, guildAuditEntry.payload) &&
        Objects.equals(this.createdAt, guildAuditEntry.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryId, actorId, action, payload, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildAuditEntry {\n");
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
    sb.append("    actorId: ").append(toIndentedString(actorId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

