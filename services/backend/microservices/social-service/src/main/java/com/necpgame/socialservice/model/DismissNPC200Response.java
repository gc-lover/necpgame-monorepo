package com.necpgame.socialservice.model;

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
 * DismissNPC200Response
 */

@JsonTypeName("dismissNPC_200_response")

public class DismissNPC200Response {

  private @Nullable Boolean success;

  private @Nullable BigDecimal penalty;

  private @Nullable BigDecimal reputationChange;

  public DismissNPC200Response success(@Nullable Boolean success) {
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

  public DismissNPC200Response penalty(@Nullable BigDecimal penalty) {
    this.penalty = penalty;
    return this;
  }

  /**
   * Штраф за досрочное расторжение
   * @return penalty
   */
  @Valid 
  @Schema(name = "penalty", description = "Штраф за досрочное расторжение", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalty")
  public @Nullable BigDecimal getPenalty() {
    return penalty;
  }

  public void setPenalty(@Nullable BigDecimal penalty) {
    this.penalty = penalty;
  }

  public DismissNPC200Response reputationChange(@Nullable BigDecimal reputationChange) {
    this.reputationChange = reputationChange;
    return this;
  }

  /**
   * Get reputationChange
   * @return reputationChange
   */
  @Valid 
  @Schema(name = "reputation_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_change")
  public @Nullable BigDecimal getReputationChange() {
    return reputationChange;
  }

  public void setReputationChange(@Nullable BigDecimal reputationChange) {
    this.reputationChange = reputationChange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DismissNPC200Response dismissNPC200Response = (DismissNPC200Response) o;
    return Objects.equals(this.success, dismissNPC200Response.success) &&
        Objects.equals(this.penalty, dismissNPC200Response.penalty) &&
        Objects.equals(this.reputationChange, dismissNPC200Response.reputationChange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, penalty, reputationChange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DismissNPC200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    penalty: ").append(toIndentedString(penalty)).append("\n");
    sb.append("    reputationChange: ").append(toIndentedString(reputationChange)).append("\n");
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

