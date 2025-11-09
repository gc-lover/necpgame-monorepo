package com.necpgame.adminservice.model;

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
 * SendMessage429Response
 */

@JsonTypeName("sendMessage_429_response")

public class SendMessage429Response {

  private @Nullable String error;

  private @Nullable BigDecimal cooldownRemaining;

  public SendMessage429Response error(@Nullable String error) {
    this.error = error;
    return this;
  }

  /**
   * Get error
   * @return error
   */
  
  @Schema(name = "error", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error")
  public @Nullable String getError() {
    return error;
  }

  public void setError(@Nullable String error) {
    this.error = error;
  }

  public SendMessage429Response cooldownRemaining(@Nullable BigDecimal cooldownRemaining) {
    this.cooldownRemaining = cooldownRemaining;
    return this;
  }

  /**
   * Get cooldownRemaining
   * @return cooldownRemaining
   */
  @Valid 
  @Schema(name = "cooldown_remaining", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldown_remaining")
  public @Nullable BigDecimal getCooldownRemaining() {
    return cooldownRemaining;
  }

  public void setCooldownRemaining(@Nullable BigDecimal cooldownRemaining) {
    this.cooldownRemaining = cooldownRemaining;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendMessage429Response sendMessage429Response = (SendMessage429Response) o;
    return Objects.equals(this.error, sendMessage429Response.error) &&
        Objects.equals(this.cooldownRemaining, sendMessage429Response.cooldownRemaining);
  }

  @Override
  public int hashCode() {
    return Objects.hash(error, cooldownRemaining);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendMessage429Response {\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    cooldownRemaining: ").append(toIndentedString(cooldownRemaining)).append("\n");
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

