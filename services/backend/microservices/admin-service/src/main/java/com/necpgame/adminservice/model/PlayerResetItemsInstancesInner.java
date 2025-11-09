package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * PlayerResetItemsInstancesInner
 */

@JsonTypeName("PlayerResetItems_instances_inner")

public class PlayerResetItemsInstancesInner {

  private @Nullable String instanceId;

  private @Nullable String name;

  private @Nullable Integer attemptsUsed;

  private @Nullable Integer maxAttempts;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime resetsAt;

  public PlayerResetItemsInstancesInner instanceId(@Nullable String instanceId) {
    this.instanceId = instanceId;
    return this;
  }

  /**
   * Get instanceId
   * @return instanceId
   */
  
  @Schema(name = "instance_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instance_id")
  public @Nullable String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(@Nullable String instanceId) {
    this.instanceId = instanceId;
  }

  public PlayerResetItemsInstancesInner name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public PlayerResetItemsInstancesInner attemptsUsed(@Nullable Integer attemptsUsed) {
    this.attemptsUsed = attemptsUsed;
    return this;
  }

  /**
   * Get attemptsUsed
   * @return attemptsUsed
   */
  
  @Schema(name = "attempts_used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attempts_used")
  public @Nullable Integer getAttemptsUsed() {
    return attemptsUsed;
  }

  public void setAttemptsUsed(@Nullable Integer attemptsUsed) {
    this.attemptsUsed = attemptsUsed;
  }

  public PlayerResetItemsInstancesInner maxAttempts(@Nullable Integer maxAttempts) {
    this.maxAttempts = maxAttempts;
    return this;
  }

  /**
   * Get maxAttempts
   * @return maxAttempts
   */
  
  @Schema(name = "max_attempts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_attempts")
  public @Nullable Integer getMaxAttempts() {
    return maxAttempts;
  }

  public void setMaxAttempts(@Nullable Integer maxAttempts) {
    this.maxAttempts = maxAttempts;
  }

  public PlayerResetItemsInstancesInner resetsAt(@Nullable OffsetDateTime resetsAt) {
    this.resetsAt = resetsAt;
    return this;
  }

  /**
   * Get resetsAt
   * @return resetsAt
   */
  @Valid 
  @Schema(name = "resets_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resets_at")
  public @Nullable OffsetDateTime getResetsAt() {
    return resetsAt;
  }

  public void setResetsAt(@Nullable OffsetDateTime resetsAt) {
    this.resetsAt = resetsAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerResetItemsInstancesInner playerResetItemsInstancesInner = (PlayerResetItemsInstancesInner) o;
    return Objects.equals(this.instanceId, playerResetItemsInstancesInner.instanceId) &&
        Objects.equals(this.name, playerResetItemsInstancesInner.name) &&
        Objects.equals(this.attemptsUsed, playerResetItemsInstancesInner.attemptsUsed) &&
        Objects.equals(this.maxAttempts, playerResetItemsInstancesInner.maxAttempts) &&
        Objects.equals(this.resetsAt, playerResetItemsInstancesInner.resetsAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, name, attemptsUsed, maxAttempts, resetsAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerResetItemsInstancesInner {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    attemptsUsed: ").append(toIndentedString(attemptsUsed)).append("\n");
    sb.append("    maxAttempts: ").append(toIndentedString(maxAttempts)).append("\n");
    sb.append("    resetsAt: ").append(toIndentedString(resetsAt)).append("\n");
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

