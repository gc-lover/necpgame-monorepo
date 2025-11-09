package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * PersonalNPC
 */


public class PersonalNPC {

  private @Nullable String npcId;

  private @Nullable String ownerId;

  private @Nullable String ownerType;

  private @Nullable String name;

  /**
   * Gets or Sets npcType
   */
  public enum NpcTypeEnum {
    HUMAN("human"),
    
    ROBOT("robot"),
    
    ANDROID("android");

    private final String value;

    NpcTypeEnum(String value) {
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
    public static NpcTypeEnum fromValue(String value) {
      for (NpcTypeEnum b : NpcTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable NpcTypeEnum npcType;

  private @Nullable String role;

  private @Nullable Integer rank;

  private @Nullable BigDecimal experience;

  private @Nullable BigDecimal costDaily;

  private @Nullable String currentTask;

  public PersonalNPC npcId(@Nullable String npcId) {
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

  public PersonalNPC ownerId(@Nullable String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * Get ownerId
   * @return ownerId
   */
  
  @Schema(name = "owner_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("owner_id")
  public @Nullable String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(@Nullable String ownerId) {
    this.ownerId = ownerId;
  }

  public PersonalNPC ownerType(@Nullable String ownerType) {
    this.ownerType = ownerType;
    return this;
  }

  /**
   * Get ownerType
   * @return ownerType
   */
  
  @Schema(name = "owner_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("owner_type")
  public @Nullable String getOwnerType() {
    return ownerType;
  }

  public void setOwnerType(@Nullable String ownerType) {
    this.ownerType = ownerType;
  }

  public PersonalNPC name(@Nullable String name) {
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

  public PersonalNPC npcType(@Nullable NpcTypeEnum npcType) {
    this.npcType = npcType;
    return this;
  }

  /**
   * Get npcType
   * @return npcType
   */
  
  @Schema(name = "npc_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_type")
  public @Nullable NpcTypeEnum getNpcType() {
    return npcType;
  }

  public void setNpcType(@Nullable NpcTypeEnum npcType) {
    this.npcType = npcType;
  }

  public PersonalNPC role(@Nullable String role) {
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

  public PersonalNPC rank(@Nullable Integer rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   * @return rank
   */
  
  @Schema(name = "rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable Integer getRank() {
    return rank;
  }

  public void setRank(@Nullable Integer rank) {
    this.rank = rank;
  }

  public PersonalNPC experience(@Nullable BigDecimal experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Get experience
   * @return experience
   */
  @Valid 
  @Schema(name = "experience", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience")
  public @Nullable BigDecimal getExperience() {
    return experience;
  }

  public void setExperience(@Nullable BigDecimal experience) {
    this.experience = experience;
  }

  public PersonalNPC costDaily(@Nullable BigDecimal costDaily) {
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

  public PersonalNPC currentTask(@Nullable String currentTask) {
    this.currentTask = currentTask;
    return this;
  }

  /**
   * Get currentTask
   * @return currentTask
   */
  
  @Schema(name = "current_task", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_task")
  public @Nullable String getCurrentTask() {
    return currentTask;
  }

  public void setCurrentTask(@Nullable String currentTask) {
    this.currentTask = currentTask;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonalNPC personalNPC = (PersonalNPC) o;
    return Objects.equals(this.npcId, personalNPC.npcId) &&
        Objects.equals(this.ownerId, personalNPC.ownerId) &&
        Objects.equals(this.ownerType, personalNPC.ownerType) &&
        Objects.equals(this.name, personalNPC.name) &&
        Objects.equals(this.npcType, personalNPC.npcType) &&
        Objects.equals(this.role, personalNPC.role) &&
        Objects.equals(this.rank, personalNPC.rank) &&
        Objects.equals(this.experience, personalNPC.experience) &&
        Objects.equals(this.costDaily, personalNPC.costDaily) &&
        Objects.equals(this.currentTask, personalNPC.currentTask);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, ownerId, ownerType, name, npcType, role, rank, experience, costDaily, currentTask);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersonalNPC {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    ownerType: ").append(toIndentedString(ownerType)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    npcType: ").append(toIndentedString(npcType)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    costDaily: ").append(toIndentedString(costDaily)).append("\n");
    sb.append("    currentTask: ").append(toIndentedString(currentTask)).append("\n");
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

