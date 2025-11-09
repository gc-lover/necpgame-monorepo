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
 * EraSettingsDcScaling
 */

@JsonTypeName("EraSettings_dc_scaling")

public class EraSettingsDcScaling {

  @Valid
  private List<Integer> social = new ArrayList<>();

  @Valid
  private List<Integer> techHack = new ArrayList<>();

  @Valid
  private List<Integer> combat = new ArrayList<>();

  public EraSettingsDcScaling social(List<Integer> social) {
    this.social = social;
    return this;
  }

  public EraSettingsDcScaling addSocialItem(Integer socialItem) {
    if (this.social == null) {
      this.social = new ArrayList<>();
    }
    this.social.add(socialItem);
    return this;
  }

  /**
   * Get social
   * @return social
   */
  
  @Schema(name = "social", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("social")
  public List<Integer> getSocial() {
    return social;
  }

  public void setSocial(List<Integer> social) {
    this.social = social;
  }

  public EraSettingsDcScaling techHack(List<Integer> techHack) {
    this.techHack = techHack;
    return this;
  }

  public EraSettingsDcScaling addTechHackItem(Integer techHackItem) {
    if (this.techHack == null) {
      this.techHack = new ArrayList<>();
    }
    this.techHack.add(techHackItem);
    return this;
  }

  /**
   * Get techHack
   * @return techHack
   */
  
  @Schema(name = "tech_hack", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tech_hack")
  public List<Integer> getTechHack() {
    return techHack;
  }

  public void setTechHack(List<Integer> techHack) {
    this.techHack = techHack;
  }

  public EraSettingsDcScaling combat(List<Integer> combat) {
    this.combat = combat;
    return this;
  }

  public EraSettingsDcScaling addCombatItem(Integer combatItem) {
    if (this.combat == null) {
      this.combat = new ArrayList<>();
    }
    this.combat.add(combatItem);
    return this;
  }

  /**
   * Get combat
   * @return combat
   */
  
  @Schema(name = "combat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combat")
  public List<Integer> getCombat() {
    return combat;
  }

  public void setCombat(List<Integer> combat) {
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
    EraSettingsDcScaling eraSettingsDcScaling = (EraSettingsDcScaling) o;
    return Objects.equals(this.social, eraSettingsDcScaling.social) &&
        Objects.equals(this.techHack, eraSettingsDcScaling.techHack) &&
        Objects.equals(this.combat, eraSettingsDcScaling.combat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(social, techHack, combat);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EraSettingsDcScaling {\n");
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

