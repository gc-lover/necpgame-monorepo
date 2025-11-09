package com.necpgame.narrativeservice.model;

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
 * DialogueChoiceRequest
 */


public class DialogueChoiceRequest {

  private String optionId;

  public DialogueChoiceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DialogueChoiceRequest(String optionId) {
    this.optionId = optionId;
  }

  public DialogueChoiceRequest optionId(String optionId) {
    this.optionId = optionId;
    return this;
  }

  /**
   * Get optionId
   * @return optionId
   */
  @NotNull 
  @Schema(name = "option_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("option_id")
  public String getOptionId() {
    return optionId;
  }

  public void setOptionId(String optionId) {
    this.optionId = optionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueChoiceRequest dialogueChoiceRequest = (DialogueChoiceRequest) o;
    return Objects.equals(this.optionId, dialogueChoiceRequest.optionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(optionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueChoiceRequest {\n");
    sb.append("    optionId: ").append(toIndentedString(optionId)).append("\n");
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

