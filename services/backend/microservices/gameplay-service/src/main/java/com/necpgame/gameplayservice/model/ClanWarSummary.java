package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
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
 * ClanWarSummary
 */


public class ClanWarSummary {

  private @Nullable String warId;

  private @Nullable String attackerClanId;

  private @Nullable String defenderClanId;

  private @Nullable String status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime preparationEndsAt;

  private @Nullable Integer scheduledSieges;

  public ClanWarSummary warId(@Nullable String warId) {
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

  public ClanWarSummary attackerClanId(@Nullable String attackerClanId) {
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

  public ClanWarSummary defenderClanId(@Nullable String defenderClanId) {
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

  public ClanWarSummary status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  public ClanWarSummary preparationEndsAt(@Nullable OffsetDateTime preparationEndsAt) {
    this.preparationEndsAt = preparationEndsAt;
    return this;
  }

  /**
   * Get preparationEndsAt
   * @return preparationEndsAt
   */
  @Valid 
  @Schema(name = "preparationEndsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preparationEndsAt")
  public @Nullable OffsetDateTime getPreparationEndsAt() {
    return preparationEndsAt;
  }

  public void setPreparationEndsAt(@Nullable OffsetDateTime preparationEndsAt) {
    this.preparationEndsAt = preparationEndsAt;
  }

  public ClanWarSummary scheduledSieges(@Nullable Integer scheduledSieges) {
    this.scheduledSieges = scheduledSieges;
    return this;
  }

  /**
   * Get scheduledSieges
   * @return scheduledSieges
   */
  
  @Schema(name = "scheduledSieges", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledSieges")
  public @Nullable Integer getScheduledSieges() {
    return scheduledSieges;
  }

  public void setScheduledSieges(@Nullable Integer scheduledSieges) {
    this.scheduledSieges = scheduledSieges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClanWarSummary clanWarSummary = (ClanWarSummary) o;
    return Objects.equals(this.warId, clanWarSummary.warId) &&
        Objects.equals(this.attackerClanId, clanWarSummary.attackerClanId) &&
        Objects.equals(this.defenderClanId, clanWarSummary.defenderClanId) &&
        Objects.equals(this.status, clanWarSummary.status) &&
        Objects.equals(this.preparationEndsAt, clanWarSummary.preparationEndsAt) &&
        Objects.equals(this.scheduledSieges, clanWarSummary.scheduledSieges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(warId, attackerClanId, defenderClanId, status, preparationEndsAt, scheduledSieges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClanWarSummary {\n");
    sb.append("    warId: ").append(toIndentedString(warId)).append("\n");
    sb.append("    attackerClanId: ").append(toIndentedString(attackerClanId)).append("\n");
    sb.append("    defenderClanId: ").append(toIndentedString(defenderClanId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    preparationEndsAt: ").append(toIndentedString(preparationEndsAt)).append("\n");
    sb.append("    scheduledSieges: ").append(toIndentedString(scheduledSieges)).append("\n");
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

