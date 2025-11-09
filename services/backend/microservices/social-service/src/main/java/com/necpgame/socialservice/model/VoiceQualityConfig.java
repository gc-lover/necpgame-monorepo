package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * VoiceQualityConfig
 */


public class VoiceQualityConfig {

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

  private Integer bitrateKbps;

  private Integer sampleRateHz;

  private @Nullable Integer maxParticipants;

  @Valid
  private List<String> recommendedDevices = new ArrayList<>();

  public VoiceQualityConfig() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceQualityConfig(PresetEnum preset, Integer bitrateKbps, Integer sampleRateHz) {
    this.preset = preset;
    this.bitrateKbps = bitrateKbps;
    this.sampleRateHz = sampleRateHz;
  }

  public VoiceQualityConfig preset(PresetEnum preset) {
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

  public VoiceQualityConfig bitrateKbps(Integer bitrateKbps) {
    this.bitrateKbps = bitrateKbps;
    return this;
  }

  /**
   * Get bitrateKbps
   * @return bitrateKbps
   */
  @NotNull 
  @Schema(name = "bitrateKbps", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("bitrateKbps")
  public Integer getBitrateKbps() {
    return bitrateKbps;
  }

  public void setBitrateKbps(Integer bitrateKbps) {
    this.bitrateKbps = bitrateKbps;
  }

  public VoiceQualityConfig sampleRateHz(Integer sampleRateHz) {
    this.sampleRateHz = sampleRateHz;
    return this;
  }

  /**
   * Get sampleRateHz
   * @return sampleRateHz
   */
  @NotNull 
  @Schema(name = "sampleRateHz", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sampleRateHz")
  public Integer getSampleRateHz() {
    return sampleRateHz;
  }

  public void setSampleRateHz(Integer sampleRateHz) {
    this.sampleRateHz = sampleRateHz;
  }

  public VoiceQualityConfig maxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
    return this;
  }

  /**
   * Get maxParticipants
   * @return maxParticipants
   */
  
  @Schema(name = "maxParticipants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxParticipants")
  public @Nullable Integer getMaxParticipants() {
    return maxParticipants;
  }

  public void setMaxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
  }

  public VoiceQualityConfig recommendedDevices(List<String> recommendedDevices) {
    this.recommendedDevices = recommendedDevices;
    return this;
  }

  public VoiceQualityConfig addRecommendedDevicesItem(String recommendedDevicesItem) {
    if (this.recommendedDevices == null) {
      this.recommendedDevices = new ArrayList<>();
    }
    this.recommendedDevices.add(recommendedDevicesItem);
    return this;
  }

  /**
   * Get recommendedDevices
   * @return recommendedDevices
   */
  
  @Schema(name = "recommendedDevices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedDevices")
  public List<String> getRecommendedDevices() {
    return recommendedDevices;
  }

  public void setRecommendedDevices(List<String> recommendedDevices) {
    this.recommendedDevices = recommendedDevices;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceQualityConfig voiceQualityConfig = (VoiceQualityConfig) o;
    return Objects.equals(this.preset, voiceQualityConfig.preset) &&
        Objects.equals(this.bitrateKbps, voiceQualityConfig.bitrateKbps) &&
        Objects.equals(this.sampleRateHz, voiceQualityConfig.sampleRateHz) &&
        Objects.equals(this.maxParticipants, voiceQualityConfig.maxParticipants) &&
        Objects.equals(this.recommendedDevices, voiceQualityConfig.recommendedDevices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(preset, bitrateKbps, sampleRateHz, maxParticipants, recommendedDevices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceQualityConfig {\n");
    sb.append("    preset: ").append(toIndentedString(preset)).append("\n");
    sb.append("    bitrateKbps: ").append(toIndentedString(bitrateKbps)).append("\n");
    sb.append("    sampleRateHz: ").append(toIndentedString(sampleRateHz)).append("\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    recommendedDevices: ").append(toIndentedString(recommendedDevices)).append("\n");
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

