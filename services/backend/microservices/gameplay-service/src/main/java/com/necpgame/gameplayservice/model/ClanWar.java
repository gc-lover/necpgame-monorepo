package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.AllianceMember;
import com.necpgame.gameplayservice.model.ClanWarCasualties;
import com.necpgame.gameplayservice.model.SiegePlan;
import com.necpgame.gameplayservice.model.WarPhase;
import com.necpgame.gameplayservice.model.WarScore;
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
 * ClanWar
 */


public class ClanWar {

  private String warId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    DECLARED("DECLARED"),
    
    PREPARATION("PREPARATION"),
    
    IN_PROGRESS("IN_PROGRESS"),
    
    RESOLVED("RESOLVED"),
    
    CANCELLED("CANCELLED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  private String attackerClanId;

  private String defenderClanId;

  @Valid
  private List<@Valid AllianceMember> allies = new ArrayList<>();

  @Valid
  private List<@Valid WarPhase> timeline = new ArrayList<>();

  private @Nullable WarScore score;

  private @Nullable ClanWarCasualties casualties;

  @Valid
  private List<@Valid SiegePlan> activeSieges = new ArrayList<>();

  private @Nullable Integer economicImpact;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime resolvedAt;

  public ClanWar() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ClanWar(String warId, StatusEnum status, String attackerClanId, String defenderClanId, OffsetDateTime createdAt) {
    this.warId = warId;
    this.status = status;
    this.attackerClanId = attackerClanId;
    this.defenderClanId = defenderClanId;
    this.createdAt = createdAt;
  }

  public ClanWar warId(String warId) {
    this.warId = warId;
    return this;
  }

  /**
   * Get warId
   * @return warId
   */
  @NotNull 
  @Schema(name = "warId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("warId")
  public String getWarId() {
    return warId;
  }

  public void setWarId(String warId) {
    this.warId = warId;
  }

  public ClanWar status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public ClanWar attackerClanId(String attackerClanId) {
    this.attackerClanId = attackerClanId;
    return this;
  }

  /**
   * Get attackerClanId
   * @return attackerClanId
   */
  @NotNull 
  @Schema(name = "attackerClanId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("attackerClanId")
  public String getAttackerClanId() {
    return attackerClanId;
  }

  public void setAttackerClanId(String attackerClanId) {
    this.attackerClanId = attackerClanId;
  }

  public ClanWar defenderClanId(String defenderClanId) {
    this.defenderClanId = defenderClanId;
    return this;
  }

  /**
   * Get defenderClanId
   * @return defenderClanId
   */
  @NotNull 
  @Schema(name = "defenderClanId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("defenderClanId")
  public String getDefenderClanId() {
    return defenderClanId;
  }

  public void setDefenderClanId(String defenderClanId) {
    this.defenderClanId = defenderClanId;
  }

  public ClanWar allies(List<@Valid AllianceMember> allies) {
    this.allies = allies;
    return this;
  }

  public ClanWar addAlliesItem(AllianceMember alliesItem) {
    if (this.allies == null) {
      this.allies = new ArrayList<>();
    }
    this.allies.add(alliesItem);
    return this;
  }

  /**
   * Get allies
   * @return allies
   */
  @Valid 
  @Schema(name = "allies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allies")
  public List<@Valid AllianceMember> getAllies() {
    return allies;
  }

  public void setAllies(List<@Valid AllianceMember> allies) {
    this.allies = allies;
  }

  public ClanWar timeline(List<@Valid WarPhase> timeline) {
    this.timeline = timeline;
    return this;
  }

  public ClanWar addTimelineItem(WarPhase timelineItem) {
    if (this.timeline == null) {
      this.timeline = new ArrayList<>();
    }
    this.timeline.add(timelineItem);
    return this;
  }

  /**
   * Get timeline
   * @return timeline
   */
  @Valid 
  @Schema(name = "timeline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeline")
  public List<@Valid WarPhase> getTimeline() {
    return timeline;
  }

  public void setTimeline(List<@Valid WarPhase> timeline) {
    this.timeline = timeline;
  }

  public ClanWar score(@Nullable WarScore score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  @Valid 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score")
  public @Nullable WarScore getScore() {
    return score;
  }

  public void setScore(@Nullable WarScore score) {
    this.score = score;
  }

  public ClanWar casualties(@Nullable ClanWarCasualties casualties) {
    this.casualties = casualties;
    return this;
  }

  /**
   * Get casualties
   * @return casualties
   */
  @Valid 
  @Schema(name = "casualties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("casualties")
  public @Nullable ClanWarCasualties getCasualties() {
    return casualties;
  }

  public void setCasualties(@Nullable ClanWarCasualties casualties) {
    this.casualties = casualties;
  }

  public ClanWar activeSieges(List<@Valid SiegePlan> activeSieges) {
    this.activeSieges = activeSieges;
    return this;
  }

  public ClanWar addActiveSiegesItem(SiegePlan activeSiegesItem) {
    if (this.activeSieges == null) {
      this.activeSieges = new ArrayList<>();
    }
    this.activeSieges.add(activeSiegesItem);
    return this;
  }

  /**
   * Get activeSieges
   * @return activeSieges
   */
  @Valid 
  @Schema(name = "activeSieges", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeSieges")
  public List<@Valid SiegePlan> getActiveSieges() {
    return activeSieges;
  }

  public void setActiveSieges(List<@Valid SiegePlan> activeSieges) {
    this.activeSieges = activeSieges;
  }

  public ClanWar economicImpact(@Nullable Integer economicImpact) {
    this.economicImpact = economicImpact;
    return this;
  }

  /**
   * Get economicImpact
   * @return economicImpact
   */
  
  @Schema(name = "economicImpact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economicImpact")
  public @Nullable Integer getEconomicImpact() {
    return economicImpact;
  }

  public void setEconomicImpact(@Nullable Integer economicImpact) {
    this.economicImpact = economicImpact;
  }

  public ClanWar createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ClanWar resolvedAt(@Nullable OffsetDateTime resolvedAt) {
    this.resolvedAt = resolvedAt;
    return this;
  }

  /**
   * Get resolvedAt
   * @return resolvedAt
   */
  @Valid 
  @Schema(name = "resolvedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolvedAt")
  public @Nullable OffsetDateTime getResolvedAt() {
    return resolvedAt;
  }

  public void setResolvedAt(@Nullable OffsetDateTime resolvedAt) {
    this.resolvedAt = resolvedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClanWar clanWar = (ClanWar) o;
    return Objects.equals(this.warId, clanWar.warId) &&
        Objects.equals(this.status, clanWar.status) &&
        Objects.equals(this.attackerClanId, clanWar.attackerClanId) &&
        Objects.equals(this.defenderClanId, clanWar.defenderClanId) &&
        Objects.equals(this.allies, clanWar.allies) &&
        Objects.equals(this.timeline, clanWar.timeline) &&
        Objects.equals(this.score, clanWar.score) &&
        Objects.equals(this.casualties, clanWar.casualties) &&
        Objects.equals(this.activeSieges, clanWar.activeSieges) &&
        Objects.equals(this.economicImpact, clanWar.economicImpact) &&
        Objects.equals(this.createdAt, clanWar.createdAt) &&
        Objects.equals(this.resolvedAt, clanWar.resolvedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(warId, status, attackerClanId, defenderClanId, allies, timeline, score, casualties, activeSieges, economicImpact, createdAt, resolvedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClanWar {\n");
    sb.append("    warId: ").append(toIndentedString(warId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    attackerClanId: ").append(toIndentedString(attackerClanId)).append("\n");
    sb.append("    defenderClanId: ").append(toIndentedString(defenderClanId)).append("\n");
    sb.append("    allies: ").append(toIndentedString(allies)).append("\n");
    sb.append("    timeline: ").append(toIndentedString(timeline)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    casualties: ").append(toIndentedString(casualties)).append("\n");
    sb.append("    activeSieges: ").append(toIndentedString(activeSieges)).append("\n");
    sb.append("    economicImpact: ").append(toIndentedString(economicImpact)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    resolvedAt: ").append(toIndentedString(resolvedAt)).append("\n");
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

