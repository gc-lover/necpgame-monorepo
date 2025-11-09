package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RollParticipantBonuses
 */

@JsonTypeName("RollParticipant_bonuses")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RollParticipantBonuses {

  private @Nullable BigDecimal guildBonus;

  private @Nullable BigDecimal streakBonus;

  private @Nullable BigDecimal badLuckBonus;

  public RollParticipantBonuses guildBonus(@Nullable BigDecimal guildBonus) {
    this.guildBonus = guildBonus;
    return this;
  }

  /**
   * Get guildBonus
   * @return guildBonus
   */
  @Valid 
  @Schema(name = "guildBonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guildBonus")
  public @Nullable BigDecimal getGuildBonus() {
    return guildBonus;
  }

  public void setGuildBonus(@Nullable BigDecimal guildBonus) {
    this.guildBonus = guildBonus;
  }

  public RollParticipantBonuses streakBonus(@Nullable BigDecimal streakBonus) {
    this.streakBonus = streakBonus;
    return this;
  }

  /**
   * Get streakBonus
   * @return streakBonus
   */
  @Valid 
  @Schema(name = "streakBonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("streakBonus")
  public @Nullable BigDecimal getStreakBonus() {
    return streakBonus;
  }

  public void setStreakBonus(@Nullable BigDecimal streakBonus) {
    this.streakBonus = streakBonus;
  }

  public RollParticipantBonuses badLuckBonus(@Nullable BigDecimal badLuckBonus) {
    this.badLuckBonus = badLuckBonus;
    return this;
  }

  /**
   * Get badLuckBonus
   * @return badLuckBonus
   */
  @Valid 
  @Schema(name = "badLuckBonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("badLuckBonus")
  public @Nullable BigDecimal getBadLuckBonus() {
    return badLuckBonus;
  }

  public void setBadLuckBonus(@Nullable BigDecimal badLuckBonus) {
    this.badLuckBonus = badLuckBonus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RollParticipantBonuses rollParticipantBonuses = (RollParticipantBonuses) o;
    return Objects.equals(this.guildBonus, rollParticipantBonuses.guildBonus) &&
        Objects.equals(this.streakBonus, rollParticipantBonuses.streakBonus) &&
        Objects.equals(this.badLuckBonus, rollParticipantBonuses.badLuckBonus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildBonus, streakBonus, badLuckBonus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollParticipantBonuses {\n");
    sb.append("    guildBonus: ").append(toIndentedString(guildBonus)).append("\n");
    sb.append("    streakBonus: ").append(toIndentedString(streakBonus)).append("\n");
    sb.append("    badLuckBonus: ").append(toIndentedString(badLuckBonus)).append("\n");
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

