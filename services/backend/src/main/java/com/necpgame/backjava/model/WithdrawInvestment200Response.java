package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * WithdrawInvestment200Response
 */

@JsonTypeName("withdrawInvestment_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:01:47.984013400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class WithdrawInvestment200Response {

  private @Nullable Integer withdrawnAmount;

  private @Nullable Integer penalty;

  private @Nullable Integer netAmount;

  private @Nullable BigDecimal roi;

  public WithdrawInvestment200Response withdrawnAmount(@Nullable Integer withdrawnAmount) {
    this.withdrawnAmount = withdrawnAmount;
    return this;
  }

  /**
   * Get withdrawnAmount
   * @return withdrawnAmount
   */
  
  @Schema(name = "withdrawn_amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("withdrawn_amount")
  public @Nullable Integer getWithdrawnAmount() {
    return withdrawnAmount;
  }

  public void setWithdrawnAmount(@Nullable Integer withdrawnAmount) {
    this.withdrawnAmount = withdrawnAmount;
  }

  public WithdrawInvestment200Response penalty(@Nullable Integer penalty) {
    this.penalty = penalty;
    return this;
  }

  /**
   * Get penalty
   * @return penalty
   */
  
  @Schema(name = "penalty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalty")
  public @Nullable Integer getPenalty() {
    return penalty;
  }

  public void setPenalty(@Nullable Integer penalty) {
    this.penalty = penalty;
  }

  public WithdrawInvestment200Response netAmount(@Nullable Integer netAmount) {
    this.netAmount = netAmount;
    return this;
  }

  /**
   * Get netAmount
   * @return netAmount
   */
  
  @Schema(name = "net_amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("net_amount")
  public @Nullable Integer getNetAmount() {
    return netAmount;
  }

  public void setNetAmount(@Nullable Integer netAmount) {
    this.netAmount = netAmount;
  }

  public WithdrawInvestment200Response roi(@Nullable BigDecimal roi) {
    this.roi = roi;
    return this;
  }

  /**
   * Get roi
   * @return roi
   */
  @Valid 
  @Schema(name = "roi", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roi")
  public @Nullable BigDecimal getRoi() {
    return roi;
  }

  public void setRoi(@Nullable BigDecimal roi) {
    this.roi = roi;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WithdrawInvestment200Response withdrawInvestment200Response = (WithdrawInvestment200Response) o;
    return Objects.equals(this.withdrawnAmount, withdrawInvestment200Response.withdrawnAmount) &&
        Objects.equals(this.penalty, withdrawInvestment200Response.penalty) &&
        Objects.equals(this.netAmount, withdrawInvestment200Response.netAmount) &&
        Objects.equals(this.roi, withdrawInvestment200Response.roi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(withdrawnAmount, penalty, netAmount, roi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WithdrawInvestment200Response {\n");
    sb.append("    withdrawnAmount: ").append(toIndentedString(withdrawnAmount)).append("\n");
    sb.append("    penalty: ").append(toIndentedString(penalty)).append("\n");
    sb.append("    netAmount: ").append(toIndentedString(netAmount)).append("\n");
    sb.append("    roi: ").append(toIndentedString(roi)).append("\n");
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

