package com.necpgame.narrativeservice.model;

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
 * FactionQuestsResponseReputationRewardsInner
 */

@JsonTypeName("FactionQuestsResponse_reputation_rewards_inner")

public class FactionQuestsResponseReputationRewardsInner {

  private @Nullable String faction;

  private @Nullable Integer reputationChange;

  public FactionQuestsResponseReputationRewardsInner faction(@Nullable String faction) {
    this.faction = faction;
    return this;
  }

  /**
   * Get faction
   * @return faction
   */
  
  @Schema(name = "faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public @Nullable String getFaction() {
    return faction;
  }

  public void setFaction(@Nullable String faction) {
    this.faction = faction;
  }

  public FactionQuestsResponseReputationRewardsInner reputationChange(@Nullable Integer reputationChange) {
    this.reputationChange = reputationChange;
    return this;
  }

  /**
   * Get reputationChange
   * @return reputationChange
   */
  
  @Schema(name = "reputation_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_change")
  public @Nullable Integer getReputationChange() {
    return reputationChange;
  }

  public void setReputationChange(@Nullable Integer reputationChange) {
    this.reputationChange = reputationChange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionQuestsResponseReputationRewardsInner factionQuestsResponseReputationRewardsInner = (FactionQuestsResponseReputationRewardsInner) o;
    return Objects.equals(this.faction, factionQuestsResponseReputationRewardsInner.faction) &&
        Objects.equals(this.reputationChange, factionQuestsResponseReputationRewardsInner.reputationChange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(faction, reputationChange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionQuestsResponseReputationRewardsInner {\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    reputationChange: ").append(toIndentedString(reputationChange)).append("\n");
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

