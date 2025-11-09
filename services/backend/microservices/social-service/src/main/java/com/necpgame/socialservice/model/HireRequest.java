package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HireRequest
 */


public class HireRequest {

  private UUID characterId;

  private String npcId;

  private Integer contractDurationDays;

  private @Nullable Integer offeredSalaryPerDay;

  private JsonNullable<Object> bonuses = JsonNullable.<Object>undefined();

  public HireRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HireRequest(UUID characterId, String npcId, Integer contractDurationDays) {
    this.characterId = characterId;
    this.npcId = npcId;
    this.contractDurationDays = contractDurationDays;
  }

  public HireRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public HireRequest npcId(String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  @NotNull 
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npc_id")
  public String getNpcId() {
    return npcId;
  }

  public void setNpcId(String npcId) {
    this.npcId = npcId;
  }

  public HireRequest contractDurationDays(Integer contractDurationDays) {
    this.contractDurationDays = contractDurationDays;
    return this;
  }

  /**
   * Get contractDurationDays
   * @return contractDurationDays
   */
  @NotNull 
  @Schema(name = "contract_duration_days", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contract_duration_days")
  public Integer getContractDurationDays() {
    return contractDurationDays;
  }

  public void setContractDurationDays(Integer contractDurationDays) {
    this.contractDurationDays = contractDurationDays;
  }

  public HireRequest offeredSalaryPerDay(@Nullable Integer offeredSalaryPerDay) {
    this.offeredSalaryPerDay = offeredSalaryPerDay;
    return this;
  }

  /**
   * Get offeredSalaryPerDay
   * @return offeredSalaryPerDay
   */
  
  @Schema(name = "offered_salary_per_day", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("offered_salary_per_day")
  public @Nullable Integer getOfferedSalaryPerDay() {
    return offeredSalaryPerDay;
  }

  public void setOfferedSalaryPerDay(@Nullable Integer offeredSalaryPerDay) {
    this.offeredSalaryPerDay = offeredSalaryPerDay;
  }

  public HireRequest bonuses(Object bonuses) {
    this.bonuses = JsonNullable.of(bonuses);
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public JsonNullable<Object> getBonuses() {
    return bonuses;
  }

  public void setBonuses(JsonNullable<Object> bonuses) {
    this.bonuses = bonuses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HireRequest hireRequest = (HireRequest) o;
    return Objects.equals(this.characterId, hireRequest.characterId) &&
        Objects.equals(this.npcId, hireRequest.npcId) &&
        Objects.equals(this.contractDurationDays, hireRequest.contractDurationDays) &&
        Objects.equals(this.offeredSalaryPerDay, hireRequest.offeredSalaryPerDay) &&
        equalsNullable(this.bonuses, hireRequest.bonuses);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, npcId, contractDurationDays, offeredSalaryPerDay, hashCodeNullable(bonuses));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HireRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    contractDurationDays: ").append(toIndentedString(contractDurationDays)).append("\n");
    sb.append("    offeredSalaryPerDay: ").append(toIndentedString(offeredSalaryPerDay)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
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

