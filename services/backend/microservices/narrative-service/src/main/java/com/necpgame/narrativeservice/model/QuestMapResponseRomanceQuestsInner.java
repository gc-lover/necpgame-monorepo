package com.necpgame.narrativeservice.model;

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
 * QuestMapResponseRomanceQuestsInner
 */

@JsonTypeName("QuestMapResponse_romance_quests_inner")

public class QuestMapResponseRomanceQuestsInner {

  private @Nullable String npcId;

  private @Nullable String npcName;

  private @Nullable Integer questChainLength;

  public QuestMapResponseRomanceQuestsInner npcId(@Nullable String npcId) {
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

  public QuestMapResponseRomanceQuestsInner npcName(@Nullable String npcName) {
    this.npcName = npcName;
    return this;
  }

  /**
   * Get npcName
   * @return npcName
   */
  
  @Schema(name = "npc_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_name")
  public @Nullable String getNpcName() {
    return npcName;
  }

  public void setNpcName(@Nullable String npcName) {
    this.npcName = npcName;
  }

  public QuestMapResponseRomanceQuestsInner questChainLength(@Nullable Integer questChainLength) {
    this.questChainLength = questChainLength;
    return this;
  }

  /**
   * Get questChainLength
   * @return questChainLength
   */
  
  @Schema(name = "quest_chain_length", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_chain_length")
  public @Nullable Integer getQuestChainLength() {
    return questChainLength;
  }

  public void setQuestChainLength(@Nullable Integer questChainLength) {
    this.questChainLength = questChainLength;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestMapResponseRomanceQuestsInner questMapResponseRomanceQuestsInner = (QuestMapResponseRomanceQuestsInner) o;
    return Objects.equals(this.npcId, questMapResponseRomanceQuestsInner.npcId) &&
        Objects.equals(this.npcName, questMapResponseRomanceQuestsInner.npcName) &&
        Objects.equals(this.questChainLength, questMapResponseRomanceQuestsInner.questChainLength);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, npcName, questChainLength);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestMapResponseRomanceQuestsInner {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    npcName: ").append(toIndentedString(npcName)).append("\n");
    sb.append("    questChainLength: ").append(toIndentedString(questChainLength)).append("\n");
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

