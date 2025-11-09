package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * SkillModifier
 */


public class SkillModifier {

  private String source;

  private BigDecimal value;

  private @Nullable String condition;

  public SkillModifier() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SkillModifier(String source, BigDecimal value) {
    this.source = source;
    this.value = value;
  }

  public SkillModifier source(String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @NotNull 
  @Schema(name = "source", example = "gear.smart-goggles", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public SkillModifier value(BigDecimal value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @NotNull @Valid 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value")
  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public SkillModifier condition(@Nullable String condition) {
    this.condition = condition;
    return this;
  }

  /**
   * Get condition
   * @return condition
   */
  
  @Schema(name = "condition", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("condition")
  public @Nullable String getCondition() {
    return condition;
  }

  public void setCondition(@Nullable String condition) {
    this.condition = condition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillModifier skillModifier = (SkillModifier) o;
    return Objects.equals(this.source, skillModifier.source) &&
        Objects.equals(this.value, skillModifier.value) &&
        Objects.equals(this.condition, skillModifier.condition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, value, condition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillModifier {\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
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

