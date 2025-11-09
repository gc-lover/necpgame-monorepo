package com.necpgame.partymodule.model;

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
 * ReadyCheckRequest
 */


public class ReadyCheckRequest {

  private Integer durationSeconds = 30;

  private @Nullable Boolean autoNotify;

  public ReadyCheckRequest durationSeconds(Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
    return this;
  }

  /**
   * Get durationSeconds
   * @return durationSeconds
   */
  
  @Schema(name = "durationSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationSeconds")
  public Integer getDurationSeconds() {
    return durationSeconds;
  }

  public void setDurationSeconds(Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public ReadyCheckRequest autoNotify(@Nullable Boolean autoNotify) {
    this.autoNotify = autoNotify;
    return this;
  }

  /**
   * Get autoNotify
   * @return autoNotify
   */
  
  @Schema(name = "autoNotify", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoNotify")
  public @Nullable Boolean getAutoNotify() {
    return autoNotify;
  }

  public void setAutoNotify(@Nullable Boolean autoNotify) {
    this.autoNotify = autoNotify;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadyCheckRequest readyCheckRequest = (ReadyCheckRequest) o;
    return Objects.equals(this.durationSeconds, readyCheckRequest.durationSeconds) &&
        Objects.equals(this.autoNotify, readyCheckRequest.autoNotify);
  }

  @Override
  public int hashCode() {
    return Objects.hash(durationSeconds, autoNotify);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadyCheckRequest {\n");
    sb.append("    durationSeconds: ").append(toIndentedString(durationSeconds)).append("\n");
    sb.append("    autoNotify: ").append(toIndentedString(autoNotify)).append("\n");
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

