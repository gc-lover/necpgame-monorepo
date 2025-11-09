package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.PendingConsequence;
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
 * GetPendingConsequences200Response
 */

@JsonTypeName("getPendingConsequences_200_response")

public class GetPendingConsequences200Response {

  @Valid
  private List<@Valid PendingConsequence> consequences = new ArrayList<>();

  public GetPendingConsequences200Response consequences(List<@Valid PendingConsequence> consequences) {
    this.consequences = consequences;
    return this;
  }

  public GetPendingConsequences200Response addConsequencesItem(PendingConsequence consequencesItem) {
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
  @Valid 
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public List<@Valid PendingConsequence> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<@Valid PendingConsequence> consequences) {
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
    GetPendingConsequences200Response getPendingConsequences200Response = (GetPendingConsequences200Response) o;
    return Objects.equals(this.consequences, getPendingConsequences200Response.consequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(consequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPendingConsequences200Response {\n");
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

