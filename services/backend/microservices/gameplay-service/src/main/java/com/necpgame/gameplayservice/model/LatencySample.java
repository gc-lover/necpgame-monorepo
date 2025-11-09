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
 * LatencySample
 */


public class LatencySample {

  private Integer ms;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime recordedAt;

  private @Nullable String region;

  public LatencySample() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LatencySample(Integer ms) {
    this.ms = ms;
  }

  public LatencySample ms(Integer ms) {
    this.ms = ms;
    return this;
  }

  /**
   * Get ms
   * minimum: 0
   * @return ms
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "ms", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ms")
  public Integer getMs() {
    return ms;
  }

  public void setMs(Integer ms) {
    this.ms = ms;
  }

  public LatencySample recordedAt(@Nullable OffsetDateTime recordedAt) {
    this.recordedAt = recordedAt;
    return this;
  }

  /**
   * Get recordedAt
   * @return recordedAt
   */
  @Valid 
  @Schema(name = "recordedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recordedAt")
  public @Nullable OffsetDateTime getRecordedAt() {
    return recordedAt;
  }

  public void setRecordedAt(@Nullable OffsetDateTime recordedAt) {
    this.recordedAt = recordedAt;
  }

  public LatencySample region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LatencySample latencySample = (LatencySample) o;
    return Objects.equals(this.ms, latencySample.ms) &&
        Objects.equals(this.recordedAt, latencySample.recordedAt) &&
        Objects.equals(this.region, latencySample.region);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ms, recordedAt, region);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LatencySample {\n");
    sb.append("    ms: ").append(toIndentedString(ms)).append("\n");
    sb.append("    recordedAt: ").append(toIndentedString(recordedAt)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
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

