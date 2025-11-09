package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.QuestLootTableGuaranteedLootInner;
import com.necpgame.backjava.model.QuestLootTableRandomLootInner;
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
 * QuestLootTable
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestLootTable {

  private @Nullable String questId;

  @Valid
  private List<@Valid QuestLootTableGuaranteedLootInner> guaranteedLoot = new ArrayList<>();

  @Valid
  private List<@Valid QuestLootTableRandomLootInner> randomLoot = new ArrayList<>();

  public QuestLootTable questId(@Nullable String questId) {
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

  public QuestLootTable guaranteedLoot(List<@Valid QuestLootTableGuaranteedLootInner> guaranteedLoot) {
    this.guaranteedLoot = guaranteedLoot;
    return this;
  }

  public QuestLootTable addGuaranteedLootItem(QuestLootTableGuaranteedLootInner guaranteedLootItem) {
    if (this.guaranteedLoot == null) {
      this.guaranteedLoot = new ArrayList<>();
    }
    this.guaranteedLoot.add(guaranteedLootItem);
    return this;
  }

  /**
   * Гарантированный лут
   * @return guaranteedLoot
   */
  @Valid 
  @Schema(name = "guaranteed_loot", description = "Гарантированный лут", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guaranteed_loot")
  public List<@Valid QuestLootTableGuaranteedLootInner> getGuaranteedLoot() {
    return guaranteedLoot;
  }

  public void setGuaranteedLoot(List<@Valid QuestLootTableGuaranteedLootInner> guaranteedLoot) {
    this.guaranteedLoot = guaranteedLoot;
  }

  public QuestLootTable randomLoot(List<@Valid QuestLootTableRandomLootInner> randomLoot) {
    this.randomLoot = randomLoot;
    return this;
  }

  public QuestLootTable addRandomLootItem(QuestLootTableRandomLootInner randomLootItem) {
    if (this.randomLoot == null) {
      this.randomLoot = new ArrayList<>();
    }
    this.randomLoot.add(randomLootItem);
    return this;
  }

  /**
   * Случайный лут с вероятностями
   * @return randomLoot
   */
  @Valid 
  @Schema(name = "random_loot", description = "Случайный лут с вероятностями", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("random_loot")
  public List<@Valid QuestLootTableRandomLootInner> getRandomLoot() {
    return randomLoot;
  }

  public void setRandomLoot(List<@Valid QuestLootTableRandomLootInner> randomLoot) {
    this.randomLoot = randomLoot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestLootTable questLootTable = (QuestLootTable) o;
    return Objects.equals(this.questId, questLootTable.questId) &&
        Objects.equals(this.guaranteedLoot, questLootTable.guaranteedLoot) &&
        Objects.equals(this.randomLoot, questLootTable.randomLoot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, guaranteedLoot, randomLoot);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestLootTable {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    guaranteedLoot: ").append(toIndentedString(guaranteedLoot)).append("\n");
    sb.append("    randomLoot: ").append(toIndentedString(randomLoot)).append("\n");
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

