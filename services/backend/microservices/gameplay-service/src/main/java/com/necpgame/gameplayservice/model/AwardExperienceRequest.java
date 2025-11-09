package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AwardExperienceRequest
 */


public class AwardExperienceRequest {

  private Integer amount;

  private String source;

  private Float multiplier = 1.0f;

  private @Nullable String reason;

  public AwardExperienceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AwardExperienceRequest(Integer amount, String source) {
    this.amount = amount;
    this.source = source;
  }

  public AwardExperienceRequest amount(Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * minimum: 0
   * @return amount
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "amount", example = "5000", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public AwardExperienceRequest source(String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @NotNull 
  @Schema(name = "source", example = "quest_completion", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public AwardExperienceRequest multiplier(Float multiplier) {
    this.multiplier = multiplier;
    return this;
  }

  /**
   * Множитель (например, бонус за premium)
   * @return multiplier
   */
  
  @Schema(name = "multiplier", description = "Множитель (например, бонус за premium)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("multiplier")
  public Float getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(Float multiplier) {
    this.multiplier = multiplier;
  }

  public AwardExperienceRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", example = "Completed quest: First Contact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AwardExperienceRequest awardExperienceRequest = (AwardExperienceRequest) o;
    return Objects.equals(this.amount, awardExperienceRequest.amount) &&
        Objects.equals(this.source, awardExperienceRequest.source) &&
        Objects.equals(this.multiplier, awardExperienceRequest.multiplier) &&
        Objects.equals(this.reason, awardExperienceRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, source, multiplier, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AwardExperienceRequest {\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    multiplier: ").append(toIndentedString(multiplier)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

