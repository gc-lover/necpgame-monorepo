package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderValidationChecklistItem;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * PlayerOrderValidationSummary
 */


public class PlayerOrderValidationSummary {

  /**
   * Gets or Sets result
   */
  public enum ResultEnum {
    PASSED("passed"),
    
    FAILED("failed");

    private final String value;

    ResultEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ResultEnum fromValue(String value) {
      for (ResultEnum b : ResultEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ResultEnum result;

  @Valid
  private List<@Valid PlayerOrderValidationChecklistItem> checklist = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime validatedAt;

  private @Nullable String nextRequiredAction;

  public PlayerOrderValidationSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderValidationSummary(ResultEnum result, List<@Valid PlayerOrderValidationChecklistItem> checklist) {
    this.result = result;
    this.checklist = checklist;
  }

  public PlayerOrderValidationSummary result(ResultEnum result) {
    this.result = result;
    return this;
  }

  /**
   * Get result
   * @return result
   */
  @NotNull 
  @Schema(name = "result", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("result")
  public ResultEnum getResult() {
    return result;
  }

  public void setResult(ResultEnum result) {
    this.result = result;
  }

  public PlayerOrderValidationSummary checklist(List<@Valid PlayerOrderValidationChecklistItem> checklist) {
    this.checklist = checklist;
    return this;
  }

  public PlayerOrderValidationSummary addChecklistItem(PlayerOrderValidationChecklistItem checklistItem) {
    if (this.checklist == null) {
      this.checklist = new ArrayList<>();
    }
    this.checklist.add(checklistItem);
    return this;
  }

  /**
   * Get checklist
   * @return checklist
   */
  @NotNull @Valid 
  @Schema(name = "checklist", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("checklist")
  public List<@Valid PlayerOrderValidationChecklistItem> getChecklist() {
    return checklist;
  }

  public void setChecklist(List<@Valid PlayerOrderValidationChecklistItem> checklist) {
    this.checklist = checklist;
  }

  public PlayerOrderValidationSummary validatedAt(@Nullable OffsetDateTime validatedAt) {
    this.validatedAt = validatedAt;
    return this;
  }

  /**
   * Get validatedAt
   * @return validatedAt
   */
  @Valid 
  @Schema(name = "validatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("validatedAt")
  public @Nullable OffsetDateTime getValidatedAt() {
    return validatedAt;
  }

  public void setValidatedAt(@Nullable OffsetDateTime validatedAt) {
    this.validatedAt = validatedAt;
  }

  public PlayerOrderValidationSummary nextRequiredAction(@Nullable String nextRequiredAction) {
    this.nextRequiredAction = nextRequiredAction;
    return this;
  }

  /**
   * Рекомендация по дальнейшим действиям (редактирование брифа, запрос допуска фракций).
   * @return nextRequiredAction
   */
  
  @Schema(name = "nextRequiredAction", description = "Рекомендация по дальнейшим действиям (редактирование брифа, запрос допуска фракций).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextRequiredAction")
  public @Nullable String getNextRequiredAction() {
    return nextRequiredAction;
  }

  public void setNextRequiredAction(@Nullable String nextRequiredAction) {
    this.nextRequiredAction = nextRequiredAction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderValidationSummary playerOrderValidationSummary = (PlayerOrderValidationSummary) o;
    return Objects.equals(this.result, playerOrderValidationSummary.result) &&
        Objects.equals(this.checklist, playerOrderValidationSummary.checklist) &&
        Objects.equals(this.validatedAt, playerOrderValidationSummary.validatedAt) &&
        Objects.equals(this.nextRequiredAction, playerOrderValidationSummary.nextRequiredAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(result, checklist, validatedAt, nextRequiredAction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderValidationSummary {\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    checklist: ").append(toIndentedString(checklist)).append("\n");
    sb.append("    validatedAt: ").append(toIndentedString(validatedAt)).append("\n");
    sb.append("    nextRequiredAction: ").append(toIndentedString(nextRequiredAction)).append("\n");
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

