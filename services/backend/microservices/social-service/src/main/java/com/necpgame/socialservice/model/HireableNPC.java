package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
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
 * HireableNPC
 */


public class HireableNPC {

  private @Nullable String npcId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    COMBAT("combat"),
    
    VENDOR("vendor"),
    
    SPECIALIST("specialist");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable String role;

  private @Nullable Integer tier;

  private @Nullable BigDecimal costDaily;

  private @Nullable BigDecimal reputationRequired;

  private @Nullable Object skills;

  @Valid
  private List<String> abilities = new ArrayList<>();

  public HireableNPC npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public HireableNPC name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public HireableNPC type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public HireableNPC role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public HireableNPC tier(@Nullable Integer tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * minimum: 1
   * maximum: 5
   * @return tier
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable Integer getTier() {
    return tier;
  }

  public void setTier(@Nullable Integer tier) {
    this.tier = tier;
  }

  public HireableNPC costDaily(@Nullable BigDecimal costDaily) {
    this.costDaily = costDaily;
    return this;
  }

  /**
   * Get costDaily
   * @return costDaily
   */
  @Valid 
  @Schema(name = "cost_daily", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost_daily")
  public @Nullable BigDecimal getCostDaily() {
    return costDaily;
  }

  public void setCostDaily(@Nullable BigDecimal costDaily) {
    this.costDaily = costDaily;
  }

  public HireableNPC reputationRequired(@Nullable BigDecimal reputationRequired) {
    this.reputationRequired = reputationRequired;
    return this;
  }

  /**
   * Get reputationRequired
   * @return reputationRequired
   */
  @Valid 
  @Schema(name = "reputation_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_required")
  public @Nullable BigDecimal getReputationRequired() {
    return reputationRequired;
  }

  public void setReputationRequired(@Nullable BigDecimal reputationRequired) {
    this.reputationRequired = reputationRequired;
  }

  public HireableNPC skills(@Nullable Object skills) {
    this.skills = skills;
    return this;
  }

  /**
   * Навыки NPC
   * @return skills
   */
  
  @Schema(name = "skills", description = "Навыки NPC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skills")
  public @Nullable Object getSkills() {
    return skills;
  }

  public void setSkills(@Nullable Object skills) {
    this.skills = skills;
  }

  public HireableNPC abilities(List<String> abilities) {
    this.abilities = abilities;
    return this;
  }

  public HireableNPC addAbilitiesItem(String abilitiesItem) {
    if (this.abilities == null) {
      this.abilities = new ArrayList<>();
    }
    this.abilities.add(abilitiesItem);
    return this;
  }

  /**
   * Get abilities
   * @return abilities
   */
  
  @Schema(name = "abilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilities")
  public List<String> getAbilities() {
    return abilities;
  }

  public void setAbilities(List<String> abilities) {
    this.abilities = abilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HireableNPC hireableNPC = (HireableNPC) o;
    return Objects.equals(this.npcId, hireableNPC.npcId) &&
        Objects.equals(this.name, hireableNPC.name) &&
        Objects.equals(this.type, hireableNPC.type) &&
        Objects.equals(this.role, hireableNPC.role) &&
        Objects.equals(this.tier, hireableNPC.tier) &&
        Objects.equals(this.costDaily, hireableNPC.costDaily) &&
        Objects.equals(this.reputationRequired, hireableNPC.reputationRequired) &&
        Objects.equals(this.skills, hireableNPC.skills) &&
        Objects.equals(this.abilities, hireableNPC.abilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, name, type, role, tier, costDaily, reputationRequired, skills, abilities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HireableNPC {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    costDaily: ").append(toIndentedString(costDaily)).append("\n");
    sb.append("    reputationRequired: ").append(toIndentedString(reputationRequired)).append("\n");
    sb.append("    skills: ").append(toIndentedString(skills)).append("\n");
    sb.append("    abilities: ").append(toIndentedString(abilities)).append("\n");
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

