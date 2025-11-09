package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * StartCEOFight200Response
 */

@JsonTypeName("startCEOFight_200_response")

public class StartCEOFight200Response {

  private @Nullable String raidId;

  private @Nullable String bossId;

  private @Nullable String bossName;

  private String phase = "ceo_boss";

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime fightStartedAt;

  public StartCEOFight200Response raidId(@Nullable String raidId) {
    this.raidId = raidId;
    return this;
  }

  /**
   * Get raidId
   * @return raidId
   */
  
  @Schema(name = "raid_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("raid_id")
  public @Nullable String getRaidId() {
    return raidId;
  }

  public void setRaidId(@Nullable String raidId) {
    this.raidId = raidId;
  }

  public StartCEOFight200Response bossId(@Nullable String bossId) {
    this.bossId = bossId;
    return this;
  }

  /**
   * Get bossId
   * @return bossId
   */
  
  @Schema(name = "boss_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boss_id")
  public @Nullable String getBossId() {
    return bossId;
  }

  public void setBossId(@Nullable String bossId) {
    this.bossId = bossId;
  }

  public StartCEOFight200Response bossName(@Nullable String bossName) {
    this.bossName = bossName;
    return this;
  }

  /**
   * Get bossName
   * @return bossName
   */
  
  @Schema(name = "boss_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boss_name")
  public @Nullable String getBossName() {
    return bossName;
  }

  public void setBossName(@Nullable String bossName) {
    this.bossName = bossName;
  }

  public StartCEOFight200Response phase(String phase) {
    this.phase = phase;
    return this;
  }

  /**
   * Get phase
   * @return phase
   */
  
  @Schema(name = "phase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phase")
  public String getPhase() {
    return phase;
  }

  public void setPhase(String phase) {
    this.phase = phase;
  }

  public StartCEOFight200Response fightStartedAt(@Nullable OffsetDateTime fightStartedAt) {
    this.fightStartedAt = fightStartedAt;
    return this;
  }

  /**
   * Get fightStartedAt
   * @return fightStartedAt
   */
  @Valid 
  @Schema(name = "fight_started_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fight_started_at")
  public @Nullable OffsetDateTime getFightStartedAt() {
    return fightStartedAt;
  }

  public void setFightStartedAt(@Nullable OffsetDateTime fightStartedAt) {
    this.fightStartedAt = fightStartedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartCEOFight200Response startCEOFight200Response = (StartCEOFight200Response) o;
    return Objects.equals(this.raidId, startCEOFight200Response.raidId) &&
        Objects.equals(this.bossId, startCEOFight200Response.bossId) &&
        Objects.equals(this.bossName, startCEOFight200Response.bossName) &&
        Objects.equals(this.phase, startCEOFight200Response.phase) &&
        Objects.equals(this.fightStartedAt, startCEOFight200Response.fightStartedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raidId, bossId, bossName, phase, fightStartedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartCEOFight200Response {\n");
    sb.append("    raidId: ").append(toIndentedString(raidId)).append("\n");
    sb.append("    bossId: ").append(toIndentedString(bossId)).append("\n");
    sb.append("    bossName: ").append(toIndentedString(bossName)).append("\n");
    sb.append("    phase: ").append(toIndentedString(phase)).append("\n");
    sb.append("    fightStartedAt: ").append(toIndentedString(fightStartedAt)).append("\n");
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

