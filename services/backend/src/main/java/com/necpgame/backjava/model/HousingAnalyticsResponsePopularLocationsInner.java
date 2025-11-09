package com.necpgame.backjava.model;

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
 * HousingAnalyticsResponsePopularLocationsInner
 */

@JsonTypeName("HousingAnalyticsResponse_popularLocations_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class HousingAnalyticsResponsePopularLocationsInner {

  private @Nullable String locationId;

  private @Nullable BigDecimal visitShare;

  public HousingAnalyticsResponsePopularLocationsInner locationId(@Nullable String locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
   */
  
  @Schema(name = "locationId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locationId")
  public @Nullable String getLocationId() {
    return locationId;
  }

  public void setLocationId(@Nullable String locationId) {
    this.locationId = locationId;
  }

  public HousingAnalyticsResponsePopularLocationsInner visitShare(@Nullable BigDecimal visitShare) {
    this.visitShare = visitShare;
    return this;
  }

  /**
   * Get visitShare
   * @return visitShare
   */
  @Valid 
  @Schema(name = "visitShare", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visitShare")
  public @Nullable BigDecimal getVisitShare() {
    return visitShare;
  }

  public void setVisitShare(@Nullable BigDecimal visitShare) {
    this.visitShare = visitShare;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HousingAnalyticsResponsePopularLocationsInner housingAnalyticsResponsePopularLocationsInner = (HousingAnalyticsResponsePopularLocationsInner) o;
    return Objects.equals(this.locationId, housingAnalyticsResponsePopularLocationsInner.locationId) &&
        Objects.equals(this.visitShare, housingAnalyticsResponsePopularLocationsInner.visitShare);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locationId, visitShare);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HousingAnalyticsResponsePopularLocationsInner {\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    visitShare: ").append(toIndentedString(visitShare)).append("\n");
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

