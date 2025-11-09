package com.necpgame.backjava.model;

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
 * QuestChainQuestsInner
 */

@JsonTypeName("QuestChain_quests_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestChainQuestsInner {

  private @Nullable String questId;

  private @Nullable String title;

  private @Nullable Integer order;

  private @Nullable Boolean optional;

  public QuestChainQuestsInner questId(@Nullable String questId) {
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

  public QuestChainQuestsInner title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public QuestChainQuestsInner order(@Nullable Integer order) {
    this.order = order;
    return this;
  }

  /**
   * Get order
   * @return order
   */
  
  @Schema(name = "order", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order")
  public @Nullable Integer getOrder() {
    return order;
  }

  public void setOrder(@Nullable Integer order) {
    this.order = order;
  }

  public QuestChainQuestsInner optional(@Nullable Boolean optional) {
    this.optional = optional;
    return this;
  }

  /**
   * Get optional
   * @return optional
   */
  
  @Schema(name = "optional", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("optional")
  public @Nullable Boolean getOptional() {
    return optional;
  }

  public void setOptional(@Nullable Boolean optional) {
    this.optional = optional;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestChainQuestsInner questChainQuestsInner = (QuestChainQuestsInner) o;
    return Objects.equals(this.questId, questChainQuestsInner.questId) &&
        Objects.equals(this.title, questChainQuestsInner.title) &&
        Objects.equals(this.order, questChainQuestsInner.order) &&
        Objects.equals(this.optional, questChainQuestsInner.optional);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, title, order, optional);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestChainQuestsInner {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
    sb.append("    optional: ").append(toIndentedString(optional)).append("\n");
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

