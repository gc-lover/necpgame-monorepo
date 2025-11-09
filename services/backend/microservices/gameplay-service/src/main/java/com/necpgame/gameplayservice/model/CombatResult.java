package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.CombatResultRewards;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CombatResult
 */


public class CombatResult {

  private Boolean victory;

  private CombatResultRewards rewards;

  private JsonNullable<Object> penalties = JsonNullable.<Object>undefined();

  public CombatResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CombatResult(Boolean victory, CombatResultRewards rewards) {
    this.victory = victory;
    this.rewards = rewards;
  }

  public CombatResult victory(Boolean victory) {
    this.victory = victory;
    return this;
  }

  /**
   * Get victory
   * @return victory
   */
  @NotNull 
  @Schema(name = "victory", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("victory")
  public Boolean getVictory() {
    return victory;
  }

  public void setVictory(Boolean victory) {
    this.victory = victory;
  }

  public CombatResult rewards(CombatResultRewards rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @NotNull @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rewards")
  public CombatResultRewards getRewards() {
    return rewards;
  }

  public void setRewards(CombatResultRewards rewards) {
    this.rewards = rewards;
  }

  public CombatResult penalties(Object penalties) {
    this.penalties = JsonNullable.of(penalties);
    return this;
  }

  /**
   * Get penalties
   * @return penalties
   */
  
  @Schema(name = "penalties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalties")
  public JsonNullable<Object> getPenalties() {
    return penalties;
  }

  public void setPenalties(JsonNullable<Object> penalties) {
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
    CombatResult combatResult = (CombatResult) o;
    return Objects.equals(this.victory, combatResult.victory) &&
        Objects.equals(this.rewards, combatResult.rewards) &&
        equalsNullable(this.penalties, combatResult.penalties);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(victory, rewards, hashCodeNullable(penalties));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatResult {\n");
    sb.append("    victory: ").append(toIndentedString(victory)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
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

