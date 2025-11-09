package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.FactionQuest;
import com.necpgame.backjava.model.QuestRequirements;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetAvailableFactionQuests200ResponseLockedQuestsInner
 */

@JsonTypeName("getAvailableFactionQuests_200_response_locked_quests_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetAvailableFactionQuests200ResponseLockedQuestsInner {

  private @Nullable FactionQuest quest;

  private @Nullable QuestRequirements requirements;

  public GetAvailableFactionQuests200ResponseLockedQuestsInner quest(@Nullable FactionQuest quest) {
    this.quest = quest;
    return this;
  }

  /**
   * Get quest
   * @return quest
   */
  @Valid 
  @Schema(name = "quest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest")
  public @Nullable FactionQuest getQuest() {
    return quest;
  }

  public void setQuest(@Nullable FactionQuest quest) {
    this.quest = quest;
  }

  public GetAvailableFactionQuests200ResponseLockedQuestsInner requirements(@Nullable QuestRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable QuestRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable QuestRequirements requirements) {
    this.requirements = requirements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailableFactionQuests200ResponseLockedQuestsInner getAvailableFactionQuests200ResponseLockedQuestsInner = (GetAvailableFactionQuests200ResponseLockedQuestsInner) o;
    return Objects.equals(this.quest, getAvailableFactionQuests200ResponseLockedQuestsInner.quest) &&
        Objects.equals(this.requirements, getAvailableFactionQuests200ResponseLockedQuestsInner.requirements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quest, requirements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableFactionQuests200ResponseLockedQuestsInner {\n");
    sb.append("    quest: ").append(toIndentedString(quest)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
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

