package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.PlayerOrderBudgetFactors;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EstimatePlayerOrderBudgetRequest
 */

@JsonTypeName("estimatePlayerOrderBudget_request")

public class EstimatePlayerOrderBudgetRequest {

  private PlayerOrderBudgetFactors factors;

  private @Nullable Boolean corporateOrder;

  private @Nullable UUID factionId;

  public EstimatePlayerOrderBudgetRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EstimatePlayerOrderBudgetRequest(PlayerOrderBudgetFactors factors) {
    this.factors = factors;
  }

  public EstimatePlayerOrderBudgetRequest factors(PlayerOrderBudgetFactors factors) {
    this.factors = factors;
    return this;
  }

  /**
   * Get factors
   * @return factors
   */
  @NotNull @Valid 
  @Schema(name = "factors", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("factors")
  public PlayerOrderBudgetFactors getFactors() {
    return factors;
  }

  public void setFactors(PlayerOrderBudgetFactors factors) {
    this.factors = factors;
  }

  public EstimatePlayerOrderBudgetRequest corporateOrder(@Nullable Boolean corporateOrder) {
    this.corporateOrder = corporateOrder;
    return this;
  }

  /**
   * Признак корпоративного заказа (влияет на коэффициенты).
   * @return corporateOrder
   */
  
  @Schema(name = "corporateOrder", description = "Признак корпоративного заказа (влияет на коэффициенты).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("corporateOrder")
  public @Nullable Boolean getCorporateOrder() {
    return corporateOrder;
  }

  public void setCorporateOrder(@Nullable Boolean corporateOrder) {
    this.corporateOrder = corporateOrder;
  }

  public EstimatePlayerOrderBudgetRequest factionId(@Nullable UUID factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Корпоративный идентификатор для запросов factions-service.
   * @return factionId
   */
  @Valid 
  @Schema(name = "factionId", description = "Корпоративный идентификатор для запросов factions-service.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionId")
  public @Nullable UUID getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable UUID factionId) {
    this.factionId = factionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EstimatePlayerOrderBudgetRequest estimatePlayerOrderBudgetRequest = (EstimatePlayerOrderBudgetRequest) o;
    return Objects.equals(this.factors, estimatePlayerOrderBudgetRequest.factors) &&
        Objects.equals(this.corporateOrder, estimatePlayerOrderBudgetRequest.corporateOrder) &&
        Objects.equals(this.factionId, estimatePlayerOrderBudgetRequest.factionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factors, corporateOrder, factionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstimatePlayerOrderBudgetRequest {\n");
    sb.append("    factors: ").append(toIndentedString(factors)).append("\n");
    sb.append("    corporateOrder: ").append(toIndentedString(corporateOrder)).append("\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
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

