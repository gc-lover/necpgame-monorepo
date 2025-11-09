package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.ActiveQuestProgress;
import com.necpgame.narrativeservice.model.GetFactionQuestProgress200ResponseCompletedQuestsInner;
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
 * GetFactionQuestProgress200Response
 */

@JsonTypeName("getFactionQuestProgress_200_response")

public class GetFactionQuestProgress200Response {

  @Valid
  private List<@Valid ActiveQuestProgress> activeQuests = new ArrayList<>();

  @Valid
  private List<@Valid GetFactionQuestProgress200ResponseCompletedQuestsInner> completedQuests = new ArrayList<>();

  public GetFactionQuestProgress200Response activeQuests(List<@Valid ActiveQuestProgress> activeQuests) {
    this.activeQuests = activeQuests;
    return this;
  }

  public GetFactionQuestProgress200Response addActiveQuestsItem(ActiveQuestProgress activeQuestsItem) {
    if (this.activeQuests == null) {
      this.activeQuests = new ArrayList<>();
    }
    this.activeQuests.add(activeQuestsItem);
    return this;
  }

  /**
   * Get activeQuests
   * @return activeQuests
   */
  @Valid 
  @Schema(name = "active_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_quests")
  public List<@Valid ActiveQuestProgress> getActiveQuests() {
    return activeQuests;
  }

  public void setActiveQuests(List<@Valid ActiveQuestProgress> activeQuests) {
    this.activeQuests = activeQuests;
  }

  public GetFactionQuestProgress200Response completedQuests(List<@Valid GetFactionQuestProgress200ResponseCompletedQuestsInner> completedQuests) {
    this.completedQuests = completedQuests;
    return this;
  }

  public GetFactionQuestProgress200Response addCompletedQuestsItem(GetFactionQuestProgress200ResponseCompletedQuestsInner completedQuestsItem) {
    if (this.completedQuests == null) {
      this.completedQuests = new ArrayList<>();
    }
    this.completedQuests.add(completedQuestsItem);
    return this;
  }

  /**
   * Get completedQuests
   * @return completedQuests
   */
  @Valid 
  @Schema(name = "completed_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed_quests")
  public List<@Valid GetFactionQuestProgress200ResponseCompletedQuestsInner> getCompletedQuests() {
    return completedQuests;
  }

  public void setCompletedQuests(List<@Valid GetFactionQuestProgress200ResponseCompletedQuestsInner> completedQuests) {
    this.completedQuests = completedQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFactionQuestProgress200Response getFactionQuestProgress200Response = (GetFactionQuestProgress200Response) o;
    return Objects.equals(this.activeQuests, getFactionQuestProgress200Response.activeQuests) &&
        Objects.equals(this.completedQuests, getFactionQuestProgress200Response.completedQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeQuests, completedQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFactionQuestProgress200Response {\n");
    sb.append("    activeQuests: ").append(toIndentedString(activeQuests)).append("\n");
    sb.append("    completedQuests: ").append(toIndentedString(completedQuests)).append("\n");
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

