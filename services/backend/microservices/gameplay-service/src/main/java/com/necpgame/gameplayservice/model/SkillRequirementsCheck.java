package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.SkillRequirementsCheckRequirementsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SkillRequirementsCheck
 */


public class SkillRequirementsCheck {

  private @Nullable UUID characterId;

  private @Nullable UUID itemId;

  private @Nullable Boolean canUse;

  @Valid
  private List<@Valid SkillRequirementsCheckRequirementsInner> requirements = new ArrayList<>();

  private @Nullable Float effectiveness;

  @Valid
  private List<String> penalties = new ArrayList<>();

  public SkillRequirementsCheck characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public SkillRequirementsCheck itemId(@Nullable UUID itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @Valid 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable UUID getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable UUID itemId) {
    this.itemId = itemId;
  }

  public SkillRequirementsCheck canUse(@Nullable Boolean canUse) {
    this.canUse = canUse;
    return this;
  }

  /**
   * Get canUse
   * @return canUse
   */
  
  @Schema(name = "can_use", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("can_use")
  public @Nullable Boolean getCanUse() {
    return canUse;
  }

  public void setCanUse(@Nullable Boolean canUse) {
    this.canUse = canUse;
  }

  public SkillRequirementsCheck requirements(List<@Valid SkillRequirementsCheckRequirementsInner> requirements) {
    this.requirements = requirements;
    return this;
  }

  public SkillRequirementsCheck addRequirementsItem(SkillRequirementsCheckRequirementsInner requirementsItem) {
    if (this.requirements == null) {
      this.requirements = new ArrayList<>();
    }
    this.requirements.add(requirementsItem);
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public List<@Valid SkillRequirementsCheckRequirementsInner> getRequirements() {
    return requirements;
  }

  public void setRequirements(List<@Valid SkillRequirementsCheckRequirementsInner> requirements) {
    this.requirements = requirements;
  }

  public SkillRequirementsCheck effectiveness(@Nullable Float effectiveness) {
    this.effectiveness = effectiveness;
    return this;
  }

  /**
   * Эффективность использования (0-100%)
   * @return effectiveness
   */
  
  @Schema(name = "effectiveness", description = "Эффективность использования (0-100%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effectiveness")
  public @Nullable Float getEffectiveness() {
    return effectiveness;
  }

  public void setEffectiveness(@Nullable Float effectiveness) {
    this.effectiveness = effectiveness;
  }

  public SkillRequirementsCheck penalties(List<String> penalties) {
    this.penalties = penalties;
    return this;
  }

  public SkillRequirementsCheck addPenaltiesItem(String penaltiesItem) {
    if (this.penalties == null) {
      this.penalties = new ArrayList<>();
    }
    this.penalties.add(penaltiesItem);
    return this;
  }

  /**
   * Штрафы если требования не выполнены
   * @return penalties
   */
  
  @Schema(name = "penalties", description = "Штрафы если требования не выполнены", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalties")
  public List<String> getPenalties() {
    return penalties;
  }

  public void setPenalties(List<String> penalties) {
    this.penalties = penalties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillRequirementsCheck skillRequirementsCheck = (SkillRequirementsCheck) o;
    return Objects.equals(this.characterId, skillRequirementsCheck.characterId) &&
        Objects.equals(this.itemId, skillRequirementsCheck.itemId) &&
        Objects.equals(this.canUse, skillRequirementsCheck.canUse) &&
        Objects.equals(this.requirements, skillRequirementsCheck.requirements) &&
        Objects.equals(this.effectiveness, skillRequirementsCheck.effectiveness) &&
        Objects.equals(this.penalties, skillRequirementsCheck.penalties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, itemId, canUse, requirements, effectiveness, penalties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillRequirementsCheck {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    canUse: ").append(toIndentedString(canUse)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    effectiveness: ").append(toIndentedString(effectiveness)).append("\n");
    sb.append("    penalties: ").append(toIndentedString(penalties)).append("\n");
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

