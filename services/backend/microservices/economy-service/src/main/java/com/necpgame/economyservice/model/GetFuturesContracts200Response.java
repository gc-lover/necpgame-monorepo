package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.FuturesContract;
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
 * GetFuturesContracts200Response
 */

@JsonTypeName("getFuturesContracts_200_response")

public class GetFuturesContracts200Response {

  @Valid
  private List<@Valid FuturesContract> futures = new ArrayList<>();

  public GetFuturesContracts200Response futures(List<@Valid FuturesContract> futures) {
    this.futures = futures;
    return this;
  }

  public GetFuturesContracts200Response addFuturesItem(FuturesContract futuresItem) {
    if (this.futures == null) {
      this.futures = new ArrayList<>();
    }
    this.futures.add(futuresItem);
    return this;
  }

  /**
   * Get futures
   * @return futures
   */
  @Valid 
  @Schema(name = "futures", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("futures")
  public List<@Valid FuturesContract> getFutures() {
    return futures;
  }

  public void setFutures(List<@Valid FuturesContract> futures) {
    this.futures = futures;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFuturesContracts200Response getFuturesContracts200Response = (GetFuturesContracts200Response) o;
    return Objects.equals(this.futures, getFuturesContracts200Response.futures);
  }

  @Override
  public int hashCode() {
    return Objects.hash(futures);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFuturesContracts200Response {\n");
    sb.append("    futures: ").append(toIndentedString(futures)).append("\n");
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

