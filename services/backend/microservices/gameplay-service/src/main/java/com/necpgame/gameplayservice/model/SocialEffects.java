package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Социальные эффекты киберпсихоза
 */

@Schema(name = "SocialEffects", description = "Социальные эффекты киберпсихоза")

public class SocialEffects {

  private @Nullable Float reputationPenalty;

  @Valid
  private List<Object> npcAccessRestrictions = new ArrayList<>();

  @Valid
  private List<String> visualIndicators = new ArrayList<>();

  public SocialEffects reputationPenalty(@Nullable Float reputationPenalty) {
    this.reputationPenalty = reputationPenalty;
    return this;
  }

  /**
   * Штраф к репутации с фракциями
   * maximum: 0
   * @return reputationPenalty
   */
  @DecimalMax(value = "0") 
  @Schema(name = "reputation_penalty", description = "Штраф к репутации с фракциями", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_penalty")
  public @Nullable Float getReputationPenalty() {
    return reputationPenalty;
  }

  public void setReputationPenalty(@Nullable Float reputationPenalty) {
    this.reputationPenalty = reputationPenalty;
  }

  public SocialEffects npcAccessRestrictions(List<Object> npcAccessRestrictions) {
    this.npcAccessRestrictions = npcAccessRestrictions;
    return this;
  }

  public SocialEffects addNpcAccessRestrictionsItem(Object npcAccessRestrictionsItem) {
    if (this.npcAccessRestrictions == null) {
      this.npcAccessRestrictions = new ArrayList<>();
    }
    this.npcAccessRestrictions.add(npcAccessRestrictionsItem);
    return this;
  }

  /**
   * Ограничения доступа к NPC
   * @return npcAccessRestrictions
   */
  
  @Schema(name = "npc_access_restrictions", description = "Ограничения доступа к NPC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_access_restrictions")
  public List<Object> getNpcAccessRestrictions() {
    return npcAccessRestrictions;
  }

  public void setNpcAccessRestrictions(List<Object> npcAccessRestrictions) {
    this.npcAccessRestrictions = npcAccessRestrictions;
  }

  public SocialEffects visualIndicators(List<String> visualIndicators) {
    this.visualIndicators = visualIndicators;
    return this;
  }

  public SocialEffects addVisualIndicatorsItem(String visualIndicatorsItem) {
    if (this.visualIndicators == null) {
      this.visualIndicators = new ArrayList<>();
    }
    this.visualIndicators.add(visualIndicatorsItem);
    return this;
  }

  /**
   * Визуальные индикаторы для других игроков
   * @return visualIndicators
   */
  
  @Schema(name = "visual_indicators", description = "Визуальные индикаторы для других игроков", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visual_indicators")
  public List<String> getVisualIndicators() {
    return visualIndicators;
  }

  public void setVisualIndicators(List<String> visualIndicators) {
    this.visualIndicators = visualIndicators;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialEffects socialEffects = (SocialEffects) o;
    return Objects.equals(this.reputationPenalty, socialEffects.reputationPenalty) &&
        Objects.equals(this.npcAccessRestrictions, socialEffects.npcAccessRestrictions) &&
        Objects.equals(this.visualIndicators, socialEffects.visualIndicators);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reputationPenalty, npcAccessRestrictions, visualIndicators);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialEffects {\n");
    sb.append("    reputationPenalty: ").append(toIndentedString(reputationPenalty)).append("\n");
    sb.append("    npcAccessRestrictions: ").append(toIndentedString(npcAccessRestrictions)).append("\n");
    sb.append("    visualIndicators: ").append(toIndentedString(visualIndicators)).append("\n");
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

