package com.necpgame.gameplayservice.model;

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
 * Правила сброса для данного типа лиги
 */

@Schema(name = "LeagueType_wipe_rules", description = "Правила сброса для данного типа лиги")
@JsonTypeName("LeagueType_wipe_rules")

public class LeagueTypeWipeRules {

  private @Nullable Boolean wipesCharacters;

  private @Nullable Boolean wipesItems;

  private @Nullable Boolean wipesReputation;

  public LeagueTypeWipeRules wipesCharacters(@Nullable Boolean wipesCharacters) {
    this.wipesCharacters = wipesCharacters;
    return this;
  }

  /**
   * Get wipesCharacters
   * @return wipesCharacters
   */
  
  @Schema(name = "wipes_characters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wipes_characters")
  public @Nullable Boolean getWipesCharacters() {
    return wipesCharacters;
  }

  public void setWipesCharacters(@Nullable Boolean wipesCharacters) {
    this.wipesCharacters = wipesCharacters;
  }

  public LeagueTypeWipeRules wipesItems(@Nullable Boolean wipesItems) {
    this.wipesItems = wipesItems;
    return this;
  }

  /**
   * Get wipesItems
   * @return wipesItems
   */
  
  @Schema(name = "wipes_items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wipes_items")
  public @Nullable Boolean getWipesItems() {
    return wipesItems;
  }

  public void setWipesItems(@Nullable Boolean wipesItems) {
    this.wipesItems = wipesItems;
  }

  public LeagueTypeWipeRules wipesReputation(@Nullable Boolean wipesReputation) {
    this.wipesReputation = wipesReputation;
    return this;
  }

  /**
   * Get wipesReputation
   * @return wipesReputation
   */
  
  @Schema(name = "wipes_reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wipes_reputation")
  public @Nullable Boolean getWipesReputation() {
    return wipesReputation;
  }

  public void setWipesReputation(@Nullable Boolean wipesReputation) {
    this.wipesReputation = wipesReputation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeagueTypeWipeRules leagueTypeWipeRules = (LeagueTypeWipeRules) o;
    return Objects.equals(this.wipesCharacters, leagueTypeWipeRules.wipesCharacters) &&
        Objects.equals(this.wipesItems, leagueTypeWipeRules.wipesItems) &&
        Objects.equals(this.wipesReputation, leagueTypeWipeRules.wipesReputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wipesCharacters, wipesItems, wipesReputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeagueTypeWipeRules {\n");
    sb.append("    wipesCharacters: ").append(toIndentedString(wipesCharacters)).append("\n");
    sb.append("    wipesItems: ").append(toIndentedString(wipesItems)).append("\n");
    sb.append("    wipesReputation: ").append(toIndentedString(wipesReputation)).append("\n");
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

