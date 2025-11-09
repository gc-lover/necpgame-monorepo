package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * AcquisitionMethodsMethodsInner
 */

@JsonTypeName("AcquisitionMethods_methods_inner")

public class AcquisitionMethodsMethodsInner {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    PURCHASE("purchase"),
    
    CRAFT("craft"),
    
    QUEST("quest"),
    
    LOOT("loot"),
    
    TRADE("trade"),
    
    CORPORATE_REWARD("corporate_reward");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable Boolean available;

  private @Nullable String description;

  private @Nullable Object requirements;

  private @Nullable BigDecimal cost;

  public AcquisitionMethodsMethodsInner type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public AcquisitionMethodsMethodsInner available(@Nullable Boolean available) {
    this.available = available;
    return this;
  }

  /**
   * Get available
   * @return available
   */
  
  @Schema(name = "available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available")
  public @Nullable Boolean getAvailable() {
    return available;
  }

  public void setAvailable(@Nullable Boolean available) {
    this.available = available;
  }

  public AcquisitionMethodsMethodsInner description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public AcquisitionMethodsMethodsInner requirements(@Nullable Object requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable Object getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable Object requirements) {
    this.requirements = requirements;
  }

  public AcquisitionMethodsMethodsInner cost(@Nullable BigDecimal cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Стоимость (если применимо)
   * @return cost
   */
  @Valid 
  @Schema(name = "cost", description = "Стоимость (если применимо)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost")
  public @Nullable BigDecimal getCost() {
    return cost;
  }

  public void setCost(@Nullable BigDecimal cost) {
    this.cost = cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcquisitionMethodsMethodsInner acquisitionMethodsMethodsInner = (AcquisitionMethodsMethodsInner) o;
    return Objects.equals(this.type, acquisitionMethodsMethodsInner.type) &&
        Objects.equals(this.available, acquisitionMethodsMethodsInner.available) &&
        Objects.equals(this.description, acquisitionMethodsMethodsInner.description) &&
        Objects.equals(this.requirements, acquisitionMethodsMethodsInner.requirements) &&
        Objects.equals(this.cost, acquisitionMethodsMethodsInner.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, available, description, requirements, cost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AcquisitionMethodsMethodsInner {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
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

