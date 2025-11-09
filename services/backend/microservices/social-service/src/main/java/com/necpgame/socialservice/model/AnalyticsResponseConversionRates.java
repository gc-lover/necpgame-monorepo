package com.necpgame.socialservice.model;

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
 * AnalyticsResponseConversionRates
 */

@JsonTypeName("AnalyticsResponse_conversionRates")

public class AnalyticsResponseConversionRates {

  private @Nullable BigDecimal inviteToRegisterPercent;

  private @Nullable BigDecimal registerToCompletePercent;

  public AnalyticsResponseConversionRates inviteToRegisterPercent(@Nullable BigDecimal inviteToRegisterPercent) {
    this.inviteToRegisterPercent = inviteToRegisterPercent;
    return this;
  }

  /**
   * Get inviteToRegisterPercent
   * @return inviteToRegisterPercent
   */
  @Valid 
  @Schema(name = "inviteToRegisterPercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inviteToRegisterPercent")
  public @Nullable BigDecimal getInviteToRegisterPercent() {
    return inviteToRegisterPercent;
  }

  public void setInviteToRegisterPercent(@Nullable BigDecimal inviteToRegisterPercent) {
    this.inviteToRegisterPercent = inviteToRegisterPercent;
  }

  public AnalyticsResponseConversionRates registerToCompletePercent(@Nullable BigDecimal registerToCompletePercent) {
    this.registerToCompletePercent = registerToCompletePercent;
    return this;
  }

  /**
   * Get registerToCompletePercent
   * @return registerToCompletePercent
   */
  @Valid 
  @Schema(name = "registerToCompletePercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("registerToCompletePercent")
  public @Nullable BigDecimal getRegisterToCompletePercent() {
    return registerToCompletePercent;
  }

  public void setRegisterToCompletePercent(@Nullable BigDecimal registerToCompletePercent) {
    this.registerToCompletePercent = registerToCompletePercent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsResponseConversionRates analyticsResponseConversionRates = (AnalyticsResponseConversionRates) o;
    return Objects.equals(this.inviteToRegisterPercent, analyticsResponseConversionRates.inviteToRegisterPercent) &&
        Objects.equals(this.registerToCompletePercent, analyticsResponseConversionRates.registerToCompletePercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inviteToRegisterPercent, registerToCompletePercent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsResponseConversionRates {\n");
    sb.append("    inviteToRegisterPercent: ").append(toIndentedString(inviteToRegisterPercent)).append("\n");
    sb.append("    registerToCompletePercent: ").append(toIndentedString(registerToCompletePercent)).append("\n");
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

