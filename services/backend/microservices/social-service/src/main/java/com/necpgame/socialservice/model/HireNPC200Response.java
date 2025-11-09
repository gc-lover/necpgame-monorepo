package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * HireNPC200Response
 */

@JsonTypeName("hireNPC_200_response")

public class HireNPC200Response {

  private @Nullable Boolean success;

  private @Nullable String contractId;

  private @Nullable String npcId;

  private @Nullable BigDecimal costTotal;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endDate;

  public HireNPC200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public HireNPC200Response contractId(@Nullable String contractId) {
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

  public HireNPC200Response npcId(@Nullable String npcId) {
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

  public HireNPC200Response costTotal(@Nullable BigDecimal costTotal) {
    this.costTotal = costTotal;
    return this;
  }

  /**
   * Get costTotal
   * @return costTotal
   */
  @Valid 
  @Schema(name = "cost_total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost_total")
  public @Nullable BigDecimal getCostTotal() {
    return costTotal;
  }

  public void setCostTotal(@Nullable BigDecimal costTotal) {
    this.costTotal = costTotal;
  }

  public HireNPC200Response startDate(@Nullable OffsetDateTime startDate) {
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

  public HireNPC200Response endDate(@Nullable OffsetDateTime endDate) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HireNPC200Response hireNPC200Response = (HireNPC200Response) o;
    return Objects.equals(this.success, hireNPC200Response.success) &&
        Objects.equals(this.contractId, hireNPC200Response.contractId) &&
        Objects.equals(this.npcId, hireNPC200Response.npcId) &&
        Objects.equals(this.costTotal, hireNPC200Response.costTotal) &&
        Objects.equals(this.startDate, hireNPC200Response.startDate) &&
        Objects.equals(this.endDate, hireNPC200Response.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, contractId, npcId, costTotal, startDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HireNPC200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    contractId: ").append(toIndentedString(contractId)).append("\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    costTotal: ").append(toIndentedString(costTotal)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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

