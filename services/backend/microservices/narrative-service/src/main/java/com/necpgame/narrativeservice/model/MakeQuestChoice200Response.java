package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * MakeQuestChoice200Response
 */

@JsonTypeName("makeQuestChoice_200_response")

public class MakeQuestChoice200Response {

  private @Nullable String newState;

  @Valid
  private List<Object> consequences = new ArrayList<>();

  public MakeQuestChoice200Response newState(@Nullable String newState) {
    this.newState = newState;
    return this;
  }

  /**
   * Get newState
   * @return newState
   */
  
  @Schema(name = "new_state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_state")
  public @Nullable String getNewState() {
    return newState;
  }

  public void setNewState(@Nullable String newState) {
    this.newState = newState;
  }

  public MakeQuestChoice200Response consequences(List<Object> consequences) {
    this.consequences = consequences;
    return this;
  }

  public MakeQuestChoice200Response addConsequencesItem(Object consequencesItem) {
    if (this.consequences == null) {
      this.consequences = new ArrayList<>();
    }
    this.consequences.add(consequencesItem);
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public List<Object> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<Object> consequences) {
    this.consequences = consequences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MakeQuestChoice200Response makeQuestChoice200Response = (MakeQuestChoice200Response) o;
    return Objects.equals(this.newState, makeQuestChoice200Response.newState) &&
        Objects.equals(this.consequences, makeQuestChoice200Response.consequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newState, consequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MakeQuestChoice200Response {\n");
    sb.append("    newState: ").append(toIndentedString(newState)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
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

