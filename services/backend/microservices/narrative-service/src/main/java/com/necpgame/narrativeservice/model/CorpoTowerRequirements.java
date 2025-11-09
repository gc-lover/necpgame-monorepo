package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * CorpoTowerRequirements
 */


public class CorpoTowerRequirements {

  private @Nullable String characterId;

  private @Nullable Boolean eligible;

  private @Nullable Integer level;

  private @Nullable Integer levelRequired;

  private @Nullable Integer gearScore;

  private @Nullable Integer gearScoreRequired;

  private @Nullable Boolean factionWarQuestsCompleted;

  /**
   * Gets or Sets sideChosen
   */
  public enum SideChosenEnum {
    ARASAKA("arasaka"),
    
    MILITECH("militech"),
    
    NONE("none");

    private final String value;

    SideChosenEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static SideChosenEnum fromValue(String value) {
      for (SideChosenEnum b : SideChosenEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SideChosenEnum sideChosen;

  private @Nullable Boolean accessCardRequired;

  @Valid
  private List<String> reasons = new ArrayList<>();

  public CorpoTowerRequirements characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public CorpoTowerRequirements eligible(@Nullable Boolean eligible) {
    this.eligible = eligible;
    return this;
  }

  /**
   * Get eligible
   * @return eligible
   */
  
  @Schema(name = "eligible", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eligible")
  public @Nullable Boolean getEligible() {
    return eligible;
  }

  public void setEligible(@Nullable Boolean eligible) {
    this.eligible = eligible;
  }

  public CorpoTowerRequirements level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public CorpoTowerRequirements levelRequired(@Nullable Integer levelRequired) {
    this.levelRequired = levelRequired;
    return this;
  }

  /**
   * Get levelRequired
   * @return levelRequired
   */
  
  @Schema(name = "level_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level_required")
  public @Nullable Integer getLevelRequired() {
    return levelRequired;
  }

  public void setLevelRequired(@Nullable Integer levelRequired) {
    this.levelRequired = levelRequired;
  }

  public CorpoTowerRequirements gearScore(@Nullable Integer gearScore) {
    this.gearScore = gearScore;
    return this;
  }

  /**
   * Get gearScore
   * @return gearScore
   */
  
  @Schema(name = "gear_score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gear_score")
  public @Nullable Integer getGearScore() {
    return gearScore;
  }

  public void setGearScore(@Nullable Integer gearScore) {
    this.gearScore = gearScore;
  }

  public CorpoTowerRequirements gearScoreRequired(@Nullable Integer gearScoreRequired) {
    this.gearScoreRequired = gearScoreRequired;
    return this;
  }

  /**
   * Get gearScoreRequired
   * @return gearScoreRequired
   */
  
  @Schema(name = "gear_score_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gear_score_required")
  public @Nullable Integer getGearScoreRequired() {
    return gearScoreRequired;
  }

  public void setGearScoreRequired(@Nullable Integer gearScoreRequired) {
    this.gearScoreRequired = gearScoreRequired;
  }

  public CorpoTowerRequirements factionWarQuestsCompleted(@Nullable Boolean factionWarQuestsCompleted) {
    this.factionWarQuestsCompleted = factionWarQuestsCompleted;
    return this;
  }

  /**
   * Get factionWarQuestsCompleted
   * @return factionWarQuestsCompleted
   */
  
  @Schema(name = "faction_war_quests_completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_war_quests_completed")
  public @Nullable Boolean getFactionWarQuestsCompleted() {
    return factionWarQuestsCompleted;
  }

  public void setFactionWarQuestsCompleted(@Nullable Boolean factionWarQuestsCompleted) {
    this.factionWarQuestsCompleted = factionWarQuestsCompleted;
  }

  public CorpoTowerRequirements sideChosen(@Nullable SideChosenEnum sideChosen) {
    this.sideChosen = sideChosen;
    return this;
  }

  /**
   * Get sideChosen
   * @return sideChosen
   */
  
  @Schema(name = "side_chosen", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("side_chosen")
  public @Nullable SideChosenEnum getSideChosen() {
    return sideChosen;
  }

  public void setSideChosen(@Nullable SideChosenEnum sideChosen) {
    this.sideChosen = sideChosen;
  }

  public CorpoTowerRequirements accessCardRequired(@Nullable Boolean accessCardRequired) {
    this.accessCardRequired = accessCardRequired;
    return this;
  }

  /**
   * Get accessCardRequired
   * @return accessCardRequired
   */
  
  @Schema(name = "access_card_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("access_card_required")
  public @Nullable Boolean getAccessCardRequired() {
    return accessCardRequired;
  }

  public void setAccessCardRequired(@Nullable Boolean accessCardRequired) {
    this.accessCardRequired = accessCardRequired;
  }

  public CorpoTowerRequirements reasons(List<String> reasons) {
    this.reasons = reasons;
    return this;
  }

  public CorpoTowerRequirements addReasonsItem(String reasonsItem) {
    if (this.reasons == null) {
      this.reasons = new ArrayList<>();
    }
    this.reasons.add(reasonsItem);
    return this;
  }

  /**
   * Get reasons
   * @return reasons
   */
  
  @Schema(name = "reasons", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    CorpoTowerRequirements corpoTowerRequirements = (CorpoTowerRequirements) o;
    return Objects.equals(this.characterId, corpoTowerRequirements.characterId) &&
        Objects.equals(this.eligible, corpoTowerRequirements.eligible) &&
        Objects.equals(this.level, corpoTowerRequirements.level) &&
        Objects.equals(this.levelRequired, corpoTowerRequirements.levelRequired) &&
        Objects.equals(this.gearScore, corpoTowerRequirements.gearScore) &&
        Objects.equals(this.gearScoreRequired, corpoTowerRequirements.gearScoreRequired) &&
        Objects.equals(this.factionWarQuestsCompleted, corpoTowerRequirements.factionWarQuestsCompleted) &&
        Objects.equals(this.sideChosen, corpoTowerRequirements.sideChosen) &&
        Objects.equals(this.accessCardRequired, corpoTowerRequirements.accessCardRequired) &&
        Objects.equals(this.reasons, corpoTowerRequirements.reasons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, eligible, level, levelRequired, gearScore, gearScoreRequired, factionWarQuestsCompleted, sideChosen, accessCardRequired, reasons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CorpoTowerRequirements {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    eligible: ").append(toIndentedString(eligible)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    levelRequired: ").append(toIndentedString(levelRequired)).append("\n");
    sb.append("    gearScore: ").append(toIndentedString(gearScore)).append("\n");
    sb.append("    gearScoreRequired: ").append(toIndentedString(gearScoreRequired)).append("\n");
    sb.append("    factionWarQuestsCompleted: ").append(toIndentedString(factionWarQuestsCompleted)).append("\n");
    sb.append("    sideChosen: ").append(toIndentedString(sideChosen)).append("\n");
    sb.append("    accessCardRequired: ").append(toIndentedString(accessCardRequired)).append("\n");
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

