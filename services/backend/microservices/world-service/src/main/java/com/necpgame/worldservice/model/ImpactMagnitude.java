package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ImpactMagnitude
 */


public class ImpactMagnitude {

  private Float economic;

  private Float social;

  private Float political;

  private Float security;

  private @Nullable Float environmental;

  private @Nullable Float cultural;

  public ImpactMagnitude() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImpactMagnitude(Float economic, Float social, Float political, Float security) {
    this.economic = economic;
    this.social = social;
    this.political = political;
    this.security = security;
  }

  public ImpactMagnitude economic(Float economic) {
    this.economic = economic;
    return this;
  }

  /**
   * Get economic
   * minimum: 0
   * maximum: 1
   * @return economic
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "economic", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("economic")
  public Float getEconomic() {
    return economic;
  }

  public void setEconomic(Float economic) {
    this.economic = economic;
  }

  public ImpactMagnitude social(Float social) {
    this.social = social;
    return this;
  }

  /**
   * Get social
   * minimum: 0
   * maximum: 1
   * @return social
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "social", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("social")
  public Float getSocial() {
    return social;
  }

  public void setSocial(Float social) {
    this.social = social;
  }

  public ImpactMagnitude political(Float political) {
    this.political = political;
    return this;
  }

  /**
   * Get political
   * minimum: 0
   * maximum: 1
   * @return political
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "political", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("political")
  public Float getPolitical() {
    return political;
  }

  public void setPolitical(Float political) {
    this.political = political;
  }

  public ImpactMagnitude security(Float security) {
    this.security = security;
    return this;
  }

  /**
   * Get security
   * minimum: 0
   * maximum: 1
   * @return security
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "security", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("security")
  public Float getSecurity() {
    return security;
  }

  public void setSecurity(Float security) {
    this.security = security;
  }

  public ImpactMagnitude environmental(@Nullable Float environmental) {
    this.environmental = environmental;
    return this;
  }

  /**
   * Get environmental
   * minimum: 0
   * maximum: 1
   * @return environmental
   */
  @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "environmental", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("environmental")
  public @Nullable Float getEnvironmental() {
    return environmental;
  }

  public void setEnvironmental(@Nullable Float environmental) {
    this.environmental = environmental;
  }

  public ImpactMagnitude cultural(@Nullable Float cultural) {
    this.cultural = cultural;
    return this;
  }

  /**
   * Get cultural
   * minimum: 0
   * maximum: 1
   * @return cultural
   */
  @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "cultural", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cultural")
  public @Nullable Float getCultural() {
    return cultural;
  }

  public void setCultural(@Nullable Float cultural) {
    this.cultural = cultural;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImpactMagnitude impactMagnitude = (ImpactMagnitude) o;
    return Objects.equals(this.economic, impactMagnitude.economic) &&
        Objects.equals(this.social, impactMagnitude.social) &&
        Objects.equals(this.political, impactMagnitude.political) &&
        Objects.equals(this.security, impactMagnitude.security) &&
        Objects.equals(this.environmental, impactMagnitude.environmental) &&
        Objects.equals(this.cultural, impactMagnitude.cultural);
  }

  @Override
  public int hashCode() {
    return Objects.hash(economic, social, political, security, environmental, cultural);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImpactMagnitude {\n");
    sb.append("    economic: ").append(toIndentedString(economic)).append("\n");
    sb.append("    social: ").append(toIndentedString(social)).append("\n");
    sb.append("    political: ").append(toIndentedString(political)).append("\n");
    sb.append("    security: ").append(toIndentedString(security)).append("\n");
    sb.append("    environmental: ").append(toIndentedString(environmental)).append("\n");
    sb.append("    cultural: ").append(toIndentedString(cultural)).append("\n");
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

