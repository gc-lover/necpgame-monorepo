package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * NPCLifecycleLifecycleStagesInner
 */

@JsonTypeName("NPCLifecycle_lifecycle_stages_inner")

public class NPCLifecycleLifecycleStagesInner {

  /**
   * Gets or Sets stage
   */
  public enum StageEnum {
    INTRODUCTION("INTRODUCTION"),
    
    ACTIVE("ACTIVE"),
    
    MAJOR_ROLE("MAJOR_ROLE"),
    
    RETIRED("RETIRED"),
    
    DECEASED("DECEASED"),
    
    UNKNOWN("UNKNOWN");

    private final String value;

    StageEnum(String value) {
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
    public static StageEnum fromValue(String value) {
      for (StageEnum b : StageEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StageEnum stage;

  private @Nullable Integer year;

  private @Nullable String location;

  @Valid
  private List<String> questsInvolved = new ArrayList<>();

  public NPCLifecycleLifecycleStagesInner stage(@Nullable StageEnum stage) {
    this.stage = stage;
    return this;
  }

  /**
   * Get stage
   * @return stage
   */
  
  @Schema(name = "stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage")
  public @Nullable StageEnum getStage() {
    return stage;
  }

  public void setStage(@Nullable StageEnum stage) {
    this.stage = stage;
  }

  public NPCLifecycleLifecycleStagesInner year(@Nullable Integer year) {
    this.year = year;
    return this;
  }

  /**
   * Get year
   * @return year
   */
  
  @Schema(name = "year", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("year")
  public @Nullable Integer getYear() {
    return year;
  }

  public void setYear(@Nullable Integer year) {
    this.year = year;
  }

  public NPCLifecycleLifecycleStagesInner location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public NPCLifecycleLifecycleStagesInner questsInvolved(List<String> questsInvolved) {
    this.questsInvolved = questsInvolved;
    return this;
  }

  public NPCLifecycleLifecycleStagesInner addQuestsInvolvedItem(String questsInvolvedItem) {
    if (this.questsInvolved == null) {
      this.questsInvolved = new ArrayList<>();
    }
    this.questsInvolved.add(questsInvolvedItem);
    return this;
  }

  /**
   * Get questsInvolved
   * @return questsInvolved
   */
  
  @Schema(name = "quests_involved", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quests_involved")
  public List<String> getQuestsInvolved() {
    return questsInvolved;
  }

  public void setQuestsInvolved(List<String> questsInvolved) {
    this.questsInvolved = questsInvolved;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NPCLifecycleLifecycleStagesInner npCLifecycleLifecycleStagesInner = (NPCLifecycleLifecycleStagesInner) o;
    return Objects.equals(this.stage, npCLifecycleLifecycleStagesInner.stage) &&
        Objects.equals(this.year, npCLifecycleLifecycleStagesInner.year) &&
        Objects.equals(this.location, npCLifecycleLifecycleStagesInner.location) &&
        Objects.equals(this.questsInvolved, npCLifecycleLifecycleStagesInner.questsInvolved);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stage, year, location, questsInvolved);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NPCLifecycleLifecycleStagesInner {\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    questsInvolved: ").append(toIndentedString(questsInvolved)).append("\n");
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

