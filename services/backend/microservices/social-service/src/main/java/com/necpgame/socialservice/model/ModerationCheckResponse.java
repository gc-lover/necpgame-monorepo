package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.Violation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ModerationCheckResponse
 */


public class ModerationCheckResponse {

  private String filteredText;

  @Valid
  private List<@Valid Violation> violations = new ArrayList<>();

  private Float spamScore;

  private Boolean autoBanTriggered = false;

  public ModerationCheckResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ModerationCheckResponse(String filteredText, List<@Valid Violation> violations, Float spamScore) {
    this.filteredText = filteredText;
    this.violations = violations;
    this.spamScore = spamScore;
  }

  public ModerationCheckResponse filteredText(String filteredText) {
    this.filteredText = filteredText;
    return this;
  }

  /**
   * Get filteredText
   * @return filteredText
   */
  @NotNull 
  @Schema(name = "filteredText", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("filteredText")
  public String getFilteredText() {
    return filteredText;
  }

  public void setFilteredText(String filteredText) {
    this.filteredText = filteredText;
  }

  public ModerationCheckResponse violations(List<@Valid Violation> violations) {
    this.violations = violations;
    return this;
  }

  public ModerationCheckResponse addViolationsItem(Violation violationsItem) {
    if (this.violations == null) {
      this.violations = new ArrayList<>();
    }
    this.violations.add(violationsItem);
    return this;
  }

  /**
   * Get violations
   * @return violations
   */
  @NotNull @Valid 
  @Schema(name = "violations", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("violations")
  public List<@Valid Violation> getViolations() {
    return violations;
  }

  public void setViolations(List<@Valid Violation> violations) {
    this.violations = violations;
  }

  public ModerationCheckResponse spamScore(Float spamScore) {
    this.spamScore = spamScore;
    return this;
  }

  /**
   * Get spamScore
   * minimum: 0
   * maximum: 1
   * @return spamScore
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "spamScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("spamScore")
  public Float getSpamScore() {
    return spamScore;
  }

  public void setSpamScore(Float spamScore) {
    this.spamScore = spamScore;
  }

  public ModerationCheckResponse autoBanTriggered(Boolean autoBanTriggered) {
    this.autoBanTriggered = autoBanTriggered;
    return this;
  }

  /**
   * Get autoBanTriggered
   * @return autoBanTriggered
   */
  
  @Schema(name = "autoBanTriggered", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoBanTriggered")
  public Boolean getAutoBanTriggered() {
    return autoBanTriggered;
  }

  public void setAutoBanTriggered(Boolean autoBanTriggered) {
    this.autoBanTriggered = autoBanTriggered;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModerationCheckResponse moderationCheckResponse = (ModerationCheckResponse) o;
    return Objects.equals(this.filteredText, moderationCheckResponse.filteredText) &&
        Objects.equals(this.violations, moderationCheckResponse.violations) &&
        Objects.equals(this.spamScore, moderationCheckResponse.spamScore) &&
        Objects.equals(this.autoBanTriggered, moderationCheckResponse.autoBanTriggered);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filteredText, violations, spamScore, autoBanTriggered);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModerationCheckResponse {\n");
    sb.append("    filteredText: ").append(toIndentedString(filteredText)).append("\n");
    sb.append("    violations: ").append(toIndentedString(violations)).append("\n");
    sb.append("    spamScore: ").append(toIndentedString(spamScore)).append("\n");
    sb.append("    autoBanTriggered: ").append(toIndentedString(autoBanTriggered)).append("\n");
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

