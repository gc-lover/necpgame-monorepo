package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.SideQuestInfo;
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
 * GetSideQuestsByPeriod200Response
 */

@JsonTypeName("getSideQuestsByPeriod_200_response")

public class GetSideQuestsByPeriod200Response {

  private @Nullable String period;

  @Valid
  private List<@Valid SideQuestInfo> quests = new ArrayList<>();

  public GetSideQuestsByPeriod200Response period(@Nullable String period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  
  @Schema(name = "period", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("period")
  public @Nullable String getPeriod() {
    return period;
  }

  public void setPeriod(@Nullable String period) {
    this.period = period;
  }

  public GetSideQuestsByPeriod200Response quests(List<@Valid SideQuestInfo> quests) {
    this.quests = quests;
    return this;
  }

  public GetSideQuestsByPeriod200Response addQuestsItem(SideQuestInfo questsItem) {
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
  public List<@Valid SideQuestInfo> getQuests() {
    return quests;
  }

  public void setQuests(List<@Valid SideQuestInfo> quests) {
    this.quests = quests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetSideQuestsByPeriod200Response getSideQuestsByPeriod200Response = (GetSideQuestsByPeriod200Response) o;
    return Objects.equals(this.period, getSideQuestsByPeriod200Response.period) &&
        Objects.equals(this.quests, getSideQuestsByPeriod200Response.quests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(period, quests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSideQuestsByPeriod200Response {\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    quests: ").append(toIndentedString(quests)).append("\n");
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

