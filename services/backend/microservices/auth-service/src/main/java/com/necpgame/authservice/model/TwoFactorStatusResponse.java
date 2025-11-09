package com.necpgame.authservice.model;

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
 * TwoFactorStatusResponse
 */


public class TwoFactorStatusResponse {

  private @Nullable Boolean enabled;

  private @Nullable Integer backupCodesRemaining;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastConfirmedAt;

  private @Nullable String enforcementPolicy;

  public TwoFactorStatusResponse enabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public @Nullable Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
  }

  public TwoFactorStatusResponse backupCodesRemaining(@Nullable Integer backupCodesRemaining) {
    this.backupCodesRemaining = backupCodesRemaining;
    return this;
  }

  /**
   * Get backupCodesRemaining
   * @return backupCodesRemaining
   */
  
  @Schema(name = "backup_codes_remaining", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("backup_codes_remaining")
  public @Nullable Integer getBackupCodesRemaining() {
    return backupCodesRemaining;
  }

  public void setBackupCodesRemaining(@Nullable Integer backupCodesRemaining) {
    this.backupCodesRemaining = backupCodesRemaining;
  }

  public TwoFactorStatusResponse lastConfirmedAt(@Nullable OffsetDateTime lastConfirmedAt) {
    this.lastConfirmedAt = lastConfirmedAt;
    return this;
  }

  /**
   * Get lastConfirmedAt
   * @return lastConfirmedAt
   */
  @Valid 
  @Schema(name = "last_confirmed_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_confirmed_at")
  public @Nullable OffsetDateTime getLastConfirmedAt() {
    return lastConfirmedAt;
  }

  public void setLastConfirmedAt(@Nullable OffsetDateTime lastConfirmedAt) {
    this.lastConfirmedAt = lastConfirmedAt;
  }

  public TwoFactorStatusResponse enforcementPolicy(@Nullable String enforcementPolicy) {
    this.enforcementPolicy = enforcementPolicy;
    return this;
  }

  /**
   * optional | recommended | required
   * @return enforcementPolicy
   */
  
  @Schema(name = "enforcement_policy", description = "optional | recommended | required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enforcement_policy")
  public @Nullable String getEnforcementPolicy() {
    return enforcementPolicy;
  }

  public void setEnforcementPolicy(@Nullable String enforcementPolicy) {
    this.enforcementPolicy = enforcementPolicy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TwoFactorStatusResponse twoFactorStatusResponse = (TwoFactorStatusResponse) o;
    return Objects.equals(this.enabled, twoFactorStatusResponse.enabled) &&
        Objects.equals(this.backupCodesRemaining, twoFactorStatusResponse.backupCodesRemaining) &&
        Objects.equals(this.lastConfirmedAt, twoFactorStatusResponse.lastConfirmedAt) &&
        Objects.equals(this.enforcementPolicy, twoFactorStatusResponse.enforcementPolicy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, backupCodesRemaining, lastConfirmedAt, enforcementPolicy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TwoFactorStatusResponse {\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    backupCodesRemaining: ").append(toIndentedString(backupCodesRemaining)).append("\n");
    sb.append("    lastConfirmedAt: ").append(toIndentedString(lastConfirmedAt)).append("\n");
    sb.append("    enforcementPolicy: ").append(toIndentedString(enforcementPolicy)).append("\n");
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

