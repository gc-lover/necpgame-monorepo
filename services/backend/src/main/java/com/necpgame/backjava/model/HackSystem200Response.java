package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HackSystem200Response
 */

@JsonTypeName("hackSystem_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:35.859669800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class HackSystem200Response {

  private @Nullable Boolean success;

  private @Nullable String result;

  @Valid
  private JsonNullable<List<String>> dataAccessed = JsonNullable.<List<String>>undefined();

  public HackSystem200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public HackSystem200Response result(@Nullable String result) {
    this.result = result;
    return this;
  }

  /**
   * Get result
   * @return result
   */
  
  @Schema(name = "result", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("result")
  public @Nullable String getResult() {
    return result;
  }

  public void setResult(@Nullable String result) {
    this.result = result;
  }

  public HackSystem200Response dataAccessed(List<String> dataAccessed) {
    this.dataAccessed = JsonNullable.of(dataAccessed);
    return this;
  }

  public HackSystem200Response addDataAccessedItem(String dataAccessedItem) {
    if (this.dataAccessed == null || !this.dataAccessed.isPresent()) {
      this.dataAccessed = JsonNullable.of(new ArrayList<>());
    }
    this.dataAccessed.get().add(dataAccessedItem);
    return this;
  }

  /**
   * Get dataAccessed
   * @return dataAccessed
   */
  
  @Schema(name = "dataAccessed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dataAccessed")
  public JsonNullable<List<String>> getDataAccessed() {
    return dataAccessed;
  }

  public void setDataAccessed(JsonNullable<List<String>> dataAccessed) {
    this.dataAccessed = dataAccessed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HackSystem200Response hackSystem200Response = (HackSystem200Response) o;
    return Objects.equals(this.success, hackSystem200Response.success) &&
        Objects.equals(this.result, hackSystem200Response.result) &&
        equalsNullable(this.dataAccessed, hackSystem200Response.dataAccessed);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, result, hashCodeNullable(dataAccessed));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackSystem200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    dataAccessed: ").append(toIndentedString(dataAccessed)).append("\n");
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


