package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HousingAnalyticsResponseTopPrestigeApartmentsInner
 */

@JsonTypeName("HousingAnalyticsResponse_topPrestigeApartments_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class HousingAnalyticsResponseTopPrestigeApartmentsInner {

  private @Nullable String apartmentId;

  private @Nullable Integer prestige;

  private @Nullable String ownerId;

  public HousingAnalyticsResponseTopPrestigeApartmentsInner apartmentId(@Nullable String apartmentId) {
    this.apartmentId = apartmentId;
    return this;
  }

  /**
   * Get apartmentId
   * @return apartmentId
   */
  
  @Schema(name = "apartmentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("apartmentId")
  public @Nullable String getApartmentId() {
    return apartmentId;
  }

  public void setApartmentId(@Nullable String apartmentId) {
    this.apartmentId = apartmentId;
  }

  public HousingAnalyticsResponseTopPrestigeApartmentsInner prestige(@Nullable Integer prestige) {
    this.prestige = prestige;
    return this;
  }

  /**
   * Get prestige
   * @return prestige
   */
  
  @Schema(name = "prestige", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prestige")
  public @Nullable Integer getPrestige() {
    return prestige;
  }

  public void setPrestige(@Nullable Integer prestige) {
    this.prestige = prestige;
  }

  public HousingAnalyticsResponseTopPrestigeApartmentsInner ownerId(@Nullable String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * Get ownerId
   * @return ownerId
   */
  
  @Schema(name = "ownerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ownerId")
  public @Nullable String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(@Nullable String ownerId) {
    this.ownerId = ownerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HousingAnalyticsResponseTopPrestigeApartmentsInner housingAnalyticsResponseTopPrestigeApartmentsInner = (HousingAnalyticsResponseTopPrestigeApartmentsInner) o;
    return Objects.equals(this.apartmentId, housingAnalyticsResponseTopPrestigeApartmentsInner.apartmentId) &&
        Objects.equals(this.prestige, housingAnalyticsResponseTopPrestigeApartmentsInner.prestige) &&
        Objects.equals(this.ownerId, housingAnalyticsResponseTopPrestigeApartmentsInner.ownerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apartmentId, prestige, ownerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HousingAnalyticsResponseTopPrestigeApartmentsInner {\n");
    sb.append("    apartmentId: ").append(toIndentedString(apartmentId)).append("\n");
    sb.append("    prestige: ").append(toIndentedString(prestige)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
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

