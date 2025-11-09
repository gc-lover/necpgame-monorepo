package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * EnableMarginTrading200Response
 */

@JsonTypeName("enableMarginTrading_200_response")

public class EnableMarginTrading200Response {

  private @Nullable Boolean approved;

  private @Nullable BigDecimal leverageLimit;

  private @Nullable BigDecimal marginRequirement;

  public EnableMarginTrading200Response approved(@Nullable Boolean approved) {
    this.approved = approved;
    return this;
  }

  /**
   * Get approved
   * @return approved
   */
  
  @Schema(name = "approved", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approved")
  public @Nullable Boolean getApproved() {
    return approved;
  }

  public void setApproved(@Nullable Boolean approved) {
    this.approved = approved;
  }

  public EnableMarginTrading200Response leverageLimit(@Nullable BigDecimal leverageLimit) {
    this.leverageLimit = leverageLimit;
    return this;
  }

  /**
   * Максимальное плечо (2x, 3x, etc.)
   * @return leverageLimit
   */
  @Valid 
  @Schema(name = "leverage_limit", description = "Максимальное плечо (2x, 3x, etc.)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leverage_limit")
  public @Nullable BigDecimal getLeverageLimit() {
    return leverageLimit;
  }

  public void setLeverageLimit(@Nullable BigDecimal leverageLimit) {
    this.leverageLimit = leverageLimit;
  }

  public EnableMarginTrading200Response marginRequirement(@Nullable BigDecimal marginRequirement) {
    this.marginRequirement = marginRequirement;
    return this;
  }

  /**
   * Get marginRequirement
   * @return marginRequirement
   */
  @Valid 
  @Schema(name = "margin_requirement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("margin_requirement")
  public @Nullable BigDecimal getMarginRequirement() {
    return marginRequirement;
  }

  public void setMarginRequirement(@Nullable BigDecimal marginRequirement) {
    this.marginRequirement = marginRequirement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnableMarginTrading200Response enableMarginTrading200Response = (EnableMarginTrading200Response) o;
    return Objects.equals(this.approved, enableMarginTrading200Response.approved) &&
        Objects.equals(this.leverageLimit, enableMarginTrading200Response.leverageLimit) &&
        Objects.equals(this.marginRequirement, enableMarginTrading200Response.marginRequirement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approved, leverageLimit, marginRequirement);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnableMarginTrading200Response {\n");
    sb.append("    approved: ").append(toIndentedString(approved)).append("\n");
    sb.append("    leverageLimit: ").append(toIndentedString(leverageLimit)).append("\n");
    sb.append("    marginRequirement: ").append(toIndentedString(marginRequirement)).append("\n");
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

