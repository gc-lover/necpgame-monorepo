package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NPCPersonality
 */


public class NPCPersonality {

  private @Nullable String npcId;

  @Valid
  private List<String> traits = new ArrayList<>();

  @Valid
  private Map<String, Integer> values = new HashMap<>();

  @Valid
  private List<String> quirks = new ArrayList<>();

  private @Nullable String speechPattern;

  private @Nullable Object emotionalRange;

  public NPCPersonality npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public NPCPersonality traits(List<String> traits) {
    this.traits = traits;
    return this;
  }

  public NPCPersonality addTraitsItem(String traitsItem) {
    if (this.traits == null) {
      this.traits = new ArrayList<>();
    }
    this.traits.add(traitsItem);
    return this;
  }

  /**
   * Get traits
   * @return traits
   */
  
  @Schema(name = "traits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("traits")
  public List<String> getTraits() {
    return traits;
  }

  public void setTraits(List<String> traits) {
    this.traits = traits;
  }

  public NPCPersonality values(Map<String, Integer> values) {
    this.values = values;
    return this;
  }

  public NPCPersonality putValuesItem(String key, Integer valuesItem) {
    if (this.values == null) {
      this.values = new HashMap<>();
    }
    this.values.put(key, valuesItem);
    return this;
  }

  /**
   * Get values
   * @return values
   */
  
  @Schema(name = "values", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("values")
  public Map<String, Integer> getValues() {
    return values;
  }

  public void setValues(Map<String, Integer> values) {
    this.values = values;
  }

  public NPCPersonality quirks(List<String> quirks) {
    this.quirks = quirks;
    return this;
  }

  public NPCPersonality addQuirksItem(String quirksItem) {
    if (this.quirks == null) {
      this.quirks = new ArrayList<>();
    }
    this.quirks.add(quirksItem);
    return this;
  }

  /**
   * Get quirks
   * @return quirks
   */
  
  @Schema(name = "quirks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quirks")
  public List<String> getQuirks() {
    return quirks;
  }

  public void setQuirks(List<String> quirks) {
    this.quirks = quirks;
  }

  public NPCPersonality speechPattern(@Nullable String speechPattern) {
    this.speechPattern = speechPattern;
    return this;
  }

  /**
   * Get speechPattern
   * @return speechPattern
   */
  
  @Schema(name = "speech_pattern", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("speech_pattern")
  public @Nullable String getSpeechPattern() {
    return speechPattern;
  }

  public void setSpeechPattern(@Nullable String speechPattern) {
    this.speechPattern = speechPattern;
  }

  public NPCPersonality emotionalRange(@Nullable Object emotionalRange) {
    this.emotionalRange = emotionalRange;
    return this;
  }

  /**
   * Get emotionalRange
   * @return emotionalRange
   */
  
  @Schema(name = "emotional_range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("emotional_range")
  public @Nullable Object getEmotionalRange() {
    return emotionalRange;
  }

  public void setEmotionalRange(@Nullable Object emotionalRange) {
    this.emotionalRange = emotionalRange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NPCPersonality npCPersonality = (NPCPersonality) o;
    return Objects.equals(this.npcId, npCPersonality.npcId) &&
        Objects.equals(this.traits, npCPersonality.traits) &&
        Objects.equals(this.values, npCPersonality.values) &&
        Objects.equals(this.quirks, npCPersonality.quirks) &&
        Objects.equals(this.speechPattern, npCPersonality.speechPattern) &&
        Objects.equals(this.emotionalRange, npCPersonality.emotionalRange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, traits, values, quirks, speechPattern, emotionalRange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NPCPersonality {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    traits: ").append(toIndentedString(traits)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
    sb.append("    quirks: ").append(toIndentedString(quirks)).append("\n");
    sb.append("    speechPattern: ").append(toIndentedString(speechPattern)).append("\n");
    sb.append("    emotionalRange: ").append(toIndentedString(emotionalRange)).append("\n");
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

