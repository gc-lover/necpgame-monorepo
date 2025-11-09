package com.necpgame.economyservice.model;

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
 * CloseShortPosition200Response
 */

@JsonTypeName("closeShortPosition_200_response")

public class CloseShortPosition200Response {

  private @Nullable String positionId;

  private @Nullable BigDecimal profitLoss;

  private @Nullable BigDecimal roiPercent;

  public CloseShortPosition200Response positionId(@Nullable String positionId) {
    this.positionId = positionId;
    return this;
  }

  /**
   * Get positionId
   * @return positionId
   */
  
  @Schema(name = "position_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("position_id")
  public @Nullable String getPositionId() {
    return positionId;
  }

  public void setPositionId(@Nullable String positionId) {
    this.positionId = positionId;
  }

  public CloseShortPosition200Response profitLoss(@Nullable BigDecimal profitLoss) {
    this.profitLoss = profitLoss;
    return this;
  }

  /**
   * Get profitLoss
   * @return profitLoss
   */
  @Valid 
  @Schema(name = "profit_loss", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profit_loss")
  public @Nullable BigDecimal getProfitLoss() {
    return profitLoss;
  }

  public void setProfitLoss(@Nullable BigDecimal profitLoss) {
    this.profitLoss = profitLoss;
  }

  public CloseShortPosition200Response roiPercent(@Nullable BigDecimal roiPercent) {
    this.roiPercent = roiPercent;
    return this;
  }

  /**
   * Get roiPercent
   * @return roiPercent
   */
  @Valid 
  @Schema(name = "roi_percent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roi_percent")
  public @Nullable BigDecimal getRoiPercent() {
    return roiPercent;
  }

  public void setRoiPercent(@Nullable BigDecimal roiPercent) {
    this.roiPercent = roiPercent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CloseShortPosition200Response closeShortPosition200Response = (CloseShortPosition200Response) o;
    return Objects.equals(this.positionId, closeShortPosition200Response.positionId) &&
        Objects.equals(this.profitLoss, closeShortPosition200Response.profitLoss) &&
        Objects.equals(this.roiPercent, closeShortPosition200Response.roiPercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(positionId, profitLoss, roiPercent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CloseShortPosition200Response {\n");
    sb.append("    positionId: ").append(toIndentedString(positionId)).append("\n");
    sb.append("    profitLoss: ").append(toIndentedString(profitLoss)).append("\n");
    sb.append("    roiPercent: ").append(toIndentedString(roiPercent)).append("\n");
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

