package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * InvestRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:01:47.984013400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class InvestRequest {

  private UUID characterId;

  private String opportunityId;

  private Integer amount;

  public InvestRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InvestRequest(UUID characterId, String opportunityId, Integer amount) {
    this.characterId = characterId;
    this.opportunityId = opportunityId;
    this.amount = amount;
  }

  public InvestRequest characterId(UUID characterId) {
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

  public InvestRequest opportunityId(String opportunityId) {
    this.opportunityId = opportunityId;
    return this;
  }

  /**
   * Get opportunityId
   * @return opportunityId
   */
  @NotNull 
  @Schema(name = "opportunity_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("opportunity_id")
  public String getOpportunityId() {
    return opportunityId;
  }

  public void setOpportunityId(String opportunityId) {
    this.opportunityId = opportunityId;
  }

  public InvestRequest amount(Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  @NotNull 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvestRequest investRequest = (InvestRequest) o;
    return Objects.equals(this.characterId, investRequest.characterId) &&
        Objects.equals(this.opportunityId, investRequest.opportunityId) &&
        Objects.equals(this.amount, investRequest.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, opportunityId, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InvestRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    opportunityId: ").append(toIndentedString(opportunityId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

