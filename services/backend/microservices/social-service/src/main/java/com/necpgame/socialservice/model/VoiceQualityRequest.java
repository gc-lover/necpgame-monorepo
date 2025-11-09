package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * VoiceQualityRequest
 */


public class VoiceQualityRequest {

  /**
   * Gets or Sets preset
   */
  public enum PresetEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    ULTRA("ultra");

    private final String value;

    PresetEnum(String value) {
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
    public static PresetEnum fromValue(String value) {
      for (PresetEnum b : PresetEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PresetEnum preset;

  private @Nullable Integer bitrateKbps;

  private @Nullable Integer sampleRateHz;

  private @Nullable Integer maxVideoStreams;

  public VoiceQualityRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceQualityRequest(PresetEnum preset) {
    this.preset = preset;
  }

  public VoiceQualityRequest preset(PresetEnum preset) {
    this.preset = preset;
    return this;
  }

  /**
   * Get preset
   * @return preset
   */
  @NotNull 
  @Schema(name = "preset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("preset")
  public PresetEnum getPreset() {
    return preset;
  }

  public void setPreset(PresetEnum preset) {
    this.preset = preset;
  }

  public VoiceQualityRequest bitrateKbps(@Nullable Integer bitrateKbps) {
    this.bitrateKbps = bitrateKbps;
    return this;
  }

  /**
   * Get bitrateKbps
   * @return bitrateKbps
   */
  
  @Schema(name = "bitrateKbps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bitrateKbps")
  public @Nullable Integer getBitrateKbps() {
    return bitrateKbps;
  }

  public void setBitrateKbps(@Nullable Integer bitrateKbps) {
    this.bitrateKbps = bitrateKbps;
  }

  public VoiceQualityRequest sampleRateHz(@Nullable Integer sampleRateHz) {
    this.sampleRateHz = sampleRateHz;
    return this;
  }

  /**
   * Get sampleRateHz
   * @return sampleRateHz
   */
  
  @Schema(name = "sampleRateHz", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sampleRateHz")
  public @Nullable Integer getSampleRateHz() {
    return sampleRateHz;
  }

  public void setSampleRateHz(@Nullable Integer sampleRateHz) {
    this.sampleRateHz = sampleRateHz;
  }

  public VoiceQualityRequest maxVideoStreams(@Nullable Integer maxVideoStreams) {
    this.maxVideoStreams = maxVideoStreams;
    return this;
  }

  /**
   * Get maxVideoStreams
   * @return maxVideoStreams
   */
  
  @Schema(name = "maxVideoStreams", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxVideoStreams")
  public @Nullable Integer getMaxVideoStreams() {
    return maxVideoStreams;
  }

  public void setMaxVideoStreams(@Nullable Integer maxVideoStreams) {
    this.maxVideoStreams = maxVideoStreams;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceQualityRequest voiceQualityRequest = (VoiceQualityRequest) o;
    return Objects.equals(this.preset, voiceQualityRequest.preset) &&
        Objects.equals(this.bitrateKbps, voiceQualityRequest.bitrateKbps) &&
        Objects.equals(this.sampleRateHz, voiceQualityRequest.sampleRateHz) &&
        Objects.equals(this.maxVideoStreams, voiceQualityRequest.maxVideoStreams);
  }

  @Override
  public int hashCode() {
    return Objects.hash(preset, bitrateKbps, sampleRateHz, maxVideoStreams);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceQualityRequest {\n");
    sb.append("    preset: ").append(toIndentedString(preset)).append("\n");
    sb.append("    bitrateKbps: ").append(toIndentedString(bitrateKbps)).append("\n");
    sb.append("    sampleRateHz: ").append(toIndentedString(sampleRateHz)).append("\n");
    sb.append("    maxVideoStreams: ").append(toIndentedString(maxVideoStreams)).append("\n");
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

