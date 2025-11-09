package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * GetFactionQuestProgress200ResponseCompletedQuestsInner
 */

@JsonTypeName("getFactionQuestProgress_200_response_completed_quests_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetFactionQuestProgress200ResponseCompletedQuestsInner {

  private @Nullable String questId;

  private @Nullable String endingAchieved;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime completionDate;

  public GetFactionQuestProgress200ResponseCompletedQuestsInner questId(@Nullable String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  
  @Schema(name = "quest_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_id")
  public @Nullable String getQuestId() {
    return questId;
  }

  public void setQuestId(@Nullable String questId) {
    this.questId = questId;
  }

  public GetFactionQuestProgress200ResponseCompletedQuestsInner endingAchieved(@Nullable String endingAchieved) {
    this.endingAchieved = endingAchieved;
    return this;
  }

  /**
   * Get endingAchieved
   * @return endingAchieved
   */
  
  @Schema(name = "ending_achieved", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ending_achieved")
  public @Nullable String getEndingAchieved() {
    return endingAchieved;
  }

  public void setEndingAchieved(@Nullable String endingAchieved) {
    this.endingAchieved = endingAchieved;
  }

  public GetFactionQuestProgress200ResponseCompletedQuestsInner completionDate(@Nullable OffsetDateTime completionDate) {
    this.completionDate = completionDate;
    return this;
  }

  /**
   * Get completionDate
   * @return completionDate
   */
  @Valid 
  @Schema(name = "completion_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completion_date")
  public @Nullable OffsetDateTime getCompletionDate() {
    return completionDate;
  }

  public void setCompletionDate(@Nullable OffsetDateTime completionDate) {
    this.completionDate = completionDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFactionQuestProgress200ResponseCompletedQuestsInner getFactionQuestProgress200ResponseCompletedQuestsInner = (GetFactionQuestProgress200ResponseCompletedQuestsInner) o;
    return Objects.equals(this.questId, getFactionQuestProgress200ResponseCompletedQuestsInner.questId) &&
        Objects.equals(this.endingAchieved, getFactionQuestProgress200ResponseCompletedQuestsInner.endingAchieved) &&
        Objects.equals(this.completionDate, getFactionQuestProgress200ResponseCompletedQuestsInner.completionDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, endingAchieved, completionDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFactionQuestProgress200ResponseCompletedQuestsInner {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    endingAchieved: ").append(toIndentedString(endingAchieved)).append("\n");
    sb.append("    completionDate: ").append(toIndentedString(completionDate)).append("\n");
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

