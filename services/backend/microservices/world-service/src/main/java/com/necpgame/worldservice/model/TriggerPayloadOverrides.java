package com.necpgame.worldservice.model;

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
 * TriggerPayloadOverrides
 */

@JsonTypeName("TriggerPayload_overrides")

public class TriggerPayloadOverrides {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startTimeUtc;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endTimeUtc;

  private Boolean forceActivation = false;

  public TriggerPayloadOverrides startTimeUtc(@Nullable OffsetDateTime startTimeUtc) {
    this.startTimeUtc = startTimeUtc;
    return this;
  }

  /**
   * Get startTimeUtc
   * @return startTimeUtc
   */
  @Valid 
  @Schema(name = "startTimeUtc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startTimeUtc")
  public @Nullable OffsetDateTime getStartTimeUtc() {
    return startTimeUtc;
  }

  public void setStartTimeUtc(@Nullable OffsetDateTime startTimeUtc) {
    this.startTimeUtc = startTimeUtc;
  }

  public TriggerPayloadOverrides endTimeUtc(@Nullable OffsetDateTime endTimeUtc) {
    this.endTimeUtc = endTimeUtc;
    return this;
  }

  /**
   * Get endTimeUtc
   * @return endTimeUtc
   */
  @Valid 
  @Schema(name = "endTimeUtc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endTimeUtc")
  public @Nullable OffsetDateTime getEndTimeUtc() {
    return endTimeUtc;
  }

  public void setEndTimeUtc(@Nullable OffsetDateTime endTimeUtc) {
    this.endTimeUtc = endTimeUtc;
  }

  public TriggerPayloadOverrides forceActivation(Boolean forceActivation) {
    this.forceActivation = forceActivation;
    return this;
  }

  /**
   * Get forceActivation
   * @return forceActivation
   */
  
  @Schema(name = "forceActivation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("forceActivation")
  public Boolean getForceActivation() {
    return forceActivation;
  }

  public void setForceActivation(Boolean forceActivation) {
    this.forceActivation = forceActivation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerPayloadOverrides triggerPayloadOverrides = (TriggerPayloadOverrides) o;
    return Objects.equals(this.startTimeUtc, triggerPayloadOverrides.startTimeUtc) &&
        Objects.equals(this.endTimeUtc, triggerPayloadOverrides.endTimeUtc) &&
        Objects.equals(this.forceActivation, triggerPayloadOverrides.forceActivation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startTimeUtc, endTimeUtc, forceActivation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerPayloadOverrides {\n");
    sb.append("    startTimeUtc: ").append(toIndentedString(startTimeUtc)).append("\n");
    sb.append("    endTimeUtc: ").append(toIndentedString(endTimeUtc)).append("\n");
    sb.append("    forceActivation: ").append(toIndentedString(forceActivation)).append("\n");
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

