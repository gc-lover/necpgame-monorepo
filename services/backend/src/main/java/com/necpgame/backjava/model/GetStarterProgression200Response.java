package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.GetStarterProgression200ResponseRecommendedOrderInner;
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
 * GetStarterProgression200Response
 */

@JsonTypeName("getStarterProgression_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetStarterProgression200Response {

  @Valid
  private List<@Valid GetStarterProgression200ResponseRecommendedOrderInner> recommendedOrder = new ArrayList<>();

  public GetStarterProgression200Response recommendedOrder(List<@Valid GetStarterProgression200ResponseRecommendedOrderInner> recommendedOrder) {
    this.recommendedOrder = recommendedOrder;
    return this;
  }

  public GetStarterProgression200Response addRecommendedOrderItem(GetStarterProgression200ResponseRecommendedOrderInner recommendedOrderItem) {
    if (this.recommendedOrder == null) {
      this.recommendedOrder = new ArrayList<>();
    }
    this.recommendedOrder.add(recommendedOrderItem);
    return this;
  }

  /**
   * Get recommendedOrder
   * @return recommendedOrder
   */
  @Valid 
  @Schema(name = "recommended_order", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommended_order")
  public List<@Valid GetStarterProgression200ResponseRecommendedOrderInner> getRecommendedOrder() {
    return recommendedOrder;
  }

  public void setRecommendedOrder(List<@Valid GetStarterProgression200ResponseRecommendedOrderInner> recommendedOrder) {
    this.recommendedOrder = recommendedOrder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetStarterProgression200Response getStarterProgression200Response = (GetStarterProgression200Response) o;
    return Objects.equals(this.recommendedOrder, getStarterProgression200Response.recommendedOrder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recommendedOrder);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetStarterProgression200Response {\n");
    sb.append("    recommendedOrder: ").append(toIndentedString(recommendedOrder)).append("\n");
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

