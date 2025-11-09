package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.LatencySample;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LatencyProfile
 */


public class LatencyProfile {

  private Integer medianMs;

  private @Nullable Integer p95Ms;

  private @Nullable String region;

  /**
   * Gets or Sets bucket
   */
  public enum BucketEnum {
    LOW("LOW"),
    
    MEDIUM("MEDIUM"),
    
    HIGH("HIGH");

    private final String value;

    BucketEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static BucketEnum fromValue(String value) {
      for (BucketEnum b : BucketEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable BucketEnum bucket;

  @Valid
  private List<@Valid LatencySample> samples = new ArrayList<>();

  public LatencyProfile() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LatencyProfile(Integer medianMs) {
    this.medianMs = medianMs;
  }

  public LatencyProfile medianMs(Integer medianMs) {
    this.medianMs = medianMs;
    return this;
  }

  /**
   * Get medianMs
   * minimum: 0
   * @return medianMs
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "medianMs", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("medianMs")
  public Integer getMedianMs() {
    return medianMs;
  }

  public void setMedianMs(Integer medianMs) {
    this.medianMs = medianMs;
  }

  public LatencyProfile p95Ms(@Nullable Integer p95Ms) {
    this.p95Ms = p95Ms;
    return this;
  }

  /**
   * Get p95Ms
   * minimum: 0
   * @return p95Ms
   */
  @Min(value = 0) 
  @Schema(name = "p95Ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("p95Ms")
  public @Nullable Integer getP95Ms() {
    return p95Ms;
  }

  public void setP95Ms(@Nullable Integer p95Ms) {
    this.p95Ms = p95Ms;
  }

  public LatencyProfile region(@Nullable String region) {
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

  public LatencyProfile bucket(@Nullable BucketEnum bucket) {
    this.bucket = bucket;
    return this;
  }

  /**
   * Get bucket
   * @return bucket
   */
  
  @Schema(name = "bucket", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bucket")
  public @Nullable BucketEnum getBucket() {
    return bucket;
  }

  public void setBucket(@Nullable BucketEnum bucket) {
    this.bucket = bucket;
  }

  public LatencyProfile samples(List<@Valid LatencySample> samples) {
    this.samples = samples;
    return this;
  }

  public LatencyProfile addSamplesItem(LatencySample samplesItem) {
    if (this.samples == null) {
      this.samples = new ArrayList<>();
    }
    this.samples.add(samplesItem);
    return this;
  }

  /**
   * Get samples
   * @return samples
   */
  @Valid 
  @Schema(name = "samples", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("samples")
  public List<@Valid LatencySample> getSamples() {
    return samples;
  }

  public void setSamples(List<@Valid LatencySample> samples) {
    this.samples = samples;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LatencyProfile latencyProfile = (LatencyProfile) o;
    return Objects.equals(this.medianMs, latencyProfile.medianMs) &&
        Objects.equals(this.p95Ms, latencyProfile.p95Ms) &&
        Objects.equals(this.region, latencyProfile.region) &&
        Objects.equals(this.bucket, latencyProfile.bucket) &&
        Objects.equals(this.samples, latencyProfile.samples);
  }

  @Override
  public int hashCode() {
    return Objects.hash(medianMs, p95Ms, region, bucket, samples);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LatencyProfile {\n");
    sb.append("    medianMs: ").append(toIndentedString(medianMs)).append("\n");
    sb.append("    p95Ms: ").append(toIndentedString(p95Ms)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    bucket: ").append(toIndentedString(bucket)).append("\n");
    sb.append("    samples: ").append(toIndentedString(samples)).append("\n");
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

