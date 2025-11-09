package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EnterStealth200Response
 */

@JsonTypeName("enterStealth_200_response")

public class EnterStealth200Response {

  private @Nullable Boolean success;

  private @Nullable String stealthLevel;

  private @Nullable BigDecimal visibilityModifier;

  private @Nullable BigDecimal noiseModifier;

  public EnterStealth200Response success(@Nullable Boolean success) {
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

  public EnterStealth200Response stealthLevel(@Nullable String stealthLevel) {
    this.stealthLevel = stealthLevel;
    return this;
  }

  /**
   * Get stealthLevel
   * @return stealthLevel
   */
  
  @Schema(name = "stealth_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stealth_level")
  public @Nullable String getStealthLevel() {
    return stealthLevel;
  }

  public void setStealthLevel(@Nullable String stealthLevel) {
    this.stealthLevel = stealthLevel;
  }

  public EnterStealth200Response visibilityModifier(@Nullable BigDecimal visibilityModifier) {
    this.visibilityModifier = visibilityModifier;
    return this;
  }

  /**
   * Get visibilityModifier
   * @return visibilityModifier
   */
  @Valid 
  @Schema(name = "visibility_modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility_modifier")
  public @Nullable BigDecimal getVisibilityModifier() {
    return visibilityModifier;
  }

  public void setVisibilityModifier(@Nullable BigDecimal visibilityModifier) {
    this.visibilityModifier = visibilityModifier;
  }

  public EnterStealth200Response noiseModifier(@Nullable BigDecimal noiseModifier) {
    this.noiseModifier = noiseModifier;
    return this;
  }

  /**
   * Get noiseModifier
   * @return noiseModifier
   */
  @Valid 
  @Schema(name = "noise_modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("noise_modifier")
  public @Nullable BigDecimal getNoiseModifier() {
    return noiseModifier;
  }

  public void setNoiseModifier(@Nullable BigDecimal noiseModifier) {
    this.noiseModifier = noiseModifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnterStealth200Response enterStealth200Response = (EnterStealth200Response) o;
    return Objects.equals(this.success, enterStealth200Response.success) &&
        Objects.equals(this.stealthLevel, enterStealth200Response.stealthLevel) &&
        Objects.equals(this.visibilityModifier, enterStealth200Response.visibilityModifier) &&
        Objects.equals(this.noiseModifier, enterStealth200Response.noiseModifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, stealthLevel, visibilityModifier, noiseModifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnterStealth200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    stealthLevel: ").append(toIndentedString(stealthLevel)).append("\n");
    sb.append("    visibilityModifier: ").append(toIndentedString(visibilityModifier)).append("\n");
    sb.append("    noiseModifier: ").append(toIndentedString(noiseModifier)).append("\n");
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

