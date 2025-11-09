package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * StartProductionRequest
 */


public class StartProductionRequest {

  private UUID characterId;

  private String chainId;

  private Integer stageNumber;

  private Integer quantity = 1;

  private JsonNullable<String> facilityId = JsonNullable.<String>undefined();

  @Valid
  private List<UUID> useBonuses = new ArrayList<>();

  public StartProductionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StartProductionRequest(UUID characterId, String chainId, Integer stageNumber) {
    this.characterId = characterId;
    this.chainId = chainId;
    this.stageNumber = stageNumber;
  }

  public StartProductionRequest characterId(UUID characterId) {
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

  public StartProductionRequest chainId(String chainId) {
    this.chainId = chainId;
    return this;
  }

  /**
   * Get chainId
   * @return chainId
   */
  @NotNull 
  @Schema(name = "chain_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("chain_id")
  public String getChainId() {
    return chainId;
  }

  public void setChainId(String chainId) {
    this.chainId = chainId;
  }

  public StartProductionRequest stageNumber(Integer stageNumber) {
    this.stageNumber = stageNumber;
    return this;
  }

  /**
   * Get stageNumber
   * @return stageNumber
   */
  @NotNull 
  @Schema(name = "stage_number", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stage_number")
  public Integer getStageNumber() {
    return stageNumber;
  }

  public void setStageNumber(Integer stageNumber) {
    this.stageNumber = stageNumber;
  }

  public StartProductionRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public StartProductionRequest facilityId(String facilityId) {
    this.facilityId = JsonNullable.of(facilityId);
    return this;
  }

  /**
   * Get facilityId
   * @return facilityId
   */
  
  @Schema(name = "facility_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("facility_id")
  public JsonNullable<String> getFacilityId() {
    return facilityId;
  }

  public void setFacilityId(JsonNullable<String> facilityId) {
    this.facilityId = facilityId;
  }

  public StartProductionRequest useBonuses(List<UUID> useBonuses) {
    this.useBonuses = useBonuses;
    return this;
  }

  public StartProductionRequest addUseBonusesItem(UUID useBonusesItem) {
    if (this.useBonuses == null) {
      this.useBonuses = new ArrayList<>();
    }
    this.useBonuses.add(useBonusesItem);
    return this;
  }

  /**
   * Get useBonuses
   * @return useBonuses
   */
  @Valid 
  @Schema(name = "use_bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("use_bonuses")
  public List<UUID> getUseBonuses() {
    return useBonuses;
  }

  public void setUseBonuses(List<UUID> useBonuses) {
    this.useBonuses = useBonuses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartProductionRequest startProductionRequest = (StartProductionRequest) o;
    return Objects.equals(this.characterId, startProductionRequest.characterId) &&
        Objects.equals(this.chainId, startProductionRequest.chainId) &&
        Objects.equals(this.stageNumber, startProductionRequest.stageNumber) &&
        Objects.equals(this.quantity, startProductionRequest.quantity) &&
        equalsNullable(this.facilityId, startProductionRequest.facilityId) &&
        Objects.equals(this.useBonuses, startProductionRequest.useBonuses);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, chainId, stageNumber, quantity, hashCodeNullable(facilityId), useBonuses);
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
    sb.append("class StartProductionRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    stageNumber: ").append(toIndentedString(stageNumber)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    facilityId: ").append(toIndentedString(facilityId)).append("\n");
    sb.append("    useBonuses: ").append(toIndentedString(useBonuses)).append("\n");
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

