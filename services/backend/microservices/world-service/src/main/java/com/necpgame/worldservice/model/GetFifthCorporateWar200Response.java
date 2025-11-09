package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetFifthCorporateWar200Response
 */

@JsonTypeName("getFifthCorporateWar_200_response")

public class GetFifthCorporateWar200Response {

  private @Nullable String warPeriod;

  @Valid
  private List<String> participants = new ArrayList<>();

  @Valid
  private List<String> battles = new ArrayList<>();

  @Valid
  private List<String> heroes = new ArrayList<>();

  @Valid
  private List<String> victims = new ArrayList<>();

  private @Nullable String outcome;

  public GetFifthCorporateWar200Response warPeriod(@Nullable String warPeriod) {
    this.warPeriod = warPeriod;
    return this;
  }

  /**
   * Get warPeriod
   * @return warPeriod
   */
  
  @Schema(name = "war_period", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("war_period")
  public @Nullable String getWarPeriod() {
    return warPeriod;
  }

  public void setWarPeriod(@Nullable String warPeriod) {
    this.warPeriod = warPeriod;
  }

  public GetFifthCorporateWar200Response participants(List<String> participants) {
    this.participants = participants;
    return this;
  }

  public GetFifthCorporateWar200Response addParticipantsItem(String participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
   */
  
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<String> getParticipants() {
    return participants;
  }

  public void setParticipants(List<String> participants) {
    this.participants = participants;
  }

  public GetFifthCorporateWar200Response battles(List<String> battles) {
    this.battles = battles;
    return this;
  }

  public GetFifthCorporateWar200Response addBattlesItem(String battlesItem) {
    if (this.battles == null) {
      this.battles = new ArrayList<>();
    }
    this.battles.add(battlesItem);
    return this;
  }

  /**
   * Get battles
   * @return battles
   */
  
  @Schema(name = "battles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("battles")
  public List<String> getBattles() {
    return battles;
  }

  public void setBattles(List<String> battles) {
    this.battles = battles;
  }

  public GetFifthCorporateWar200Response heroes(List<String> heroes) {
    this.heroes = heroes;
    return this;
  }

  public GetFifthCorporateWar200Response addHeroesItem(String heroesItem) {
    if (this.heroes == null) {
      this.heroes = new ArrayList<>();
    }
    this.heroes.add(heroesItem);
    return this;
  }

  /**
   * Get heroes
   * @return heroes
   */
  
  @Schema(name = "heroes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heroes")
  public List<String> getHeroes() {
    return heroes;
  }

  public void setHeroes(List<String> heroes) {
    this.heroes = heroes;
  }

  public GetFifthCorporateWar200Response victims(List<String> victims) {
    this.victims = victims;
    return this;
  }

  public GetFifthCorporateWar200Response addVictimsItem(String victimsItem) {
    if (this.victims == null) {
      this.victims = new ArrayList<>();
    }
    this.victims.add(victimsItem);
    return this;
  }

  /**
   * Get victims
   * @return victims
   */
  
  @Schema(name = "victims", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("victims")
  public List<String> getVictims() {
    return victims;
  }

  public void setVictims(List<String> victims) {
    this.victims = victims;
  }

  public GetFifthCorporateWar200Response outcome(@Nullable String outcome) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFifthCorporateWar200Response getFifthCorporateWar200Response = (GetFifthCorporateWar200Response) o;
    return Objects.equals(this.warPeriod, getFifthCorporateWar200Response.warPeriod) &&
        Objects.equals(this.participants, getFifthCorporateWar200Response.participants) &&
        Objects.equals(this.battles, getFifthCorporateWar200Response.battles) &&
        Objects.equals(this.heroes, getFifthCorporateWar200Response.heroes) &&
        Objects.equals(this.victims, getFifthCorporateWar200Response.victims) &&
        Objects.equals(this.outcome, getFifthCorporateWar200Response.outcome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(warPeriod, participants, battles, heroes, victims, outcome);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFifthCorporateWar200Response {\n");
    sb.append("    warPeriod: ").append(toIndentedString(warPeriod)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    battles: ").append(toIndentedString(battles)).append("\n");
    sb.append("    heroes: ").append(toIndentedString(heroes)).append("\n");
    sb.append("    victims: ").append(toIndentedString(victims)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
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

