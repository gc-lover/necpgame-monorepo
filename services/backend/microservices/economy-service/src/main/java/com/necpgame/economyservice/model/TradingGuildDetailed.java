package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.TradingGuildDetailedAllOfBonuses;
import com.necpgame.economyservice.model.TradingGuildDetailedAllOfGuildMaster;
import com.necpgame.economyservice.model.TradingGuildDetailedAllOfUpgrades;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TradingGuildDetailed
 */


public class TradingGuildDetailed {

  private @Nullable UUID guildId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    MERCHANT("MERCHANT"),
    
    CRAFTSMAN("CRAFTSMAN"),
    
    TRANSPORT("TRANSPORT"),
    
    FINANCIAL("FINANCIAL"),
    
    MIXED("MIXED");

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

  private @Nullable Integer level;

  private @Nullable Integer memberCount;

  private @Nullable String headquartersLocation;

  private @Nullable Integer reputation;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  private @Nullable TradingGuildDetailedAllOfGuildMaster guildMaster;

  private @Nullable String description;

  @Valid
  private List<String> specialization = new ArrayList<>();

  private @Nullable TradingGuildDetailedAllOfBonuses bonuses;

  private @Nullable TradingGuildDetailedAllOfUpgrades upgrades;

  public TradingGuildDetailed guildId(@Nullable UUID guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  @Valid 
  @Schema(name = "guild_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_id")
  public @Nullable UUID getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable UUID guildId) {
    this.guildId = guildId;
  }

  public TradingGuildDetailed name(@Nullable String name) {
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

  public TradingGuildDetailed type(@Nullable TypeEnum type) {
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

  public TradingGuildDetailed level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * minimum: 1
   * maximum: 5
   * @return level
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public TradingGuildDetailed memberCount(@Nullable Integer memberCount) {
    this.memberCount = memberCount;
    return this;
  }

  /**
   * Get memberCount
   * @return memberCount
   */
  
  @Schema(name = "member_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("member_count")
  public @Nullable Integer getMemberCount() {
    return memberCount;
  }

  public void setMemberCount(@Nullable Integer memberCount) {
    this.memberCount = memberCount;
  }

  public TradingGuildDetailed headquartersLocation(@Nullable String headquartersLocation) {
    this.headquartersLocation = headquartersLocation;
    return this;
  }

  /**
   * Get headquartersLocation
   * @return headquartersLocation
   */
  
  @Schema(name = "headquarters_location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("headquarters_location")
  public @Nullable String getHeadquartersLocation() {
    return headquartersLocation;
  }

  public void setHeadquartersLocation(@Nullable String headquartersLocation) {
    this.headquartersLocation = headquartersLocation;
  }

  public TradingGuildDetailed reputation(@Nullable Integer reputation) {
    this.reputation = reputation;
    return this;
  }

  /**
   * Get reputation
   * @return reputation
   */
  
  @Schema(name = "reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation")
  public @Nullable Integer getReputation() {
    return reputation;
  }

  public void setReputation(@Nullable Integer reputation) {
    this.reputation = reputation;
  }

  public TradingGuildDetailed createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public TradingGuildDetailed guildMaster(@Nullable TradingGuildDetailedAllOfGuildMaster guildMaster) {
    this.guildMaster = guildMaster;
    return this;
  }

  /**
   * Get guildMaster
   * @return guildMaster
   */
  @Valid 
  @Schema(name = "guild_master", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_master")
  public @Nullable TradingGuildDetailedAllOfGuildMaster getGuildMaster() {
    return guildMaster;
  }

  public void setGuildMaster(@Nullable TradingGuildDetailedAllOfGuildMaster guildMaster) {
    this.guildMaster = guildMaster;
  }

  public TradingGuildDetailed description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public TradingGuildDetailed specialization(List<String> specialization) {
    this.specialization = specialization;
    return this;
  }

  public TradingGuildDetailed addSpecializationItem(String specializationItem) {
    if (this.specialization == null) {
      this.specialization = new ArrayList<>();
    }
    this.specialization.add(specializationItem);
    return this;
  }

  /**
   * Get specialization
   * @return specialization
   */
  
  @Schema(name = "specialization", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("specialization")
  public List<String> getSpecialization() {
    return specialization;
  }

  public void setSpecialization(List<String> specialization) {
    this.specialization = specialization;
  }

  public TradingGuildDetailed bonuses(@Nullable TradingGuildDetailedAllOfBonuses bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public @Nullable TradingGuildDetailedAllOfBonuses getBonuses() {
    return bonuses;
  }

  public void setBonuses(@Nullable TradingGuildDetailedAllOfBonuses bonuses) {
    this.bonuses = bonuses;
  }

  public TradingGuildDetailed upgrades(@Nullable TradingGuildDetailedAllOfUpgrades upgrades) {
    this.upgrades = upgrades;
    return this;
  }

  /**
   * Get upgrades
   * @return upgrades
   */
  @Valid 
  @Schema(name = "upgrades", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upgrades")
  public @Nullable TradingGuildDetailedAllOfUpgrades getUpgrades() {
    return upgrades;
  }

  public void setUpgrades(@Nullable TradingGuildDetailedAllOfUpgrades upgrades) {
    this.upgrades = upgrades;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradingGuildDetailed tradingGuildDetailed = (TradingGuildDetailed) o;
    return Objects.equals(this.guildId, tradingGuildDetailed.guildId) &&
        Objects.equals(this.name, tradingGuildDetailed.name) &&
        Objects.equals(this.type, tradingGuildDetailed.type) &&
        Objects.equals(this.level, tradingGuildDetailed.level) &&
        Objects.equals(this.memberCount, tradingGuildDetailed.memberCount) &&
        Objects.equals(this.headquartersLocation, tradingGuildDetailed.headquartersLocation) &&
        Objects.equals(this.reputation, tradingGuildDetailed.reputation) &&
        Objects.equals(this.createdAt, tradingGuildDetailed.createdAt) &&
        Objects.equals(this.guildMaster, tradingGuildDetailed.guildMaster) &&
        Objects.equals(this.description, tradingGuildDetailed.description) &&
        Objects.equals(this.specialization, tradingGuildDetailed.specialization) &&
        Objects.equals(this.bonuses, tradingGuildDetailed.bonuses) &&
        Objects.equals(this.upgrades, tradingGuildDetailed.upgrades);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildId, name, type, level, memberCount, headquartersLocation, reputation, createdAt, guildMaster, description, specialization, bonuses, upgrades);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradingGuildDetailed {\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    memberCount: ").append(toIndentedString(memberCount)).append("\n");
    sb.append("    headquartersLocation: ").append(toIndentedString(headquartersLocation)).append("\n");
    sb.append("    reputation: ").append(toIndentedString(reputation)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    guildMaster: ").append(toIndentedString(guildMaster)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    specialization: ").append(toIndentedString(specialization)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    upgrades: ").append(toIndentedString(upgrades)).append("\n");
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

