package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.ActionXpBatchRequestContext;
import com.necpgame.worldservice.model.ActionXpEntry;
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
 * ActionXpBatchRequest
 */


public class ActionXpBatchRequest {

  private UUID characterId;

  private @Nullable UUID traceId;

  private @Nullable UUID sessionId;

  private @Nullable ActionXpBatchRequestContext context;

  @Valid
  private List<@Valid ActionXpEntry> entries = new ArrayList<>();

  public ActionXpBatchRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActionXpBatchRequest(UUID characterId, List<@Valid ActionXpEntry> entries) {
    this.characterId = characterId;
    this.entries = entries;
  }

  public ActionXpBatchRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public ActionXpBatchRequest traceId(@Nullable UUID traceId) {
    this.traceId = traceId;
    return this;
  }

  /**
   * Get traceId
   * @return traceId
   */
  @Valid 
  @Schema(name = "traceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("traceId")
  public @Nullable UUID getTraceId() {
    return traceId;
  }

  public void setTraceId(@Nullable UUID traceId) {
    this.traceId = traceId;
  }

  public ActionXpBatchRequest sessionId(@Nullable UUID sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  @Valid 
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessionId")
  public @Nullable UUID getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable UUID sessionId) {
    this.sessionId = sessionId;
  }

  public ActionXpBatchRequest context(@Nullable ActionXpBatchRequestContext context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  @Valid 
  @Schema(name = "context", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public @Nullable ActionXpBatchRequestContext getContext() {
    return context;
  }

  public void setContext(@Nullable ActionXpBatchRequestContext context) {
    this.context = context;
  }

  public ActionXpBatchRequest entries(List<@Valid ActionXpEntry> entries) {
    this.entries = entries;
    return this;
  }

  public ActionXpBatchRequest addEntriesItem(ActionXpEntry entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    this.entries.add(entriesItem);
    return this;
  }

  /**
   * Get entries
   * @return entries
   */
  @NotNull @Valid @Size(min = 1, max = 50) 
  @Schema(name = "entries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("entries")
  public List<@Valid ActionXpEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<@Valid ActionXpEntry> entries) {
    this.entries = entries;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionXpBatchRequest actionXpBatchRequest = (ActionXpBatchRequest) o;
    return Objects.equals(this.characterId, actionXpBatchRequest.characterId) &&
        Objects.equals(this.traceId, actionXpBatchRequest.traceId) &&
        Objects.equals(this.sessionId, actionXpBatchRequest.sessionId) &&
        Objects.equals(this.context, actionXpBatchRequest.context) &&
        Objects.equals(this.entries, actionXpBatchRequest.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, traceId, sessionId, context, entries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionXpBatchRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    traceId: ").append(toIndentedString(traceId)).append("\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
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

