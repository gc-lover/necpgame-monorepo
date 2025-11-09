package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
 * NPCContract
 */


public class NPCContract {

  private @Nullable String contractId;

  private @Nullable String characterId;

  private @Nullable String npcId;

  private @Nullable String npcName;

  private @Nullable String role;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endDate;

  private @Nullable BigDecimal costDaily;

  private @Nullable BigDecimal loyaltyLevel;

  private @Nullable BigDecimal performance;

  public NPCContract contractId(@Nullable String contractId) {
    this.contractId = contractId;
    return this;
  }

  /**
   * Get contractId
   * @return contractId
   */
  
  @Schema(name = "contract_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contract_id")
  public @Nullable String getContractId() {
    return contractId;
  }

  public void setContractId(@Nullable String contractId) {
    this.contractId = contractId;
  }

  public NPCContract characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public NPCContract npcId(@Nullable String npcId) {
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

  public NPCContract npcName(@Nullable String npcName) {
    this.npcName = npcName;
    return this;
  }

  /**
   * Get npcName
   * @return npcName
   */
  
  @Schema(name = "npc_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_name")
  public @Nullable String getNpcName() {
    return npcName;
  }

  public void setNpcName(@Nullable String npcName) {
    this.npcName = npcName;
  }

  public NPCContract role(@Nullable String role) {
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

  public NPCContract startDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   * @return startDate
   */
  @Valid 
  @Schema(name = "start_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("start_date")
  public @Nullable OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public NPCContract endDate(@Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Get endDate
   * @return endDate
   */
  @Valid 
  @Schema(name = "end_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("end_date")
  public @Nullable OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(@Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  public NPCContract costDaily(@Nullable BigDecimal costDaily) {
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

  public NPCContract loyaltyLevel(@Nullable BigDecimal loyaltyLevel) {
    this.loyaltyLevel = loyaltyLevel;
    return this;
  }

  /**
   * Уровень лояльности (0-100)
   * @return loyaltyLevel
   */
  @Valid 
  @Schema(name = "loyalty_level", description = "Уровень лояльности (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loyalty_level")
  public @Nullable BigDecimal getLoyaltyLevel() {
    return loyaltyLevel;
  }

  public void setLoyaltyLevel(@Nullable BigDecimal loyaltyLevel) {
    this.loyaltyLevel = loyaltyLevel;
  }

  public NPCContract performance(@Nullable BigDecimal performance) {
    this.performance = performance;
    return this;
  }

  /**
   * Эффективность работы
   * @return performance
   */
  @Valid 
  @Schema(name = "performance", description = "Эффективность работы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performance")
  public @Nullable BigDecimal getPerformance() {
    return performance;
  }

  public void setPerformance(@Nullable BigDecimal performance) {
    this.performance = performance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NPCContract npCContract = (NPCContract) o;
    return Objects.equals(this.contractId, npCContract.contractId) &&
        Objects.equals(this.characterId, npCContract.characterId) &&
        Objects.equals(this.npcId, npCContract.npcId) &&
        Objects.equals(this.npcName, npCContract.npcName) &&
        Objects.equals(this.role, npCContract.role) &&
        Objects.equals(this.startDate, npCContract.startDate) &&
        Objects.equals(this.endDate, npCContract.endDate) &&
        Objects.equals(this.costDaily, npCContract.costDaily) &&
        Objects.equals(this.loyaltyLevel, npCContract.loyaltyLevel) &&
        Objects.equals(this.performance, npCContract.performance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contractId, characterId, npcId, npcName, role, startDate, endDate, costDaily, loyaltyLevel, performance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NPCContract {\n");
    sb.append("    contractId: ").append(toIndentedString(contractId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    npcName: ").append(toIndentedString(npcName)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    costDaily: ").append(toIndentedString(costDaily)).append("\n");
    sb.append("    loyaltyLevel: ").append(toIndentedString(loyaltyLevel)).append("\n");
    sb.append("    performance: ").append(toIndentedString(performance)).append("\n");
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

