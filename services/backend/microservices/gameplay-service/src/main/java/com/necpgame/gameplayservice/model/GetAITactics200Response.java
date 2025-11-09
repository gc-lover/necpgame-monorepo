package com.necpgame.gameplayservice.model;

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
 * GetAITactics200Response
 */

@JsonTypeName("getAITactics_200_response")

public class GetAITactics200Response {

  @Valid
  private List<Object> tactics = new ArrayList<>();

  public GetAITactics200Response tactics(List<Object> tactics) {
    this.tactics = tactics;
    return this;
  }

  public GetAITactics200Response addTacticsItem(Object tacticsItem) {
    if (this.tactics == null) {
      this.tactics = new ArrayList<>();
    }
    this.tactics.add(tacticsItem);
    return this;
  }

  /**
   * Get tactics
   * @return tactics
   */
  
  @Schema(name = "tactics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tactics")
  public List<Object> getTactics() {
    return tactics;
  }

  public void setTactics(List<Object> tactics) {
    this.tactics = tactics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAITactics200Response getAITactics200Response = (GetAITactics200Response) o;
    return Objects.equals(this.tactics, getAITactics200Response.tactics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tactics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAITactics200Response {\n");
    sb.append("    tactics: ").append(toIndentedString(tactics)).append("\n");
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

