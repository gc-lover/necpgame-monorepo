package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.DailyQuest;
import com.necpgame.backjava.model.RegionalQuest;
import com.necpgame.backjava.model.WeeklyQuest;
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
 * GetAvailableRegionalQuests200Response
 */

@JsonTypeName("getAvailableRegionalQuests_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetAvailableRegionalQuests200Response {

  @Valid
  private List<@Valid DailyQuest> dailyQuests = new ArrayList<>();

  @Valid
  private List<@Valid WeeklyQuest> weeklyQuests = new ArrayList<>();

  @Valid
  private List<@Valid RegionalQuest> regionalQuests = new ArrayList<>();

  public GetAvailableRegionalQuests200Response dailyQuests(List<@Valid DailyQuest> dailyQuests) {
    this.dailyQuests = dailyQuests;
    return this;
  }

  public GetAvailableRegionalQuests200Response addDailyQuestsItem(DailyQuest dailyQuestsItem) {
    if (this.dailyQuests == null) {
      this.dailyQuests = new ArrayList<>();
    }
    this.dailyQuests.add(dailyQuestsItem);
    return this;
  }

  /**
   * Get dailyQuests
   * @return dailyQuests
   */
  @Valid 
  @Schema(name = "daily_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("daily_quests")
  public List<@Valid DailyQuest> getDailyQuests() {
    return dailyQuests;
  }

  public void setDailyQuests(List<@Valid DailyQuest> dailyQuests) {
    this.dailyQuests = dailyQuests;
  }

  public GetAvailableRegionalQuests200Response weeklyQuests(List<@Valid WeeklyQuest> weeklyQuests) {
    this.weeklyQuests = weeklyQuests;
    return this;
  }

  public GetAvailableRegionalQuests200Response addWeeklyQuestsItem(WeeklyQuest weeklyQuestsItem) {
    if (this.weeklyQuests == null) {
      this.weeklyQuests = new ArrayList<>();
    }
    this.weeklyQuests.add(weeklyQuestsItem);
    return this;
  }

  /**
   * Get weeklyQuests
   * @return weeklyQuests
   */
  @Valid 
  @Schema(name = "weekly_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weekly_quests")
  public List<@Valid WeeklyQuest> getWeeklyQuests() {
    return weeklyQuests;
  }

  public void setWeeklyQuests(List<@Valid WeeklyQuest> weeklyQuests) {
    this.weeklyQuests = weeklyQuests;
  }

  public GetAvailableRegionalQuests200Response regionalQuests(List<@Valid RegionalQuest> regionalQuests) {
    this.regionalQuests = regionalQuests;
    return this;
  }

  public GetAvailableRegionalQuests200Response addRegionalQuestsItem(RegionalQuest regionalQuestsItem) {
    if (this.regionalQuests == null) {
      this.regionalQuests = new ArrayList<>();
    }
    this.regionalQuests.add(regionalQuestsItem);
    return this;
  }

  /**
   * Get regionalQuests
   * @return regionalQuests
   */
  @Valid 
  @Schema(name = "regional_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regional_quests")
  public List<@Valid RegionalQuest> getRegionalQuests() {
    return regionalQuests;
  }

  public void setRegionalQuests(List<@Valid RegionalQuest> regionalQuests) {
    this.regionalQuests = regionalQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailableRegionalQuests200Response getAvailableRegionalQuests200Response = (GetAvailableRegionalQuests200Response) o;
    return Objects.equals(this.dailyQuests, getAvailableRegionalQuests200Response.dailyQuests) &&
        Objects.equals(this.weeklyQuests, getAvailableRegionalQuests200Response.weeklyQuests) &&
        Objects.equals(this.regionalQuests, getAvailableRegionalQuests200Response.regionalQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dailyQuests, weeklyQuests, regionalQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableRegionalQuests200Response {\n");
    sb.append("    dailyQuests: ").append(toIndentedString(dailyQuests)).append("\n");
    sb.append("    weeklyQuests: ").append(toIndentedString(weeklyQuests)).append("\n");
    sb.append("    regionalQuests: ").append(toIndentedString(regionalQuests)).append("\n");
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

