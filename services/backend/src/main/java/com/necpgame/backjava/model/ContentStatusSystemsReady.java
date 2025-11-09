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
 * ContentStatusSystemsReady
 */

@JsonTypeName("ContentStatus_systems_ready")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ContentStatusSystemsReady {

  private @Nullable Boolean questEngine;

  private @Nullable Boolean combat;

  private @Nullable Boolean progression;

  private @Nullable Boolean social;

  private @Nullable Boolean economy;

  public ContentStatusSystemsReady questEngine(@Nullable Boolean questEngine) {
    this.questEngine = questEngine;
    return this;
  }

  /**
   * Get questEngine
   * @return questEngine
   */
  
  @Schema(name = "quest_engine", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_engine")
  public @Nullable Boolean getQuestEngine() {
    return questEngine;
  }

  public void setQuestEngine(@Nullable Boolean questEngine) {
    this.questEngine = questEngine;
  }

  public ContentStatusSystemsReady combat(@Nullable Boolean combat) {
    this.combat = combat;
    return this;
  }

  /**
   * Get combat
   * @return combat
   */
  
  @Schema(name = "combat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combat")
  public @Nullable Boolean getCombat() {
    return combat;
  }

  public void setCombat(@Nullable Boolean combat) {
    this.combat = combat;
  }

  public ContentStatusSystemsReady progression(@Nullable Boolean progression) {
    this.progression = progression;
    return this;
  }

  /**
   * Get progression
   * @return progression
   */
  
  @Schema(name = "progression", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progression")
  public @Nullable Boolean getProgression() {
    return progression;
  }

  public void setProgression(@Nullable Boolean progression) {
    this.progression = progression;
  }

  public ContentStatusSystemsReady social(@Nullable Boolean social) {
    this.social = social;
    return this;
  }

  /**
   * Get social
   * @return social
   */
  
  @Schema(name = "social", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("social")
  public @Nullable Boolean getSocial() {
    return social;
  }

  public void setSocial(@Nullable Boolean social) {
    this.social = social;
  }

  public ContentStatusSystemsReady economy(@Nullable Boolean economy) {
    this.economy = economy;
    return this;
  }

  /**
   * Get economy
   * @return economy
   */
  
  @Schema(name = "economy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economy")
  public @Nullable Boolean getEconomy() {
    return economy;
  }

  public void setEconomy(@Nullable Boolean economy) {
    this.economy = economy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContentStatusSystemsReady contentStatusSystemsReady = (ContentStatusSystemsReady) o;
    return Objects.equals(this.questEngine, contentStatusSystemsReady.questEngine) &&
        Objects.equals(this.combat, contentStatusSystemsReady.combat) &&
        Objects.equals(this.progression, contentStatusSystemsReady.progression) &&
        Objects.equals(this.social, contentStatusSystemsReady.social) &&
        Objects.equals(this.economy, contentStatusSystemsReady.economy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questEngine, combat, progression, social, economy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentStatusSystemsReady {\n");
    sb.append("    questEngine: ").append(toIndentedString(questEngine)).append("\n");
    sb.append("    combat: ").append(toIndentedString(combat)).append("\n");
    sb.append("    progression: ").append(toIndentedString(progression)).append("\n");
    sb.append("    social: ").append(toIndentedString(social)).append("\n");
    sb.append("    economy: ").append(toIndentedString(economy)).append("\n");
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

