package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WorldEventDetailedAllOfQuestHooks
 */

@JsonTypeName("WorldEventDetailed_allOf_quest_hooks")

public class WorldEventDetailedAllOfQuestHooks {

  private @Nullable String questId;

  private @Nullable String triggerCondition;

  public WorldEventDetailedAllOfQuestHooks questId(@Nullable String questId) {
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

  public WorldEventDetailedAllOfQuestHooks triggerCondition(@Nullable String triggerCondition) {
    this.triggerCondition = triggerCondition;
    return this;
  }

  /**
   * Get triggerCondition
   * @return triggerCondition
   */
  
  @Schema(name = "trigger_condition", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trigger_condition")
  public @Nullable String getTriggerCondition() {
    return triggerCondition;
  }

  public void setTriggerCondition(@Nullable String triggerCondition) {
    this.triggerCondition = triggerCondition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldEventDetailedAllOfQuestHooks worldEventDetailedAllOfQuestHooks = (WorldEventDetailedAllOfQuestHooks) o;
    return Objects.equals(this.questId, worldEventDetailedAllOfQuestHooks.questId) &&
        Objects.equals(this.triggerCondition, worldEventDetailedAllOfQuestHooks.triggerCondition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, triggerCondition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldEventDetailedAllOfQuestHooks {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    triggerCondition: ").append(toIndentedString(triggerCondition)).append("\n");
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

