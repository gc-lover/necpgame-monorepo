package com.necpgame.worldservice.model;

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
 * ActionXpEntryContext
 */

@JsonTypeName("ActionXpEntry_context")

public class ActionXpEntryContext {

  private @Nullable String combatMode;

  private @Nullable String zoneType;

  private @Nullable BigDecimal fatigueModifier;

  public ActionXpEntryContext combatMode(@Nullable String combatMode) {
    this.combatMode = combatMode;
    return this;
  }

  /**
   * Get combatMode
   * @return combatMode
   */
  
  @Schema(name = "combatMode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combatMode")
  public @Nullable String getCombatMode() {
    return combatMode;
  }

  public void setCombatMode(@Nullable String combatMode) {
    this.combatMode = combatMode;
  }

  public ActionXpEntryContext zoneType(@Nullable String zoneType) {
    this.zoneType = zoneType;
    return this;
  }

  /**
   * Get zoneType
   * @return zoneType
   */
  
  @Schema(name = "zoneType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zoneType")
  public @Nullable String getZoneType() {
    return zoneType;
  }

  public void setZoneType(@Nullable String zoneType) {
    this.zoneType = zoneType;
  }

  public ActionXpEntryContext fatigueModifier(@Nullable BigDecimal fatigueModifier) {
    this.fatigueModifier = fatigueModifier;
    return this;
  }

  /**
   * Get fatigueModifier
   * @return fatigueModifier
   */
  @Valid 
  @Schema(name = "fatigueModifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fatigueModifier")
  public @Nullable BigDecimal getFatigueModifier() {
    return fatigueModifier;
  }

  public void setFatigueModifier(@Nullable BigDecimal fatigueModifier) {
    this.fatigueModifier = fatigueModifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionXpEntryContext actionXpEntryContext = (ActionXpEntryContext) o;
    return Objects.equals(this.combatMode, actionXpEntryContext.combatMode) &&
        Objects.equals(this.zoneType, actionXpEntryContext.zoneType) &&
        Objects.equals(this.fatigueModifier, actionXpEntryContext.fatigueModifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(combatMode, zoneType, fatigueModifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionXpEntryContext {\n");
    sb.append("    combatMode: ").append(toIndentedString(combatMode)).append("\n");
    sb.append("    zoneType: ").append(toIndentedString(zoneType)).append("\n");
    sb.append("    fatigueModifier: ").append(toIndentedString(fatigueModifier)).append("\n");
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

