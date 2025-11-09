package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.BoostStatus;
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
 * BoostActivationResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class BoostActivationResponse {

  private @Nullable BoostStatus boost;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public BoostActivationResponse boost(@Nullable BoostStatus boost) {
    this.boost = boost;
    return this;
  }

  /**
   * Get boost
   * @return boost
   */
  @Valid 
  @Schema(name = "boost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boost")
  public @Nullable BoostStatus getBoost() {
    return boost;
  }

  public void setBoost(@Nullable BoostStatus boost) {
    this.boost = boost;
  }

  public BoostActivationResponse expiresAt(@Nullable OffsetDateTime expiresAt) {
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
    BoostActivationResponse boostActivationResponse = (BoostActivationResponse) o;
    return Objects.equals(this.boost, boostActivationResponse.boost) &&
        Objects.equals(this.expiresAt, boostActivationResponse.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(boost, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoostActivationResponse {\n");
    sb.append("    boost: ").append(toIndentedString(boost)).append("\n");
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

