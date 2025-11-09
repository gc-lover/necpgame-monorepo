package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.QuestCatalogEntry;
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
 * GetQuestRecommendations200ResponseRecommendationsInner
 */

@JsonTypeName("getQuestRecommendations_200_response_recommendations_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetQuestRecommendations200ResponseRecommendationsInner {

  private @Nullable QuestCatalogEntry quest;

  private @Nullable Float matchScore;

  @Valid
  private List<String> reasons = new ArrayList<>();

  public GetQuestRecommendations200ResponseRecommendationsInner quest(@Nullable QuestCatalogEntry quest) {
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
  public @Nullable QuestCatalogEntry getQuest() {
    return quest;
  }

  public void setQuest(@Nullable QuestCatalogEntry quest) {
    this.quest = quest;
  }

  public GetQuestRecommendations200ResponseRecommendationsInner matchScore(@Nullable Float matchScore) {
    this.matchScore = matchScore;
    return this;
  }

  /**
   * Насколько квест подходит игроку (0-100)
   * @return matchScore
   */
  
  @Schema(name = "match_score", description = "Насколько квест подходит игроку (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("match_score")
  public @Nullable Float getMatchScore() {
    return matchScore;
  }

  public void setMatchScore(@Nullable Float matchScore) {
    this.matchScore = matchScore;
  }

  public GetQuestRecommendations200ResponseRecommendationsInner reasons(List<String> reasons) {
    this.reasons = reasons;
    return this;
  }

  public GetQuestRecommendations200ResponseRecommendationsInner addReasonsItem(String reasonsItem) {
    if (this.reasons == null) {
      this.reasons = new ArrayList<>();
    }
    this.reasons.add(reasonsItem);
    return this;
  }

  /**
   * Почему рекомендуется
   * @return reasons
   */
  
  @Schema(name = "reasons", description = "Почему рекомендуется", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reasons")
  public List<String> getReasons() {
    return reasons;
  }

  public void setReasons(List<String> reasons) {
    this.reasons = reasons;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetQuestRecommendations200ResponseRecommendationsInner getQuestRecommendations200ResponseRecommendationsInner = (GetQuestRecommendations200ResponseRecommendationsInner) o;
    return Objects.equals(this.quest, getQuestRecommendations200ResponseRecommendationsInner.quest) &&
        Objects.equals(this.matchScore, getQuestRecommendations200ResponseRecommendationsInner.matchScore) &&
        Objects.equals(this.reasons, getQuestRecommendations200ResponseRecommendationsInner.reasons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quest, matchScore, reasons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetQuestRecommendations200ResponseRecommendationsInner {\n");
    sb.append("    quest: ").append(toIndentedString(quest)).append("\n");
    sb.append("    matchScore: ").append(toIndentedString(matchScore)).append("\n");
    sb.append("    reasons: ").append(toIndentedString(reasons)).append("\n");
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

