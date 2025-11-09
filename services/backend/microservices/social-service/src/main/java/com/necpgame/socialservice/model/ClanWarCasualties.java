package com.necpgame.socialservice.model;

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
 * ClanWarCasualties
 */

@JsonTypeName("ClanWar_casualties")

public class ClanWarCasualties {

  private @Nullable Integer attacker;

  private @Nullable Integer defender;

  public ClanWarCasualties attacker(@Nullable Integer attacker) {
    this.attacker = attacker;
    return this;
  }

  /**
   * Get attacker
   * @return attacker
   */
  
  @Schema(name = "attacker", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attacker")
  public @Nullable Integer getAttacker() {
    return attacker;
  }

  public void setAttacker(@Nullable Integer attacker) {
    this.attacker = attacker;
  }

  public ClanWarCasualties defender(@Nullable Integer defender) {
    this.defender = defender;
    return this;
  }

  /**
   * Get defender
   * @return defender
   */
  
  @Schema(name = "defender", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defender")
  public @Nullable Integer getDefender() {
    return defender;
  }

  public void setDefender(@Nullable Integer defender) {
    this.defender = defender;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClanWarCasualties clanWarCasualties = (ClanWarCasualties) o;
    return Objects.equals(this.attacker, clanWarCasualties.attacker) &&
        Objects.equals(this.defender, clanWarCasualties.defender);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attacker, defender);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClanWarCasualties {\n");
    sb.append("    attacker: ").append(toIndentedString(attacker)).append("\n");
    sb.append("    defender: ").append(toIndentedString(defender)).append("\n");
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

