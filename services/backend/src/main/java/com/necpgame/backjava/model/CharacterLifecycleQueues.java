package com.necpgame.backjava.model;

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
 * CharacterLifecycleQueues
 */


public class CharacterLifecycleQueues {

  private @Nullable String restoreQueue;

  private @Nullable String deleteExpiryCron;

  private @Nullable String sessionInvalidation;

  public CharacterLifecycleQueues restoreQueue(@Nullable String restoreQueue) {
    this.restoreQueue = restoreQueue;
    return this;
  }

  /**
   * Get restoreQueue
   * @return restoreQueue
   */
  
  @Schema(name = "restoreQueue", example = "character-restore-queue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("restoreQueue")
  public @Nullable String getRestoreQueue() {
    return restoreQueue;
  }

  public void setRestoreQueue(@Nullable String restoreQueue) {
    this.restoreQueue = restoreQueue;
  }

  public CharacterLifecycleQueues deleteExpiryCron(@Nullable String deleteExpiryCron) {
    this.deleteExpiryCron = deleteExpiryCron;
    return this;
  }

  /**
   * Get deleteExpiryCron
   * @return deleteExpiryCron
   */
  
  @Schema(name = "deleteExpiryCron", example = "character-soft-delete-expirer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deleteExpiryCron")
  public @Nullable String getDeleteExpiryCron() {
    return deleteExpiryCron;
  }

  public void setDeleteExpiryCron(@Nullable String deleteExpiryCron) {
    this.deleteExpiryCron = deleteExpiryCron;
  }

  public CharacterLifecycleQueues sessionInvalidation(@Nullable String sessionInvalidation) {
    this.sessionInvalidation = sessionInvalidation;
    return this;
  }

  /**
   * Get sessionInvalidation
   * @return sessionInvalidation
   */
  
  @Schema(name = "sessionInvalidation", example = "session-service.invalidate-character", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessionInvalidation")
  public @Nullable String getSessionInvalidation() {
    return sessionInvalidation;
  }

  public void setSessionInvalidation(@Nullable String sessionInvalidation) {
    this.sessionInvalidation = sessionInvalidation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterLifecycleQueues characterLifecycleQueues = (CharacterLifecycleQueues) o;
    return Objects.equals(this.restoreQueue, characterLifecycleQueues.restoreQueue) &&
        Objects.equals(this.deleteExpiryCron, characterLifecycleQueues.deleteExpiryCron) &&
        Objects.equals(this.sessionInvalidation, characterLifecycleQueues.sessionInvalidation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(restoreQueue, deleteExpiryCron, sessionInvalidation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterLifecycleQueues {\n");
    sb.append("    restoreQueue: ").append(toIndentedString(restoreQueue)).append("\n");
    sb.append("    deleteExpiryCron: ").append(toIndentedString(deleteExpiryCron)).append("\n");
    sb.append("    sessionInvalidation: ").append(toIndentedString(sessionInvalidation)).append("\n");
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

