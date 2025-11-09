package com.necpgame.lootservice.model;

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
 * LootConfigResponseSeasonalRulesInner
 */

@JsonTypeName("LootConfigResponse_seasonalRules_inner")

public class LootConfigResponseSeasonalRulesInner {

  private @Nullable String seasonId;

  private @Nullable Float bonus;

  public LootConfigResponseSeasonalRulesInner seasonId(@Nullable String seasonId) {
    this.seasonId = seasonId;
    return this;
  }

  /**
   * Get seasonId
   * @return seasonId
   */
  
  @Schema(name = "seasonId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seasonId")
  public @Nullable String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(@Nullable String seasonId) {
    this.seasonId = seasonId;
  }

  public LootConfigResponseSeasonalRulesInner bonus(@Nullable Float bonus) {
    this.bonus = bonus;
    return this;
  }

  /**
   * Get bonus
   * @return bonus
   */
  
  @Schema(name = "bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonus")
  public @Nullable Float getBonus() {
    return bonus;
  }

  public void setBonus(@Nullable Float bonus) {
    this.bonus = bonus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootConfigResponseSeasonalRulesInner lootConfigResponseSeasonalRulesInner = (LootConfigResponseSeasonalRulesInner) o;
    return Objects.equals(this.seasonId, lootConfigResponseSeasonalRulesInner.seasonId) &&
        Objects.equals(this.bonus, lootConfigResponseSeasonalRulesInner.bonus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seasonId, bonus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootConfigResponseSeasonalRulesInner {\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
    sb.append("    bonus: ").append(toIndentedString(bonus)).append("\n");
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

