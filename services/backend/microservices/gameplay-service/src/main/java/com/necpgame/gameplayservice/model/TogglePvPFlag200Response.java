package com.necpgame.gameplayservice.model;

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
 * TogglePvPFlag200Response
 */

@JsonTypeName("togglePvPFlag_200_response")

public class TogglePvPFlag200Response {

  private @Nullable Boolean success;

  private @Nullable Boolean pvpEnabled;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime cooldownUntil;

  public TogglePvPFlag200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public TogglePvPFlag200Response pvpEnabled(@Nullable Boolean pvpEnabled) {
    this.pvpEnabled = pvpEnabled;
    return this;
  }

  /**
   * Get pvpEnabled
   * @return pvpEnabled
   */
  
  @Schema(name = "pvp_enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pvp_enabled")
  public @Nullable Boolean getPvpEnabled() {
    return pvpEnabled;
  }

  public void setPvpEnabled(@Nullable Boolean pvpEnabled) {
    this.pvpEnabled = pvpEnabled;
  }

  public TogglePvPFlag200Response cooldownUntil(@Nullable OffsetDateTime cooldownUntil) {
    this.cooldownUntil = cooldownUntil;
    return this;
  }

  /**
   * Когда можно снова переключить
   * @return cooldownUntil
   */
  @Valid 
  @Schema(name = "cooldown_until", description = "Когда можно снова переключить", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldown_until")
  public @Nullable OffsetDateTime getCooldownUntil() {
    return cooldownUntil;
  }

  public void setCooldownUntil(@Nullable OffsetDateTime cooldownUntil) {
    this.cooldownUntil = cooldownUntil;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TogglePvPFlag200Response togglePvPFlag200Response = (TogglePvPFlag200Response) o;
    return Objects.equals(this.success, togglePvPFlag200Response.success) &&
        Objects.equals(this.pvpEnabled, togglePvPFlag200Response.pvpEnabled) &&
        Objects.equals(this.cooldownUntil, togglePvPFlag200Response.cooldownUntil);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, pvpEnabled, cooldownUntil);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TogglePvPFlag200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    pvpEnabled: ").append(toIndentedString(pvpEnabled)).append("\n");
    sb.append("    cooldownUntil: ").append(toIndentedString(cooldownUntil)).append("\n");
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

