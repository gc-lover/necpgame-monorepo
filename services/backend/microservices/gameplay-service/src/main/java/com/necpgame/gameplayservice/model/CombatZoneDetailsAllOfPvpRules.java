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
 * CombatZoneDetailsAllOfPvpRules
 */

@JsonTypeName("CombatZoneDetails_allOf_pvp_rules")

public class CombatZoneDetailsAllOfPvpRules {

  private @Nullable Boolean enabled;

  private @Nullable Boolean factionOnly;

  private @Nullable Boolean fullLoot;

  public CombatZoneDetailsAllOfPvpRules enabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public @Nullable Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
  }

  public CombatZoneDetailsAllOfPvpRules factionOnly(@Nullable Boolean factionOnly) {
    this.factionOnly = factionOnly;
    return this;
  }

  /**
   * Get factionOnly
   * @return factionOnly
   */
  
  @Schema(name = "faction_only", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_only")
  public @Nullable Boolean getFactionOnly() {
    return factionOnly;
  }

  public void setFactionOnly(@Nullable Boolean factionOnly) {
    this.factionOnly = factionOnly;
  }

  public CombatZoneDetailsAllOfPvpRules fullLoot(@Nullable Boolean fullLoot) {
    this.fullLoot = fullLoot;
    return this;
  }

  /**
   * Get fullLoot
   * @return fullLoot
   */
  
  @Schema(name = "full_loot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("full_loot")
  public @Nullable Boolean getFullLoot() {
    return fullLoot;
  }

  public void setFullLoot(@Nullable Boolean fullLoot) {
    this.fullLoot = fullLoot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatZoneDetailsAllOfPvpRules combatZoneDetailsAllOfPvpRules = (CombatZoneDetailsAllOfPvpRules) o;
    return Objects.equals(this.enabled, combatZoneDetailsAllOfPvpRules.enabled) &&
        Objects.equals(this.factionOnly, combatZoneDetailsAllOfPvpRules.factionOnly) &&
        Objects.equals(this.fullLoot, combatZoneDetailsAllOfPvpRules.fullLoot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, factionOnly, fullLoot);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatZoneDetailsAllOfPvpRules {\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    factionOnly: ").append(toIndentedString(factionOnly)).append("\n");
    sb.append("    fullLoot: ").append(toIndentedString(fullLoot)).append("\n");
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

