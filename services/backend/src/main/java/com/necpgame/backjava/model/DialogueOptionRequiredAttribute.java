package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DialogueOptionRequiredAttribute
 */

@JsonTypeName("DialogueOption_required_attribute")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DialogueOptionRequiredAttribute {

  private @Nullable String attribute;

  private @Nullable Integer minValue;

  public DialogueOptionRequiredAttribute attribute(@Nullable String attribute) {
    this.attribute = attribute;
    return this;
  }

  /**
   * Get attribute
   * @return attribute
   */
  
  @Schema(name = "attribute", example = "INTELLIGENCE", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute")
  public @Nullable String getAttribute() {
    return attribute;
  }

  public void setAttribute(@Nullable String attribute) {
    this.attribute = attribute;
  }

  public DialogueOptionRequiredAttribute minValue(@Nullable Integer minValue) {
    this.minValue = minValue;
    return this;
  }

  /**
   * Get minValue
   * @return minValue
   */
  
  @Schema(name = "min_value", example = "12", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_value")
  public @Nullable Integer getMinValue() {
    return minValue;
  }

  public void setMinValue(@Nullable Integer minValue) {
    this.minValue = minValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueOptionRequiredAttribute dialogueOptionRequiredAttribute = (DialogueOptionRequiredAttribute) o;
    return Objects.equals(this.attribute, dialogueOptionRequiredAttribute.attribute) &&
        Objects.equals(this.minValue, dialogueOptionRequiredAttribute.minValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attribute, minValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueOptionRequiredAttribute {\n");
    sb.append("    attribute: ").append(toIndentedString(attribute)).append("\n");
    sb.append("    minValue: ").append(toIndentedString(minValue)).append("\n");
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

