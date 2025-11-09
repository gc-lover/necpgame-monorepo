package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RemoveImplant200Response
 */

@JsonTypeName("removeImplant_200_response")

public class RemoveImplant200Response {

  private @Nullable Boolean success;

  private @Nullable BigDecimal humanityRestored;

  public RemoveImplant200Response success(@Nullable Boolean success) {
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

  public RemoveImplant200Response humanityRestored(@Nullable BigDecimal humanityRestored) {
    this.humanityRestored = humanityRestored;
    return this;
  }

  /**
   * Get humanityRestored
   * @return humanityRestored
   */
  @Valid 
  @Schema(name = "humanity_restored", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_restored")
  public @Nullable BigDecimal getHumanityRestored() {
    return humanityRestored;
  }

  public void setHumanityRestored(@Nullable BigDecimal humanityRestored) {
    this.humanityRestored = humanityRestored;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemoveImplant200Response removeImplant200Response = (RemoveImplant200Response) o;
    return Objects.equals(this.success, removeImplant200Response.success) &&
        Objects.equals(this.humanityRestored, removeImplant200Response.humanityRestored);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, humanityRestored);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RemoveImplant200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    humanityRestored: ").append(toIndentedString(humanityRestored)).append("\n");
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

