package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WeightingParametersWeights
 */

@JsonTypeName("WeightingParameters_weights")

public class WeightingParametersWeights {

  private @Nullable BigDecimal relationshipCompatibility;

  private @Nullable BigDecimal contextMatch;

  private @Nullable BigDecimal playerPreferences;

  private @Nullable BigDecimal npcPersonality;

  private @Nullable BigDecimal storyProgression;

  public WeightingParametersWeights relationshipCompatibility(@Nullable BigDecimal relationshipCompatibility) {
    this.relationshipCompatibility = relationshipCompatibility;
    return this;
  }

  /**
   * Get relationshipCompatibility
   * @return relationshipCompatibility
   */
  @Valid 
  @Schema(name = "relationship_compatibility", example = "0.3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_compatibility")
  public @Nullable BigDecimal getRelationshipCompatibility() {
    return relationshipCompatibility;
  }

  public void setRelationshipCompatibility(@Nullable BigDecimal relationshipCompatibility) {
    this.relationshipCompatibility = relationshipCompatibility;
  }

  public WeightingParametersWeights contextMatch(@Nullable BigDecimal contextMatch) {
    this.contextMatch = contextMatch;
    return this;
  }

  /**
   * Get contextMatch
   * @return contextMatch
   */
  @Valid 
  @Schema(name = "context_match", example = "0.25", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context_match")
  public @Nullable BigDecimal getContextMatch() {
    return contextMatch;
  }

  public void setContextMatch(@Nullable BigDecimal contextMatch) {
    this.contextMatch = contextMatch;
  }

  public WeightingParametersWeights playerPreferences(@Nullable BigDecimal playerPreferences) {
    this.playerPreferences = playerPreferences;
    return this;
  }

  /**
   * Get playerPreferences
   * @return playerPreferences
   */
  @Valid 
  @Schema(name = "player_preferences", example = "0.2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_preferences")
  public @Nullable BigDecimal getPlayerPreferences() {
    return playerPreferences;
  }

  public void setPlayerPreferences(@Nullable BigDecimal playerPreferences) {
    this.playerPreferences = playerPreferences;
  }

  public WeightingParametersWeights npcPersonality(@Nullable BigDecimal npcPersonality) {
    this.npcPersonality = npcPersonality;
    return this;
  }

  /**
   * Get npcPersonality
   * @return npcPersonality
   */
  @Valid 
  @Schema(name = "npc_personality", example = "0.15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_personality")
  public @Nullable BigDecimal getNpcPersonality() {
    return npcPersonality;
  }

  public void setNpcPersonality(@Nullable BigDecimal npcPersonality) {
    this.npcPersonality = npcPersonality;
  }

  public WeightingParametersWeights storyProgression(@Nullable BigDecimal storyProgression) {
    this.storyProgression = storyProgression;
    return this;
  }

  /**
   * Get storyProgression
   * @return storyProgression
   */
  @Valid 
  @Schema(name = "story_progression", example = "0.1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("story_progression")
  public @Nullable BigDecimal getStoryProgression() {
    return storyProgression;
  }

  public void setStoryProgression(@Nullable BigDecimal storyProgression) {
    this.storyProgression = storyProgression;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeightingParametersWeights weightingParametersWeights = (WeightingParametersWeights) o;
    return Objects.equals(this.relationshipCompatibility, weightingParametersWeights.relationshipCompatibility) &&
        Objects.equals(this.contextMatch, weightingParametersWeights.contextMatch) &&
        Objects.equals(this.playerPreferences, weightingParametersWeights.playerPreferences) &&
        Objects.equals(this.npcPersonality, weightingParametersWeights.npcPersonality) &&
        Objects.equals(this.storyProgression, weightingParametersWeights.storyProgression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationshipCompatibility, contextMatch, playerPreferences, npcPersonality, storyProgression);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeightingParametersWeights {\n");
    sb.append("    relationshipCompatibility: ").append(toIndentedString(relationshipCompatibility)).append("\n");
    sb.append("    contextMatch: ").append(toIndentedString(contextMatch)).append("\n");
    sb.append("    playerPreferences: ").append(toIndentedString(playerPreferences)).append("\n");
    sb.append("    npcPersonality: ").append(toIndentedString(npcPersonality)).append("\n");
    sb.append("    storyProgression: ").append(toIndentedString(storyProgression)).append("\n");
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

