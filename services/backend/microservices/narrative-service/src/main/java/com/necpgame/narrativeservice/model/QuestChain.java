package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.QuestChainQuestsInner;
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
 * QuestChain
 */


public class QuestChain {

  private @Nullable String chainId;

  private @Nullable String name;

  private @Nullable String description;

  @Valid
  private List<@Valid QuestChainQuestsInner> quests = new ArrayList<>();

  private @Nullable Object totalRewards;

  public QuestChain chainId(@Nullable String chainId) {
    this.chainId = chainId;
    return this;
  }

  /**
   * Get chainId
   * @return chainId
   */
  
  @Schema(name = "chain_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chain_id")
  public @Nullable String getChainId() {
    return chainId;
  }

  public void setChainId(@Nullable String chainId) {
    this.chainId = chainId;
  }

  public QuestChain name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "NCPD Detective Story", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public QuestChain description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public QuestChain quests(List<@Valid QuestChainQuestsInner> quests) {
    this.quests = quests;
    return this;
  }

  public QuestChain addQuestsItem(QuestChainQuestsInner questsItem) {
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
  public List<@Valid QuestChainQuestsInner> getQuests() {
    return quests;
  }

  public void setQuests(List<@Valid QuestChainQuestsInner> quests) {
    this.quests = quests;
  }

  public QuestChain totalRewards(@Nullable Object totalRewards) {
    this.totalRewards = totalRewards;
    return this;
  }

  /**
   * Награды за завершение всей цепочки
   * @return totalRewards
   */
  
  @Schema(name = "total_rewards", description = "Награды за завершение всей цепочки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_rewards")
  public @Nullable Object getTotalRewards() {
    return totalRewards;
  }

  public void setTotalRewards(@Nullable Object totalRewards) {
    this.totalRewards = totalRewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestChain questChain = (QuestChain) o;
    return Objects.equals(this.chainId, questChain.chainId) &&
        Objects.equals(this.name, questChain.name) &&
        Objects.equals(this.description, questChain.description) &&
        Objects.equals(this.quests, questChain.quests) &&
        Objects.equals(this.totalRewards, questChain.totalRewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chainId, name, description, quests, totalRewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestChain {\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    quests: ").append(toIndentedString(quests)).append("\n");
    sb.append("    totalRewards: ").append(toIndentedString(totalRewards)).append("\n");
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

