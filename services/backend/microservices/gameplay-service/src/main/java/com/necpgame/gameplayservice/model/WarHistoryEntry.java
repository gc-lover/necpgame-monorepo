package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.TerritoryChange;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WarHistoryEntry
 */


public class WarHistoryEntry {

  private @Nullable String warId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endedAt;

  private @Nullable String attackerClanId;

  private @Nullable String defenderClanId;

  private @Nullable String outcome;

  @Valid
  private List<@Valid TerritoryChange> territoryChanges = new ArrayList<>();

  public WarHistoryEntry warId(@Nullable String warId) {
    this.warId = warId;
    return this;
  }

  /**
   * Get warId
   * @return warId
   */
  
  @Schema(name = "warId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warId")
  public @Nullable String getWarId() {
    return warId;
  }

  public void setWarId(@Nullable String warId) {
    this.warId = warId;
  }

  public WarHistoryEntry startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "startedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startedAt")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public WarHistoryEntry endedAt(@Nullable OffsetDateTime endedAt) {
    this.endedAt = endedAt;
    return this;
  }

  /**
   * Get endedAt
   * @return endedAt
   */
  @Valid 
  @Schema(name = "endedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endedAt")
  public @Nullable OffsetDateTime getEndedAt() {
    return endedAt;
  }

  public void setEndedAt(@Nullable OffsetDateTime endedAt) {
    this.endedAt = endedAt;
  }

  public WarHistoryEntry attackerClanId(@Nullable String attackerClanId) {
    this.attackerClanId = attackerClanId;
    return this;
  }

  /**
   * Get attackerClanId
   * @return attackerClanId
   */
  
  @Schema(name = "attackerClanId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attackerClanId")
  public @Nullable String getAttackerClanId() {
    return attackerClanId;
  }

  public void setAttackerClanId(@Nullable String attackerClanId) {
    this.attackerClanId = attackerClanId;
  }

  public WarHistoryEntry defenderClanId(@Nullable String defenderClanId) {
    this.defenderClanId = defenderClanId;
    return this;
  }

  /**
   * Get defenderClanId
   * @return defenderClanId
   */
  
  @Schema(name = "defenderClanId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defenderClanId")
  public @Nullable String getDefenderClanId() {
    return defenderClanId;
  }

  public void setDefenderClanId(@Nullable String defenderClanId) {
    this.defenderClanId = defenderClanId;
  }

  public WarHistoryEntry outcome(@Nullable String outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome")
  public @Nullable String getOutcome() {
    return outcome;
  }

  public void setOutcome(@Nullable String outcome) {
    this.outcome = outcome;
  }

  public WarHistoryEntry territoryChanges(List<@Valid TerritoryChange> territoryChanges) {
    this.territoryChanges = territoryChanges;
    return this;
  }

  public WarHistoryEntry addTerritoryChangesItem(TerritoryChange territoryChangesItem) {
    if (this.territoryChanges == null) {
      this.territoryChanges = new ArrayList<>();
    }
    this.territoryChanges.add(territoryChangesItem);
    return this;
  }

  /**
   * Get territoryChanges
   * @return territoryChanges
   */
  @Valid 
  @Schema(name = "territoryChanges", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("territoryChanges")
  public List<@Valid TerritoryChange> getTerritoryChanges() {
    return territoryChanges;
  }

  public void setTerritoryChanges(List<@Valid TerritoryChange> territoryChanges) {
    this.territoryChanges = territoryChanges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarHistoryEntry warHistoryEntry = (WarHistoryEntry) o;
    return Objects.equals(this.warId, warHistoryEntry.warId) &&
        Objects.equals(this.startedAt, warHistoryEntry.startedAt) &&
        Objects.equals(this.endedAt, warHistoryEntry.endedAt) &&
        Objects.equals(this.attackerClanId, warHistoryEntry.attackerClanId) &&
        Objects.equals(this.defenderClanId, warHistoryEntry.defenderClanId) &&
        Objects.equals(this.outcome, warHistoryEntry.outcome) &&
        Objects.equals(this.territoryChanges, warHistoryEntry.territoryChanges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(warId, startedAt, endedAt, attackerClanId, defenderClanId, outcome, territoryChanges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarHistoryEntry {\n");
    sb.append("    warId: ").append(toIndentedString(warId)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    endedAt: ").append(toIndentedString(endedAt)).append("\n");
    sb.append("    attackerClanId: ").append(toIndentedString(attackerClanId)).append("\n");
    sb.append("    defenderClanId: ").append(toIndentedString(defenderClanId)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    territoryChanges: ").append(toIndentedString(territoryChanges)).append("\n");
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

