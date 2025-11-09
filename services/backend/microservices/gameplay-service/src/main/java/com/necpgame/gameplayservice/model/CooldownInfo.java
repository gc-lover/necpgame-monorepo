package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * CooldownInfo
 */


public class CooldownInfo {

  private @Nullable String abilityId;

  private @Nullable Integer remainingSeconds;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUsedAt;

  public CooldownInfo abilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
    return this;
  }

  /**
   * Get abilityId
   * @return abilityId
   */
  
  @Schema(name = "abilityId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilityId")
  public @Nullable String getAbilityId() {
    return abilityId;
  }

  public void setAbilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
  }

  public CooldownInfo remainingSeconds(@Nullable Integer remainingSeconds) {
    this.remainingSeconds = remainingSeconds;
    return this;
  }

  /**
   * Get remainingSeconds
   * @return remainingSeconds
   */
  
  @Schema(name = "remainingSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remainingSeconds")
  public @Nullable Integer getRemainingSeconds() {
    return remainingSeconds;
  }

  public void setRemainingSeconds(@Nullable Integer remainingSeconds) {
    this.remainingSeconds = remainingSeconds;
  }

  public CooldownInfo lastUsedAt(@Nullable OffsetDateTime lastUsedAt) {
    this.lastUsedAt = lastUsedAt;
    return this;
  }

  /**
   * Get lastUsedAt
   * @return lastUsedAt
   */
  @Valid 
  @Schema(name = "lastUsedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUsedAt")
  public @Nullable OffsetDateTime getLastUsedAt() {
    return lastUsedAt;
  }

  public void setLastUsedAt(@Nullable OffsetDateTime lastUsedAt) {
    this.lastUsedAt = lastUsedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CooldownInfo cooldownInfo = (CooldownInfo) o;
    return Objects.equals(this.abilityId, cooldownInfo.abilityId) &&
        Objects.equals(this.remainingSeconds, cooldownInfo.remainingSeconds) &&
        Objects.equals(this.lastUsedAt, cooldownInfo.lastUsedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilityId, remainingSeconds, lastUsedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CooldownInfo {\n");
    sb.append("    abilityId: ").append(toIndentedString(abilityId)).append("\n");
    sb.append("    remainingSeconds: ").append(toIndentedString(remainingSeconds)).append("\n");
    sb.append("    lastUsedAt: ").append(toIndentedString(lastUsedAt)).append("\n");
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

