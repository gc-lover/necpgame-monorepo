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
 * ParticipantStats
 */

@JsonTypeName("Participant_stats")

public class ParticipantStats {

  private @Nullable Integer damageDealt;

  private @Nullable Integer damageTaken;

  private @Nullable Integer kills;

  private @Nullable Integer deaths;

  public ParticipantStats damageDealt(@Nullable Integer damageDealt) {
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

  public ParticipantStats damageTaken(@Nullable Integer damageTaken) {
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

  public ParticipantStats kills(@Nullable Integer kills) {
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

  public ParticipantStats deaths(@Nullable Integer deaths) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParticipantStats participantStats = (ParticipantStats) o;
    return Objects.equals(this.damageDealt, participantStats.damageDealt) &&
        Objects.equals(this.damageTaken, participantStats.damageTaken) &&
        Objects.equals(this.kills, participantStats.kills) &&
        Objects.equals(this.deaths, participantStats.deaths);
  }

  @Override
  public int hashCode() {
    return Objects.hash(damageDealt, damageTaken, kills, deaths);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParticipantStats {\n");
    sb.append("    damageDealt: ").append(toIndentedString(damageDealt)).append("\n");
    sb.append("    damageTaken: ").append(toIndentedString(damageTaken)).append("\n");
    sb.append("    kills: ").append(toIndentedString(kills)).append("\n");
    sb.append("    deaths: ").append(toIndentedString(deaths)).append("\n");
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

