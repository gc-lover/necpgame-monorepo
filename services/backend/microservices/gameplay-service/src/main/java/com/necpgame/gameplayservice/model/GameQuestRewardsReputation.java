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
 * Изменение репутации
 */

@Schema(name = "GameQuestRewards_reputation", description = "Изменение репутации")
@JsonTypeName("GameQuestRewards_reputation")

public class GameQuestRewardsReputation {

  private @Nullable String faction;

  private @Nullable Integer amount;

  public GameQuestRewardsReputation faction(@Nullable String faction) {
    this.faction = faction;
    return this;
  }

  /**
   * Фракция, с которой изменяется репутация
   * @return faction
   */
  
  @Schema(name = "faction", example = "ncpd", description = "Фракция, с которой изменяется репутация", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public @Nullable String getFaction() {
    return faction;
  }

  public void setFaction(@Nullable String faction) {
    this.faction = faction;
  }

  public GameQuestRewardsReputation amount(@Nullable Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Изменение репутации (может быть отрицательным)
   * @return amount
   */
  
  @Schema(name = "amount", example = "5", description = "Изменение репутации (может быть отрицательным)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable Integer getAmount() {
    return amount;
  }

  public void setAmount(@Nullable Integer amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameQuestRewardsReputation gameQuestRewardsReputation = (GameQuestRewardsReputation) o;
    return Objects.equals(this.faction, gameQuestRewardsReputation.faction) &&
        Objects.equals(this.amount, gameQuestRewardsReputation.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(faction, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameQuestRewardsReputation {\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

