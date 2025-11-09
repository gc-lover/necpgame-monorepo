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
 * VoiceSettings
 */


public class VoiceSettings {

  /**
   * Gets or Sets tier
   */
  public enum TierEnum {
    HIGH("high"),
    
    STANDARD("standard"),
    
    LOW("low");

    private final String value;

    TierEnum(String value) {
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
    public static TierEnum fromValue(String value) {
      for (TierEnum b : TierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TierEnum tier;

  private @Nullable String codec;

  private @Nullable Integer bitrateKbps;

  private @Nullable Boolean pushToTalk;

  public VoiceSettings tier(@Nullable TierEnum tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable TierEnum getTier() {
    return tier;
  }

  public void setTier(@Nullable TierEnum tier) {
    this.tier = tier;
  }

  public VoiceSettings codec(@Nullable String codec) {
    this.codec = codec;
    return this;
  }

  /**
   * Get codec
   * @return codec
   */
  
  @Schema(name = "codec", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("codec")
  public @Nullable String getCodec() {
    return codec;
  }

  public void setCodec(@Nullable String codec) {
    this.codec = codec;
  }

  public VoiceSettings bitrateKbps(@Nullable Integer bitrateKbps) {
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

  public VoiceSettings pushToTalk(@Nullable Boolean pushToTalk) {
    this.pushToTalk = pushToTalk;
    return this;
  }

  /**
   * Get pushToTalk
   * @return pushToTalk
   */
  
  @Schema(name = "pushToTalk", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pushToTalk")
  public @Nullable Boolean getPushToTalk() {
    return pushToTalk;
  }

  public void setPushToTalk(@Nullable Boolean pushToTalk) {
    this.pushToTalk = pushToTalk;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceSettings voiceSettings = (VoiceSettings) o;
    return Objects.equals(this.tier, voiceSettings.tier) &&
        Objects.equals(this.codec, voiceSettings.codec) &&
        Objects.equals(this.bitrateKbps, voiceSettings.bitrateKbps) &&
        Objects.equals(this.pushToTalk, voiceSettings.pushToTalk);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tier, codec, bitrateKbps, pushToTalk);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceSettings {\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    codec: ").append(toIndentedString(codec)).append("\n");
    sb.append("    bitrateKbps: ").append(toIndentedString(bitrateKbps)).append("\n");
    sb.append("    pushToTalk: ").append(toIndentedString(pushToTalk)).append("\n");
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

