package com.necpgame.worldservice.model;

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
 * WorldEventDcScaling
 */

@JsonTypeName("WorldEvent_dc_scaling")

public class WorldEventDcScaling {

  private @Nullable Integer social;

  private @Nullable Integer techHack;

  private @Nullable Integer combat;

  public WorldEventDcScaling social(@Nullable Integer social) {
    this.social = social;
    return this;
  }

  /**
   * Get social
   * @return social
   */
  
  @Schema(name = "social", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("social")
  public @Nullable Integer getSocial() {
    return social;
  }

  public void setSocial(@Nullable Integer social) {
    this.social = social;
  }

  public WorldEventDcScaling techHack(@Nullable Integer techHack) {
    this.techHack = techHack;
    return this;
  }

  /**
   * Get techHack
   * @return techHack
   */
  
  @Schema(name = "tech_hack", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tech_hack")
  public @Nullable Integer getTechHack() {
    return techHack;
  }

  public void setTechHack(@Nullable Integer techHack) {
    this.techHack = techHack;
  }

  public WorldEventDcScaling combat(@Nullable Integer combat) {
    this.combat = combat;
    return this;
  }

  /**
   * Get combat
   * @return combat
   */
  
  @Schema(name = "combat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combat")
  public @Nullable Integer getCombat() {
    return combat;
  }

  public void setCombat(@Nullable Integer combat) {
    this.combat = combat;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldEventDcScaling worldEventDcScaling = (WorldEventDcScaling) o;
    return Objects.equals(this.social, worldEventDcScaling.social) &&
        Objects.equals(this.techHack, worldEventDcScaling.techHack) &&
        Objects.equals(this.combat, worldEventDcScaling.combat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(social, techHack, combat);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldEventDcScaling {\n");
    sb.append("    social: ").append(toIndentedString(social)).append("\n");
    sb.append("    techHack: ").append(toIndentedString(techHack)).append("\n");
    sb.append("    combat: ").append(toIndentedString(combat)).append("\n");
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

