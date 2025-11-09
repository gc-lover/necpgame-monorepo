package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.WeeklyQuest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * GetWeeklyQuests200Response
 */

@JsonTypeName("getWeeklyQuests_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetWeeklyQuests200Response {

  @Valid
  private List<@Valid WeeklyQuest> quests = new ArrayList<>();

  private @Nullable Integer slotsAvailable;

  private @Nullable Integer slotsUsed;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime resetsAt;

  public GetWeeklyQuests200Response quests(List<@Valid WeeklyQuest> quests) {
    this.quests = quests;
    return this;
  }

  public GetWeeklyQuests200Response addQuestsItem(WeeklyQuest questsItem) {
    if (this.quests == null) {
      this.quests = new ArrayList<>();
    }
    this.quests.add(questsItem);
    return this;
  }

  /**
   * Get quests
   * @return quests
   */
  @Valid 
  @Schema(name = "quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quests")
  public List<@Valid WeeklyQuest> getQuests() {
    return quests;
  }

  public void setQuests(List<@Valid WeeklyQuest> quests) {
    this.quests = quests;
  }

  public GetWeeklyQuests200Response slotsAvailable(@Nullable Integer slotsAvailable) {
    this.slotsAvailable = slotsAvailable;
    return this;
  }

  /**
   * Get slotsAvailable
   * @return slotsAvailable
   */
  
  @Schema(name = "slots_available", example = "3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slots_available")
  public @Nullable Integer getSlotsAvailable() {
    return slotsAvailable;
  }

  public void setSlotsAvailable(@Nullable Integer slotsAvailable) {
    this.slotsAvailable = slotsAvailable;
  }

  public GetWeeklyQuests200Response slotsUsed(@Nullable Integer slotsUsed) {
    this.slotsUsed = slotsUsed;
    return this;
  }

  /**
   * Get slotsUsed
   * @return slotsUsed
   */
  
  @Schema(name = "slots_used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slots_used")
  public @Nullable Integer getSlotsUsed() {
    return slotsUsed;
  }

  public void setSlotsUsed(@Nullable Integer slotsUsed) {
    this.slotsUsed = slotsUsed;
  }

  public GetWeeklyQuests200Response resetsAt(@Nullable OffsetDateTime resetsAt) {
    this.resetsAt = resetsAt;
    return this;
  }

  /**
   * Get resetsAt
   * @return resetsAt
   */
  @Valid 
  @Schema(name = "resets_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resets_at")
  public @Nullable OffsetDateTime getResetsAt() {
    return resetsAt;
  }

  public void setResetsAt(@Nullable OffsetDateTime resetsAt) {
    this.resetsAt = resetsAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetWeeklyQuests200Response getWeeklyQuests200Response = (GetWeeklyQuests200Response) o;
    return Objects.equals(this.quests, getWeeklyQuests200Response.quests) &&
        Objects.equals(this.slotsAvailable, getWeeklyQuests200Response.slotsAvailable) &&
        Objects.equals(this.slotsUsed, getWeeklyQuests200Response.slotsUsed) &&
        Objects.equals(this.resetsAt, getWeeklyQuests200Response.resetsAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quests, slotsAvailable, slotsUsed, resetsAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetWeeklyQuests200Response {\n");
    sb.append("    quests: ").append(toIndentedString(quests)).append("\n");
    sb.append("    slotsAvailable: ").append(toIndentedString(slotsAvailable)).append("\n");
    sb.append("    slotsUsed: ").append(toIndentedString(slotsUsed)).append("\n");
    sb.append("    resetsAt: ").append(toIndentedString(resetsAt)).append("\n");
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

