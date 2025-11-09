package com.necpgame.socialservice.model;

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
 * GuildDisbandRequest
 */


public class GuildDisbandRequest {

  private Boolean confirm;

  private @Nullable String reason;

  public GuildDisbandRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GuildDisbandRequest(Boolean confirm) {
    this.confirm = confirm;
  }

  public GuildDisbandRequest confirm(Boolean confirm) {
    this.confirm = confirm;
    return this;
  }

  /**
   * Get confirm
   * @return confirm
   */
  @NotNull 
  @Schema(name = "confirm", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("confirm")
  public Boolean getConfirm() {
    return confirm;
  }

  public void setConfirm(Boolean confirm) {
    this.confirm = confirm;
  }

  public GuildDisbandRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildDisbandRequest guildDisbandRequest = (GuildDisbandRequest) o;
    return Objects.equals(this.confirm, guildDisbandRequest.confirm) &&
        Objects.equals(this.reason, guildDisbandRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(confirm, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildDisbandRequest {\n");
    sb.append("    confirm: ").append(toIndentedString(confirm)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

