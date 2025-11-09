package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.GetDCScale200ResponseDifficultiesInner;
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
 * GetDCScale200Response
 */

@JsonTypeName("getDCScale_200_response")

public class GetDCScale200Response {

  @Valid
  private List<@Valid GetDCScale200ResponseDifficultiesInner> difficulties = new ArrayList<>();

  public GetDCScale200Response difficulties(List<@Valid GetDCScale200ResponseDifficultiesInner> difficulties) {
    this.difficulties = difficulties;
    return this;
  }

  public GetDCScale200Response addDifficultiesItem(GetDCScale200ResponseDifficultiesInner difficultiesItem) {
    if (this.difficulties == null) {
      this.difficulties = new ArrayList<>();
    }
    this.difficulties.add(difficultiesItem);
    return this;
  }

  /**
   * Get difficulties
   * @return difficulties
   */
  @Valid 
  @Schema(name = "difficulties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulties")
  public List<@Valid GetDCScale200ResponseDifficultiesInner> getDifficulties() {
    return difficulties;
  }

  public void setDifficulties(List<@Valid GetDCScale200ResponseDifficultiesInner> difficulties) {
    this.difficulties = difficulties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetDCScale200Response getDCScale200Response = (GetDCScale200Response) o;
    return Objects.equals(this.difficulties, getDCScale200Response.difficulties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(difficulties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetDCScale200Response {\n");
    sb.append("    difficulties: ").append(toIndentedString(difficulties)).append("\n");
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

