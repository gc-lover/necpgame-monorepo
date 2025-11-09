package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.CurrencyPair;
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
 * GetAvailablePairs200Response
 */

@JsonTypeName("getAvailablePairs_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetAvailablePairs200Response {

  @Valid
  private List<@Valid CurrencyPair> pairs = new ArrayList<>();

  public GetAvailablePairs200Response pairs(List<@Valid CurrencyPair> pairs) {
    this.pairs = pairs;
    return this;
  }

  public GetAvailablePairs200Response addPairsItem(CurrencyPair pairsItem) {
    if (this.pairs == null) {
      this.pairs = new ArrayList<>();
    }
    this.pairs.add(pairsItem);
    return this;
  }

  /**
   * Get pairs
   * @return pairs
   */
  @Valid 
  @Schema(name = "pairs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pairs")
  public List<@Valid CurrencyPair> getPairs() {
    return pairs;
  }

  public void setPairs(List<@Valid CurrencyPair> pairs) {
    this.pairs = pairs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailablePairs200Response getAvailablePairs200Response = (GetAvailablePairs200Response) o;
    return Objects.equals(this.pairs, getAvailablePairs200Response.pairs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pairs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailablePairs200Response {\n");
    sb.append("    pairs: ").append(toIndentedString(pairs)).append("\n");
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

