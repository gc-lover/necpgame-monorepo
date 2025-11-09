package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.BudgetWarning;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * BudgetComparisonResponse
 */


public class BudgetComparisonResponse {

  private BigDecimal proposed;

  private BigDecimal recommended;

  private BigDecimal median;

  private BigDecimal deviationPercent;

  @Valid
  private List<@Valid BudgetWarning> warnings = new ArrayList<>();

  private @Nullable Boolean requiresAcknowledgement;

  private JsonNullable<String> acknowledgmentToken = JsonNullable.<String>undefined();

  public BudgetComparisonResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BudgetComparisonResponse(BigDecimal proposed, BigDecimal recommended, BigDecimal median, BigDecimal deviationPercent, List<@Valid BudgetWarning> warnings) {
    this.proposed = proposed;
    this.recommended = recommended;
    this.median = median;
    this.deviationPercent = deviationPercent;
    this.warnings = warnings;
  }

  public BudgetComparisonResponse proposed(BigDecimal proposed) {
    this.proposed = proposed;
    return this;
  }

  /**
   * Предложенный бюджет.
   * @return proposed
   */
  @NotNull @Valid 
  @Schema(name = "proposed", description = "Предложенный бюджет.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("proposed")
  public BigDecimal getProposed() {
    return proposed;
  }

  public void setProposed(BigDecimal proposed) {
    this.proposed = proposed;
  }

  public BudgetComparisonResponse recommended(BigDecimal recommended) {
    this.recommended = recommended;
    return this;
  }

  /**
   * Рекомендованный бюджет.
   * @return recommended
   */
  @NotNull @Valid 
  @Schema(name = "recommended", description = "Рекомендованный бюджет.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("recommended")
  public BigDecimal getRecommended() {
    return recommended;
  }

  public void setRecommended(BigDecimal recommended) {
    this.recommended = recommended;
  }

  public BudgetComparisonResponse median(BigDecimal median) {
    this.median = median;
    return this;
  }

  /**
   * Медианное значение.
   * @return median
   */
  @NotNull @Valid 
  @Schema(name = "median", description = "Медианное значение.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("median")
  public BigDecimal getMedian() {
    return median;
  }

  public void setMedian(BigDecimal median) {
    this.median = median;
  }

  public BudgetComparisonResponse deviationPercent(BigDecimal deviationPercent) {
    this.deviationPercent = deviationPercent;
    return this;
  }

  /**
   * Отклонение от медианы в процентах.
   * @return deviationPercent
   */
  @NotNull @Valid 
  @Schema(name = "deviationPercent", description = "Отклонение от медианы в процентах.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("deviationPercent")
  public BigDecimal getDeviationPercent() {
    return deviationPercent;
  }

  public void setDeviationPercent(BigDecimal deviationPercent) {
    this.deviationPercent = deviationPercent;
  }

  public BudgetComparisonResponse warnings(List<@Valid BudgetWarning> warnings) {
    this.warnings = warnings;
    return this;
  }

  public BudgetComparisonResponse addWarningsItem(BudgetWarning warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Get warnings
   * @return warnings
   */
  @NotNull @Valid 
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("warnings")
  public List<@Valid BudgetWarning> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<@Valid BudgetWarning> warnings) {
    this.warnings = warnings;
  }

  public BudgetComparisonResponse requiresAcknowledgement(@Nullable Boolean requiresAcknowledgement) {
    this.requiresAcknowledgement = requiresAcknowledgement;
    return this;
  }

  /**
   * Требуется ли подтверждение заказчика.
   * @return requiresAcknowledgement
   */
  
  @Schema(name = "requiresAcknowledgement", description = "Требуется ли подтверждение заказчика.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiresAcknowledgement")
  public @Nullable Boolean getRequiresAcknowledgement() {
    return requiresAcknowledgement;
  }

  public void setRequiresAcknowledgement(@Nullable Boolean requiresAcknowledgement) {
    this.requiresAcknowledgement = requiresAcknowledgement;
  }

  public BudgetComparisonResponse acknowledgmentToken(String acknowledgmentToken) {
    this.acknowledgmentToken = JsonNullable.of(acknowledgmentToken);
    return this;
  }

  /**
   * Токен подтверждения world-service.
   * @return acknowledgmentToken
   */
  
  @Schema(name = "acknowledgmentToken", description = "Токен подтверждения world-service.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("acknowledgmentToken")
  public JsonNullable<String> getAcknowledgmentToken() {
    return acknowledgmentToken;
  }

  public void setAcknowledgmentToken(JsonNullable<String> acknowledgmentToken) {
    this.acknowledgmentToken = acknowledgmentToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetComparisonResponse budgetComparisonResponse = (BudgetComparisonResponse) o;
    return Objects.equals(this.proposed, budgetComparisonResponse.proposed) &&
        Objects.equals(this.recommended, budgetComparisonResponse.recommended) &&
        Objects.equals(this.median, budgetComparisonResponse.median) &&
        Objects.equals(this.deviationPercent, budgetComparisonResponse.deviationPercent) &&
        Objects.equals(this.warnings, budgetComparisonResponse.warnings) &&
        Objects.equals(this.requiresAcknowledgement, budgetComparisonResponse.requiresAcknowledgement) &&
        equalsNullable(this.acknowledgmentToken, budgetComparisonResponse.acknowledgmentToken);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(proposed, recommended, median, deviationPercent, warnings, requiresAcknowledgement, hashCodeNullable(acknowledgmentToken));
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
    sb.append("class BudgetComparisonResponse {\n");
    sb.append("    proposed: ").append(toIndentedString(proposed)).append("\n");
    sb.append("    recommended: ").append(toIndentedString(recommended)).append("\n");
    sb.append("    median: ").append(toIndentedString(median)).append("\n");
    sb.append("    deviationPercent: ").append(toIndentedString(deviationPercent)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
    sb.append("    requiresAcknowledgement: ").append(toIndentedString(requiresAcknowledgement)).append("\n");
    sb.append("    acknowledgmentToken: ").append(toIndentedString(acknowledgmentToken)).append("\n");
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

