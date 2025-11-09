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
 * ParticipantResultStats
 */

@JsonTypeName("ParticipantResult_stats")

public class ParticipantResultStats {

  private @Nullable Integer damageDealt;

  private @Nullable Integer damageTaken;

  private @Nullable Integer kills;

  private @Nullable Integer deaths;

  private @Nullable Integer headshots;

  private @Nullable Integer abilitiesUsed;

  public ParticipantResultStats damageDealt(@Nullable Integer damageDealt) {
    this.damageDealt = damageDealt;
    return this;
  }

  /**
   * Get damageDealt
   * @return damageDealt
   */
  
  @Schema(name = "damage_dealt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage_dealt")
  public @Nullable Integer getDamageDealt() {
    return damageDealt;
  }

  public void setDamageDealt(@Nullable Integer damageDealt) {
    this.damageDealt = damageDealt;
  }

  public ParticipantResultStats damageTaken(@Nullable Integer damageTaken) {
    this.damageTaken = damageTaken;
    return this;
  }

  /**
   * Get damageTaken
   * @return damageTaken
   */
  
  @Schema(name = "damage_taken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage_taken")
  public @Nullable Integer getDamageTaken() {
    return damageTaken;
  }

  public void setDamageTaken(@Nullable Integer damageTaken) {
    this.damageTaken = damageTaken;
  }

  public ParticipantResultStats kills(@Nullable Integer kills) {
    this.kills = kills;
    return this;
  }

  /**
   * Get kills
   * @return kills
   */
  
  @Schema(name = "kills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kills")
  public @Nullable Integer getKills() {
    return kills;
  }

  public void setKills(@Nullable Integer kills) {
    this.kills = kills;
  }

  public ParticipantResultStats deaths(@Nullable Integer deaths) {
    this.deaths = deaths;
    return this;
  }

  /**
   * Get deaths
   * @return deaths
   */
  
  @Schema(name = "deaths", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deaths")
  public @Nullable Integer getDeaths() {
    return deaths;
  }

  public void setDeaths(@Nullable Integer deaths) {
    this.deaths = deaths;
  }

  public ParticipantResultStats headshots(@Nullable Integer headshots) {
    this.headshots = headshots;
    return this;
  }

  /**
   * Get headshots
   * @return headshots
   */
  
  @Schema(name = "headshots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("headshots")
  public @Nullable Integer getHeadshots() {
    return headshots;
  }

  public void setHeadshots(@Nullable Integer headshots) {
    this.headshots = headshots;
  }

  public ParticipantResultStats abilitiesUsed(@Nullable Integer abilitiesUsed) {
    this.abilitiesUsed = abilitiesUsed;
    return this;
  }

  /**
   * Get abilitiesUsed
   * @return abilitiesUsed
   */
  
  @Schema(name = "abilities_used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilities_used")
  public @Nullable Integer getAbilitiesUsed() {
    return abilitiesUsed;
  }

  public void setAbilitiesUsed(@Nullable Integer abilitiesUsed) {
    this.abilitiesUsed = abilitiesUsed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParticipantResultStats participantResultStats = (ParticipantResultStats) o;
    return Objects.equals(this.damageDealt, participantResultStats.damageDealt) &&
        Objects.equals(this.damageTaken, participantResultStats.damageTaken) &&
        Objects.equals(this.kills, participantResultStats.kills) &&
        Objects.equals(this.deaths, participantResultStats.deaths) &&
        Objects.equals(this.headshots, participantResultStats.headshots) &&
        Objects.equals(this.abilitiesUsed, participantResultStats.abilitiesUsed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(damageDealt, damageTaken, kills, deaths, headshots, abilitiesUsed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParticipantResultStats {\n");
    sb.append("    damageDealt: ").append(toIndentedString(damageDealt)).append("\n");
    sb.append("    damageTaken: ").append(toIndentedString(damageTaken)).append("\n");
    sb.append("    kills: ").append(toIndentedString(kills)).append("\n");
    sb.append("    deaths: ").append(toIndentedString(deaths)).append("\n");
    sb.append("    headshots: ").append(toIndentedString(headshots)).append("\n");
    sb.append("    abilitiesUsed: ").append(toIndentedString(abilitiesUsed)).append("\n");
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

