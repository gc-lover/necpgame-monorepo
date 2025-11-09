package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.WeaponMasteryProgressBonusesInner;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WeaponMasteryProgress
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:14:20.180301500+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class WeaponMasteryProgress {

  private @Nullable String characterId;

  private @Nullable String weaponClass;

  /**
   * Novice: 0-100 kills Adept: 100-500 kills Expert: 500-2000 kills Master: 2000-5000 kills Legend: 5000-10000 kills 
   */
  public enum RankEnum {
    NOVICE("novice"),
    
    ADEPT("adept"),
    
    EXPERT("expert"),
    
    MASTER("master"),
    
    LEGEND("legend");

    private final String value;

    RankEnum(String value) {
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
    public static RankEnum fromValue(String value) {
      for (RankEnum b : RankEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RankEnum rank;

  private @Nullable Integer totalKills;

  private @Nullable Integer killsToNextRank;

  @Valid
  private List<@Valid WeaponMasteryProgressBonusesInner> bonuses = new ArrayList<>();

  public WeaponMasteryProgress characterId(@Nullable String characterId) {
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

  public WeaponMasteryProgress weaponClass(@Nullable String weaponClass) {
    this.weaponClass = weaponClass;
    return this;
  }

  /**
   * Get weaponClass
   * @return weaponClass
   */
  
  @Schema(name = "weapon_class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weapon_class")
  public @Nullable String getWeaponClass() {
    return weaponClass;
  }

  public void setWeaponClass(@Nullable String weaponClass) {
    this.weaponClass = weaponClass;
  }

  public WeaponMasteryProgress rank(@Nullable RankEnum rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Novice: 0-100 kills Adept: 100-500 kills Expert: 500-2000 kills Master: 2000-5000 kills Legend: 5000-10000 kills 
   * @return rank
   */
  
  @Schema(name = "rank", description = "Novice: 0-100 kills Adept: 100-500 kills Expert: 500-2000 kills Master: 2000-5000 kills Legend: 5000-10000 kills ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable RankEnum getRank() {
    return rank;
  }

  public void setRank(@Nullable RankEnum rank) {
    this.rank = rank;
  }

  public WeaponMasteryProgress totalKills(@Nullable Integer totalKills) {
    this.totalKills = totalKills;
    return this;
  }

  /**
   * Get totalKills
   * @return totalKills
   */
  
  @Schema(name = "total_kills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_kills")
  public @Nullable Integer getTotalKills() {
    return totalKills;
  }

  public void setTotalKills(@Nullable Integer totalKills) {
    this.totalKills = totalKills;
  }

  public WeaponMasteryProgress killsToNextRank(@Nullable Integer killsToNextRank) {
    this.killsToNextRank = killsToNextRank;
    return this;
  }

  /**
   * Get killsToNextRank
   * @return killsToNextRank
   */
  
  @Schema(name = "kills_to_next_rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kills_to_next_rank")
  public @Nullable Integer getKillsToNextRank() {
    return killsToNextRank;
  }

  public void setKillsToNextRank(@Nullable Integer killsToNextRank) {
    this.killsToNextRank = killsToNextRank;
  }

  public WeaponMasteryProgress bonuses(List<@Valid WeaponMasteryProgressBonusesInner> bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  public WeaponMasteryProgress addBonusesItem(WeaponMasteryProgressBonusesInner bonusesItem) {
    if (this.bonuses == null) {
      this.bonuses = new ArrayList<>();
    }
    this.bonuses.add(bonusesItem);
    return this;
  }

  /**
   * Бонусы от Mastery
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", description = "Бонусы от Mastery", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public List<@Valid WeaponMasteryProgressBonusesInner> getBonuses() {
    return bonuses;
  }

  public void setBonuses(List<@Valid WeaponMasteryProgressBonusesInner> bonuses) {
    this.bonuses = bonuses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeaponMasteryProgress weaponMasteryProgress = (WeaponMasteryProgress) o;
    return Objects.equals(this.characterId, weaponMasteryProgress.characterId) &&
        Objects.equals(this.weaponClass, weaponMasteryProgress.weaponClass) &&
        Objects.equals(this.rank, weaponMasteryProgress.rank) &&
        Objects.equals(this.totalKills, weaponMasteryProgress.totalKills) &&
        Objects.equals(this.killsToNextRank, weaponMasteryProgress.killsToNextRank) &&
        Objects.equals(this.bonuses, weaponMasteryProgress.bonuses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, weaponClass, rank, totalKills, killsToNextRank, bonuses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeaponMasteryProgress {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    weaponClass: ").append(toIndentedString(weaponClass)).append("\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    totalKills: ").append(toIndentedString(totalKills)).append("\n");
    sb.append("    killsToNextRank: ").append(toIndentedString(killsToNextRank)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
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


