package com.necpgame.narrativeservice.model;

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
 * TutorialHintUiBinding
 */

@JsonTypeName("TutorialHint_uiBinding")

public class TutorialHintUiBinding {

  private @Nullable String module;

  private @Nullable String element;

  private @Nullable String action;

  public TutorialHintUiBinding module(@Nullable String module) {
    this.module = module;
    return this;
  }

  /**
   * Get module
   * @return module
   */
  
  @Schema(name = "module", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("module")
  public @Nullable String getModule() {
    return module;
  }

  public void setModule(@Nullable String module) {
    this.module = module;
  }

  public TutorialHintUiBinding element(@Nullable String element) {
    this.element = element;
    return this;
  }

  /**
   * Get element
   * @return element
   */
  
  @Schema(name = "element", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("element")
  public @Nullable String getElement() {
    return element;
  }

  public void setElement(@Nullable String element) {
    this.element = element;
  }

  public TutorialHintUiBinding action(@Nullable String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable String getAction() {
    return action;
  }

  public void setAction(@Nullable String action) {
    this.action = action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TutorialHintUiBinding tutorialHintUiBinding = (TutorialHintUiBinding) o;
    return Objects.equals(this.module, tutorialHintUiBinding.module) &&
        Objects.equals(this.element, tutorialHintUiBinding.element) &&
        Objects.equals(this.action, tutorialHintUiBinding.action);
  }

  @Override
  public int hashCode() {
    return Objects.hash(module, element, action);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TutorialHintUiBinding {\n");
    sb.append("    module: ").append(toIndentedString(module)).append("\n");
    sb.append("    element: ").append(toIndentedString(element)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
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

