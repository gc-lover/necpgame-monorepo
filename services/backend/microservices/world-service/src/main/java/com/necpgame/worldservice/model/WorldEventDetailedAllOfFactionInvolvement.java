package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WorldEventDetailedAllOfFactionInvolvement
 */

@JsonTypeName("WorldEventDetailed_allOf_faction_involvement")

public class WorldEventDetailedAllOfFactionInvolvement {

  private @Nullable String faction;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    INSTIGATOR("INSTIGATOR"),
    
    VICTIM("VICTIM"),
    
    BENEFICIARY("BENEFICIARY"),
    
    NEUTRAL("NEUTRAL");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RoleEnum role;

  public WorldEventDetailedAllOfFactionInvolvement faction(@Nullable String faction) {
    this.faction = faction;
    return this;
  }

  /**
   * Get faction
   * @return faction
   */
  
  @Schema(name = "faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public @Nullable String getFaction() {
    return faction;
  }

  public void setFaction(@Nullable String faction) {
    this.faction = faction;
  }

  public WorldEventDetailedAllOfFactionInvolvement role(@Nullable RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable RoleEnum getRole() {
    return role;
  }

  public void setRole(@Nullable RoleEnum role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldEventDetailedAllOfFactionInvolvement worldEventDetailedAllOfFactionInvolvement = (WorldEventDetailedAllOfFactionInvolvement) o;
    return Objects.equals(this.faction, worldEventDetailedAllOfFactionInvolvement.faction) &&
        Objects.equals(this.role, worldEventDetailedAllOfFactionInvolvement.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(faction, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldEventDetailedAllOfFactionInvolvement {\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
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

